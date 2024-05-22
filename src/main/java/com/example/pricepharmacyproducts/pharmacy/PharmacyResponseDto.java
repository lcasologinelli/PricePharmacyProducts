package com.example.pricepharmacyproducts.pharmacy;

import java.util.List;

public record PharmacyResponseDto(
        String name,
        String webAddress,
        String city,
        Integer shippingFees,
        Integer freeShipping,
        List<Integer> productID,
        List<Integer> price
) {
}
