package com.hariom.hariKart.config;

import com.hariom.hariKart.entity.User;
import com.hariom.hariKart.enums.AccountStatus;
import com.hariom.hariKart.enums.Role;
import com.hariom.hariKart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.phone}") private String phone;
    @Value("${app.admin.password}") private String password;

    @Override
    public void run(String... args) {
        if (userRepository.existsByPhoneNumber(phone)) return;

        User admin = new User();
        admin.setPhoneNumber(phone);
        admin.setPasswordHash(passwordEncoder.encode(password));
        admin.setRole(Role.ADMIN);
        admin.setAccountStatus(AccountStatus.APPROVED);
        admin.setShopName("HariKart Admin");
        admin.setOwnerName("Admin");
        userRepository.save(admin);
        log.info("Seeded admin user {}", phone);
    }
}