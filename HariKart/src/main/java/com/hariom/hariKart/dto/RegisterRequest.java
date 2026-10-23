package com.hariom.hariKart.dto;

import com.hariom.hariKart.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank
        @Pattern(regexp = "^[6-9]\\d{9}$", message = "Enter a valid 10-digit mobile number")
        String phoneNumber,

        @NotBlank @Size(min = 8, max = 64, message = "Password must be 8 to 64 characters")
        String password,

        @NotNull Role role,

        @NotBlank @Size(max = 150) String shopName,
        @NotBlank @Size(max = 100) String ownerName,
        @NotBlank String address,
        Double latitude,
        Double longitude,

        @Pattern(regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z][1-9A-Z]Z[0-9A-Z]$",
                message = "Invalid GSTIN")
        String gstNumber
) {}