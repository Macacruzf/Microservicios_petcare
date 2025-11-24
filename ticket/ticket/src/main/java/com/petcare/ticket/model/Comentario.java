package com.petcare.ticket.model;


import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comentario")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Comentarios de un ticket")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idComentario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idTicket", nullable = false)
    private Ticket ticket;

    @Column(nullable = false)
    private Long idUsuario;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false, length = 15)
    private String tipoMensaje; 
    // CLIENTE o SOPORTE
}
