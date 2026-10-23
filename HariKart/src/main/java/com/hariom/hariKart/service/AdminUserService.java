package com.hariom.hariKart.service;

import com.hariom.hariKart.dto.UserResponse;
import com.hariom.hariKart.entity.User;
import com.hariom.hariKart.enums.AccountStatus;
import com.hariom.hariKart.enums.Role;
import com.hariom.hariKart.exception.ApiException;
import com.hariom.hariKart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    public List<UserResponse> list(AccountStatus status) {
        return userRepository.findByAccountStatusOrderByCreatedAtDesc(status)
                .stream().map(UserResponse::from).toList();
    }

    @Transactional
    public UserResponse setStatus(Long id, AccountStatus status) {
        User user = find(id);
        if (user.getRole() == Role.ADMIN) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Admin accounts cannot be changed here");
        }
        user.setAccountStatus(status);
        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse assignRoute(Long id, Integer route) {
        User user = find(id);
        user.setRouteAssigned(route);
        return UserResponse.from(user);
    }

    private User find(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
    }
}