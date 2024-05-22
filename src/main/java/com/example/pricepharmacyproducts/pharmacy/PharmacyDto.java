package com.example.pricepharmacyproducts.pharmacy;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
        @NotNull(message = "Pharmacy shipping fees should not be null")
        Integer shippingFees,
        @NotNull(message = "Pharmacy should contains when the amount of the ships grants a free shipping fees. if not put 0")
        Integer freeShipping,
        List<Integer> productID,
        List<Integer> price
) {

}
