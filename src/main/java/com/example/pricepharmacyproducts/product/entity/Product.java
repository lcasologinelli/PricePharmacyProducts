package com.example.pricepharmacyproducts.product.entity;

import com.example.pricepharmacyproducts.sale.entity.Sale;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue
    private Integer product_id;
    private String format;
    private String name;
    private String producer;
    private String category;
    private String dose;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Sale> sales = new HashSet<>();
}
