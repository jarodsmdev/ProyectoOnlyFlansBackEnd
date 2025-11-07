package com.onlyflans.bakery.service;

import com.onlyflans.bakery.model.Order;
import com.onlyflans.bakery.model.OrderDetail;
import com.onlyflans.bakery.persistence.IOrderDetailPersistence;
import com.onlyflans.bakery.persistence.IOrderPersistence;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderDetailService {

    private final IOrderDetailPersistence orderDetailPersistence;

    public OrderDetailService(IOrderDetailPersistence orderDetailPersistence) {
        this.orderDetailPersistence = orderDetailPersistence;
    }

    public List<OrderDetail> getAllOrderDetails(){
        return orderDetailPersistence.findAll();
    }

    public OrderDetail getOrderDetail(String id){
        return orderDetailPersistence.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Detalle de orden con ID '" + id + "' no encontrada"
                ));
    }

    public OrderDetail createOrderDetail(OrderDetail orderDetail){
        if (orderDetailPersistence.existsById(orderDetail.getId())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Detalle de orden ya existe"
            );
        }
        return orderDetailPersistence.save(orderDetail);

    }

    public OrderDetail updateOrderDetail(String id, OrderDetail orderDetail){
        OrderDetail orderDetailToUpdate = orderDetailPersistence.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Detalle de orden no encontrada"
                ));

        // Actualizar solo campos con datos
        if (orderDetail.getId() != null){
            orderDetailToUpdate.setId(orderDetail.getId());
        }
        if (orderDetail.getProduct() != null){
            orderDetailToUpdate.setProduct(orderDetail.getProduct());
        }
        if (orderDetail.getCantidad() != null){
            orderDetailToUpdate.setCantidad(orderDetail.getCantidad());
        }
        if (orderDetail.getSubtotal() != null){
            orderDetailToUpdate.setSubtotal(orderDetail.getSubtotal());
        }

        return orderDetailPersistence.save(orderDetailToUpdate);
    }

    public void deleteOrderDetail(String id){
        OrderDetail orderDetail = orderDetailPersistence.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Orden no encontrada"
                ));
        orderDetailPersistence.delete(orderDetail);
    }
}
