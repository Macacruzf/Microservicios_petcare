package com.petcare.ticket.repository;

import com.petcare.ticket.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByClasificacion(Integer clasificacion);
    List<Ticket> findByIdProducto(Long idProducto);
}
