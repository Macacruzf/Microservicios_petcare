package com.petcare.ticket.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.petcare.ticket.model.Comentario;
import com.petcare.ticket.model.Ticket;
import com.petcare.ticket.repository.ComentarioRepository;
import com.petcare.ticket.repository.TicketRepository;
import com.petcare.ticket.webclient.ProductoWebClient;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepo;

    @Mock
    private ComentarioRepository comentarioRepo;

    @Mock
    private ProductoWebClient productoWebClient;

    @InjectMocks
    private TicketService ticketService;

    // Helper para crear JSON simulado del microservicio de productos
    private final ObjectMapper mapper = new ObjectMapper();

    // ==========================================================
    // TEST: CREAR TICKET
    // ==========================================================

    @Test
    @DisplayName("crearTicket - Exito: Debería guardar el ticket si todo es válido")
    void crearTicket_Exito() {
        // Arrange
        Ticket input = new Ticket();
        input.setIdProducto(100L);
        input.setComentario("Todo bien");
        input.setClasificacion(5);

        // Simulamos que el microservicio de productos responde un JSON válido
        ObjectNode jsonProducto = mapper.createObjectNode();
        jsonProducto.put("id", 100L);
        jsonProducto.put("nombre", "Collar antipulgas");
        
        when(productoWebClient.getProducto(100L)).thenReturn(jsonProducto);
        when(ticketRepo.save(any(Ticket.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Ticket resultado = ticketService.crearTicket(input);

        // Assert
        assertNotNull(resultado);
        verify(ticketRepo).save(input);
    }

    @Test
    @DisplayName("crearTicket - Error: ID Producto nulo")
    void crearTicket_ErrorIdNulo() {
        Ticket input = new Ticket();
        input.setIdProducto(null);

        Exception ex = assertThrows(RuntimeException.class, () -> ticketService.crearTicket(input));
        assertEquals("Debe indicar el ID del producto.", ex.getMessage());
    }

    @Test
    @DisplayName("crearTicket - Error: Producto no existe en microservicio")
    void crearTicket_ErrorProductoNoExiste() {
        Ticket input = new Ticket();
        input.setIdProducto(999L);
        input.setComentario("Hola");
        input.setClasificacion(5);

        // Simulamos respuesta nula o vacía del cliente web
        when(productoWebClient.getProducto(999L)).thenReturn(null);

        Exception ex = assertThrows(RuntimeException.class, () -> ticketService.crearTicket(input));
        assertEquals("Producto no encontrado en microservicio PRODUCTO.", ex.getMessage());
    }

    @Test
    @DisplayName("crearTicket - Error: Comentario vacío")
    void crearTicket_ErrorComentarioVacio() {
        Ticket input = new Ticket();
        input.setIdProducto(100L);
        input.setComentario(""); // Vacío
        input.setClasificacion(5);

        // Simulamos producto válido para pasar esa validación primero
        ObjectNode jsonProducto = mapper.createObjectNode();
        when(productoWebClient.getProducto(100L)).thenReturn(jsonProducto);

        Exception ex = assertThrows(RuntimeException.class, () -> ticketService.crearTicket(input));
        assertEquals("El comentario no puede estar vacío.", ex.getMessage());
    }

    @Test
    @DisplayName("crearTicket - Error: Clasificación inválida")
    void crearTicket_ErrorClasificacion() {
        Ticket input = new Ticket();
        input.setIdProducto(100L);
        input.setComentario("Ok");
        input.setClasificacion(6); // Inválido (>5)

        ObjectNode jsonProducto = mapper.createObjectNode();
        when(productoWebClient.getProducto(100L)).thenReturn(jsonProducto);

        Exception ex = assertThrows(RuntimeException.class, () -> ticketService.crearTicket(input));
        assertEquals("La clasificación debe ser entre 1 y 5 estrellas.", ex.getMessage());
    }

    // ==========================================================
    // TEST: CRUD BÁSICO
    // ==========================================================

    @Test
    @DisplayName("obtener - Retorna ticket si existe")
    void obtener_Exito() {
        Ticket t = new Ticket();
        t.setIdTicket(1L);
        when(ticketRepo.findById(1L)).thenReturn(Optional.of(t));

        Ticket res = ticketService.obtener(1L);
        assertNotNull(res);
        assertEquals(1L, res.getIdTicket());
    }

    @Test
    @DisplayName("eliminar - Exito si existe")
    void eliminar_Exito() {
        when(ticketRepo.existsById(1L)).thenReturn(true);
        
        String res = ticketService.eliminar(1L);
        
        assertEquals("Ticket eliminado", res);
        verify(ticketRepo).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar - Mensaje error si no existe")
    void eliminar_NoExiste() {
        when(ticketRepo.existsById(1L)).thenReturn(false);
        
        String res = ticketService.eliminar(1L);
        
        assertEquals("Ticket no encontrado", res);
        verify(ticketRepo, never()).deleteById(anyLong());
    }

    // ==========================================================
    // TEST: FILTROS
    // ==========================================================
    @Test
    @DisplayName("buscarPorClasificacion - Llama al repositorio")
    void buscarPorClasificacion() {
        when(ticketRepo.findByClasificacion(5)).thenReturn(Collections.emptyList());
        ticketService.buscarPorClasificacion(5);
        verify(ticketRepo).findByClasificacion(5);
    }

    // ==========================================================
    // TEST: AGREGAR COMENTARIO
    // ==========================================================

    @Test
    @DisplayName("agregarComentario - Exito")
    void agregarComentario_Exito() {
        // Arrange
        Ticket ticketMock = new Ticket();
        ticketMock.setIdTicket(1L);

        Comentario comentarioInput = new Comentario();
        comentarioInput.setMensaje("Respuesta");
        comentarioInput.setTipoMensaje("SOPORTE");

        when(ticketRepo.findById(1L)).thenReturn(Optional.of(ticketMock));
        when(comentarioRepo.save(any(Comentario.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Comentario res = ticketService.agregarComentario(1L, comentarioInput);

        // Assert
        assertNotNull(res);
        assertEquals(ticketMock, res.getTicket()); // Verifica la asociación
        verify(comentarioRepo).save(comentarioInput);
    }

    @Test
    @DisplayName("agregarComentario - Error: Ticket no existe")
    void agregarComentario_ErrorTicketNoExiste() {
        when(ticketRepo.findById(1L)).thenReturn(Optional.empty());

        Comentario input = new Comentario();
        Exception ex = assertThrows(RuntimeException.class, () -> ticketService.agregarComentario(1L, input));
        assertEquals("Ticket no encontrado.", ex.getMessage());
    }

    @Test
    @DisplayName("agregarComentario - Error: Tipo mensaje inválido")
    void agregarComentario_ErrorTipoMensaje() {
        Ticket ticketMock = new Ticket();
        when(ticketRepo.findById(1L)).thenReturn(Optional.of(ticketMock));

        Comentario input = new Comentario();
        input.setMensaje("Hola");
        input.setTipoMensaje("UNKNOWN"); // Inválido

        Exception ex = assertThrows(RuntimeException.class, () -> ticketService.agregarComentario(1L, input));
        assertEquals("El tipo de mensaje debe ser CLIENTE o SOPORTE.", ex.getMessage());
    }
}