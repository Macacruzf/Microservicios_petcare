package com.petcare.ticket.controller;

import com.petcare.ticket.model.Ticket;
import com.petcare.ticket.model.Comentario;
import com.petcare.ticket.dto.TicketRequest;
import com.petcare.ticket.service.TicketService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Tag(name = "Tickets", description = "Endpoints para gestionar reseñas, tickets y comentarios")
public class TicketController {

    private final TicketService ticketService;

    // ============================================================
    //  CREAR TICKET
    // ============================================================
        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
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
    //  LISTAR TODOS
    // ============================================================
    @GetMapping
    @Operation(
        summary = "Listar tickets",
        description = "Obtiene todos los tickets o reseñas registradas."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista obtenida correctamente",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = Ticket.class)))
    )
    public List<Ticket> listar() {
        return ticketService.listar();
    }

    // ============================================================
    //  OBTENER TICKET
    // ============================================================
    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener ticket por ID",
        description = "Devuelve el detalle de un ticket específico."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ticket encontrado"),
        @ApiResponse(responseCode = "404", description = "Ticket no encontrado")
    })
    public Ticket obtener(
            @Parameter(description = "ID del ticket a buscar")
            @PathVariable Long id
    ) {
        return ticketService.obtener(id);
    }

    // ============================================================
    //  ELIMINAR TICKET
    // ============================================================
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar ticket",
        description = "Elimina un ticket o reseña del sistema."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ticket eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Ticket no encontrado")
    })
    public String eliminar(
            @Parameter(description = "ID del ticket a eliminar")
            @PathVariable Long id
    ) {
        return ticketService.eliminar(id);
    }

    // ============================================================
    //  FILTRAR POR CLASIFICACIÓN
    // ============================================================
    @GetMapping("/clasificacion/{estrellas}")
    @Operation(
        summary = "Filtrar tickets por clasificación",
        description = "Busca tickets según la cantidad de estrellas otorgadas (1 a 5)."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista filtrada obtenida correctamente"),
        @ApiResponse(responseCode = "400", description = "Clasificación fuera del rango permitido")
    })
    public List<Ticket> buscarPorClasificacion(
            @Parameter(description = "Número de estrellas (1 a 5)")
            @PathVariable Integer estrellas
    ) {
        return ticketService.buscarPorClasificacion(estrellas);
    }

    // ============================================================
    //  LISTAR TICKETS POR PRODUCTO
    // ============================================================
    @GetMapping("/producto/{idProducto}")
    @Operation(
        summary = "Tickets de un producto",
        description = "Retorna todas las reseñas asociadas a un producto."
    )
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    public List<Ticket> listarPorProducto(
            @Parameter(description = "ID del producto")
            @PathVariable Long idProducto
    ) {
        return ticketService.listarPorProducto(idProducto);
    }

    // ============================================================
    //  AGREGAR COMENTARIO
    // ============================================================
    @PostMapping("/{idTicket}/comentarios")
    @Operation(
        summary = "Agregar comentario a un ticket",
        description = "Permite que soporte o cliente respondan dentro del ticket."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Comentario agregado correctamente"),
        @ApiResponse(responseCode = "404", description = "Ticket no encontrado")
    })
    public Comentario agregarComentario(
            @Parameter(description = "ID del ticket padre")
            @PathVariable Long idTicket,

            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos del comentario",
                required = true,
                content = @Content(
                    schema = @Schema(implementation = Comentario.class),
                    examples = @ExampleObject(
                        value = """
                        {
                          "idUsuario": 15,
                          "mensaje": "¿Podrían ayudarme con este problema?",
                          "tipoMensaje": "CLIENTE"
                        }
                        """
                    )
                )
            )
            Comentario comentario
    ) {
        return ticketService.agregarComentario(idTicket, comentario);
    }

    // ============================================================
    //  OBTENER COMENTARIOS DE UN TICKET
    // ============================================================
    @GetMapping("/{idTicket}/comentarios")
    @Operation(
        summary = "Listar comentarios del ticket",
        description = "Muestra el hilo de conversación completo entre cliente y soporte."
    )
    public List<Comentario> obtenerComentarios(
            @Parameter(description = "ID del ticket")
            @PathVariable Long idTicket
    ) {
        return ticketService.obtenerComentarios(idTicket);
    }
}
