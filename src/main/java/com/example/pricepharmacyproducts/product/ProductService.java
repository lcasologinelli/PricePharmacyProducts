package com.example.pricepharmacyproducts.product;

import com.example.pricepharmacyproducts.product.ProductDto;
import com.example.pricepharmacyproducts.product.Product;
import com.example.pricepharmacyproducts.product.ProductMapper;
import com.example.pricepharmacyproducts.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    ProductRepository productRepository;
    ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public Product saveProduct(ProductDto productDto){
        Product newProduct = productMapper.productDtoToProduct(productDto);
        return productRepository.save(newProduct);
    }

    public List<Product> findAllProducts(){
        return productRepository.findAll();
    }

    public Product findProductById(Integer id){
        return productRepository.findById(id).orElseThrow(()-> new RuntimeException("No Product by ID: "+ id));
    }

    public List<Product> findProductByName(String name){
        return productRepository.findAllByNameContaining(name);
    }

    public void deleteProductById(Integer id){
        productRepository.deleteById(id);
    }
}
