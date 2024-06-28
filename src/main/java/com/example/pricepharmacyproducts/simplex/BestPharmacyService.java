package com.example.pricepharmacyproducts.simplex;

import com.example.pricepharmacyproducts.order.Order;
import com.example.pricepharmacyproducts.order.OrderRepository;
import com.example.pricepharmacyproducts.pharmacy.Pharmacy;
import com.example.pricepharmacyproducts.pharmacy.PharmacyRepository;
import com.example.pricepharmacyproducts.product.Product;
import com.example.pricepharmacyproducts.product.ProductRepository;
import com.example.pricepharmacyproducts.sale.Sale;
import com.example.pricepharmacyproducts.sale.SaleDto;
import com.example.pricepharmacyproducts.sale.SaleRepository;
import com.google.ortools.Loader;
import com.google.ortools.init.OrToolsVersion;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BestPharmacyService {


    private final OrderRepository orderRepository;
    private final SaleRepository saleRepository;

    private final PharmacyRepository pharmacyRepository;
    private final ProductRepository productRepository;

    public BestPharmacyService(OrderRepository orderRepository,
                               SaleRepository saleRepository,
                               PharmacyRepository pharmacyRepository,
                               ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.saleRepository = saleRepository;
        this.pharmacyRepository = pharmacyRepository;
        this.productRepository = productRepository;
    }

    public List<SimplexDto> findBestPharmacy(){



        //get back order from repository
        List<Order> orders = orderRepository.findAll();

        //extract sales information from order
        List<Sale> sales = orders.stream()
                .flatMap(order -> order.getSaleList().stream())
                .toList();

        int n_pharmacies = (int) sales.stream().map(Sale::getPharmacy).distinct().count();
        int n_products = (int) sales.stream().map(Sale::getProduct).distinct().count();

        //Initialize Pharmacy and Products index Maps
        Map<Integer,Integer> pharmacyIndexMap = new HashMap<>();
        Map<Integer,Integer> productIndexMap = new HashMap<>();

        int pharmacyIndex = 0;
        int productIndex = 0;

        //Create index Maps
        for(Sale sale: sales){
            int pharmacyId = sale.getPharmacy().getPharmacy_id();
            int productId = sale.getProduct().getProduct_id();

            if(!pharmacyIndexMap.containsKey(pharmacyId))
                pharmacyIndexMap.put(pharmacyId,pharmacyIndex++);
            if(!productIndexMap.containsKey(productId))
                productIndexMap.put(productId,productIndex++);
        }



        //prepare data for optimization
        double[][] product_cost = new double[n_pharmacies][n_products];
        double[] shipping_fees = new double[n_pharmacies];
        double[] free_shipping = new double[n_pharmacies];
        int[] quantity = new int[n_products];

        //initialize matrix and vectors
        for(Sale sale: sales){
            int i = pharmacyIndexMap.get(sale.getPharmacy().getPharmacy_id());
            int j = productIndexMap.get(sale.getProduct().getProduct_id());
            product_cost[i][j] = sale.getPrice();
            shipping_fees[i] = sale.getPharmacy().getShippingFees();
            free_shipping[i] = sale.getPharmacy().getFreeShipping();
        }

        //populate quantities for each product from all orders
        for (Order order: orders){
            for(Sale sale: order.getSaleList()){
                int j = productIndexMap.get(sale.getProduct().getProduct_id());
                quantity[j] = order.getQuantity();
            }
        }

        //solver definition
        MPSolver solver = new MPSolver("ProductOptimization", MPSolver.OptimizationProblemType.CBC_MIXED_INTEGER_PROGRAMMING);

        //Variable definition
        MPVariable[][] x = new MPVariable[n_pharmacies][n_products];
        for(int i=0; i<n_pharmacies; i++)
            for(int j=0; j<n_products; j++)
                x[i][j] = solver.makeNumVar(0, Double.POSITIVE_INFINITY, "x_"+i+"_"+j);

        MPVariable[] y = new MPVariable[n_pharmacies];
        MPVariable[] z = new MPVariable[n_pharmacies];

        for(int i=0; i<n_pharmacies; i++){
            y[i] = solver.makeBoolVar("y_"+i);
            z[i] = solver.makeBoolVar("z_"+i);
        }

        MPObjective objective = solver.objective();
        for(int i=0; i<n_pharmacies; i++) {
            for (int j = 0; j < n_products; j++) {
                objective.setCoefficient(x[i][j], product_cost[i][j]);
            }
            objective.setCoefficient(y[i], shipping_fees[i]);
            objective.setCoefficient(z[i], -shipping_fees[i]);
        }
        objective.setMinimization();

        // demand constraints
        for(int j=0; j<n_products; j++){
            MPConstraint demandConstraint = solver.makeConstraint(quantity[j], quantity[j], "quantity_"+j);
            for(int i=0; i<n_pharmacies; i++)
                demandConstraint.setCoefficient(x[i][j],1);
        }

        // free shipping constraints
        for(int i=0; i<n_pharmacies; i++){
            MPConstraint freeShippingConstraint = solver.makeConstraint(0,Double.POSITIVE_INFINITY,"free_shipping_"+i);
            for(int j=0; j<n_products; j++){
                freeShippingConstraint.setCoefficient(x[i][j], product_cost[i][j]);
            }
            freeShippingConstraint.setCoefficient(z[i], -free_shipping[i]);

        }

        //purchase constraints
        double M = 1e6;
        for(int i=0; i<n_pharmacies; i++){
            MPConstraint purchaseConstraint = solver.makeConstraint(0,Double.POSITIVE_INFINITY, "purchase_"+i);
            for(int j=0; j<n_products; j++){
                purchaseConstraint.setCoefficient(x[i][j], 1);
            }
            purchaseConstraint.setCoefficient(y[i],M);
        }

        //solve the problem
        MPSolver.ResultStatus resultStatus = solver.solve();

        // Recupera i risultati dell'ottimizzazione
        List<SimplexDto> optimizedResults = new ArrayList<>();
        if (resultStatus == MPSolver.ResultStatus.OPTIMAL || resultStatus == MPSolver.ResultStatus.FEASIBLE) {
            for (int i = 0; i < n_pharmacies; i++) {
                for (int j = 0; j < n_products; j++) {
                    if (x[i][j].solutionValue() > 0.5) {
                        SimplexDto simplexDto = new SimplexDto();
                        Pharmacy pharmacy = getPharmacyFromIndex(pharmacyIndexMap, i);
                        Product product = getProductFromIndex(productIndexMap, j);

                        simplexDto.setPharmacy(pharmacy);
                        simplexDto.setProduct(product);
                        simplexDto.setQuantity((int) x[i][j].solutionValue());
                        simplexDto.setCost(product_cost[i][j] * x[i][j].solutionValue());
                        optimizedResults.add(simplexDto);
                    }
                }
            }
        }

        return optimizedResults;
    }

    private Product getProductFromIndex(Map<Integer, Integer> productIndexMap, int index) {
        for (Map.Entry<Integer, Integer> entry : productIndexMap.entrySet()) {
            if (entry.getValue() == index) {
                return productRepository.findById(entry.getKey()).orElse(null);
            }
        }
        return null;
    }

    private Pharmacy getPharmacyFromIndex(Map<Integer, Integer> pharmacyIndexMap, int index) {
        for (Map.Entry<Integer, Integer> entry : pharmacyIndexMap.entrySet()) {
            if (entry.getValue() == index) {
                return pharmacyRepository.findById(entry.getKey()).orElse(null);
            }
        }
        return null;
    }


}
