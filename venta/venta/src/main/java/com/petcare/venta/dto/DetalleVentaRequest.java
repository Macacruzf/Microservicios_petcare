package com.petcare.venta.dto;


import lombok.Data;

@Data
public class DetalleVentaRequest {
    private Long idProducto;
    private Integer cantidad;
}
