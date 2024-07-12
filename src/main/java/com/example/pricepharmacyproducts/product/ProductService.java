package com.example.pricepharmacyproducts.product;

import com.example.pricepharmacyproducts.exception.IdNotFoundException;
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

    public void saveProduct(Product product){
        productRepository.save(product);
    }

    public void updateProduct(Product product){
        productRepository.save(product);

    }

    public List<Product> findAllProducts(){
        return productRepository.findAll();
    }

    public Product findProductById(Integer id){
        return productRepository.findById(id).orElseThrow(()-> new IdNotFoundException("No Product by ID: "+ id));
    }

    public List<Product> findProductByName(String name){
        return productRepository.findAllByNameContainingIgnoreCase(name);
    }

    public void deleteProductById(Integer id){
        productRepository.deleteById(id);
    }
}
