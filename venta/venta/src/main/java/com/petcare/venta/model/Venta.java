package com.petcare.venta.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa una venta realizada por un usuario dentro del sistema.")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
        description = "ID único de la venta.",
        example = "50",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long idVenta;

    @Schema(
        description = "ID del usuario que realizó la compra.",
        example = "5",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long usuarioId;

    @Schema(
        description = "Total pagado en la venta.",
        example = "15990.0",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Double total;

    @Schema(
        description = "Método de pago utilizado en la venta.",
        example = "Tarjeta",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String metodoPago;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_estado", nullable = false)
    @JsonIgnore
    @Schema(
        description = "Estado actual de la venta. Relación hacia la tabla de estados.",
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private EstadoVentaEntity estado;

    @JsonProperty("estado")
    @Schema(
        description = "Nombre del estado de la venta. Este valor es solo de lectura.",
        example = "PENDIENTE",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    public String getEstadoNombre() {
        return estado != null ? estado.getNombreEstado() : null;
    }

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(
        description = "Lista de productos y cantidades incluidos en esta venta.",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private List<DetalleVenta> detalles = new ArrayList<>();
}
