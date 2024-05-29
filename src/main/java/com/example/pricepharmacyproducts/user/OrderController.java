package com.example.pricepharmacyproducts.user;

import com.example.pricepharmacyproducts.sale.SaleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class OrderController {

    private final UserRepository userRepository;
    private final SaleRepository saleRepository;

    public OrderController(UserRepository userRepository, SaleRepository saleRepository) {
        this.userRepository = userRepository;
        this.saleRepository = saleRepository;
    }

    @PostMapping("/placeOrder")
    public Order placeOrder(@RequestBody Order order){
        return  userRepository.save(order);
    }

    @GetMapping("/findAllOrders")
    public List<Order> findAllOrders(){
        return userRepository.findAll();
    }
}
