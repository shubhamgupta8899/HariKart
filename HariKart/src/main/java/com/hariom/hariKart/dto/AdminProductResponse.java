package com.hariom.hariKart.dto;

import com.hariom.hariKart.entity.Inventory;
import com.hariom.hariKart.entity.Product;
import com.hariom.hariKart.enums.StockStatus;

import java.math.BigDecimal;

public record AdminProductResponse(
        Long id, String sku, String name, String brand,
        Long categoryId, String categoryName,
        String description, String hsnCode, BigDecimal gstPercent,
        BigDecimal mrp, BigDecimal retailPrice, BigDecimal wholesalePrice,
        Integer minOrderQty, String imageUrl, Boolean active,
        Integer quantityOnHand, Integer reservedQuantity,
        Integer availableQuantity, Integer lowStockThreshold, StockStatus stockStatus
) {
    public static AdminProductResponse from(Product p, Inventory inv) {
        int onHand = inv == null ? 0 : inv.getQuantityOnHand();
        int reserved = inv == null ? 0 : inv.getReservedQuantity();
        int threshold = inv == null ? 5 : inv.getLowStockThreshold();
        int available = onHand - reserved;
        return new AdminProductResponse(
                p.getId(), p.getSku(), p.getName(), p.getBrand(),
                p.getCategory().getId(), p.getCategory().getName(),
                p.getDescription(), p.getHsnCode(), p.getGstPercent(),
                p.getMrp(), p.getRetailPrice(), p.getWholesalePrice(),
                p.getMinOrderQty(), p.getImageUrl(), p.getIsActive(),
                onHand, reserved, available, threshold,
                StockStatus.of(available, threshold));
    }
}