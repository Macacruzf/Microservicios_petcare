package com.petcare.producto.webclient;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VentaWebClient {

    private final WebClient.Builder builder;

    private WebClient client() {
        return builder.baseUrl("http://localhost:8088/venta").build();
    }

    public JsonNode getVenta(Long idVenta) {
        try {
            return client()
                    .get()
                    .uri("/{id}", idVenta)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (Exception e) {
            return null;
        }
    }
}
