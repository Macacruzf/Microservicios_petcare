package com.petcare.producto.model;

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
    @Schema(description = "ID único del producto", example = "1")
    private Long idProducto;

    @Column(nullable = false)
    @Schema(description = "Nombre del producto", example = "Dog Chow Adulto 15kg")
    private String nombre;

    @Column(nullable = false)
    @Schema(description = "Precio del producto", example = "23990.0")
    private Double precio;

    @Column(nullable = false)
    @Schema(description = "Stock disponible", example = "20")
    private Integer stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Estado actual del producto", example = "DISPONIBLE")
    private EstadoProducto estado;

    @ManyToOne
    @JoinColumn(name = "id_categoria", referencedColumnName = "idCategoria", nullable = false)
    @Schema(description = "Categoría asociada al producto")
    private Categoria categoria;

}
