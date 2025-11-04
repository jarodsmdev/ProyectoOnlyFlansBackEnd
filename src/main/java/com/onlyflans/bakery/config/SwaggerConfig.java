// Swagger by Luis :3

package com.onlyflans.bakery.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
            .info(new Info()
                    .title("API OnlyFlans project")
                    .version("1.0.5")
                    .description("Documentación de la API para el proyecto de la pagina OnlyFlans")
                    //.contact("Developed by: Leonel Briones, Luis Maulen and Andres Ortega")
                    );
    }
}
