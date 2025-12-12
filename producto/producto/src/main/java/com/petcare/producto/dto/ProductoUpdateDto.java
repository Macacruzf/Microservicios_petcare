package com.petcare.producto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para actualizar o representar un producto en listados del frontend / Android")
public class ProductoUpdateDto {

    @Schema(
        description = "ID único del producto",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long idProducto;

    @Schema(
        description = "Nombre del producto. Opcional en actualización.",
        example = "Dog Chow Adulto 15kg",
        nullable = true
    )
    private String nombre;

    @Positive(message = "El precio debe ser mayor que 0")
    @Schema(
        description = "Precio del producto. Opcional en actualización.",
        example = "23990",
        nullable = true
    )
    private Double precio;

    @Min(value = 0, message = "El stock no puede ser negativo")
    @Schema(
        description = "Stock disponible del producto. Opcional en actualización.",
        example = "20",
        nullable = true
    )
    private Integer stock;

    @Schema(
        description = "Estado actual del producto.",
        allowableValues = {"DISPONIBLE", "NO_DISPONIBLE", "SIN_STOCK"},
        example = "DISPONIBLE",
        nullable = true
    )
    private String estado;

    @Schema(
        description = "Datos simplificados de la categoría del producto",
        implementation = CategoriaSimpleDto.class,
        nullable = true
    )
    private CategoriaSimpleDto categoria;

    @Schema(
        description = "URL para obtener la imagen del producto desde el backend",
        example = "/api/v1/productos/1/imagen",
        nullable = true
    )
    private String imagenUrl;
}
