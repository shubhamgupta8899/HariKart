package com.hariom.hariKart.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hariom.hariKart.enums.StockStatus;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CatalogProductResponse(
        Long id, String sku, String name, String brand, String category,
        String description, String imageUrl,
        BigDecimal price,            // wholesale price for shopkeepers, retail price for retailers
        BigDecimal mrp, BigDecimal gstPercent, Integer minOrderQty,
        StockStatus stockStatus,
        Integer availableQuantity    // only shopkeepers see the exact number
) {}