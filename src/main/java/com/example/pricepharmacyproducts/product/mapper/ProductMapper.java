package com.example.pricepharmacyproducts.product.mapper;

import com.example.pricepharmacyproducts.product.dto.ProductDto;
import com.example.pricepharmacyproducts.product.entity.Product;
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
