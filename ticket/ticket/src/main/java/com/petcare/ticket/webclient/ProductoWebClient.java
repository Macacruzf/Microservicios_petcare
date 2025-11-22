package com.petcare.ticket.webclient;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ProductoWebClient {

    private final WebClient webClient;

    public ProductoWebClient() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8086/productos")  // AJUSTA EL PUERTO DE PRODUCTO
                .build();
    }

    //  Obtener un producto por ID usando JsonNode
    public JsonNode getProducto(Long idProducto) {
        return webClient.get()
                .uri("/" + idProducto)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }
}
