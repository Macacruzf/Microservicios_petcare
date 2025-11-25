package com.petcare.ticket.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.petcare.ticket.model.Comentario;
import com.petcare.ticket.model.Ticket;
import com.petcare.ticket.repository.ComentarioRepository;
import com.petcare.ticket.repository.TicketRepository;
import com.petcare.ticket.webclient.ProductoWebClient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepo;
    private final ComentarioRepository comentarioRepo;
    private final ProductoWebClient productoWebClient;

    // ==========================================================
    // CREAR TICKET (con validación en microservicio PRODUCTOS)
    // ==========================================================
    public Ticket crearTicket(Ticket ticket) {

        // ✔ Validación: idProducto no puede venir nulo
        if (ticket.getIdProducto() == null) {
            throw new RuntimeException("Debe indicar el ID del producto.");
        }

        // ✔ Validación en microservicio PRODUCTO
        JsonNode producto = productoWebClient.getProducto(ticket.getIdProducto());

        if (producto == null || producto.isEmpty()) {
            throw new RuntimeException("Producto no encontrado en microservicio PRODUCTO.");
        }

        // ✔ Validación comentario
        if (ticket.getComentario() == null || ticket.getComentario().trim().isEmpty()) {
            throw new RuntimeException("El comentario no puede estar vacío.");
        }

        // ✔ Validación clasificación 1-5
        if (ticket.getClasificacion() == null ||
                ticket.getClasificacion() < 1 ||
                ticket.getClasificacion() > 5) {
            throw new RuntimeException("La clasificación debe ser entre 1 y 5 estrellas.");
        }

        // ✔ Guardar ticket
        return ticketRepo.save(ticket);
    }


    // ==========================================================
    // CRUD BÁSICO
    // ==========================================================
    public List<Ticket> listar() {
        return ticketRepo.findAll();
    }

    public Ticket obtener(Long id) {
        return ticketRepo.findById(id).orElse(null);
    }

    public String eliminar(Long id) {
        if (ticketRepo.existsById(id)) {
            ticketRepo.deleteById(id);
            return "Ticket eliminado";
        }
        return "Ticket no encontrado";
    }


    // ==========================================================
    // FILTROS
    // ==========================================================
    public List<Ticket> buscarPorClasificacion(Integer estrellas) {
        return ticketRepo.findByClasificacion(estrellas);
    }

    public List<Ticket> listarPorProducto(Long idProducto) {
        return ticketRepo.findByIdProducto(idProducto);
    }


    // ==========================================================
    // COMENTARIOS DE TICKETS
    // ==========================================================
    public Comentario agregarComentario(Long idTicket, Comentario comentario) {

        // ✔ Validar existencia del ticket
        Ticket ticket = ticketRepo.findById(idTicket)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado."));

        // ✔ Validar mensaje
        if (comentario.getMensaje() == null || comentario.getMensaje().trim().isEmpty()) {
            throw new RuntimeException("El mensaje no puede estar vacío.");
        }

        // ✔ Validar tipo de mensaje
        if (comentario.getTipoMensaje() == null ||
                (!comentario.getTipoMensaje().equalsIgnoreCase("CLIENTE")
                        && !comentario.getTipoMensaje().equalsIgnoreCase("SOPORTE"))) {

            throw new RuntimeException("El tipo de mensaje debe ser CLIENTE o SOPORTE.");
        }

        // ✔ Asociar comentario al ticket
        comentario.setTicket(ticket);

        return comentarioRepo.save(comentario);
    }

    public List<Comentario> obtenerComentarios(Long idTicket) {
        return comentarioRepo.findByTicket_IdTicket(idTicket);
    }
}
