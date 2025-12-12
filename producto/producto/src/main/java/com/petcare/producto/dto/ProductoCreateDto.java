package com.petcare.producto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la creación de un nuevo producto en el sistema")
public class ProductoCreateDto {

    
    @Schema(
        description = "Nombre del producto",
        example = "Dog Chow Adulto 15kg",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nombre;

    
    @Positive(message = "El precio debe ser mayor que 0")
    @Schema(
        description = "Precio del producto",
        example = "23990",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Double precio;

    @Min(value = 0, message = "El stock no puede ser negativo")
    @Schema(
        description = "Stock disponible",
        example = "20",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer stock;

    @Schema(
        description = "Estado inicial del producto. Si no se envía, se asignará el estado por defecto en backend.",
        allowableValues = {"DISPONIBLE", "NO_DISPONIBLE", "SIN_STOCK"},
        example = "DISPONIBLE",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String estado;

    @Schema(
        description = "Identificador de la categoría a la que pertenece el producto",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long categoriaId;
}
