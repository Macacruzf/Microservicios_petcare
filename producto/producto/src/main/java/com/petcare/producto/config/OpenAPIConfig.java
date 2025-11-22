package com.petcare.producto.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API de Productos - Perfunlandia")
                .version("1.0")
                .description("Microservicio para gestión de productos, incluyendo operaciones de lectura y administración de productos y categorías."));
    }
}