package com.petcare.venta.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carrito")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa el carrito de compras de un usuario dentro del sistema.")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito")
    @Schema(
        description = "ID único del carrito.",
        example = "10",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long idCarrito;

    @Column(name = "usuario_id", nullable = false)
    @Schema(
        description = "ID del usuario dueño del carrito.",
        example = "5",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long usuarioId;

    @Column(nullable = false)
    @Schema(
        description = "Monto total acumulado del carrito.",
        example = "15990.0",
        defaultValue = "0.0"
    )
    private Double total = 0.0;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Schema(
        description = "Lista de productos añadidos al carrito.",
        implementation = DetalleCarrito.class
    )
    private List<DetalleCarrito> detalles = new ArrayList<>();
}
