package com.onlyflans.bakery.model.User.DTO;


import java.time.LocalDate;

public record UserUpdateRequest (
        String nombres,
        String apellidos,
        LocalDate fechaNacimiento,
        String email,
        String contrasenna
){}
