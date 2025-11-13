package com.onlyflans.bakery.config;

import com.onlyflans.bakery.persistence.token.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Clase de configuración para Spring Security.
 * @Configuration indica que esta clase es una fuente de definiciones de beans.
 * @EnableWebSecurity habilita la configuración de seguridad web de Spring Security.
 * @EnableMethodSecurity permite la seguridad a nivel de método mediante anotaciones.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Al agregar esta anotación, se permite el uso de anotaciones
@RequiredArgsConstructor
// de seguridad como PreAuthorize o PostAuthorize en los métodos de los controladores. 
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final TokenRepository tokenRepository;

    /**
     * Define la cadena de filtros de seguridad que se aplicará a las solicitudes HTTP.
     * @param http el objeto HttpSecurity para configurar la seguridad web.
     * @return el SecurityFilterChain construido.
     * @throws Exception si ocurre un error durante la configuración.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                // Deshabilita la protección contra Cross-Site Request Forgery (CSRF).
                // CSRF es un ataque que obliga a un usuario final a ejecutar acciones no deseadas en una aplicación web en la que está autenticado.
                // A menudo se deshabilita para APIs sin estado (stateless) donde no se usan cookies de sesión.
                .csrf(AbstractHttpConfigurer::disable)

                // Configura las reglas de autorización para las solicitudes HTTP.
                .authorizeHttpRequests(req -> req
                        .requestMatchers(
                                "/auth/**",
                                "/h2-console/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // Necesario para frames de H2 Console
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                );

        // Construye y devuelve el objeto SecurityFilterChain.
        return http.build();

    }
}
