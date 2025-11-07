package com.onlyflans.bakery.service;

import com.onlyflans.bakery.model.Order;
import com.onlyflans.bakery.persistence.IOrderPersistence;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {

    private final IOrderPersistence orderPersistence;

    public OrderService(IOrderPersistence orderPersistence) {
        this.orderPersistence = orderPersistence;
    }

    public List<Order> getAllOrders(){
        return orderPersistence.findAll();
    }

    public Order getOrder(UUID id){
        return orderPersistence.findById(id.toString())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Orden con ID '" + id + "' no encontrada"
                ));
    }

    public Order createOrder(Order order){
        if (orderPersistence.existsById(order.getId())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Orden ya existe"
            );
        }
        return orderPersistence.save(order);

    }

    public Order updateOrder(UUID id, Order order){
        Order orderToUpdate = orderPersistence.findById(id.toString())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Orden no encontrada"
                ));

        // Actualizar solo campos con datos
        if (order.getFecha() != null){
            orderToUpdate.setFecha(order.getFecha());
        }
        if (order.getEstado() != null){
            orderToUpdate.setEstado(order.getEstado());
        }
        if (order.getTotal() != null){
            orderToUpdate.setTotal(order.getTotal());
        }
        if (order.getUser() != null){
            orderToUpdate.setUser(order.getUser());
        }

        return orderPersistence.save(orderToUpdate);
    }

    public void deleteOrder(UUID id){
        Order order = orderPersistence.findById(id.toString())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Orden no encontrada"
                ));
        orderPersistence.delete(order);
    }
}
