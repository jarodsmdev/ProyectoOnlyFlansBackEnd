package com.onlyflans.bakery.service;

import com.onlyflans.bakery.model.Order;
import com.onlyflans.bakery.model.OrderDetail;
import com.onlyflans.bakery.model.Product;
import com.onlyflans.bakery.model.User;
import com.onlyflans.bakery.model.dto.request.OrderCreateRequest;
import com.onlyflans.bakery.model.dto.request.OrderUpdateRequest;
import com.onlyflans.bakery.model.dto.response.OrderDTO;
import com.onlyflans.bakery.model.mapper.OrderMapper;
import com.onlyflans.bakery.persistence.IOrderPersistence;
import com.onlyflans.bakery.persistence.IProductPersistence;
import com.onlyflans.bakery.persistence.IUserPersistence;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {

    /* Aparte del repositorio de Order, tambien necesitamos los otros
    * para construir relaciones y mantener un correcto flujo */
    private final IOrderPersistence orderPersistence;
    private final IUserPersistence userPersistence;
    private final IProductPersistence productPersistence;

    public OrderService(IOrderPersistence orderPersistence,
                        IUserPersistence userPersistence,
                        IProductPersistence productPersistence) {
        this.orderPersistence = orderPersistence;
        this.userPersistence = userPersistence;
        this.productPersistence = productPersistence;
    }

    public List<OrderDTO> getAllOrders(){
        /*
        * orderPersistence.findAll() devuelve una List<Order> desde la base de datos.
        * .stream() convierte esa lista en un flujo.
        * .map(OrderMapper::toDTO) aplica el método OrderMapper.toDTO() a cada elemento (convierte de entidad a DTO).
        * .toList() recopila el resultado final como una lista de OrderDTO.
        * */
        return orderPersistence.findAll().stream().map(OrderMapper::toDTO).toList();
    }


    public OrderDTO getOrder(String id){
        Order order =  orderPersistence.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Orden con ID '" + id + "' no encontrada"
                ));
        return OrderMapper.toDTO(order);
    }

    public OrderDTO createOrder(OrderCreateRequest request){
        /* se busca al usuario*/
        User user = userPersistence.findById(request.rutUsuario())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario con rut '" + request.rutUsuario() + "' no encontrado."
                ));

        /* Si existe, creamos la orden base */
        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setFecha(LocalDate.now());
        order.setEstado("PENDIENTE"); //ESTADO INICIAL
        order.setUser(user);

        /* Crear los detalles de la orden */
        int total = 0;
        for (var item : request.items()) { // Se recorre la lista items del request, cada producto del pedido
            // Buscamos el producto por el codigo
            Product product = productPersistence.findById(item.productoCodigo())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Producto con código '" + item.productoCodigo() + "' no encontrado"
                    ));

            OrderDetail detail = new OrderDetail();
            detail.setId(UUID.randomUUID().toString());
            detail.setOrder(order);
            detail.setProduct(product);
            detail.setCantidad(item.cantidad());
            detail.setSubtotal(product.getPrecio() * item.cantidad());
            order.getOrderDetails().add(detail);

            total += detail.getSubtotal();
        }

        order.setTotal(total);
        orderPersistence.save(order);

        return OrderMapper.toDTO(order);

    }

    public OrderDTO updateOrder(String id, OrderUpdateRequest request) {
        Order order = orderPersistence.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Orden no encontrada"
                ));

        if (request.estado() != null) {
            order.setEstado(request.estado());
        }
        if (request.total() != null) {
            order.setTotal(request.total());
        }

        return OrderMapper.toDTO(orderPersistence.save(order));
    }

    public void deleteOrder(String id){
        Order order = orderPersistence.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Orden no encontrada"
                ));
        orderPersistence.delete(order);
    }
}
