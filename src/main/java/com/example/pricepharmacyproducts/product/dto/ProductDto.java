package com.example.pricepharmacyproducts.product.dto;

public record ProductDto(
        String format,
        String name,
        String producer,
        String category,
        String dose
) {
}
