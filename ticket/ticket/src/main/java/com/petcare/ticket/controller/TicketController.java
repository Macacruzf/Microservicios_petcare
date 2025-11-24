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
@Tag(name = "Tickets", description = "Gestión de tickets y comentarios")
public class TicketController {

    private final TicketService ticketService;

    // ============================================================
    //  CREAR TICKET
    // ============================================================
    @PostMapping
    @Operation(summary = "Crear ticket", description = "Crea un nuevo ticket validando el producto en otro microservicio")
    public Ticket crearTicket(@RequestBody Ticket ticket) {
        return ticketService.crearTicket(ticket);
    }

    // ============================================================
    //  LISTAR TICKETS
    // ============================================================
    @GetMapping
    @Operation(summary = "Listar tickets", description = "Retorna todos los tickets")
    public List<Ticket> listar() {
        return ticketService.listar();
    }

    // ============================================================
    //  OBTENER TICKET POR ID
    // ============================================================
    @GetMapping("/{id}")
    @Operation(summary = "Obtener ticket", description = "Retorna un ticket por su ID")
    public Ticket obtener(@PathVariable Long id) {
        return ticketService.obtener(id);
    }

    // ============================================================
    //  ELIMINAR TICKET
    // ============================================================
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar ticket", description = "Elimina un ticket por su ID")
    public String eliminar(@PathVariable Long id) {
        return ticketService.eliminar(id);
    }

    // ============================================================
    // FILTRAR TICKETS POR ESTRELLAS
    // ============================================================
    @GetMapping("/clasificacion/{estrellas}")
    @Operation(summary = "Filtrar tickets por clasificación", description = "Retorna tickets filtrados por la cantidad de estrellas")
    public List<Ticket> buscarPorClasificacion(@PathVariable Integer estrellas) {
        return ticketService.buscarPorClasificacion(estrellas);
    }

    // ============================================================
    //  TICKETS POR PRODUCTO
    // ============================================================
    @GetMapping("/producto/{idProducto}")
    @Operation(summary = "Listar tickets de un producto", description = "Obtiene los tickets asociados a un producto específico")
    public List<Ticket> listarPorProducto(@PathVariable Long idProducto) {
        return ticketService.listarPorProducto(idProducto);
    }

    // ============================================================
    //  COMENTARIOS
    // ============================================================

    @PostMapping("/{idTicket}/comentarios")
    @Operation(summary = "Agregar comentario", description = "Agrega un comentario a un ticket")
    public Comentario agregarComentario(
            @PathVariable Long idTicket,
            @RequestBody Comentario comentario
    ) {
        return ticketService.agregarComentario(idTicket, comentario);
    }

    @GetMapping("/{idTicket}/comentarios")
    @Operation(summary = "Listar comentarios", description = "Retorna los comentarios asociados a un ticket")
    public List<Comentario> obtenerComentarios(@PathVariable Long idTicket) {
        return ticketService.obtenerComentariosDeTicket(idTicket);
    }
}
