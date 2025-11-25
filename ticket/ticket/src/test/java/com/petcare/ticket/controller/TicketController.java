package com.petcare.ticket.controller;

import com.petcare.ticket.model.Comentario;
import com.petcare.ticket.service.TicketService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TicketControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean   // <-- ESTA ERA LA ANOTACIÓN QUE FALTABA
    private TicketService ticketService;

    @Test
    void testAgregarComentario() {

        Comentario respuesta = new Comentario(
                1L,
                null,
                5L,
                "Mensaje",
                "CLIENTE"
        );

        when(ticketService.agregarComentario(eq(1L), any(Comentario.class)))
                .thenReturn(respuesta);

        webTestClient.post()
                .uri("/api/tickets/1/comentarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "idUsuario": 5,
                          "mensaje": "Mensaje",
                          "tipoMensaje": "CLIENTE"
                        }
                        """)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.mensaje").isEqualTo("Mensaje");
    }

    @Test
    void testListarComentarios() {

        Comentario c = new Comentario(
                1L,
                null,
                5L,
                "Comentario ok",
                "CLIENTE"
        ); }
}
