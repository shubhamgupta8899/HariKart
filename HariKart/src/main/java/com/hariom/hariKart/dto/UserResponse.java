package com.hariom.hariKart.dto;

import com.hariom.hariKart.entity.User;
import com.hariom.hariKart.enums.AccountStatus;
import com.hariom.hariKart.enums.Role;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String phoneNumber,
        Role role,
        AccountStatus accountStatus,
        String shopName,
        String ownerName,
        String address,
        Integer routeAssigned,
        String gstNumber,
        LocalDateTime createdAt
) {
    public static UserResponse from(User u) {
        return new UserResponse(u.getId(), u.getPhoneNumber(), u.getRole(), u.getAccountStatus(),
                u.getShopName(), u.getOwnerName(), u.getAddress(), u.getRouteAssigned(),
                u.getGstNumber(), u.getCreatedAt());
    }
}