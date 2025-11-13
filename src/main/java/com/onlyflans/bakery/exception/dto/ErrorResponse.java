package com.onlyflans.bakery.exception.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Representa la estructura estándar de respuesta de error.
 * Será serializado automáticamente a JSON por Spring.
 * @param timestamp
 * @param status
 * @param error
 * @param message
 * @param path
 */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldErrorDetail> errors
) {
    public ErrorResponse(
            LocalDateTime timestamp,
            int status,
            String error,
            String message,
            String path) {
        this(timestamp, status, error, message, path, Collections.emptyList());
    }
}
