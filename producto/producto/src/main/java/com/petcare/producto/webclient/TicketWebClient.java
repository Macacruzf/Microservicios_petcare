package com.petcare.producto.webclient;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class TicketWebClient {

    private final WebClient.Builder builder;

    private WebClient client() {
        return builder.baseUrl("http://localhost:8087/tickets").build();
    }

    public JsonNode getTicketsPorProducto(Long idProducto) {
        try {
            return client()
                    .get()
                    .uri("/producto/{idProducto}", idProducto)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (Exception e) {
            return null;
        }
    }
}