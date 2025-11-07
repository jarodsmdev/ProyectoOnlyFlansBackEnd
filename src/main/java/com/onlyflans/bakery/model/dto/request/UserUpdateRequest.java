package com.onlyflans.bakery.model.dto.request;


import java.time.LocalDate;

public record UserUpdateRequest (
        String nombres,
        String apellidos,
        LocalDate fechaNacimiento,
        String email,
        String contrasenna
){}
