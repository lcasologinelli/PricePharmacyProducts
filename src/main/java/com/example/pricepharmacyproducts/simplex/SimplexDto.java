package com.example.pricepharmacyproducts.simplex;

import com.example.pricepharmacyproducts.pharmacy.Pharmacy;
import com.example.pricepharmacyproducts.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimplexDto {

    private Pharmacy pharmacy;
    private Product product;
    private int quantity;
    private double cost;
}
