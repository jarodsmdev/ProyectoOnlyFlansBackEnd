package com.onlyflans.bakery.model.mapper;

import com.onlyflans.bakery.model.Order;
import com.onlyflans.bakery.model.dto.response.OrderDTO;
import com.onlyflans.bakery.model.dto.response.OrderDetailDTO;

import java.util.Collections;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderDTO toDTO(Order order){

        if (order == null) return null;

        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setFecha(order.getFecha());
        dto.setEstado(order.getEstado());
        dto.setTotal(order.getTotal());
        dto.setRutUsuario(order.getUser() != null ? order.getUser().getRut() : null);

        // Pasamos order.getId() explícitamente a cada OrderDetailMapper
        dto.setOrderDetails(
                order.getOrderDetails() == null ?
                        Collections.emptyList() :
                        order.getOrderDetails()
                                .stream()
                                .map(orderDetail -> OrderDetailMapper.toDTO(orderDetail, order.getId()))
                                .collect(Collectors.toList())
        );



        return dto;
    }
}
