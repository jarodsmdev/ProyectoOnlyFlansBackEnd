package com.onlyflans.bakery.model.dto.response;

import lombok.*;

@Setter @Getter
public class ProductDTO {
    private String codigo;
    private String categoria;
    private String nombre;
    private String descripcion;
    private Integer precio;
    private String url;
}
