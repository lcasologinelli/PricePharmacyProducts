package com.example.pricepharmacyproducts.order;

import com.example.pricepharmacyproducts.sale.Sale;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_order")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue
    private Integer order_id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sale_order",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = {
                    @JoinColumn(name = "pharmacy_id"),
                    @JoinColumn(name = "product_id")
            }
    )
    private List<Sale> saleList;

    private Integer quantity;
}
