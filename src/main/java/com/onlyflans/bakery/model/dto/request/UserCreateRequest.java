package com.onlyflans.bakery.model.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

import com.onlyflans.bakery.model.UserRole;

public record UserCreateRequest(
        @NotBlank(message = "El RUT es obligatorio")
        String rut,

        @NotBlank(message = "Los nombres son obligatorios")
        @Size(min = 2, max = 50)
        String nombres,

        @NotBlank(message = "Los apellidos son obligatorios")
        @Size(min = 2, max = 50)
        String apellidos,

        @NotNull(message = "La fecha de nacimiento es obligatoria")
        @Past
        LocalDate fechaNacimiento,

        @NotBlank(message = "El email es obligatorio")
        @Email
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 6)
        String contrasenna,

        @NotNull(message = "El rol de usuario es obligatorio")
        UserRole userRole
) {}
