package com.example.pricepharmacyproducts.simplex;

import com.example.pricepharmacyproducts.order.Order;
import com.example.pricepharmacyproducts.order.OrderService;
import com.example.pricepharmacyproducts.pharmacy.PharmacyDto;
import com.example.pricepharmacyproducts.pharmacy.PharmacyMapper;
import com.example.pricepharmacyproducts.pharmacy.PharmacyService;
import com.example.pricepharmacyproducts.product.ProductService;
import com.example.pricepharmacyproducts.sale.Sale;
import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BestPharmacyService {


    private final OrderService orderService;
    private final PharmacyService pharmacyService;
    private final ProductService productService;
    private final PharmacyMapper pharmacyMapper;
    private final PharmacyUtility pharmacyUtility;

    public BestPharmacyService(OrderService orderService,
                               PharmacyService pharmacyService,
                               ProductService productService,
                               PharmacyMapper pharmacyMapper,
                               PharmacyUtility pharmacyUtility) {
        this.orderService = orderService;
        this.pharmacyService = pharmacyService;
        this.productService = productService;
        this.pharmacyMapper = pharmacyMapper;
        this.pharmacyUtility = pharmacyUtility;
    }

    public List<SimplexDto> findBestPharmacy(){

        Loader.loadNativeLibraries();

        //get back order from repository
        List<Order> orders = orderService.findAll();

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

        for(int i=0; i< n_pharmacies;i++)
            for (int j=0; j< n_products; j++)
                if(product_cost[i][j]==0)
                    product_cost[i][j] = Integer.MAX_VALUE;  //use infinity for unavailable products

        //populate quantities for each product from all orders
        for (Order order: orders){
            for(Sale sale: order.getSaleList()){
                int j = productIndexMap.get(sale.getProduct().getProduct_id());
                quantity[j] = order.getQuantity();
            }
        }

        //solver definition
        MPSolver solver = new MPSolver("ProductOptimization", MPSolver.OptimizationProblemType.GLOP_LINEAR_PROGRAMMING);

        //Variable definition
        MPVariable[][] x = new MPVariable[n_pharmacies][n_products];
        MPVariable[] totalCost = new MPVariable[n_pharmacies];

        //creation of variable x_ij
        for(int i=0; i<n_pharmacies; i++)
            for(int j=0; j<n_products; j++)
                x[i][j] = solver.makeBoolVar("x_"+i+"_"+j);

        //creation of vector total_cost
        for(int i=0; i< n_pharmacies; i++)
            totalCost[i] = solver.makeNumVar(0,MPSolver.infinity(),"total_cost"+ i);

        //objective function
        MPObjective objective = solver.objective();

        //first terms:
        for(int i = 0; i< n_pharmacies; i++){
            for(int j=0; j<n_products; j++){
                if(product_cost[i][j] < Double.POSITIVE_INFINITY) {
                    objective.setCoefficient(x[i][j], quantity[j] * product_cost[i][j] + shipping_fees[i]);
                }
            }
        }

        //Constraints

        //Constraint for unique selection product
        for (int j = 0; j < n_products; j++) {
            MPConstraint demandConstraint = solver.makeConstraint(1, 1);
            for (int i = 0; i < n_pharmacies; i++) {
                demandConstraint.setCoefficient(x[i][j], 1);
            }
        }

        //totalCost constraint
        for (int i = 0; i < n_pharmacies; i++) {
            MPConstraint costConstraint = solver.makeConstraint(0, 0, "costConstraint_" + i);
            for (int j = 0; j <n_products; j++) {
                if (product_cost[i][j] < Double.POSITIVE_INFINITY) { // Exclude unavailable products
                    costConstraint.setCoefficient(x[i][j], quantity[j] * product_cost[i][j]);
                }
            }
            costConstraint.setCoefficient(totalCost[i], -1);
        }

        //solve problem: first iteration
        solver.solve();

        int[] noShippingFees = new int[n_pharmacies];

        for(int i=0; i<n_pharmacies; i++){
            double totalCostValue = totalCost[i].solutionValue();
            if(totalCostValue >= free_shipping[i])
                noShippingFees[i] = 1;
            else
                noShippingFees[i] = 0;
        }



        //Create variables for second interaction
        MPVariable[] y = new MPVariable[n_pharmacies];
        MPVariable[] z = new MPVariable[n_pharmacies];

        for(int i=0; i<n_pharmacies; i++){
            y[i] = solver.makeNumVar(0,shipping_fees[i],"y_"+i);
            z[i] = solver.makeBoolVar("z_"+i);
        }

        //Update objective function
        for(int i=0; i<n_pharmacies; i++){
            objective.setCoefficient(y[i], 1);
        }

        //Update constraints
        for (int i = 0; i < n_pharmacies; i++) {
            MPConstraint zConstraint = solver.makeConstraint(0, 1, "z_constraint_" + i);
            zConstraint.setCoefficient(z[i], 1 - noShippingFees[i]);
        }

        for(int i = 0; i< n_pharmacies ; i++){
            double lb = shipping_fees[i] * (1 - noShippingFees[i]);
            MPConstraint yConstraint = solver.makeConstraint(lb, lb, "y_constraint");
            yConstraint.setCoefficient(y[i], 1);
        }

        //solve the problem
        MPSolver.ResultStatus resultStatus = solver.solve();


        List<SimplexDto> bestSolution = new ArrayList<>();
        List<SimplexDto> comparableResult = new ArrayList<>();

        //collecting results
        if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {

            for(int i=0; i<n_pharmacies; i++)
                for (int j=0; j<n_products; j++) {
                    if (x[i][j].solutionValue() == 1 && y[i].solutionValue() == 0 && totalCost[i].solutionValue()!=0) {
                        int finalI = i;
                        int finalJ = j;
                        SimplexDto dto = new SimplexDto(
                                pharmacyService.findPharmacyById(pharmacyIndexMap.entrySet().stream()
                                        .filter(entry -> entry.getValue().equals(finalI))
                                        .map(Map.Entry::getKey)
                                        .findFirst()
                                        .orElseThrow()),
                                productService.findProductById(productIndexMap.entrySet().stream()
                                        .filter(entry -> entry.getValue().equals(finalJ))
                                        .map(Map.Entry::getKey)
                                        .findFirst()
                                        .orElseThrow()),
                                quantity[j],
                                quantity[j] * product_cost[i][j],
                                0
                        );
                        bestSolution.add(dto);
                    }

                    else if(x[i][j].solutionValue() == 1 && y[i].solutionValue() > 0){
                        int finalI1 = i;
                        int finalJ1 = j;
                        SimplexDto dto = new SimplexDto(
                                pharmacyService.findPharmacyById(pharmacyIndexMap.entrySet().stream()
                                        .filter(entry -> entry.getValue().equals(finalI1))
                                        .map(Map.Entry::getKey)
                                        .findFirst()
                                        .orElseThrow()),
                                productService.findProductById(productIndexMap.entrySet().stream()
                                        .filter(entry -> entry.getValue().equals(finalJ1))
                                        .map(Map.Entry::getKey)
                                        .findFirst()
                                        .orElseThrow()),
                                quantity[j],
                                (quantity[j] * product_cost[i][j]),
                                y[i].solutionValue()*(1-z[i].solutionValue())
                        );
                        comparableResult.add(dto);
                    }
                }

            List<Map.Entry<Integer, Set<Integer>>> ids = pharmacyUtility.extractPharmaciesForProducts(bestSolution,comparableResult);

            List<Sale> comparableSales = pharmacyUtility.extractSalesFromProductPharmacyEntries(ids);

            List<AggregatedPharmacyDto> aggregatedPharmacyDtos = pharmacyUtility.aggregatePharmacies(comparableResult);

            System.out.println("Aggregated:");
            for(AggregatedPharmacyDto pri: aggregatedPharmacyDtos) {
                System.out.println(pri);
                System.out.println();
            }

            SimplexDto min;

            for(SimplexDto result :comparableResult){
                min = result;
                for (Sale sale : comparableSales)
                    if(Objects.equals(result.getProduct().getProduct_id(), sale.getId().getProduct_id()) &&
                            result.getQuantity()*sale.getPrice() < result.getQuantity()*result.getCost()+ result.getShippingFees()
                            )
                        min = new SimplexDto(pharmacyMapper.toDto(sale.getPharmacy()), sale.getProduct(), result.getQuantity(),result.getQuantity()*sale.getPrice(),0);
                bestSolution.add(min);
            }

            List<SimplexDto> finalSolution = pharmacyUtility.updateSimplexDtosWithAggregates(aggregatedPharmacyDtos,bestSolution);

            for (SimplexDto simplexDto: finalSolution)
                System.out.println(simplexDto);

            return finalSolution;
       }

        return null;
    }

    public List<PharmacyShippingFeesDto> calculateShippingFees(List<SimplexDto> bestPharmacy) {
        // Lista di ritorno
        List<PharmacyShippingFeesDto> pharmacyShippingFeesList = new ArrayList<>();

        // Insieme per tracciare gli ID delle farmacie già aggiunte
        Set<Integer> pharmacyIds = new HashSet<>();

        // Iteriamo sulla lista di SimplexDto
        for (SimplexDto simplexDto : bestPharmacy) {
            PharmacyDto pharmacy = simplexDto.getPharmacy();

            // Controlliamo se l'ID della farmacia è già presente nell'insieme
            if (!pharmacyIds.contains(pharmacy.getId())) {
                // Aggiungiamo l'ID della farmacia all'insieme
                pharmacyIds.add(pharmacy.getId());

                // Creiamo un nuovo PharmacyShippingFeesDto
                PharmacyShippingFeesDto pharmacyShippingFeesDto = new PharmacyShippingFeesDto();
                pharmacyShippingFeesDto.setPharmacy(pharmacy);
                pharmacyShippingFeesDto.setShippingFees(simplexDto.getShippingFees());

                // Aggiungiamo il nuovo PharmacyShippingFeesDto alla lista di ritorno
                pharmacyShippingFeesList.add(pharmacyShippingFeesDto);
            }
        }

        return pharmacyShippingFeesList;
    }

    public Double calculateTotalCost(List<SimplexDto> bestPharmacy, List<PharmacyShippingFeesDto> pharmacyShipping) {
        double totalCost = 0;
        for(SimplexDto dto: bestPharmacy){
            totalCost += dto.getCost();
        }
        for (PharmacyShippingFeesDto pharmacy : pharmacyShipping ){
            totalCost += pharmacy.getShippingFees();
        }

        return totalCost;
    }
}
