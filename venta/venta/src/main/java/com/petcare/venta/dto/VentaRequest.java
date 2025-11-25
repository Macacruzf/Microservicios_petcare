package com.petcare.venta.dto;

import lombok.Data;
import java.util.List;

@Data
public class VentaRequest {
    private Long idUsuario;
    private String formaPago;
    private List<DetalleVentaRequest> detalles;
}
