package com.example.pricepharmacyproducts.order;

import com.example.pricepharmacyproducts.product.Product;
import com.example.pricepharmacyproducts.sale.Sale;
import com.example.pricepharmacyproducts.sale.SaleDto;
import com.example.pricepharmacyproducts.sale.SaleRepository;
import com.example.pricepharmacyproducts.sale.SaleService;
import com.example.pricepharmacyproducts.simplex.BestPharmacyService;
import com.example.pricepharmacyproducts.simplex.SimplexDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
        model.addAttribute("findBestPharmacy",bestPharmacy);
        return "find_best_pharmacy";
    }


}
