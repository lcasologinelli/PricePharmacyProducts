package com.example.pricepharmacyproducts.simplex;

import com.example.pricepharmacyproducts.pharmacy.PharmacyDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PharmacyShippingFeesDto {

    private PharmacyDto pharmacy;
    private Double shippingFees;


}
