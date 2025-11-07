package com.onlyflans.bakery.model;

import com.onlyflans.bakery.model.User.User;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Identificador único de la orden.", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id = UUID.randomUUID().toString();

    @Schema(description = "Fecha de la orden.", example = "2024-05-15")
    private LocalDate fecha;

    @Schema(description = "Estado de la orden.", example = "PENDIENTE")
    private String estado;

    @Schema(description = "Monto total de la orden.", example = "25000")
    private Integer total;

    /* Usuario que pertenece a la orden */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RUT_USUARIO", referencedColumnName = "RUT")
    @Schema(description = "Usuario asociado a la orden.", hidden = true)
    private User user;

    /* Relacion con detalle de compra */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Lista de detalles de la orden.", hidden = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();

}
