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
@Schema(description = "Catálogo de estados de producto")
public class EstadoProductoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    @Schema(description = "ID único del estado", example = "1")
    private Integer idEstado;

    @Column(name = "nombre_estado", nullable = false, unique = true, length = 50)
    @Schema(description = "Nombre del estado", example = "DISPONIBLE")
    private String nombreEstado;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Descripción del estado")
    private String descripcion;

    @Column(name = "visible_catalogo")
    @Schema(description = "Si el producto aparece en el catálogo")
    private Boolean visibleCatalogo = true;

    @Column(name = "permite_venta")
    @Schema(description = "Si se puede vender el producto")
    private Boolean permiteVenta = true;

    @Column(name = "color_hex", length = 7)
    @Schema(description = "Color hexadecimal para UI", example = "#4CAF50")
    private String colorHex;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}

