package com.example.pricepharmacyproducts.pharmacy;

import com.example.pricepharmacyproducts.product.ProductService;
import com.example.pricepharmacyproducts.sale.Sale;
import com.example.pricepharmacyproducts.sale.SaleDto;
import com.example.pricepharmacyproducts.sale.SaleKey;
import com.example.pricepharmacyproducts.sale.SaleMapper;
import com.example.pricepharmacyproducts.user.Order;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class PharmacyMapper {

    ProductService productService;
    SaleMapper saleMapper;

    public PharmacyMapper(ProductService productService, SaleMapper saleMapper) {
        this.productService = productService;
        this.saleMapper = saleMapper;
    }

    public Pharmacy toPharmacy(PharmacyDto pharmacyDto){

        Pharmacy newPharmacy = new Pharmacy();
        newPharmacy.setPharmacy_id(pharmacyDto.getId());
        newPharmacy.setShippingFees(pharmacyDto.getShippingFees());
        newPharmacy.setName(pharmacyDto.getName());
        newPharmacy.setWebAddress(pharmacyDto.getWebAddress());
        newPharmacy.setCity(pharmacyDto.getCity());
        newPharmacy.setFreeShipping(pharmacyDto.getFreeShipping());
        newPharmacy.setSales(createSales(newPharmacy,pharmacyDto.getSales())
        );
        return  newPharmacy;
    }

    public PharmacyDto toDto(Pharmacy pharmacy){
        return new PharmacyDto(
                pharmacy.getPharmacy_id(),
                pharmacy.getName(),
                pharmacy.getWebAddress(),
                pharmacy.getCity(),
                pharmacy.getShippingFees(),
                pharmacy.getFreeShipping(),
                pharmacy.getSales().stream()
                        .map(sale -> saleMapper.toDto(sale)).collect(Collectors.toList())

        );
    }

    public Set<Sale> createSales(Pharmacy pharmacy, List<SaleDto> sales) {

        Integer pharmacyId = pharmacy.getPharmacy_id();
        Integer currentProductId;
        Set<Sale> newSales = new HashSet<>();
        if(!sales.isEmpty()){
            for (SaleDto sale : sales) {
                currentProductId = sale.getProduct_id();
                newSales.add(new Sale(new SaleKey(pharmacyId,currentProductId),
                        pharmacy,
                        productService.findProductById(currentProductId),
                        sale.getPrice(),
                        sale.getQuantity())
                        );
            }
        }
        return newSales;

    }

}
