package com.petcare.venta.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "venta")
@Data
@Schema(description = "Venta completa realizada por un cliente")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de la venta", example = "1")
    private Long idVenta;

    @Schema(description = "Fecha y hora en que se realizó la venta")
    private LocalDateTime fecha;

    @Schema(description = "Nombre o identificador del cliente", example = "Francisca Castro")
    private String cliente;

    @Schema(description = "Monto total de la venta", example = "25990.0")
    private Double total;

    @Schema(description = "Método de pago utilizado", example = "Tarjeta de crédito")
    private String metodoPago;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Lista de detalles (productos) asociados a la venta")
    private List<DetalleVenta> detalles;
}
