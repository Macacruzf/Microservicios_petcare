package com.petcare.producto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(description = "DTO para la creación o actualización de una categoría")
public class CategoriaRequest {


    @Schema(
        description = "Nombre de la categoría a registrar",
        example = "Accesorios",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nombre;
}
