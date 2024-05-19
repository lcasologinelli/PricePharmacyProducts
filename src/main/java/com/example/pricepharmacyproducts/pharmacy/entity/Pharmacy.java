package com.example.pricepharmacyproducts.pharmacy.entity;

import com.example.pricepharmacyproducts.product.entity.Product;
import com.example.pricepharmacyproducts.sale.entity.Sale;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Pharmacy {
    @Id
    @GeneratedValue
    private Integer pharmacy_id;
    private String name;
    private String webAddress;
    private String city;
    private Integer shippingFees;
    private Integer freeShipping;

    @OneToMany(mappedBy = "pharmacy", cascade = CascadeType.ALL)
    private Set<Sale> sales = new HashSet<>();

}
