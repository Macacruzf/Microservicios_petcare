package com.petcare.producto.dto;

import com.petcare.producto.model.EstadoProducto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductoUpdateDto {

    private Long idProducto;
    private String nombre;
    private Double precio;
    private Integer stock;

    // Estado en String ("DISPONIBLE", "AGOTADO", etc.)
    private String estado;

    private CategoriaSimpleDto categoria;

    // Convierte el String a Enum de forma segura
    public EstadoProducto getEstadoEnum() {
        if (estado == null) return null;
        try {
            return EstadoProducto.valueOf(estado.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Estado inválido: " + estado);
        }
    }
}
