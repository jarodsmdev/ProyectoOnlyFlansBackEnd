package com.onlyflans.bakery.config;

import com.onlyflans.bakery.model.User;
import com.onlyflans.bakery.persistence.IUserPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
/**
 * Clase de configuración de Spring para la codificación de contraseñas.
 * La anotación @Configuration le indica a Spring que esta es una clase de configuración
 * que puede contener definiciones de beans.
 */
public class AppConfig {
    private final IUserPersistence userRepository;

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> {
            final User user = userRepository.findByEmail(username)
                    .orElseThrow( () -> new UsernameNotFoundException("Username '" + username + "' not found"));
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getContrasenna())
                    .build();
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    /**
     * Define un bean de PasswordEncoder que se utilizará para codificar las contraseñas en la aplicación.
     * La anotación @Bean le dice a Spring que este método producirá un bean para ser administrado por el contenedor de Spring.
     *
     * @return una instancia de PasswordEncoder, específicamente BCryptPasswordEncoder.
     */
    public PasswordEncoder passwordEncoder(){
        // BCryptPasswordEncoder es una implementación de PasswordEncoder que utiliza el algoritmo de hashing BCrypt.
        // BCrypt es una opción fuerte y segura para el hashing de contraseñas, ya que incluye un "salt" aleatorio
        // para protegerse contra ataques de tablas rainbow.
        return new BCryptPasswordEncoder();
        }
}
