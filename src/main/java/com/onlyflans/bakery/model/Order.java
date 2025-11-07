package com.onlyflans.bakery.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ORDERS")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {

    @Id
    @Column(length = 36)
    private String id = UUID.randomUUID().toString();

    private LocalDate fecha;
    private String estado;
    private Integer total;

    /* Usuario que pertenece a la orden */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RUT_USUARIO", referencedColumnName = "RUT")
    private User user;

    /* Relacion con detalle de compra */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();

}
