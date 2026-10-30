package com.hariom.hariKart.entity;

import com.hariom.hariKart.enums.MovementReason;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_Movement")
public class StockMovement {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "productId", nullable = false)
    private Long productId;

    @Column(name = "quantity_change", nullable = false)
    private Integer quantityChange;

    @Column(name = "quantity_after", nullable = false)
    private Integer quantityAfter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MovementReason reason;

    @Column(length = 255)
    private String note;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();

    }
}
