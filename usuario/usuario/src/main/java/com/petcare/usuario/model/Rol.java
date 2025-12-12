package com.petcare.usuario.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    description = "Roles disponibles para los usuarios dentro del sistema",
    enumAsRef = true
)
public enum Rol {

    @Schema(description = "Rol con acceso completo al sistema y permisos administrativos")
    ADMIN,

    @Schema(description = "Rol estándar del usuario cliente final")
    CLIENTE
}
