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
@Schema(description = "Catálogo de estados de venta")
public class EstadoVentaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    @Schema(description = "ID único del estado", example = "1")
    private Integer idEstado;

    @Column(name = "nombre_estado", nullable = false, unique = true, length = 50)
    @Schema(description = "Nombre del estado", example = "PENDIENTE")
    private String nombreEstado;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Descripción del estado")
    private String descripcion;

    @Column(name = "permite_cancelacion")
    @Schema(description = "Si la venta puede ser cancelada en este estado")
    private Boolean permiteCancelacion = true;

    @Column(name = "es_final")
    @Schema(description = "Si es un estado final (no cambia más)")
    private Boolean esFinal = false;

    @Column(name = "color_hex", length = 7)
    @Schema(description = "Color hexadecimal para UI", example = "#FFA500")
    private String colorHex;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}

