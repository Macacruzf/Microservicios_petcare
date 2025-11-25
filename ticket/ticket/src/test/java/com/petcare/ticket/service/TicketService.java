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
    // CREAR TICKET (validando en microservicio PRODUCTOS)
    // ==========================================================
    public Ticket crearTicket(Ticket ticket) {

        // Llamada al microservicio producto
        JsonNode producto = productoWebClient.getProducto(ticket.getIdProducto());

        if (producto == null || producto.isEmpty()) {
            throw new RuntimeException("Producto no encontrado en microservicio PRODUCTO");
        }

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
    // COMENTARIOS
    // ==========================================================
    public Comentario agregarComentario(Long idTicket, Comentario comentario) {

        Ticket ticket = ticketRepo.findById(idTicket)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));

        comentario.setTicket(ticket);

        return comentarioRepo.save(comentario);
    }

    public List<Comentario> obtenerComentarios(Long idTicket) {
        return comentarioRepo.findByTicket_IdTicket(idTicket);
    }
}
