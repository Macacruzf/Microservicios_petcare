package com.petcare.venta.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "estados_venta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Catálogo de estados de venta en el sistema.")
public class EstadoVentaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    @Schema(
        description = "ID único del estado de venta.",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Integer idEstado;

    @Column(name = "nombre_estado", nullable = false, unique = true, length = 50)
    @Schema(
        description = "Nombre del estado de venta.",
        example = "PENDIENTE",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nombreEstado;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Descripción detallada del estado.")
    private String descripcion;

    @Column(name = "permite_cancelacion", nullable = false)
    @Schema(
        description = "Indica si la venta puede ser cancelada mientras esté en este estado.",
        example = "true"
    )
    private Boolean permiteCancelacion = true;

    @Column(name = "es_final", nullable = false)
    @Schema(
        description = "Indica si este estado es final y la venta no debe cambiar más.",
        example = "false"
    )
    private Boolean esFinal = false;

    @Column(name = "color_hex", length = 7)
    @Schema(
        description = "Color asignado a este estado para visualización en UI.",
        example = "#FFA500"
    )
    private String colorHex;

    @Column(name = "fecha_creacion", updatable = false)
    @Schema(
        description = "Fecha en la que se creó el registro del estado.",
        example = "2025-01-15T10:45:00",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }
}
