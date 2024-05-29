package com.example.pricepharmacyproducts.sale;

import com.example.pricepharmacyproducts.pharmacy.Pharmacy;
import com.example.pricepharmacyproducts.product.Product;
import com.example.pricepharmacyproducts.user.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


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

    public Sale(SaleKey id, Pharmacy pharmacy, Product product, Float price, Integer quantity) {
        this.id = id;
        this.pharmacy = pharmacy;
        this.product = product;
        this.price = price;
        this.quantity = quantity;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sale_order",
            joinColumns = {
                    @JoinColumn(name = "pharmacy_id"),
                    @JoinColumn(name = "product_id")
            },
            inverseJoinColumns = @JoinColumn(name = "order_id")
    )
    private List<Order> orders;

    private Float price;

    private Integer quantity;
}
