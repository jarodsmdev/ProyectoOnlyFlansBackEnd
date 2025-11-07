// Swagger by Luis :3

package com.onlyflans.bakery.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        String descripcion = """
            Documentación de la API para el proyecto de la página <b>OnlyFlans 🍰</b><br><br>
            <b>Desarrollado por:</b>Andrés Ortega, Leonel Briones y Luis Maulen<br>
            <a href='mailto:contacto@onlyflans.cl'>contacto@onlyflans.cl</a> | 
            <a href='https://onlyflans2.netlify.app' target="_blank">https://onlyflans2.netlify.app</a>
            """;

        return new OpenAPI()
                .info(new Info()
                        .title("API OnlyFlans Project")
                        .version("1.0.6")
                        .description(descripcion)
                );
    }
}
