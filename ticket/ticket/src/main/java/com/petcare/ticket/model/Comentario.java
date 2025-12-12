package com.petcare.ticket.model;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "comentario")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Entidad que representa un comentario dentro de un ticket")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
        description = "ID único del comentario",
        example = "501",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long idComentario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idTicket", nullable = false)
    @Schema(
        description = "Ticket al cual pertenece este comentario",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Ticket ticket;


    @Schema(
        description = "ID del usuario que escribió el comentario",
        example = "23",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long idUsuario;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Schema(
        description = "Contenido del comentario",
        example = "Estoy teniendo un problema con mi compra, ¿podrían ayudarme?",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String mensaje;

    @Column(nullable = false, length = 15)
    @Schema(
        description = "Tipo de mensaje según origen",
        example = "CLIENTE",
        allowableValues = {"CLIENTE", "SOPORTE"},
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String tipoMensaje;
}
