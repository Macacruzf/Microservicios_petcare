package com.petcare.producto.dto;

import lombok.Data;

@Data
public class ProductoSimpleDto {

    private Long idProducto;
    private String nombre;
    private Double precio;
    private Integer stock;
    private String estado;  // Cambia de EstadoProducto a String para output

    private CategoriaSimpleDto categoria;
}