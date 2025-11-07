package com.onlyflans.bakery.controller;

import com.onlyflans.bakery.model.Order;
import com.onlyflans.bakery.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    // GET
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(){
        List<Order> orders = orderService.getAllOrders();
        return orders.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable String id){
        Order order = orderService.getOrder(id);
        return order == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(order);
    }

    //POST
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody @Valid Order order){
        Order orderCreated = orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderCreated);
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String id){
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
