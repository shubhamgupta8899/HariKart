package com.hariom.hariKart.controller;

import com.hariom.hariKart.dto.RouteRequest;
import com.hariom.hariKart.dto.UserResponse;
import com.hariom.hariKart.enums.AccountStatus;
import com.hariom.hariKart.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public List<UserResponse> list(@RequestParam(defaultValue = "PENDING") AccountStatus status) {
        return adminUserService.list(status);
    }

    @PatchMapping("/{id}/approve")
    public UserResponse approve(@PathVariable Long id) {
        return adminUserService.setStatus(id, AccountStatus.APPROVED);
    }

    @PatchMapping("/{id}/reject")
    public UserResponse reject(@PathVariable Long id) {
        return adminUserService.setStatus(id, AccountStatus.REJECTED);
    }

    @PatchMapping("/{id}/route")
    public UserResponse route(@PathVariable Long id, @Valid @RequestBody RouteRequest req) {
        return adminUserService.assignRoute(id, req.route());
    }
}