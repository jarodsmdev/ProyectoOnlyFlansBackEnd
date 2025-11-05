package com.onlyflans.bakery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Clase de configuración de Spring para la codificación de contraseñas.
 * La anotación @Configuration le indica a Spring que esta es una clase de configuración
 * que puede contener definiciones de beans.
 */
@Configuration
public class PasswordConfig {

    /**
     * Define un bean de PasswordEncoder que se utilizará para codificar las contraseñas en la aplicación.
     * La anotación @Bean le dice a Spring que este método producirá un bean para ser administrado por el contenedor de Spring.
     *
     * @return una instancia de PasswordEncoder, específicamente BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        // BCryptPasswordEncoder es una implementación de PasswordEncoder que utiliza el algoritmo de hashing BCrypt.
        // BCrypt es una opción fuerte y segura para el hashing de contraseñas, ya que incluye un "salt" aleatorio
        // para protegerse contra ataques de tablas rainbow.
        return new BCryptPasswordEncoder();
    }
}
