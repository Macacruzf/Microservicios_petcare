package com.petcare.venta.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Detalle de un producto dentro de una venta realizada.")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del detalle de venta.", example = "100")
    private Long idDetalleVenta;

    @ManyToOne
    @JoinColumn(name = "venta_id")
    private Venta venta;

    @Schema(description = "ID del producto vendido.", example = "7")
    private Long productoId;

    @Schema(description = "Cantidad vendida del producto.", example = "2")
    private Integer cantidad;

    @Schema(description = "Precio unitario del producto.", example = "4995.0")
    private Double precioUnitario;

    @Schema(description = "Subtotal del detalle.", example = "9990.0")
    private Double subtotal;
}

