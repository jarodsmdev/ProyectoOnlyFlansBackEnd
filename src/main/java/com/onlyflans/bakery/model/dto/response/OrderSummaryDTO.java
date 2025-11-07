package com.onlyflans.bakery.model.dto.response;

import lombok.Data;

@Data
public class OrderSummaryDTO {
    private String id;
    private String estado;
    private Integer total;
}