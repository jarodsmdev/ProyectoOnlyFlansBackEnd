package com.onlyflans.bakery.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "ORDER_DETAILS")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetail {

    @Id
    @Column(length = 36)
    @Schema(description = "Identificador único del detalle de la orden.", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id = UUID.randomUUID().toString();

    /* Orden que pertenece este detalle */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    @Schema(description = "Orden asociada al detalle.")
    private Order order;

    /* Producto que pertenece este detalle */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCTO_CODIGO", referencedColumnName = "CODIGO")
    @Schema(description = "Producto asociado al detalle.")
    private Product product;

    @Schema(description = "Cantidad del producto en el detalle de la orden.", example = "2")
    private Integer cantidad;

    @Schema(description = "Subtotal del detalle de la orden.", example = "90000")
    private Integer subtotal;
}
