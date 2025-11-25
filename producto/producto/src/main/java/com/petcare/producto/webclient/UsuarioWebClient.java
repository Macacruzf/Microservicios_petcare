package com.petcare.producto.webclient;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class UsuarioWebClient {

    private final WebClient.Builder builder;

    private WebClient client() {
        return builder.baseUrl("http://localhost:8081/usuario").build();
    }

    public JsonNode getUsuarioPorId(Long idUsuario) {
        try {
            return client()
                    .get()
                    .uri("/{id}", idUsuario)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (Exception e) {
            return null;
        }
    }

    public String getRol(Long idUsuario) {
        try {
            return client()
                    .get()
                    .uri("/{id}/rol", idUsuario)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            return null;
        }
    }
}
