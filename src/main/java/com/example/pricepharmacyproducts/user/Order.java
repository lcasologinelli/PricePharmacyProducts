package com.example.pricepharmacyproducts.user;

import com.example.pricepharmacyproducts.sale.Sale;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_order")
@Data
public class Order {

    @Id
    @GeneratedValue
    private Integer order_id;

    @ManyToMany(mappedBy = "orders", fetch = FetchType.LAZY)
    private List<Sale> saleList;
}
