package com.onlyflans.bakery.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.onlyflans.bakery.exception.dto.ErrorResponse;
import com.onlyflans.bakery.model.Token;
import com.onlyflans.bakery.model.User;
import com.onlyflans.bakery.persistence.IUserPersistence;
import com.onlyflans.bakery.persistence.token.TokenRepository;
import com.onlyflans.bakery.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

/**
 * Metodo que se ejecutará una vez por cada petición.
 * 1. Busca el token JWT en el header
 * 2. Valida si no está expirado o revocado
 * 3. Establecer la identidad del usuario dentro del contexto de Spring Security.*/
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    private final IUserPersistence userEntityRepository;


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull  HttpServletResponse response,
            @NonNull  FilterChain filterChain
    ) throws ServletException, IOException {

        // Si la ruta contiene /auth, se deja pasar, aun no hay token
        if(request.getServletPath().contains("/auth")){
            filterChain.doFilter(request, response);
            return;
        }

        // Buscar el encabezado de autorización
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        // Extraer el token JWT del header
        final String jwtToken = authHeader.substring(7);

        String userEmail;

        try {
            userEmail = jwtService.extractUsername(jwtToken);
        } catch (ExpiredJwtException ex) {
            ErrorResponse error = new ErrorResponse(
                    LocalDateTime.now(),
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Unauthorized",
                    "Token expired",
                    request.getRequestURI(),
                    Collections.emptyList()
            );

            writeErrorResponse(response, error);
            return;
        } catch (Exception ex) {
            ErrorResponse error = new ErrorResponse(
                    LocalDateTime.now(),
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Unauthorized",
                    "Invalid or malformed token",
                    request.getRequestURI(),
                    Collections.emptyList()
            );

            writeErrorResponse(response, error);
            return;
        }

        // Verifica que el usuario esté autenticado
        if(userEmail == null || SecurityContextHolder.getContext().getAuthentication() != null){
            filterChain.doFilter(request, response);
            return;
        }

        // Verificar si el token existe y que no esté expirado o revocado
        final Token token = tokenRepository.findByToken(jwtToken)
                .orElse(null);

        if(token == null || token.isExpired() || token.isRevoked()){
            filterChain.doFilter(request, response);
            return;
        }

        // Obtener los detalles del usuario
        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
        final Optional<User> user = userEntityRepository.findByEmail(userDetails.getUsername());

        if(user.isEmpty()){
            filterChain.doFilter(request, response);
            return;
        }

        // Validacion de token: true si el campo user del token y del usuario son iguales
        final boolean isTokenValid = jwtService.isTokenValid(jwtToken, user.get());
        if(!isTokenValid){
            return;
        }

        // EXTRAER ROL DESDE EL JWT
        Claims claims = jwtService.getClaims(jwtToken);
        String role = claims.get("role", String.class);

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

        // CREAR AUTENTICACIÓN CON EL ROL DEL TOKEN
        final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null, // el usuario es validado por el token, no su contraseña
                List.of(authority)
        );

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

    }
    private void writeErrorResponse(HttpServletResponse response, ErrorResponse error)
            throws IOException {

        response.setStatus(error.status());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();

        mapper.writeValue(response.getWriter(), error);
    }
}
