package com.petcare.producto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "estados_producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    description = "Catálogo de estados que un producto puede tener dentro del sistema",
    name = "EstadoProducto"
)
public class EstadoProductoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    @Schema(
        description = "ID único del estado",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Integer idEstado;

    @Column(name = "nombre_estado", nullable = false, unique = true, length = 50)
    @Schema(
        description = "Nombre del estado",
        example = "DISPONIBLE",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nombreEstado;

    @Column(columnDefinition = "TEXT")
    @Schema(
        description = "Descripción detallada del estado",
        example = "Producto visible para clientes y con stock suficiente"
    )
    private String descripcion;

    @Column(name = "visible_catalogo")
    @Schema(
        description = "Define si los productos con este estado son visibles en catálogo",
        example = "true"
    )
    private Boolean visibleCatalogo = true;

    @Column(name = "permite_venta")
    @Schema(
        description = "Indica si productos con este estado pueden ser vendidos",
        example = "true"
    )
    private Boolean permiteVenta = true;

    @Column(name = "color_hex", length = 7)
    @Schema(
        description = "Código hexadecimal del color asociado a la UI",
        example = "#4CAF50"
    )
    private String colorHex;

    @Column(name = "fecha_creacion")
    @Schema(
        description = "Fecha en que se creó el estado",
        example = "2025-01-15T10:30:00",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}
