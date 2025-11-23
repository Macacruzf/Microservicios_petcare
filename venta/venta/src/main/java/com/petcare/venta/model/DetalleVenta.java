package com.petcare.venta.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "detalle_venta")
@Data
@Schema(description = "Detalle individual de un producto dentro de una venta")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del detalle de venta", example = "10")
    private Long idDetalle;

    @ManyToOne
    @JoinColumn(name = "idVenta")
    @Schema(description = "Venta a la que pertenece este detalle")
    private Venta venta;

    @Schema(description = "ID del producto vendido", example = "3")
    private Long productoId;

    @Schema(description = "Nombre del producto vendido", example = "Shampoo para perros")
    private String nombre;

    @Schema(description = "Cantidad comprada del producto", example = "2")
    private Integer cantidad;

    @Schema(description = "Subtotal del producto (precio * cantidad)", example = "9990.0")
    private Double subtotal;
}
