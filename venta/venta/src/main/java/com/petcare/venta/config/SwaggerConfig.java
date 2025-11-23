package com.petcare.venta.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI ventaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Ventas - PetCare Connect")
                        .description("Microservicio para registrar ventas y sus detalles")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("PetCare Connect")
                                .email("soporte@petcare.cl")
                                .url("https://petcareconnect.cl")
                        )
                );
    }
}
