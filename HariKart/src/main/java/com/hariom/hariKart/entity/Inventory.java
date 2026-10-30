package com.hariom.hariKart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@Getter
@Setter
public class Inventory {

    @Id
    @Column(name = "product_id")
    private Long productId;                   // same id as the product

    @Column(name = "quantity_on_hand", nullable = false)
    private Integer quantityOnHand = 0;       // physically in the godown

    @Column(name = "reserved_quantity", nullable = false)
    private Integer reservedQuantity = 0;     // promised to unpaid/unpacked orders (used in the order step)

    @Column(name = "low_stock_threshold", nullable = false)
    private Integer lowStockThreshold = 5;

    // Protects against two people changing the same stock at the same moment
    @Version
    private Long version;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /** What a customer can actually buy right now. */
    public int available() {
        return quantityOnHand - reservedQuantity;
    }

    @PrePersist
    @PreUpdate
    void touch() {
        updatedAt = LocalDateTime.now();
    }
}