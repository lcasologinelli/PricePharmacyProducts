package com.example.pricepharmacyproducts.simplex;

import com.example.pricepharmacyproducts.pharmacy.Pharmacy;
import com.example.pricepharmacyproducts.pharmacy.PharmacyDto;
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

    private PharmacyDto pharmacy;
    private Product product;
    private int quantity;
    private double cost;
    private double shippingFees;

    @Override
    public String toString(){
        return "SimplexDto{"+
                "pharmacyName: " + pharmacy.getName()+
                ", productName: "+ product.getName()+
                "product_id: "+ product.getProduct_id() +
                ", quantity: " + quantity+
                ", cost: " + cost+
                "}";
    }


}
