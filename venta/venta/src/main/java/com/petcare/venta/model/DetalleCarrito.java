package com.petcare.venta.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detalle_carrito")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Detalle individual de un producto dentro del carrito de compras del usuario.")
public class DetalleCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_carrito")
    @Schema(
        description = "ID único del detalle dentro del carrito.",
        example = "20",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long idDetalleCarrito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_id", nullable = false)
    @Schema(description = "Carrito al que pertenece este detalle.")
    private Carrito carrito;

    @Column(name = "producto_id", nullable = false)
    @Schema(
        description = "ID del producto agregado al carrito.",
        example = "7",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long productoId;

    @Column(nullable = false)
    @Schema(
        description = "Cantidad del producto agregada al carrito.",
        example = "2",
        minimum = "1"
    )
    private Integer cantidad;

    @Column(nullable = false)
    @Schema(
        description = "Precio unitario del producto al momento de agregarlo al carrito.",
        example = "4995.0"
    )
    private Double precio;

    @Column(nullable = false)
    @Schema(
        description = "Subtotal calculado (precio * cantidad).",
        example = "9990.0"
    )
    private Double subtotal;
}
