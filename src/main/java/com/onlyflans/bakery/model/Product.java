package com.onlyflans.bakery.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Table(name = "products")
@Data @AllArgsConstructor @NoArgsConstructor
public class Product {

    @Id
    private String codigo;
    private String categoria;
    private String nombre;
    private String descripcion;
    private Integer precio;
    private String url;
}
