package com.hariom.hariKart.enums;

public enum StockStatus {
    IN_STOCK, LOW_STOCK, OUT_OF_STOCK;

    public static StockStatus of(int available, int lowThreshold) {
        if (available <= 0) return OUT_OF_STOCK;
        if (available <= lowThreshold) return LOW_STOCK;
        return IN_STOCK;
    }
}