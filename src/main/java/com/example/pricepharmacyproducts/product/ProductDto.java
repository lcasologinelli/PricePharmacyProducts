package com.example.pricepharmacyproducts.product;

import jakarta.validation.constraints.NotNull;

public record ProductDto(
        @NotNull(message = "Product format should not be null")
        String format,
        @NotNull(message = "Product name should not be null")
        String name,
        @NotNull(message = "Producer name should not be null")
        String producer,
        @NotNull(message = "Category name should not be null")
        String category,
        @NotNull(message = "Dose should not be null")
        String dose
) {
}
