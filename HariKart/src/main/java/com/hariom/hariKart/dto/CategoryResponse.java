package com.hariom.hariKart.dto;

import com.hariom.hariKart.entity.Category;

public record CategoryResponse(Long id, String name) {
    public static CategoryResponse from(Category c) {
        return new CategoryResponse(c.getId(), c.getName());
    }
}