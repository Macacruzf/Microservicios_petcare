package com.petcare.venta.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa el carrito de compras del usuario.")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del carrito.", example = "10")
    private Long idCarrito;

    @Schema(description = "ID del usuario dueño del carrito.", example = "5")
    private Long usuarioId;

    @Schema(description = "Total acumulado del carrito.", example = "15990.0")
    private Double total = 0.0;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Lista de productos añadidos al carrito.")
    private List<DetalleCarrito> detalles = new ArrayList<>();
}
