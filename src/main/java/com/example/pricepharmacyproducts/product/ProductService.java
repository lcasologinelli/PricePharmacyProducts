package com.example.pricepharmacyproducts.product;

import com.example.pricepharmacyproducts.exception.IdNotFoundException;
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

    public Product saveProduct(Product product){
//        Product newProduct = productMapper.productDtoToProduct(productDto);
        return productRepository.save(product);
    }

    public Product updateProduct(Product product){
        return productRepository.save(product);

    }

    public List<Product> findAllProducts(){
        return productRepository.findAll();
    }

    public Product findProductById(Integer id){
        return productRepository.findById(id).orElseThrow(()-> new IdNotFoundException("No Product by ID: "+ id));
    }

    public List<Product> findProductByName(String name){
        return productRepository.findAllByNameContaining(name);
    }

    public void deleteProductById(Integer id){
        productRepository.deleteById(id);
    }
}
