package com.onlyflans.bakery.model.dto;


import java.time.LocalDate;

public record UserUpdateRequest (
        String nombres,
        String apellidos,
        LocalDate fechaNacimiento,
        String email,
        String contrasenna
){}
