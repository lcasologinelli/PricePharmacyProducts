package com.example.pricepharmacyproducts.sale;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaleDto{
        Integer product_id;
        Float price;
        Integer quantity;
}
