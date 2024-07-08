package com.example.pricepharmacyproducts.sale;

import com.example.pricepharmacyproducts.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, SaleKey> {
}
