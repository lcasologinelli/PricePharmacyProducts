package com.example.pricepharmacyproducts.sale.entity;

import com.example.pricepharmacyproducts.pharmacy.entity.Pharmacy;
import com.example.pricepharmacyproducts.product.entity.Product;
import com.example.pricepharmacyproducts.sale.key.SaleKey;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Sale {
    @EmbeddedId
    private SaleKey id;

    @ManyToOne
    @MapsId("pharmacy_id")
    @JoinColumn(name = "pharmacy_id")
    @JsonIgnore
    private Pharmacy pharmacy;

    @ManyToOne
    @MapsId("product_id")
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer price;
}
