package com.petcare.producto.model;

import org.springframework.hateoas.RepresentationModel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "categorias")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)// Evita problemas con equals/hashCode de RepresentationModel
@Schema(description = " Tipos de categorias") 
public class Categoria extends RepresentationModel<Categoria> {

    //ID único de la categoría (clave primaria, ID unico e irrepetible).
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la categoría", example = "1")
    private Long idCategoria;

    //Nombre de la categoría .
    @Column(nullable = false)
    @Schema(description = "Nombre de la categoría", example = "Pelota de goma", required = true)
    private String nombre;
}