package com.onlyflans.bakery.controller.auth;

import com.onlyflans.bakery.model.dto.TokenDTOResponse;
import com.onlyflans.bakery.model.dto.request.LoginRequest;
import com.onlyflans.bakery.model.dto.request.UserCreateRequest;
import com.onlyflans.bakery.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<TokenDTOResponse> register(@Valid @RequestBody final UserCreateRequest request){
        final TokenDTOResponse token = authService.register(request);
        URI location = URI.create("/api/v1/users/" + request.rut());
        return ResponseEntity
                .created(location)
                .body(token);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTOResponse> authenticate(@Valid @RequestBody LoginRequest request){
        final TokenDTOResponse token = authService.login(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenDTOResponse> refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) final String authHeader){
        final TokenDTOResponse token = authService.refreshToken(authHeader);
        return ResponseEntity.ok(token);
    }
}
