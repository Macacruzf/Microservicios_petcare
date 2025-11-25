package com.petcare.venta.model;

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
@Schema(description = "Entidad que representa una venta confirmada por el usuario.")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la venta.", example = "50")
    private Long idVenta;

    @Schema(description = "ID del usuario que realizó la compra.", example = "5")
    private Long usuarioId;

    @Schema(description = "Total pagado en la venta.", example = "15990.0")
    private Double total;

    @Schema(description = "Método de pago de la venta.", example = "Tarjeta")
    private String metodoPago;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    @Schema(description = "Detalles asociados a la venta.")
    private List<DetalleVenta> detalles = new ArrayList<>();
}

