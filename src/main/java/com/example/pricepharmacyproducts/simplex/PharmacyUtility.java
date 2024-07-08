package com.example.pricepharmacyproducts.simplex;

import com.example.pricepharmacyproducts.sale.Sale;
import com.example.pricepharmacyproducts.sale.SaleDto;
import com.example.pricepharmacyproducts.sale.SaleService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PharmacyUtility {

    private final SaleService saleService;

    public PharmacyUtility(SaleService saleService) {
        this.saleService = saleService;
    }

    public List<Map.Entry<Integer, Set<Integer>>> extractPharmaciesForProducts(
            List<SimplexDto> bestSolution,
            List<SimplexDto> comparableResult) {

        List<Map.Entry<Integer, Set<Integer>>> productToPharmaciesList = new ArrayList<>();

        // Itera attraverso la lista dei prodotti comparabili
        for (SimplexDto c_dto : comparableResult) {
            Integer c_productId = c_dto.getProduct().getProduct_id();
            Map<Integer, Set<Integer>> tempMap = getIntegerListMap(bestSolution, c_productId);

            // Aggiungi l'entry della mappa temporanea alla lista finale
            productToPharmaciesList.addAll(tempMap.entrySet());
        }

        return productToPharmaciesList;
    }

    private static Map<Integer, Set<Integer>> getIntegerListMap(List<SimplexDto> bestSolution, Integer c_productId) {
        Set<Integer> ids = new HashSet<>();

        // Itera attraverso la bestSolution per trovare le farmacie che vendono il prodotto comparabile
        for (SimplexDto dto : bestSolution) {
            Integer pharmacyId = dto.getPharmacy().getId();

            // Verifica se la farmacia vende il prodotto comparabile
            for (SaleDto sale : dto.getPharmacy().getSales()) {
                if (sale.getProduct_id().equals(c_productId)) {
                    ids.add(pharmacyId);
                }
            }
        }

        // Crea una mappa temporanea per la coppia prodotto-farmacie
        Map<Integer, Set<Integer>> tempMap = new HashMap<>();
        tempMap.put(c_productId, ids);
        return tempMap;
    }


    public List<Sale> extractSalesFromProductPharmacyEntries(List<Map.Entry<Integer, Set<Integer>>> productPharmacyEntries) {
        // Lista per memorizzare tutti gli oggetti Sale trovati
        List<Sale> salesList = new ArrayList<>();

        // Iteriamo attraverso l'elenco di entry
        for (Map.Entry<Integer, Set<Integer>> entry : productPharmacyEntries) {
            Integer productId = entry.getKey();
            Set<Integer> pharmacyIds = entry.getValue();

            // Per ogni farmacia, troviamo le vendite del prodotto
            for (Integer pharmacyId : pharmacyIds) {
                Sale sale = saleService.findByProductIdAndPharmacyId(productId, pharmacyId);

                // Aggiungiamo le vendite alla lista principale
                salesList.add(sale);
            }
        }

        return salesList;
    }
}
