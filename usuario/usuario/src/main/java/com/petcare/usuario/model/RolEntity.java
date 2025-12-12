package com.petcare.usuario.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa un rol dentro del sistema de usuarios")
public class RolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    @Schema(
        description = "ID único del rol",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Integer idRol;

    @Column(name = "nombre_rol", nullable = false, unique = true, length = 50)
    @Schema(
        description = "Nombre del rol definido por el sistema",
        example = "ADMIN",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nombreRol;

    @Column(columnDefinition = "TEXT")
    @Schema(
        description = "Descripción detallada del rol y sus permisos",
        example = "Rol con permisos administrativos sobre todos los módulos"
    )
    private String descripcion;

    @Column(name = "fecha_creacion")
    @Schema(
        description = "Fecha y hora en que el rol fue creado",
        example = "2025-01-14T10:35:00",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    @Schema(
        description = "Indica si el rol está activo dentro del sistema",
        example = "true"
    )
    private Boolean activo = true;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}
