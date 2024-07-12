package com.example.pricepharmacyproducts.order;

import com.example.pricepharmacyproducts.simplex.BestPharmacyService;
import com.example.pricepharmacyproducts.simplex.PharmacyShippingFeesDto;
import com.example.pricepharmacyproducts.simplex.SimplexDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final BestPharmacyService bestPharmacyService;

    public OrderController(OrderService orderService, BestPharmacyService bestPharmacyService) {
        this.orderService = orderService;
        this.bestPharmacyService = bestPharmacyService;
    }

    @GetMapping
    public String showOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);

        return "order_list";
    }

    @GetMapping("/delete/{id}")
    public String delete(
            @PathVariable("id") Integer id
    ){
        orderService.deleteOrderById(id);
        return"redirect:/orders";
    }

    @GetMapping("/find")
    public String findBestOrder(Model model){
        List<SimplexDto> bestPharmacy = bestPharmacyService.findBestPharmacy();
        List<PharmacyShippingFeesDto> pharmacyShipping = bestPharmacyService.calculateShippingFees(bestPharmacy);
        Double totalCost = bestPharmacyService.calculateTotalCost(bestPharmacy,pharmacyShipping);
        model.addAttribute("findBestPharmacy",bestPharmacy);
        model.addAttribute("pharmacyShipping", pharmacyShipping);
        model.addAttribute("totalCost",totalCost);
        return "find_best_pharmacy";
    }


}
