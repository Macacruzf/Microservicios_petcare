package com.petcare.producto.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    description = "Estado actual del producto",
    enumAsRef = true
)
public enum EstadoProducto {

    @Schema(description = "Producto visible y disponible para la venta")
    DISPONIBLE,

    @Schema(description = "Producto oculto temporalmente, no se muestra en el catálogo")
    NO_DISPONIBLE,

    @Schema(description = "Producto agotado o sin stock disponible")
    SIN_STOCK
}
