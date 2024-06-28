package com.example.pricepharmacyproducts.sale;

import com.example.pricepharmacyproducts.product.Product;
import com.example.pricepharmacyproducts.product.ProductController;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SaleService {

    SaleRepository saleRepository;
    SaleMapper saleMapper;


    public SaleService(SaleRepository saleRepository, SaleMapper saleMapper) {
        this.saleRepository = saleRepository;
        this.saleMapper = saleMapper;
    }


    public  List<Sale> findSaleList() {
       return saleRepository.findAll();
    }

    public Set<Product> findAvailableProducts() {

        Set<Product> newProduct = new HashSet<>();

        List<Sale> allSales = saleRepository.findAll();
            for (Sale sale : allSales) {
                if(sale.getQuantity() > 0)
                    newProduct.add(sale.getProduct());
            }
            return newProduct;

    }

    public List<Sale> findAllByProductId(Integer productId) {
        List<Sale> newList = new ArrayList<>();
        List<Sale> allSales =saleRepository.findAll();
        for(Sale sale: allSales)
            if (sale.getId().getProduct_id().equals(productId))
                newList.add(sale);
        return newList;
    }


    public List<Integer> findMaxQuantity(Set<Product> availableProducts) {

        List<Integer> maxQuantityProducts= new ArrayList<>();
        int maxQuantity;
        List<Sale> allSales =saleRepository.findAll();
        for(Product product: availableProducts){
            maxQuantity = 0;
            for(Sale sale : allSales)
                if(sale.getId().getProduct_id().equals(product.getProduct_id()))
                    if(sale.getQuantity()>maxQuantity)
                        maxQuantity = sale.getQuantity();
            maxQuantityProducts.add(maxQuantity);
        }
        return maxQuantityProducts;
    }

    public void saveSale(Sale sale) {
        saleRepository.save(sale);
    }
}
