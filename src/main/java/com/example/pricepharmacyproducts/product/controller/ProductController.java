package com.example.pricepharmacyproducts.product.controller;

import com.example.pricepharmacyproducts.pharmacy.entity.Pharmacy;
import com.example.pricepharmacyproducts.product.dto.ProductDto;
import com.example.pricepharmacyproducts.product.entity.Product;
import com.example.pricepharmacyproducts.product.repository.ProductRepository;
import com.example.pricepharmacyproducts.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Product saveProduct(
            @RequestBody ProductDto productDto
    ){
        return this.productService.saveProduct(productDto);
    }

    @GetMapping
    public List<Product> findAllProducts(){
        return this.productService.findAllProducts();
    }

    @GetMapping("/{product_id}")
    public Product findProductById(
            @PathVariable("product_id") Integer id
    ){
        return productService.findProductById(id);
    }

    @GetMapping("/search/{p_name}")
    public List<Product> findProductByName(
            @PathVariable("p_name") String name
    ){
        return this.productService.findProductByName(name);
    }

    @DeleteMapping("/{product_delete}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(
            @PathVariable("product_delete") Integer id
    ){
        this.productService.deleteProductById(id);
    }
}
