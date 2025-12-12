package com.petcare.usuario.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa un usuario dentro del sistema")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    @Schema(
        description = "ID único del usuario",
        example = "101",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Integer idUsuario;

    @Column(name = "nombre_usuario", nullable = false)
    @Schema(
        description = "Nombre del usuario",
        example = "Francisca Castro",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nombreUsuario;

    @Column(nullable = false, unique = true)
    @Schema(
        description = "Correo electrónico único del usuario",
        example = "fran@example.com",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;

    @Column(nullable = false)
    @Schema(
        description = "Número telefónico del usuario",
        example = "+56912345678",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String telefono;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(
        description = "Contraseña del usuario (solo escritura, nunca visible en respuestas)",
        example = "P4ssw0rd!",
        accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private String password;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    @JsonIgnore
    @Schema(
        description = "Objeto Rol interno (oculto en la respuesta). Se usa getter personalizado para exponer el nombre."
    )
    private RolEntity rol;

    @JsonProperty("rol")
    @Schema(
        description = "Rol asignado al usuario. Solo devuelve el nombre del rol.",
        example = "ADMIN",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    public String getRolNombre() {
        return rol != null ? rol.getNombreRol() : null;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(
        description = "Estado actual del usuario",
        example = "ACTIVO",
        allowableValues = {"ACTIVO", "INACTIVO", "SUSPENDIDO"}
    )
    private EstadoUsuario estado = EstadoUsuario.ACTIVO;

    @Column(name = "foto_perfil", columnDefinition = "LONGBLOB")
    @Schema(
        description = "Imagen de perfil del usuario en formato binario",
        example = "byte[]"
    )
    private byte[] fotoPerfil;
}
