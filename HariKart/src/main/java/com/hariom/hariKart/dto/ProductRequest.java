package com.hariom.hariKart.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank @Size(max = 50) String sku,
        @NotBlank @Size(max = 200) String name,
        @Size(max = 100) String brand,
        @NotNull Long categoryId,
        String description,
        @Size(max = 10) String hsnCode,
        @DecimalMin("0") @DecimalMax("28") BigDecimal gstPercent,     // empty = 18
        @NotNull @Positive BigDecimal mrp,
        @NotNull @Positive BigDecimal retailPrice,
        @NotNull @Positive BigDecimal wholesalePrice,
        @Min(1) Integer minOrderQty,                                   // empty = 1
        String imageUrl,
        @Min(0) Integer openingStock,                                  // only used when creating
        @Min(0) Integer lowStockThreshold                              // empty = 5
) {}