package com.onlyflans.bakery.model.dto;

import lombok.Data;

@Data
public class OrderSummaryDTO {
    private String id;
    private String estado;
    private Integer total;
}