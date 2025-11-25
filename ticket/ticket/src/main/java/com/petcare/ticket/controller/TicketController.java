package com.petcare.ticket.controller;

import com.petcare.ticket.model.Ticket;
import com.petcare.ticket.model.Comentario;
import com.petcare.ticket.service.TicketService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Tag(name = "Tickets", description = "Gestión de tickets, reseñas y comentarios")
public class TicketController {

    private final TicketService ticketService;

    // ============================================================
    //  CREAR TICKET
    // ============================================================
    @PostMapping
    @Operation(summary = "Crear ticket (reseña del cliente)")
    public Ticket crearTicket(@RequestBody Ticket ticket) {
        return ticketService.crearTicket(ticket);
    }

    // ============================================================
    //  LISTAR TODOS LOS TICKETS
    // ============================================================
    @GetMapping
    @Operation(summary = "Listar todos los tickets")
    public List<Ticket> listar() {
        return ticketService.listar();
    }

    // ============================================================
    //  OBTENER TICKET POR ID
    // ============================================================
    @GetMapping("/{id}")
    @Operation(summary = "Obtener ticket por ID")
    public Ticket obtener(@PathVariable Long id) {
        return ticketService.obtener(id);
    }

    // ============================================================
    //  ELIMINAR TICKET
    // ============================================================
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar ticket por ID")
    public String eliminar(@PathVariable Long id) {
        return ticketService.eliminar(id);
    }

    // ============================================================
    //  FILTRAR POR CLASIFICACIÓN (ESTRELLAS)
    // ============================================================
    @GetMapping("/clasificacion/{estrellas}")
    @Operation(summary = "Filtrar tickets por clasificación")
    public List<Ticket> buscarPorClasificacion(@PathVariable Integer estrellas) {
        return ticketService.buscarPorClasificacion(estrellas);
    }

    // ============================================================
    //  LISTAR TICKETS POR PRODUCTO
    // ============================================================
    @GetMapping("/producto/{idProducto}")
    @Operation(summary = "Listar tickets por producto")
    public List<Ticket> listarPorProducto(@PathVariable Long idProducto) {
        return ticketService.listarPorProducto(idProducto);
    }

    // ============================================================
    //  AGREGAR COMENTARIO A UN TICKET
    // ============================================================
    @PostMapping("/{idTicket}/comentarios")
    @Operation(summary = "Agregar comentario a ticket")
    public Comentario agregarComentario(
            @PathVariable Long idTicket,
            @RequestBody Comentario comentario
    ) {
        return ticketService.agregarComentario(idTicket, comentario);
    }

    // ============================================================
    //  LISTAR COMENTARIOS DE UN TICKET
    // ============================================================
    @GetMapping("/{idTicket}/comentarios")
    @Operation(summary = "Listar comentarios de un ticket")
    public List<Comentario> obtenerComentarios(@PathVariable Long idTicket) {
        return ticketService.obtenerComentarios(idTicket);
    }
}
