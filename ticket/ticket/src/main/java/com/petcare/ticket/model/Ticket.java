package com.petcare.ticket.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "ticket")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(
    description = "Entidad que representa una reseña del cliente sobre un producto."
)
public class Ticket implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
        description = "ID único del ticket o reseña",
        example = "1001",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long idTicket;

    @Schema(
        description = "ID del usuario que crea la reseña",
        example = "15",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long idUsuario;

    @Schema(
        description = "ID del producto al que pertenece la reseña",
        example = "8",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long idProducto;

    @Min(1)
    @Max(5)
    @Schema(
        description = "Clasificación del producto en una escala de 1 a 5 estrellas",
        example = "4",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer clasificacion;


    @Column(nullable = false, columnDefinition = "TEXT")
    @Schema(
        description = "Comentario o reseña realizada por el cliente",
        example = "Muy buena atención, volveré :)",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String comentario;

    @Schema(
        description = "Lista de comentarios asociados al ticket (respuestas, aclaraciones, etc.)"
    )
    @OneToMany(
            mappedBy = "ticket",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Comentario> comentarios = new ArrayList<>();
}
