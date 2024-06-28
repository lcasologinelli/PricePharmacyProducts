package com.example.pricepharmacyproducts.order;

import com.example.pricepharmacyproducts.sale.Sale;
import jakarta.transaction.Transactional;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void saveOrder(Order order){
        orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void deleteOrderById(Integer id){
        orderRepository.deleteById(id);
    }

    public List<Integer> getAllOrderProductIds() {
        return getAllOrders().stream()
                .flatMap(order -> order.getSaleList().stream())
                .map(sale -> sale.getProduct().getProduct_id())
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeProductFromOrder(Integer productId) {
        List<Order> orders = orderRepository.findAll();

        for(Order order: orders){

            List<Sale> sales = order.getSaleList();
            sales.removeIf(sale -> sale.getProduct().getProduct_id().equals(productId));
            order.setSaleList(sales);
            orderRepository.save(order);
            if(order.getSaleList().isEmpty())
                orderRepository.deleteById(order.getOrder_id());
        }
    }
}
