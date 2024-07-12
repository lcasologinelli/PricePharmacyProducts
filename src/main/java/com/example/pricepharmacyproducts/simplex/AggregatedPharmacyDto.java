package com.example.pricepharmacyproducts.simplex;

import com.example.pricepharmacyproducts.pharmacy.PharmacyDto;
import com.example.pricepharmacyproducts.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AggregatedPharmacyDto {
    private PharmacyDto pharmacy;
    private List<Product> products;
    private List<Integer> quantities;
    private List<Double> costs;
    private double shippingFees;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AggregatedPharmacyDto{");
        sb.append("pharmacy=");
        if (pharmacy != null) {
            sb.append(pharmacy.getName());  // Assuming getName() gives the name of the pharmacy
        } else {
            sb.append("null");
        }
        sb.append(", products=");
        sb.append(products);
        sb.append(", quantities=");
        sb.append(quantities);
        sb.append(", costs=");
        sb.append(costs);
        sb.append(", shippingFees=");
        sb.append(shippingFees);
        sb.append('}');
        return sb.toString();
    }

}
