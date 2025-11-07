package com.onlyflans.bakery.model.dto;

import lombok.Data;

@Data
public class OrderDetailDTO {
    private String id;
    private String orderId;
    private String productoCodigo;
    private String nombreProducto;
    private Integer cantidad;
    private Integer subtotal;
}
