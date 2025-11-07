package com.onlyflans.bakery.model.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private String codigo;
    private String categoria;
    private String nombre;
    private String descripcion;
    private Integer precio;
    private String url;
}
