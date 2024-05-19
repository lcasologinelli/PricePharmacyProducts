package com.example.pricepharmacyproducts.pharmacy.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

public record PharmacyDto(
        @NotEmpty(message = "Pharmacy name should not be empty")
        String name,
        @NotEmpty(message = "Web Address should not be empty")
        String webAddress,
        String city,
        Integer shippingFees,
        Integer freeShipping,
        List<Integer> productID,
        List<Integer> price
) {

}
