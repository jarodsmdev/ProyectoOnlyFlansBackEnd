package com.onlyflans.bakery.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "ORDER_DETAILS")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetail {

    @Id
    @Column(length = 36)
    private String id = UUID.randomUUID().toString();

    /* Orden que pertenece este detalle */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    /* Producto que pertenece este detalle */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCTO_CODIGO", referencedColumnName = "CODIGO")
    private Product product;

    private Integer cantidad;
    private Integer subtotal;
}
