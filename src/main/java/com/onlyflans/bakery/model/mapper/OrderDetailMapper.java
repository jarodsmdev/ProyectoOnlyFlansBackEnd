package com.onlyflans.bakery.model.mapper;

import com.onlyflans.bakery.model.OrderDetail;
import com.onlyflans.bakery.model.dto.OrderDetailDTO;

public class OrderDetailMapper {

    public static OrderDetailDTO toDTO(OrderDetail orderDetail){

        if (orderDetail == null) return null;
        OrderDetailDTO detailDTO = new OrderDetailDTO();
        detailDTO.setId(orderDetail.getId());
        detailDTO.setOrderId(orderDetail.getOrder() != null ? orderDetail.getOrder().getId() : null);
        detailDTO.setProductoCodigo(orderDetail.getProduct() != null ? orderDetail.getProduct().getCodigo() : null);
        detailDTO.setCantidad(orderDetail.getCantidad());
        detailDTO.setSubtotal(orderDetail.getSubtotal());

        return detailDTO;
    }
}
