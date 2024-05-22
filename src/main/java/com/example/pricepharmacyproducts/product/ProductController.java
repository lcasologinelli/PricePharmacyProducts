package com.example.pricepharmacyproducts.product;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> saveProduct(
            @Valid @RequestBody ProductDto productDto
    ){
        return ResponseEntity.ok().body(this.productService.saveProduct(productDto));
    }

    @GetMapping
    public ResponseEntity<List<Product>> findAllProducts(){
        return ResponseEntity.ok().body(this.productService.findAllProducts());
    }

    @GetMapping("/{product_id}")
    public ResponseEntity<Product> findProductById(
            @PathVariable("product_id") Integer id
    ){
        return ResponseEntity.ok().body(productService.findProductById(id));
    }

    @GetMapping("/search/{p_name}")
    public ResponseEntity<List<Product>> findProductByName(
            @PathVariable("p_name") String name
    ){
        return ResponseEntity.ok().body(this.productService.findProductByName(name));
    }

    @DeleteMapping("/{product_delete}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(
            @PathVariable("product_delete") Integer id
    ){
        this.productService.deleteProductById(id);
    }
}
