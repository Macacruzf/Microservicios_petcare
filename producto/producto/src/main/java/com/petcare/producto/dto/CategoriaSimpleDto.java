package com.petcare.producto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO simple que representa la categoría asociada a un producto")
public class CategoriaSimpleDto {

    @Schema(
        description = "Identificador único de la categoría",
        example = "3",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long idCategoria;

    @Schema(
        description = "Nombre de la categoría",
        example = "Alimentos"
    )
    private String nombre;
}
