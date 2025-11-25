package com.petcare.producto.dto;

import com.petcare.producto.model.EstadoProducto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoCreateDto {

    private String nombre;
    private Double precio;
    private Integer stock;

    private String estado; // viene en texto desde el frontend o Android

    private Long categoriaId;

    public EstadoProducto getEstadoEnum() {
        if (estado == null) return EstadoProducto.DISPONIBLE; // valor por defecto
        return EstadoProducto.valueOf(estado.toUpperCase());
    }
}
