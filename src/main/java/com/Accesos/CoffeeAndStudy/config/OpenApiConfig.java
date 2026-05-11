package com.Accesos.CoffeeAndStudy.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI coffeeAndStudyOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CoffeeAndStudy API")
                        .description("API REST para gestionar lugares de estudio, usuarios, reseñas y sesiones de estudio")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Ibone")
                                .email("ibone23@example.com")));
    }
}