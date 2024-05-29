package com.example.pricepharmacyproducts.sale;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class SaleKey implements Serializable {
    @Column(name = "pharmacy_id")
    private Integer pharmacy_id;

    @Column(name = "product_id")
    private Integer product_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaleKey saleKey = (SaleKey) o;
        return Objects.equals(pharmacy_id, saleKey.pharmacy_id) && Objects.equals(product_id, saleKey.product_id) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pharmacy_id, product_id);
    }


}
