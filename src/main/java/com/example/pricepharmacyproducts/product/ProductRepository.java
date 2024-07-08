package com.example.pricepharmacyproducts.product;

import com.example.pricepharmacyproducts.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByNameContainingIgnoreCase(String p);
}
