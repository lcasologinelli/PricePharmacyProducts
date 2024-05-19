package com.example.pricepharmacyproducts.pharmacy.mapper;

import com.example.pricepharmacyproducts.pharmacy.dto.PharmacyDto;
import com.example.pricepharmacyproducts.pharmacy.dto.PharmacyResponseDto;
import com.example.pricepharmacyproducts.pharmacy.entity.Pharmacy;
import com.example.pricepharmacyproducts.product.entity.Product;
import com.example.pricepharmacyproducts.product.service.ProductService;
import com.example.pricepharmacyproducts.sale.entity.Sale;
import com.example.pricepharmacyproducts.sale.key.SaleKey;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class PharmacyMapper {

    ProductService productService;

    public PharmacyMapper(ProductService productService) {
        this.productService = productService;
    }

    public Pharmacy PharmacyDtoToPharmacy(PharmacyDto pharmacyDto){

        Pharmacy newPharmacy = new Pharmacy();
        newPharmacy.setShippingFees(pharmacyDto.shippingFees());
        newPharmacy.setName(pharmacyDto.name());
        newPharmacy.setWebAddress(pharmacyDto.webAddress());
        newPharmacy.setCity(pharmacyDto.city());
        newPharmacy.setFreeShipping(pharmacyDto.freeShipping());
        newPharmacy.setSales(createSales(newPharmacy,
                pharmacyDto.productID(),
                pharmacyDto.price())
        );
        return  newPharmacy;
    }

    public Set<Sale> createSales(Pharmacy pharmacy, List<Integer> products_ID, List<Integer> prices){

        Integer pharmacyId = pharmacy.getPharmacy_id();
        Integer currentProductId;
        Set<Sale> sales = new HashSet<>();
        if(products_ID.size() == prices.size() && !products_ID.isEmpty()){
            for(int i = 0; i< products_ID.size(); i++){
                currentProductId = products_ID.get(i);
                sales.add(new Sale(new SaleKey(pharmacyId,currentProductId),
                        pharmacy,
                        productService.findProductById(currentProductId),
                        prices.get(i) ));
            }
        }
        return sales;

    }

    public PharmacyResponseDto toPharmacyResponseDto(Pharmacy pharmacy){
        return new PharmacyResponseDto(
                pharmacy.getName(),
                pharmacy.getWebAddress(),
                pharmacy.getCity(),
                pharmacy.getShippingFees(),
                pharmacy.getFreeShipping(),
                pharmacy.getSales().stream().map(Sale::getProduct)
                        .toList()
                        .stream().map(Product::getProduct_id).collect(Collectors.toList()),
                pharmacy.getSales().stream().map(Sale::getPrice)
                        .collect(Collectors.toList())
                );
    }
}
