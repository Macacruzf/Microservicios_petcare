package com.petcare.usuario.dto;

import com.petcare.usuario.model.RolEntity;
import com.petcare.usuario.model.EstadoUsuario;
import lombok.Data;

@Data
public class ValidacionResponse {
    private boolean valido;
    private Integer idUsuario;
    private RolEntity rol;
    private EstadoUsuario estado;
}
