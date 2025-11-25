package com.petcare.venta.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Detalle individual dentro del carrito del usuario.")
public class DetalleCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del detalle del carrito.", example = "20")
    private Long idDetalleCarrito;

    @ManyToOne
    @JoinColumn(name = "carrito_id")
    private Carrito carrito;

    @Schema(description = "ID del producto agregado.", example = "7")
    private Long productoId;

    @Schema(description = "Cantidad del producto.", example = "2")
    private Integer cantidad;

    @Schema(description = "Precio unitario del producto.", example = "4995.0")
    private Double precio;

    @Schema(description = "Subtotal del detalle.", example = "9990.0")
    private Double subtotal;
}
