package com.onlyflans.bakery.model.dto.request;

import java.util.List;


public record OrderCreateRequest(
        String rutUsuario,
        List<OrderItemRequest> items
) {}
