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
        dto.setRutUsuario(order.getUser().getRut());

        dto.setOrderDetails(
                order.getOrderDetails() == null ?
                        Collections.emptyList() :
                        order.getOrderDetails().stream().map(orderDetail -> {
                            OrderDetailDTO detailDTO =  new OrderDetailDTO();
                            detailDTO.setId(orderDetail.getId());
                            detailDTO.setProductoCodigo(orderDetail.getProduct().getCodigo());
                            detailDTO.setNombreProducto(orderDetail.getProduct().getNombre());
                            detailDTO.setCantidad(orderDetail.getCantidad());
                            detailDTO.setSubtotal(orderDetail.getSubtotal());
                            return detailDTO;
                        }).collect(Collectors.toList())
        );



        return dto;
    }
}
