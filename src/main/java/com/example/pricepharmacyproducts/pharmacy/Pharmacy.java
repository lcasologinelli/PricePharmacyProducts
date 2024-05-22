package com.example.pricepharmacyproducts.pharmacy;

import com.example.pricepharmacyproducts.sale.Sale;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
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
