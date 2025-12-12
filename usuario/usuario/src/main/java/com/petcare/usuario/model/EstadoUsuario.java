package com.petcare.usuario.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    description = "Estados posibles para un usuario dentro del sistema",
    enumAsRef = true
)
public enum EstadoUsuario {

    @Schema(description = "El usuario está activo y puede acceder al sistema")
    ACTIVO,

    @Schema(description = "El usuario está inactivo, no puede iniciar sesión")
    INACTIVO,

    @Schema(description = "El usuario está suspendido temporalmente por reglas del sistema")
    SUSPENDIDO
}
