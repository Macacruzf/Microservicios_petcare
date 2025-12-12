package com.petcare.producto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO para solicitar el cambio de estado de un producto")
public class EstadoRequest {

    
    @Schema(
        description = "Nuevo estado del producto. Debe coincidir con los valores válidos del sistema.",
        allowableValues = {"DISPONIBLE", "NO_DISPONIBLE", "SIN_STOCK"},
        example = "DISPONIBLE",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String estado;

    public EstadoRequest() {}

    public EstadoRequest(String estado) {
        this.estado = estado;
    }
}
