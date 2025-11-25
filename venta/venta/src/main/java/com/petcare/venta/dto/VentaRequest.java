package com.petcare.venta.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para crear una venta manualmente.")
public class VentaRequest {

    @Schema(description = "ID del usuario que paga.", example = "5")
    private Long usuarioId;

    @Schema(description = "Método de pago elegido.", example = "Efectivo")
    private String metodoPago;
}
