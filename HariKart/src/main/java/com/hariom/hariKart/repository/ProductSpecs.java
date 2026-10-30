package com.hariom.hariKart.repository;

import com.hariom.hariKart.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public final class ProductSpecs {

    private ProductSpecs() {}

    private static Specification<Product> nothing() {
        return (root, query, cb) -> cb.conjunction();     // "no filter"
    }

    public static Specification<Product> activeOnly() {
        return (root, query, cb) -> cb.isTrue(root.get("isActive"));
    }

    public static Specification<Product> inCategory(Long categoryId) {
        if (categoryId == null) return nothing();
        return (root, query, cb) -> cb.equal(root.get("category").get("id"), categoryId);
    }

    /** Search in name, SKU and brand. */
    public static Specification<Product> matches(String text) {
        if (text == null || text.isBlank()) return nothing();
        String like = "%" + text.trim().toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("name")), like),
                cb.like(cb.lower(root.get("sku")), like),
                cb.like(cb.lower(root.get("brand")), like));
    }
}