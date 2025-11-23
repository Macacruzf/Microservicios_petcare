package com.petcare.venta.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Datos necesarios para registrar una nueva venta")
public class VentaRequest {

    @Schema(description = "Nombre o identificador del cliente", example = "Francisca Castro")
    private String cliente;

    @Schema(description = "Método de pago utilizado", example = "Transferencia")
    private String metodoPago;

    @Schema(description = "Total de la venta calculado en el cliente", example = "19990.0")
    private Double total;

    @Schema(description = "Lista de productos incluidos en la venta")
    private List<DetalleVentaRequest> items;

    @Data
    @Schema(description = "Detalle enviado desde el cliente para crear una venta")
    public static class DetalleVentaRequest {

        @Schema(description = "ID del producto", example = "5")
        private Long productoId;

        @Schema(description = "Nombre del producto", example = "Alimento Premium Gato 2kg")
        private String nombre;

        @Schema(description = "Cantidad del producto", example = "1")
        private Integer cantidad;

        @Schema(description = "Subtotal del producto (precio * cantidad)", example = "12990.0")
        private Double subtotal;
    }
}
