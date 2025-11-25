package com.petcare.ticket.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petcare.ticket.model.Comentario;
import com.petcare.ticket.model.Ticket;
import com.petcare.ticket.service.TicketService;

@WebMvcTest(TicketController.class)
@AutoConfigureMockMvc(addFilters = false) // Desactiva seguridad para testing puro
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    @Autowired
    private ObjectMapper objectMapper;

    // ============================================================
    // TEST: CREAR TICKET
    // ============================================================
    @Test
    @DisplayName("POST /api/tickets - Debería crear un ticket y retornar 201 Created")
    void crearTicket_Exito() throws Exception {
        // Arrange
        Ticket input = new Ticket();
        input.setIdUsuario(15L);
        input.setIdProducto(8L);
        input.setClasificacion(5);
        input.setComentario("Excelente producto, llegó rápido.");

        Ticket output = new Ticket();
        output.setIdTicket(1L); // Lombok genera setIdTicket, no setId
        output.setIdUsuario(15L);
        output.setIdProducto(8L);
        output.setClasificacion(5);
        output.setComentario("Excelente producto, llegó rápido.");

        when(ticketService.crearTicket(any(Ticket.class))).thenReturn(output);

        // Act & Assert
        mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idTicket").value(1L)) // JSON field coincide con nombre de variable
                .andExpect(jsonPath("$.comentario").value("Excelente producto, llegó rápido."));
    }

    // ============================================================
    // TEST: LISTAR TODOS LOS TICKETS
    // ============================================================
    @Test
    @DisplayName("GET /api/tickets - Debería retornar lista de tickets")
    void listar_Exito() throws Exception {
        Ticket t1 = new Ticket();
        t1.setIdTicket(10L);
        t1.setComentario("Consulta general");
        
        when(ticketService.listar()).thenReturn(List.of(t1));

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idTicket").value(10L))
                .andExpect(jsonPath("$[0].comentario").value("Consulta general"));
    }

    // ============================================================
    // TEST: OBTENER TICKET POR ID
    // ============================================================
    @Test
    @DisplayName("GET /api/tickets/{id} - Debería retornar ticket")
    void obtener_Exito() throws Exception {
        Ticket t = new Ticket();
        t.setIdTicket(5L);
        t.setComentario("Detalle del ticket");

        when(ticketService.obtener(5L)).thenReturn(t);

        mockMvc.perform(get("/api/tickets/{id}", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTicket").value(5L))
                .andExpect(jsonPath("$.comentario").value("Detalle del ticket"));
    }

    // ============================================================
    // TEST: ELIMINAR TICKET
    // ============================================================
    @Test
    @DisplayName("DELETE /api/tickets/{id} - Debería retornar mensaje")
    void eliminar_Exito() throws Exception {
        String mensaje = "Ticket eliminado correctamente";
        when(ticketService.eliminar(1L)).thenReturn(mensaje);

        mockMvc.perform(delete("/api/tickets/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string(mensaje));
    }

    // ============================================================
    // TEST: FILTRAR POR CLASIFICACIÓN
    // ============================================================
    @Test
    @DisplayName("GET /clasificacion/{estrellas} - Filtrar por estrellas")
    void buscarPorClasificacion_Exito() throws Exception {
        Ticket t = new Ticket();
        t.setIdTicket(2L);
        t.setClasificacion(5);
        
        when(ticketService.buscarPorClasificacion(5)).thenReturn(Arrays.asList(t));

        mockMvc.perform(get("/api/tickets/clasificacion/{estrellas}", 5))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clasificacion").value(5));
    }

    // ============================================================
    // TEST: LISTAR POR PRODUCTO
    // ============================================================
    @Test
    @DisplayName("GET /producto/{id} - Listar por producto")
    void listarPorProducto_Exito() throws Exception {
        when(ticketService.listarPorProducto(100L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/tickets/producto/{id}", 100L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ============================================================
    // TEST: GESTIÓN DE COMENTARIOS
    // ============================================================
    @Test
    @DisplayName("POST /comentarios - Agregar comentario")
    void agregarComentario_Exito() throws Exception {
        Comentario input = new Comentario();
        input.setMensaje("Respuesta soporte"); // Usando setMensaje
        input.setTipoMensaje("SOPORTE");
        input.setIdUsuario(99L);
        
        Comentario output = new Comentario();
        output.setIdComentario(200L); // Usando setIdComentario
        output.setMensaje("Respuesta soporte");

        when(ticketService.agregarComentario(eq(1L), any(Comentario.class))).thenReturn(output);

        mockMvc.perform(post("/api/tickets/{idTicket}/comentarios", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idComentario").value(200L)) // Validando campo correcto
                .andExpect(jsonPath("$.mensaje").value("Respuesta soporte"));
    }

    @Test
    @DisplayName("GET /comentarios - Ver comentarios")
    void obtenerComentarios_Exito() throws Exception {
        Comentario c = new Comentario();
        c.setIdComentario(10L);
        c.setMensaje("Hola, necesito ayuda");
        
        when(ticketService.obtenerComentarios(1L)).thenReturn(List.of(c));

        mockMvc.perform(get("/api/tickets/{idTicket}/comentarios", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].mensaje").value("Hola, necesito ayuda"));
    }
}