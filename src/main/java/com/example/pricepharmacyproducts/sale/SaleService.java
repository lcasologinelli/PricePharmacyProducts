package com.example.pricepharmacyproducts.sale;

import com.example.pricepharmacyproducts.product.Product;
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


    public List<Product> findAvailableProducts() {

        List<Product> newProduct = new ArrayList<>();

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


    public List<Integer> findMaxQuantity(List<Product> availableProducts) {

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

    public List<Product> findProductsByName(String name) {
        List<Product> matchingProducts = new ArrayList<>();
        List<Sale> allSales = saleRepository.findAll();

        for (Sale sale : allSales) {
            if (sale.getProduct().getName().toLowerCase().contains(name.toLowerCase()) && sale.getQuantity() > 0) {
                matchingProducts.add(sale.getProduct());
            }
        }
        return matchingProducts;
    }

    public void saveSale(Sale sale) {
        saleRepository.save(sale);
    }

    public Sale findByProductIdAndPharmacyId(Integer productId, Integer pharmacyId) {
        List<Sale> sales = saleRepository.findAll();

        Sale foundSale = new Sale();
        for(Sale sale: sales){
            if(sale.getId().getPharmacy_id().equals(pharmacyId)
            && sale.getProduct().getProduct_id().equals(productId))
                foundSale = sale;

        }
        return foundSale;
    }


    public Map<Integer, String> findPriceRangesForProducts(List<Product> products) {
        Map<Integer, String> priceRanges = new HashMap<>();
        for (Product product : products) {
            List<Float> prices = findPricesByProductId(product.getProduct_id());
            if (!prices.isEmpty()) {
                float minPrice = Collections.min(prices);
                float maxPrice = Collections.max(prices);
                priceRanges.put(product.getProduct_id(), minPrice +"€" + " - " + maxPrice +"€");
            } else {
                priceRanges.put(product.getProduct_id(), "N/A");
            }
        }
        return priceRanges;
    }

    private List<Float> findPricesByProductId(Integer productId) {
        List<Sale> sales = findAllByProductId(productId);
        List<Float> prices = new ArrayList<>();
        for(Sale sale: sales) {
                prices.add(sale.getPrice());
        }
        return  prices;

    }
}
