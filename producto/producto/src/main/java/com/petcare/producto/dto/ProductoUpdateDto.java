package com.petcare.producto.dto;

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

    // Estado en String ("DISPONIBLE", "NO_DISPONIBLE", "SIN_STOCK")
    private String estado;

    private CategoriaSimpleDto categoria;
    
    // URL para obtener la imagen desde el backend
    private String imagenUrl;
}
