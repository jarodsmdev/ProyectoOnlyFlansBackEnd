package com.onlyflans.bakery.controller.auth;

import com.onlyflans.bakery.model.dto.TokenDTOResponse;
import com.onlyflans.bakery.model.dto.request.LoginRequest;
import com.onlyflans.bakery.model.dto.request.UserCreateRequest;
import com.onlyflans.bakery.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Registrar un nuevo usuario", description = "Registra un nuevo usuario en la panadería OnlyFlans")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos para registrar el usuario"),
            @ApiResponse(responseCode = "409", description = "El usuario ya existe"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar registrar el usuario")
    })
    public ResponseEntity<TokenDTOResponse> register(@Valid @RequestBody final UserCreateRequest request){
        final TokenDTOResponse token = authService.register(request);
        URI location = URI.create("/api/v1/users/" + request.rut());
        return ResponseEntity
                .created(location)
                .body(token);
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar un usuario", description = "Autentica un usuario y genera un token JWT para acceder a la panadería OnlyFlans")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario autenticado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos para autenticar el usuario"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar autenticar el usuario")
    })
    public ResponseEntity<TokenDTOResponse> authenticate(@Valid @RequestBody LoginRequest request){
        final TokenDTOResponse token = authService.login(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refrescar el token JWT", description = "Genera un nuevo token JWT utilizando un token de refresco válido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refrescado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Header Authorization no proporcionado"),
            @ApiResponse(responseCode = "401", description = "Token de refresco inválido o expirado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar refrescar el token")
    })
    public ResponseEntity<TokenDTOResponse> refreshToken(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) final String authHeader){
        if (authHeader == null) {
            throw new IllegalArgumentException("El header Authorization es obligatorio");
        }
        final TokenDTOResponse token = authService.refreshToken(authHeader);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/logout")
    @Operation(summary = "Cerrar sesión de un usuario", description = "Cierra la sesión del usuario invalidando su token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario deslogueado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Header Authorization no proporcionado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar desloguear el usuario")
    })
    public ResponseEntity<?> logout(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) final String authHeader){
        if(authHeader == null){
            throw new IllegalArgumentException("El header Authorization es obligatorio");
        }
        authService.logout(authHeader);
        return ResponseEntity.ok(
                Map.of(
                        "timestamp", System.currentTimeMillis(),
                        "status", 200,
                        "error", "OK",
                        "message", "Logout successful",
                        "path", "/auth/logout"
                )
        );
    }
}
