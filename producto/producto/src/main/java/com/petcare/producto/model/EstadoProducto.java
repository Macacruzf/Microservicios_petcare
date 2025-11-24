package com.petcare.producto.model;

import io.swagger.v3.oas.annotations.media.Schema;

// Enumeración de estados posibles para un producto
@Schema(description = "Estado actual del producto")
public enum EstadoProducto {

    @Schema(description = "Producto visible y disponible para la venta")
    DISPONIBLE,

    @Schema(description = "Producto oculto o no disponible temporalmente")
    NO_DISPONIBLE,

    @Schema(description = "Producto agotado y sin stock")
    SIN_STOCK,
}