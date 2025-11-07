package com.onlyflans.bakery.model.dto.request;

import com.onlyflans.bakery.model.User.UserRole;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UserRoleUpdateRequest(
    @NotNull(message = "El nuevo rol no puede ser nulo")
    @Schema(description = "Nuevo rol a asignar al usuario.", example = "NORMAL")
    UserRole newRole
) {}
