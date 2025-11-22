package com.petcare.producto.dto;

import com.petcare.producto.model.EstadoProducto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para actualizar un producto (campos opcionales)")
public class ProductoUpdateRequest {

    @Schema(description = "Nombre del producto", example = "Dog Chow Adulto 15kg")
    private String nombre;

    @Schema(description = "Precio del producto", example = "23990.0")
    private Double precio;

    @Schema(description = "Stock disponible", example = "20")
    private Integer stock;

    @Schema(description = "Estado del producto", allowableValues = {"DISPONIBLE", "NO_DISPONIBLE", "SIN_STOCK"}, example = "DISPONIBLE")
    private String estado;  // Recibe como String para flexibilidad

    // Método para convertir estado a enum (con validación)
    public EstadoProducto getEstadoAsEnum() {
        if (estado == null || estado.isEmpty()) return null;
        try {
            return EstadoProducto.valueOf(estado.toUpperCase());  // Convierte a mayúsculas
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado inválido: " + estado + ". Valores permitidos: DISPONIBLE, NO_DISPONIBLE, SIN_STOCK");
        }
    }
}