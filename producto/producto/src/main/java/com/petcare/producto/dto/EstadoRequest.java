package com.petcare.producto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para cambiar el estado de un producto")
public class EstadoRequest {

    @Schema(description = "Nuevo estado del producto", allowableValues = {"DISPONIBLE", "NO_DISPONIBLE", "SIN_STOCK"}, example = "DISPONIBLE")
    private String estado;

    // Constructor vacío (requerido para deserialización)
    public EstadoRequest() {}

    public EstadoRequest(String estado) {
        this.estado = estado;
    }
}