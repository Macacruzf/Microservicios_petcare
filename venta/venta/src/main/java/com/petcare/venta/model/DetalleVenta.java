package com.petcare.venta.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detalle_venta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Detalle individual de un producto dentro de una venta realizada.")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_venta")
    @Schema(
        description = "ID único del detalle de venta.",
        example = "100",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long idDetalleVenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false)
    @Schema(description = "Venta a la cual pertenece este detalle.")
    private Venta venta;

    @Column(name = "producto_id", nullable = false)
    @Schema(
        description = "ID del producto vendido.",
        example = "7",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long productoId;

    @Column(nullable = false)
    @Schema(
        description = "Cantidad del producto vendida.",
        example = "2",
        minimum = "1"
    )
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false)
    @Schema(
        description = "Precio unitario del producto al momento de la venta.",
        example = "4995.0"
    )
    private Double precioUnitario;

    @Column(nullable = false)
    @Schema(
        description = "Subtotal calculado (precioUnitario × cantidad).",
        example = "9990.0"
    )
    private Double subtotal;
}
