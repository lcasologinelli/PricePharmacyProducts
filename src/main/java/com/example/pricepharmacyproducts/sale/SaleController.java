package com.example.pricepharmacyproducts.sale;

import com.example.pricepharmacyproducts.order.Order;
import com.example.pricepharmacyproducts.order.OrderService;
import com.example.pricepharmacyproducts.product.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/sales")
public class SaleController {

    private final SaleService saleService;
    private final OrderService orderService;
    private final Map<Integer, Integer> productQuantities = new HashMap<>(); // Mappa per tracciare le quantità dei prodotti

    public SaleController(SaleService saleService, OrderService orderService) {
        this.saleService = saleService;
        this.orderService = orderService;
    }

    @GetMapping
    public String findSaleList(Model model) {
        model.addAttribute("sale_list", saleService.findSaleList());
        return "sale_list";
    }

    @GetMapping("/home")
    public String productHomePage(Model model, @RequestParam(value = "orderAdded", required = false) Boolean orderAdded) {
        List<Product> availableProducts = saleService.findAvailableProducts();
        Map<Product, Integer> productMaxQuantityMap = new LinkedHashMap<>(); // Utilizza LinkedHashMap per preservare l'ordine

        List<Integer> maxQuantities = saleService.findMaxQuantity(availableProducts);
        int index = 0;
        for (Product product : availableProducts) {
            productMaxQuantityMap.put(product, maxQuantities.get(index));
            index++;
        }
        model.addAttribute("productMaxQuantityMap", productMaxQuantityMap);
        model.addAttribute("orderAdded", orderAdded);
        model.addAttribute("orderProductIds", orderService.getAllOrderProductIds());
        model.addAttribute("productQuantities", productQuantities); // Aggiungi la mappa al modello
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
            saleService.saveSale(sale); // Assicurati che l'ordine sia aggiunto alla lista degli ordini di ciascuna vendita
        }

        productQuantities.put(product_id, quantity); // Aggiorna la mappa delle quantità

        redirectAttributes.addFlashAttribute("orderAdded", true);

        return "redirect:/sales/home";
    }

    @PostMapping("/delete")
    public String deleteOrderProduct(
            @RequestParam("product_id") Integer productId,
            RedirectAttributes redirectAttributes
    ) {
        orderService.removeProductFromOrder(productId);
        productQuantities.remove(productId); // Rimuovi il prodotto dalla mappa delle quantità
        redirectAttributes.addFlashAttribute("orderAdded", false);
        return "redirect:/sales/home";
    }
}
