package com.petcare.ticket.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ticket")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Entidad que representa una reseña del cliente")
public class Ticket implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del ticket", example = "1001")
    private Long idTicket;

    @Column(nullable = false)
    @Schema(description = "Fecha y hora de creación del ticket", example = "2025-06-21T14:30:00")
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    @Schema(description = "ID del usuario creador", example = "15")
    private Long idUsuario;

    @Column(nullable = false)
    @Schema(description = "ID del producto reseñado", example = "8")
    private Long idProducto;

    @Column(nullable = false)
    @Schema(description = "Clasificación en estrellas (1 a 5)", example = "4")
    private Integer clasificacion;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Schema(description = "Comentario o reseña del cliente",
            example = "Muy buena atención, volveré :)")
    private String comentario;
}
