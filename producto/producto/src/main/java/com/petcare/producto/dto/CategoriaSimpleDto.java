package com.petcare.producto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaSimpleDto {
    private Long idCategoria;
    private String nombre;
}
