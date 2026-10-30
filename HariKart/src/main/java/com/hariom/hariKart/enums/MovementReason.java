package com.hariom.hariKart.enums;

public enum MovementReason {
    OPENING_STOCK,   // when a product is first added
    STOCK_IN,        // new goods arrived from supplier
    ADJUSTMENT,      // manual count correction (+ or -)
    DAMAGED,         // broken or lost goods
    RETURN_IN,       // customer returned goods
    ORDER_OUT        // used later by the order system
}