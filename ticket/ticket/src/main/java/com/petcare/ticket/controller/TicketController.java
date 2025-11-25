package com.petcare.ticket.controller;

import com.petcare.ticket.model.Ticket;
import com.petcare.ticket.model.Comentario;
import com.petcare.ticket.service.TicketService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Tag(name = "Tickets", description = "Gestión de tickets de soporte, reseñas y comentarios de productos")
public class TicketController {

    private final TicketService ticketService;

    // ============================================================
    //  CREAR TICKET
    // ============================================================
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Indica explícitamente que devuelve 201
    @Operation(summary = "Crear ticket", description = "Registra una nueva reseña o ticket de soporte para un producto.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Ticket creado exitosamente", 
                     content = @Content(schema = @Schema(implementation = Ticket.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public Ticket crearTicket(@RequestBody Ticket ticket) {
        return ticketService.crearTicket(ticket);
    }

    // ============================================================
    //  LISTAR TODOS LOS TICKETS
    // ============================================================
    @GetMapping
    @Operation(summary = "Listar todos los tickets", description = "Obtiene el historial completo de tickets registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de tickets", 
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ticket.class)))
    public List<Ticket> listar() {
        return ticketService.listar();
    }

    // ============================================================
    //  OBTENER TICKET POR ID
    // ============================================================
    @GetMapping("/{id}")
    @Operation(summary = "Obtener ticket por ID", description = "Busca el detalle de un ticket específico.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ticket encontrado"),
        @ApiResponse(responseCode = "404", description = "Ticket no encontrado")
    })
    public Ticket obtener(@Parameter(description = "ID del ticket") @PathVariable Long id) {
        return ticketService.obtener(id);
    }

    // ============================================================
    //  ELIMINAR TICKET
    // ============================================================
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar ticket", description = "Elimina un ticket del sistema.")
    @ApiResponse(responseCode = "200", description = "Ticket eliminado correctamente")
    public String eliminar(@Parameter(description = "ID del ticket a eliminar") @PathVariable Long id) {
        return ticketService.eliminar(id);
    }

    // ============================================================
    //  FILTRAR POR CLASIFICACIÓN (ESTRELLAS)
    // ============================================================
    @GetMapping("/clasificacion/{estrellas}")
    @Operation(summary = "Filtrar por estrellas", description = "Busca tickets según su puntuación (1-5 estrellas).")
    public List<Ticket> buscarPorClasificacion(
            @Parameter(description = "Número de estrellas (1-5)") @PathVariable Integer estrellas) {
        return ticketService.buscarPorClasificacion(estrellas);
    }

    // ============================================================
    //  LISTAR TICKETS POR PRODUCTO
    // ============================================================
    @GetMapping("/producto/{idProducto}")
    @Operation(summary = "Tickets de un producto", description = "Lista todas las reseñas o tickets asociados a un producto específico.")
    public List<Ticket> listarPorProducto(
            @Parameter(description = "ID del producto") @PathVariable Long idProducto) {
        return ticketService.listarPorProducto(idProducto);
    }

    // ============================================================
    //  AGREGAR COMENTARIO A UN TICKET
    // ============================================================
    @PostMapping("/{idTicket}/comentarios")
    @Operation(summary = "Comentar ticket", description = "Agrega una respuesta o comentario a un ticket existente.")
    @ApiResponse(responseCode = "200", description = "Comentario agregado")
    public Comentario agregarComentario(
            @Parameter(description = "ID del ticket padre") @PathVariable Long idTicket,
            @RequestBody Comentario comentario
    ) {
        return ticketService.agregarComentario(idTicket, comentario);
    }

    // ============================================================
    //  LISTAR COMENTARIOS DE UN TICKET
    // ============================================================
    @GetMapping("/{idTicket}/comentarios")
    @Operation(summary = "Ver comentarios", description = "Obtiene el hilo de conversación de un ticket.")
    public List<Comentario> obtenerComentarios(@PathVariable Long idTicket) {
        return ticketService.obtenerComentarios(idTicket);
    }
}