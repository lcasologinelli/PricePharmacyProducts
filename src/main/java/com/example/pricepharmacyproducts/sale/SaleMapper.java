package com.example.pricepharmacyproducts.sale;

import com.example.pricepharmacyproducts.product.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class SaleMapper {

    public SaleDto toDto(Sale sale){
        return new SaleDto(
                sale.getId().getProduct_id(),
                sale.getPrice(),
                sale.getQuantity()
        );
    }

}
