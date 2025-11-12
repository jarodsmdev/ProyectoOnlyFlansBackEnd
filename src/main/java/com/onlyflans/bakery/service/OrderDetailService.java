package com.onlyflans.bakery.service;

import com.onlyflans.bakery.model.Order;
import com.onlyflans.bakery.model.OrderDetail;
import com.onlyflans.bakery.model.Product;
import com.onlyflans.bakery.model.dto.request.OrderItemRequest;
import com.onlyflans.bakery.model.dto.response.OrderDetailDTO;
import com.onlyflans.bakery.model.mapper.OrderDetailMapper;
import com.onlyflans.bakery.persistence.IOrderDetailPersistence;
import com.onlyflans.bakery.persistence.IOrderPersistence;
import com.onlyflans.bakery.persistence.IProductPersistence;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class OrderDetailService {

    /* Se agregan dependecias de Order y Product para verificar la existencia
    * real en la bd de orden y producto antes de crear el detalle */
    private final IOrderDetailPersistence orderDetailPersistence;
    private final IOrderPersistence orderPersistence;
    private final IProductPersistence productPersistence;

    public OrderDetailService(
            IOrderDetailPersistence orderDetailPersistence,
            IOrderPersistence orderPersistence,
            IProductPersistence productPersistence
    ) {
        this.orderDetailPersistence = orderDetailPersistence;
        this.orderPersistence = orderPersistence;
        this.productPersistence = productPersistence;
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

    public OrderDetailDTO createOrderDetail(String orderId, OrderItemRequest request){
        // verificar que la orden exista
        Order order = orderPersistence.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Orden no encontrada"
                ));

        // verificar que el producto exista
        Product product = productPersistence.findById(request.productoCodigo())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado"
                ));

        // con todo ya listo, creamos el detalle de orden
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId(UUID.randomUUID().toString());
        orderDetail.setOrder(order);
        orderDetail.setProduct(product);
        orderDetail.setCantidad(request.cantidad());
        orderDetail.setSubtotal(product.getPrecio() * request.cantidad());

        // añadimos el detalle de orden a la orden que pertenece
        order.getOrderDetails().add(orderDetail);

        // guardar
        orderDetailPersistence.save(orderDetail);

        // lo convertimos a DTO y lo devolvemos
        return OrderDetailMapper.toDTO(orderDetail);


    }

    public OrderDetailDTO updateOrderDetail(String orderItemId, OrderItemRequest request) {
        // verificar que el detalle de orden exista
        OrderDetail orderDetail = orderDetailPersistence.findById(orderItemId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Detalle de orden no encontrada"
                ));

        // buscamos el producto que ya existe
        Product product = productPersistence.findById(orderDetail.getProduct().getCodigo())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado"
                ));

        // Actualizar solo campos con datos, en este caso, solo cantidad
        if (request.cantidad() != null){
            // si cambia la cantidad, cambia el subtotal
            orderDetail.setCantidad(request.cantidad());
            orderDetail.setSubtotal(product.getPrecio() * request.cantidad());
        }

        //guardar
        orderDetailPersistence.save(orderDetail);

        // devolver como DTO
        return OrderDetailMapper.toDTO(orderDetail);
    }

    public void deleteOrderDetail(String id){
        OrderDetail orderDetail = orderDetailPersistence.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Detalle de orden no encontrada"
                ));
        orderDetailPersistence.delete(orderDetail);
    }
}
