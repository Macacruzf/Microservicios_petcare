package com.petcare.producto.dto;

import com.petcare.producto.model.EstadoProducto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data  // Usa Lombok para getters/setters si lo tienes, o déjalo manual
@Schema(description = "DTO para cambiar el estado de un producto")
public class EstadoRequest {

    @Schema(description = "Nuevo estado del producto", allowableValues = {"DISPONIBLE", "NO_DISPONIBLE", "SIN_STOCK"}, example = "DISPONIBLE")
    private String estado;

    // Constructor vacío (requerido para deserialización)
    public EstadoRequest() {}

    public EstadoRequest(String estado) {
        this.estado = estado;
    }

    // Método para convertir a enum con validación
    public EstadoProducto getEstadoAsEnum() {
        if (estado == null || estado.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }
        try {
            return EstadoProducto.valueOf(estado.toUpperCase());  // Convierte a mayúsculas para flexibilidad
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado inválido: " + estado + ". Valores permitidos: DISPONIBLE, NO_DISPONIBLE, SIN_STOCK");
        }
    }
}