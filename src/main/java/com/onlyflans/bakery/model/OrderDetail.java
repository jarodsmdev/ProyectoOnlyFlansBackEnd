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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID orderId;
    private String productoCodigo;
    private int cantidad;
    private int subtotal;
}
