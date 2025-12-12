package com.petcare.ticket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "DTO para crear una nueva reseña o ticket del cliente")
public class TicketRequest {

    @Schema(
        description = "ID del cliente que realiza la reseña",
        example = "15",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long idUsuario;

    @Schema(
        description = "ID del producto evaluado",
        example = "8",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long idProducto;

    @Min(value = 1, message = "La clasificación mínima es 1")
    @Max(value = 5, message = "La clasificación máxima es 5")
    @Schema(
        description = "Clasificación otorgada por el cliente (1 a 5)",
        example = "4",
        minimum = "1",
        maximum = "5",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer clasificacion;

    @Schema(
        description = "Comentario o reseña del cliente",
        example = "Muy buen servicio y atención rápida.",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String comentario;
}
