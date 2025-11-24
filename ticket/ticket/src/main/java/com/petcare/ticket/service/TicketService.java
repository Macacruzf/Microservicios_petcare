package com.petcare.ticket.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.petcare.ticket.model.Comentario;
import com.petcare.ticket.model.Ticket;
import com.petcare.ticket.repository.ComentarioRepository;
import com.petcare.ticket.repository.TicketRepository;
import com.petcare.ticket.webclient.ProductoWebClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository repo;
    private final ComentarioRepository comentarioRepo;
    private final ProductoWebClient productoWebClient;

    // ----------------------------------------------------------
    //  Crear ticket con validación del microservicio de producto
    // ----------------------------------------------------------
    public Ticket crearTicket(Ticket ticket) {

        // ⭐ Llamada al microservicio de productos
        JsonNode producto = productoWebClient.getProducto(ticket.getIdProducto());

        if (producto == null || producto.isEmpty()) {
            throw new RuntimeException("Producto no encontrado en microservicio PRODUCTO");
        }

        return repo.save(ticket);
    }

    // ----------------------------------------------------------
    // CRUD
    // ----------------------------------------------------------
    public List<Ticket> listar() {
        return repo.findAll();
    }

    public Ticket obtener(Long id) {
        return repo.findById(id).orElse(null);
    }

    public String eliminar(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return "Ticket eliminado";
        }
        return "Ticket no encontrado";
    }

    // ----------------------------------------------------------
    // Filtrar estrellas
    // ----------------------------------------------------------
    public List<Ticket> buscarPorClasificacion(Integer estrellas) {
        return repo.findByClasificacion(estrellas);
    }

    // ----------------------------------------------------------
    // Tickets por producto
    // ----------------------------------------------------------
    public List<Ticket> listarPorProducto(Long idProducto) {
        return repo.findByIdProducto(idProducto);
    }

    // ==========================================================
    // ⭐⭐ COMENTARIOS (en el mismo servicio)
    // ==========================================================

    public Comentario agregarComentario(Long idTicket, Comentario comentario) {

        Ticket ticket = repo.findById(idTicket)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));

        comentario.setTicket(ticket);
        comentario.setFecha(LocalDateTime.now());

        return comentarioRepo.save(comentario);
    }

    public List<Comentario> obtenerComentariosDeTicket(Long idTicket) {
        return comentarioRepo.findByTicket_IdTicket(idTicket);
    }
}