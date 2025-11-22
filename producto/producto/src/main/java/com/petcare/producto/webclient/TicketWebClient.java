package com.petcare.producto.webclient;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TicketWebClient {

    private final WebClient.Builder builder;

    private WebClient client() {
        return builder.baseUrl("http://localhost:8087/ticket").build();
    }

    public JsonNode getTicketsDeProducto(Long idProducto) {
        try {
            return client()
                    .get()
                    .uri("/producto/{id}", idProducto)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (Exception e) {
            return null;
        }
    }
}