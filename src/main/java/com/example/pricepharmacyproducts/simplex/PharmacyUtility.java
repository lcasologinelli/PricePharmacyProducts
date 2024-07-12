package com.example.pricepharmacyproducts.simplex;

import com.example.pricepharmacyproducts.pharmacy.PharmacyDto;
import com.example.pricepharmacyproducts.product.Product;
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

    public List<AggregatedPharmacyDto> aggregatePharmacies(List<SimplexDto> simplexDtos) {
        // Mappa per aggregare i dati in base all'ID della farmacia
        Map<Integer, List<SimplexDto>> pharmacyIdToSimplexDtosMap = new HashMap<>();

        // Itera attraverso ogni SimplexDto nella lista simplexDtos
        for (SimplexDto dto : simplexDtos) {
            Integer pharmacyId = dto.getPharmacy().getId();

            // Verifica se l'ID della farmacia è già presente nella mappa
            if (!pharmacyIdToSimplexDtosMap.containsKey(pharmacyId)) {
                pharmacyIdToSimplexDtosMap.put(pharmacyId, new ArrayList<>());  // Se non è presente, aggiungi una nuova lista vuota
            }

            // Aggiungi il SimplexDto corrente alla lista della farmacia nella mappa
            pharmacyIdToSimplexDtosMap.get(pharmacyId).add(dto);
        }

        // Lista per i risultati aggregati
        List<AggregatedPharmacyDto> aggregatedList = new ArrayList<>();

        // Itera attraverso ogni entry nella mappa pharmacyIdToSimplexDtosMap
        for (Map.Entry<Integer, List<SimplexDto>> entry : pharmacyIdToSimplexDtosMap.entrySet()) {
            Integer pharmacyId = entry.getKey();
            List<SimplexDto> dtos = entry.getValue();

            // Trova il PharmacyDto corrispondente all'ID dalla lista simplexDtos
            PharmacyDto pharmacy = null;
            for (SimplexDto dto : simplexDtos) {
                if (dto.getPharmacy().getId().equals(pharmacyId)) {
                    pharmacy = dto.getPharmacy();
                    break;
                }
            }

            // Se la farmacia è null (non dovrebbe succedere), passa alla prossima iterazione
            if (pharmacy == null) {
                continue;
            }

            // Se ci sono più elementi con la stessa farmacia, crea un AggregatedPharmacyDto
            if (dtos.size() > 1) {
                List<Product> products = new ArrayList<>();
                List<Integer> quantities = new ArrayList<>();
                List<Double> costs = new ArrayList<>();
                double shippingFees = pharmacy.getShippingFees();

                for (SimplexDto dto : dtos) {
                    products.add(dto.getProduct());
                    quantities.add(dto.getQuantity());
                    costs.add(dto.getCost());
                }

                aggregatedList.add(new AggregatedPharmacyDto(pharmacy, products, quantities, costs, shippingFees));
            }
        }

        // Rimuovi i SimplexDto duplicati dalla lista originale simplexDtos
//        simplexDtos.removeIf(dto -> pharmacyIdToSimplexDtosMap.get(dto.getPharmacy().getId()).size() > 1);

        return aggregatedList;
    }

    public List<SimplexDto> updateSimplexDtosWithAggregates(List<AggregatedPharmacyDto> aggregateList, List<SimplexDto> simplexList) {
        // Itera attraverso la lista degli oggetti aggregati
        for (AggregatedPharmacyDto aggregate : aggregateList) {
            List<Product> aggregateProducts = aggregate.getProducts();
            List<Integer> aggregateQuantities = aggregate.getQuantities();
            List<Double> aggregateCosts = aggregate.getCosts();
            double aggregateShippingFees = aggregate.getShippingFees();

            double aggregateTotalCost = aggregateCosts.stream().mapToDouble(Double::doubleValue).sum() + aggregateShippingFees;

            // Trova gli elementi corrispondenti nella lista SimplexDto
            List<SimplexDto> matchedSimplexDtos = new ArrayList<>();
            for (Product aggregateProduct : aggregateProducts) {
                Integer aggregateProductId = aggregateProduct.getProduct_id();

                for (SimplexDto simplex : simplexList) {
                    if (simplex.getProduct().getProduct_id().equals(aggregateProductId)) {
                        matchedSimplexDtos.add(simplex);
                    }
                }
            }

            // Se il numero di elementi corrispondenti nella lista SimplexDto è uguale al numero di prodotti nell'aggregato
            if (matchedSimplexDtos.size() == aggregateProducts.size()) {
                double simplexTotalCost = matchedSimplexDtos.stream().mapToDouble(SimplexDto::getCost).sum()
                        + matchedSimplexDtos.get(0).getShippingFees();

                // Confronta i costi
                if (aggregateTotalCost < simplexTotalCost) {
                    // Rimuovi i vecchi SimplexDto dalla lista
                    simplexList.removeAll(matchedSimplexDtos);

                    // Aggiungi nuovi SimplexDto con i valori di Aggregate
                    for (int j = 0; j < aggregateProducts.size(); j++) {
                        SimplexDto newSimplex = new SimplexDto();
                        newSimplex.setPharmacy(aggregate.getPharmacy());
                        newSimplex.setProduct(aggregateProducts.get(j));
                        newSimplex.setQuantity(aggregateQuantities.get(j));
                        newSimplex.setCost(aggregateCosts.get(j));
                        newSimplex.setShippingFees(aggregateShippingFees);

                        // Aggiungi il nuovo SimplexDto alla lista
                        simplexList.add(newSimplex);
                    }
                }
            }
        }

        return simplexList;
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
