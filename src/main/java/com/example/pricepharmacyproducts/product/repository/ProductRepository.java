package com.example.pricepharmacyproducts.product.repository;

import com.example.pricepharmacyproducts.pharmacy.entity.Pharmacy;
import com.example.pricepharmacyproducts.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByNameContaining(String p);
}
