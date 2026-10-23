package com.hariom.hariKart.service;

import com.hariom.hariKart.service.CustomUserDetails;
import com.hariom.hariKart.service.UserDetailsServiceImpl;
import com.hariom.hariKart.dto.AuthResponse;
import com.hariom.hariKart.dto.LoginRequest;
import com.hariom.hariKart.dto.RegisterRequest;
import com.hariom.hariKart.dto.UserResponse;
import com.hariom.hariKart.entity.User;
import com.hariom.hariKart.enums.AccountStatus;
import com.hariom.hariKart.enums.Role;
import com.hariom.hariKart.exception.ApiException;
import com.hariom.hariKart.repository.UserRepository;
import com.hariom.hariKart.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Transactional
    public UserResponse register(RegisterRequest req) {
        if (req.role() != Role.SHOPKEEPER && req.role() != Role.RETAILER) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Role must be SHOPKEEPER or RETAILER");
        }
        if (userRepository.existsByPhoneNumber(req.phoneNumber())) {
            throw new ApiException(HttpStatus.CONFLICT, "Phone number already registered");
        }

        String gst = (req.gstNumber() == null || req.gstNumber().isBlank())
                ? null : req.gstNumber().trim().toUpperCase();
        if (req.role() == Role.SHOPKEEPER && gst == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "GST number is required for shopkeepers");
        }
        if (gst != null && userRepository.existsByGstNumber(gst)) {
            throw new ApiException(HttpStatus.CONFLICT, "GST number already registered");
        }

        User user = new User();
        user.setPhoneNumber(req.phoneNumber());
        user.setPasswordHash(passwordEncoder.encode(req.password()));
        user.setRole(req.role());
        user.setAccountStatus(AccountStatus.PENDING);
        user.setShopName(req.shopName().trim());
        user.setOwnerName(req.ownerName().trim());
        user.setAddress(req.address().trim());
        user.setLatitude(req.latitude());
        user.setLongitude(req.longitude());
        user.setGstNumber(gst);

        return UserResponse.from(userRepository.save(user));
    }

    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByPhoneNumber(req.phoneNumber())
                .orElseThrow(this::badCredentials);

        if (user.getPasswordHash() == null
                || !passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            throw badCredentials();
        }

        switch (user.getAccountStatus()) {
            case PENDING -> throw new ApiException(HttpStatus.FORBIDDEN,
                    "Your account is pending admin approval");
            case REJECTED -> throw new ApiException(HttpStatus.FORBIDDEN,
                    "Your registration was rejected. Please contact support");
            default -> { }
        }
        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Your account is deactivated");
        }

        return buildResponse(new CustomUserDetails(user));
    }

    public AuthResponse refresh(String refreshToken) {
        try {
            String phone = jwtUtil.extractUsername(refreshToken);
            CustomUserDetails principal = userDetailsService.loadUserByUsername(phone);

            if (!principal.isEnabled()
                    || !jwtUtil.isValid(refreshToken, principal, JwtUtil.REFRESH)) {
                throw invalidRefresh();
            }
            return buildResponse(principal);
        } catch (ApiException ex) {
            throw ex;
        } catch (Exception ex) {
            throw invalidRefresh();
        }
    }

    private AuthResponse buildResponse(CustomUserDetails principal) {
        String role = principal.getAuthorities().iterator().next()
                .getAuthority().replace("ROLE_", "");
        return new AuthResponse(
                jwtUtil.generateAccessToken(principal),
                jwtUtil.generateRefreshToken(principal),
                "Bearer",
                jwtUtil.getAccessExpiryMs() / 1000,
                principal.getUserId(),
                role);
    }

    private ApiException badCredentials() {
        return new ApiException(HttpStatus.UNAUTHORIZED, "Invalid phone number or password");
    }

    private ApiException invalidRefresh() {
        return new ApiException(HttpStatus.UNAUTHORIZED, "Invalid or expired refresh token");
    }
}