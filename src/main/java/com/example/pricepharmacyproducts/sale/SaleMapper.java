package com.example.pricepharmacyproducts.sale;

import org.springframework.stereotype.Service;

@Service
public class SaleMapper {

    public SaleDto toDto(Sale sale){
        return new SaleDto(
                sale.getId().getProduct_id(),
                sale.getProduct().getName(),
                sale.getPrice(),
                sale.getQuantity()
        );
    }

}
