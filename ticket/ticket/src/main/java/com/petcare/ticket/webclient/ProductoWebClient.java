package com.petcare.ticket.webclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class ProductoWebClient {

    private final WebClient.Builder builder;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private WebClient client() {
        return builder
                .baseUrl("http://localhost:8086")   // MS PRODUCTO
                .build();
    }

    public JsonNode getProducto(Long idProducto) {

        try {
            // 1️⃣ Obtener respuesta como String (evita errores de mapeo)
            String response = client()
                    .get()
                    .uri("/api/v1/productos/{id}", idProducto)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (response == null || response.isBlank()) {
                System.out.println("⚠ MS PRODUCTO devolvió respuesta vacía");
                return null;
            }

            // 2️⃣ Convertir String → JsonNode manualmente
            return objectMapper.readTree(response);

        } catch (Exception e) {
            System.out.println("⚠ Error al llamar al MS PRODUCTO: " + e.getMessage());
            return null;
        }
    }
}
