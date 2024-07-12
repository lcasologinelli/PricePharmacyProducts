package com.example.pricepharmacyproducts.product;

import org.springframework.stereotype.Service;

@Service
public class ProductMapper {

    public Product productDtoToProduct(ProductDto productDto){
        Product newProduct = new Product();
        newProduct.setCategory(productDto.category());
        newProduct.setName(productDto.name());
        newProduct.setDose(productDto.dose());
        newProduct.setFormat(productDto.format());
        newProduct.setProducer(productDto.producer());

        return newProduct;
    }
}
