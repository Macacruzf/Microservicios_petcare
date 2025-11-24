package com.petcare.ticket.controller;

import com.petcare.ticket.model.Comentario;
import com.petcare.ticket.model.Ticket;
import com.petcare.ticket.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
@Tag(name = "Ticket API", description = "Endpoints para gestión de tickets y comentarios")
public class TicketController {

    private final TicketService service;

    // ==========================================================
    // CREAR TICKET
    // ==========================================================
    @PostMapping
    @Operation(summary = "Crear un ticket")
    public ResponseEntity<Ticket> crear(@RequestBody Ticket ticket) {
        return ResponseEntity.ok(service.crearTicket(ticket));
    }

    // ==========================================================
    // LISTAR
    // ==========================================================
    @GetMapping
    @Operation(summary = "Listar todos los tickets")
    public ResponseEntity<List<Ticket>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    // ==========================================================
    // OBTENER
    // ==========================================================
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un ticket por ID")
    public ResponseEntity<Ticket> obtener(@PathVariable Long id) {
        Ticket ticket = service.obtener(id);
        return (ticket != null) ? ResponseEntity.ok(ticket) : ResponseEntity.notFound().build();
    }

    // ==========================================================
    // ELIMINAR
    // ==========================================================
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un ticket por ID")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        return ResponseEntity.ok(service.eliminar(id));
    }

    // ==========================================================
    // FILTRO POR CLASIFICACIÓN
    // ==========================================================
    @GetMapping("/clasificacion/{estrellas}")
    @Operation(summary = "Buscar tickets por cantidad de estrellas")
    public ResponseEntity<List<Ticket>> porEstrellas(@PathVariable Integer estrellas) {
        return ResponseEntity.ok(service.buscarPorClasificacion(estrellas));
    }

    // ==========================================================
    // LISTAR POR PRODUCTO
    // ==========================================================
    @GetMapping("/producto/{idProducto}")
    @Operation(summary = "Listar tickets asociados a un producto específico")
    public ResponseEntity<List<Ticket>> porProducto(@PathVariable Long idProducto) {
        return ResponseEntity.ok(service.listarPorProducto(idProducto));
    }

    // ==========================================================
    // AGREGAR COMENTARIO
    // ==========================================================
    @PostMapping("/{idTicket}/comentarios")
    @Operation(summary = "Agregar comentario a un ticket")
    public ResponseEntity<Comentario> comentar(
            @PathVariable Long idTicket,
            @RequestBody Comentario comentario
    ) {
        return ResponseEntity.ok(service.agregarComentario(idTicket, comentario));
    }

}
