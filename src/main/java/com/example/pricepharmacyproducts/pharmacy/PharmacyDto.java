package com.example.pricepharmacyproducts.pharmacy;

import com.example.pricepharmacyproducts.sale.SaleDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PharmacyDto {
        Integer id;
        @NotEmpty(message = "Pharmacy name should not be empty")
        String name;
        @NotEmpty(message = "Web Address should not be empty")
        String webAddress;
        String city;
        @NotNull(message = "Pharmacy shipping fees should not be null")
        Integer shippingFees;
        @NotNull(message = "Pharmacy should contains when the amount of the ships grants a free shipping fees. if not put 0")
        Integer freeShipping;
        List<SaleDto> sales;

}
