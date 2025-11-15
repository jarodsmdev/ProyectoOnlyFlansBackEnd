// Swagger by Luis :3

package com.onlyflans.bakery.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
        private static final String SCHEME_NAME = "bearerAuth"; // Nombre de nuestro esquema
        private static final String SCHEME = "Bearer";
    @Bean
    public OpenAPI customOpenAPI() {

        String descripcion = """
            Documentación de la API para el proyecto de la página <b>OnlyFlans 🍰</b><br><br>
            <b>Desarrollado por: </b>Leonel Briones, Luis Maulen y Andres Ortega<br>
            <a href='mailto:contacto@onlyflans.cl'>contacto@onlyflans.cl</a> | 
            <a href='https://onlyflans2.netlify.app' target="_blank">https://onlyflans2.netlify.app</a>
            """;

        return new OpenAPI()
                // Aplicar la seguridad a toda la API por defecto
                .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME)) 
                // Definir cómo funciona el esquema de seguridad
                .components(new Components()
                        .addSecuritySchemes(SCHEME_NAME, createSecurityScheme(SCHEME_NAME, SCHEME))) 
                .info(new Info()
                        .title("API OnlyFlans Project")
                        .version("1.0.6")
                        .description(descripcion)
                );
    }

    /**
     * Define el Security Scheme para que Swagger sepa cómo manejar los tokens JWT.
     */
    private SecurityScheme createSecurityScheme(String schemeName, String scheme) {
        return new SecurityScheme()
                .name(schemeName)
                .type(SecurityScheme.Type.HTTP)
                .scheme(scheme)
                .bearerFormat("JWT")
                .description("Token JWT para autenticación. Debe ser generado por el endpoint /auth/login y pegado sin el prefijo 'Bearer '.");
    }

}
