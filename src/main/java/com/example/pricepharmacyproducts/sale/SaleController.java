package com.example.pricepharmacyproducts.sale;

import com.example.pricepharmacyproducts.order.Order;
import com.example.pricepharmacyproducts.order.OrderService;
import com.example.pricepharmacyproducts.product.Product;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/sales")
public class SaleController {

    SaleService saleService;
    OrderService orderService;


    public SaleController(SaleService saleService, OrderService orderService) {
        this.saleService = saleService;
        this.orderService = orderService;
    }

    @GetMapping
    public String findSaleList(Model model){
        model.addAttribute("sale_list",saleService.findSaleList());
        return "sale_list";
    }

    @GetMapping("/home")
    public String productHomePage(Model model, @RequestParam(value = "orderAdded", required = false) Boolean orderAdded) {
        Set<Product> availableProducts = saleService.findAvailableProducts();
        Map<Product, Integer> productMaxQuantityMap = new HashMap<>();

        List<Integer> maxQuantities = saleService.findMaxQuantity(availableProducts);
        int index = 0;
        for (Product product : availableProducts) {
            productMaxQuantityMap.put(product, maxQuantities.get(index));
            index++;
        }
        model.addAttribute("productMaxQuantityMap", productMaxQuantityMap);
        model.addAttribute("orderAdded", orderAdded);
        model.addAttribute("orderProductIds", orderService.getAllOrderProductIds());
        return "home";
    }


    @PostMapping("/insertIntoCart")
    public String addOrder(
            @RequestParam Integer product_id,
            @RequestParam Integer quantity,
            RedirectAttributes redirectAttributes
    ) {
        Order order = new Order();
        List<Sale> sales = saleService.findAllByProductId(product_id);
        order.setSaleList(sales);
        order.setQuantity(quantity);
        orderService.saveOrder(order);

        for (Sale sale : sales) {
            sale.getOrders().add(order);
            saleService.saveSale(sale);// Assicurati che l'ordine sia aggiunto alla lista degli ordini di ciascuna vendita
        }

        redirectAttributes.addFlashAttribute("orderAdded", true);

        return "redirect:/sales/home";
    }

    @PostMapping("/delete")
    public String deleteOrderProduct(
            @RequestParam("product_id") Integer productId,
            RedirectAttributes redirectAttributes
    ){
        orderService.removeProductFromOrder(productId);
        redirectAttributes.addFlashAttribute("orderAdded",false);
        return "redirect:/sales/home";
    }


}
