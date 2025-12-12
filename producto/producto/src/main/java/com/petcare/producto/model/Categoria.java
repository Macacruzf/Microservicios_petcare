package com.petcare.producto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categorias")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Representa una categoría de productos dentro del sistema PetCare")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    @Schema(
        description = "ID único de la categoría",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long idCategoria;

    @Column(nullable = false)
    @Schema(
        description = "Nombre de la categoría",
        example = "Juguetes",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nombre;
}
