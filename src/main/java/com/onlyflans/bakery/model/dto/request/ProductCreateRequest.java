package com.onlyflans.bakery.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductCreateRequest(

    @NotNull(message = "El código es obligatorio")
    String codigo,

    @NotBlank(message = "La categoría es obligatoria")
    String categoria,

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100)
    String nombre,

    @Size(max = 500)
    String descripcion,

    @NotNull(message = "El precio es obligatorio")
    @Min(value = 1, message = "El precio debe ser positivo")
    Integer precio

//    @NotBlank(message = "La URL de la imagen es obligatoria")
//    String url

){
}
