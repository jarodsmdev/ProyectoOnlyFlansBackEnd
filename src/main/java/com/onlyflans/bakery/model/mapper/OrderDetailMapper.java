package com.onlyflans.bakery.model.mapper;

import com.onlyflans.bakery.model.OrderDetail;
import com.onlyflans.bakery.model.dto.response.OrderDetailDTO;

public class OrderDetailMapper {

    public static OrderDetailDTO toDTO(OrderDetail orderDetail, String orderId) {
        if (orderDetail == null) return null;

        OrderDetailDTO detailDTO = new OrderDetailDTO();
        detailDTO.setId(orderDetail.getId());
        // usamos el orderId pasado desde el mapper padre, más robusto
        detailDTO.setOrderId(orderId);
        detailDTO.setProductoCodigo(orderDetail.getProduct() != null ? orderDetail.getProduct().getCodigo() : null);
        detailDTO.setNombreProducto(orderDetail.getProduct() != null ? orderDetail.getProduct().getNombre() : null);
        detailDTO.setCantidad(orderDetail.getCantidad());
        detailDTO.setSubtotal(orderDetail.getSubtotal());

        return detailDTO;
    }

    public static OrderDetailDTO toDTO(OrderDetail orderDetail) {
        return orderDetail == null ? null : toDTO(orderDetail, orderDetail.getOrder() != null ? orderDetail.getOrder().getId() : null);
    }

}
