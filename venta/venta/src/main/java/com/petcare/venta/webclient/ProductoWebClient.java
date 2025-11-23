package com.petcare.venta.webclient;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class ProductoWebClient {

    private final WebClient.Builder builder;

    private WebClient client() {
        return builder.baseUrl("http://localhost:8086/api/v1/productos").build();
    }

    public JsonNode getProducto(Long idProducto) {
        try {
            return client()
                    .get()
                    .uri("/{id}", idProducto)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (Exception e) {
            return null;
        }
    }

    public void actualizarStock(Long idProducto, Integer nuevoStock) {
        try {
            client()
                .put()
                .uri("/{id}/stock", idProducto)
                .bodyValue(nuevoStock)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        } catch (Exception ignored) {}
    }
}
