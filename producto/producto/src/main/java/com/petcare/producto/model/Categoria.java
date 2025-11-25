package com.petcare.producto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categorias")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Tipos de categorías")
public class Categoria {

    // ID único
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la categoría", example = "1")
    private Long idCategoria;

    // Nombre
    @Column(nullable = false)
    @Schema(description = "Nombre de la categoría", example = "Pelota de goma", required = true)
    private String nombre;
}
