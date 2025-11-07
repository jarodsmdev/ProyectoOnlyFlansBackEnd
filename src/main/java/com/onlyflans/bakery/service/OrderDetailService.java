package com.onlyflans.bakery.service;

import com.onlyflans.bakery.model.OrderDetail;
import com.onlyflans.bakery.persistence.IOrderDetailPersistence;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

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

        if (orderDetail.getOrder() == null || orderDetail.getProduct() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Falta información de orden o producto");
        }
       
        // Enlazar ambas partes de la relación
        orderDetail.getOrder().getOrderDetails().add(orderDetail); // El detalle se agrega a la lista dentro del objeto Order.
        orderDetail.setOrder(orderDetail.getOrder()); // Asegura que la referencia del detalle a la orden esté puesta.
 
        return orderDetailPersistence.save(orderDetail);

    }

    public OrderDetail updateOrderDetail(String id, OrderDetail orderDetail){
        OrderDetail orderDetailToUpdate = orderDetailPersistence.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Detalle de orden no encontrada"
                ));

        // Actualizar solo campos con datos
//      if (orderDetail.getId() != null){
//            orderDetailToUpdate.setId(orderDetail.getId());

        if (orderDetail.getOrder() != null){
            orderDetailToUpdate.setOrder(orderDetail.getOrder());
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
                        HttpStatus.NOT_FOUND, "Detalle de orden no encontrada"
                ));
        orderDetailPersistence.delete(orderDetail);
    }
}
