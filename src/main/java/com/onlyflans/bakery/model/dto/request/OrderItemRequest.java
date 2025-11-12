package com.onlyflans.bakery.model.dto.request;

/* Se usa dentro de OrderCreateRequest para representar cada producto que se compra. */
public record OrderItemRequest(
        String productoCodigo,
        Integer cantidad
) {}
