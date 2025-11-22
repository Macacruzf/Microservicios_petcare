package com.petcare.ticket.repository;

import com.petcare.ticket.model.Comentario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    
    List<Comentario> findByTicket_IdTicket(Long idTicket);
}
