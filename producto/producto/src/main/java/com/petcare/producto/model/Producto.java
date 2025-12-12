package com.petcare.producto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "productos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad que representa un producto dentro del microservicio de productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idproducto")
    @Schema(
        description = "ID único del producto",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long idProducto;

    @Column(nullable = false)
    @Schema(
        description = "Nombre del producto",
        example = "Dog Chow Adulto 15kg",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nombre;

    @Column(nullable = false)
    @Schema(
        description = "Precio del producto",
        example = "23990.0",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Double precio;

    @Column(nullable = false)
    @Schema(
        description = "Stock disponible del producto",
        example = "20",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer stock;

    // ============================================================
    // ESTADO DEL PRODUCTO
    // ============================================================
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_estado", nullable = false)
    @JsonIgnore
    @Schema(
        description = "Estado interno del producto (no se envía en el JSON de respuesta)"
    )
    private EstadoProductoEntity estado;

    // Se expone solo el nombre del estado en JSON
    @JsonProperty("estado")
    @Transient
    @Schema(
        description = "Estado del producto mostrado al cliente",
        example = "DISPONIBLE",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    public String getEstadoNombre() {
        return (estado != null && estado.getNombreEstado() != null)
            ? estado.getNombreEstado()
            : null;
    }

    // ============================================================
    // CATEGORÍA DEL PRODUCTO
    // ============================================================
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_categoria", referencedColumnName = "idCategoria", nullable = false)
    @Schema(
        description = "Categoría asociada al producto",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Categoria categoria;

    // ============================================================
    // IMAGEN DEL PRODUCTO
    // ============================================================
    @Lob
    @Column(name = "imagen", columnDefinition = "LONGBLOB")
    @Schema(
        description = "Imagen del producto en bytes (Base64 al enviarse al cliente)"
    )
    private byte[] imagen;

}
