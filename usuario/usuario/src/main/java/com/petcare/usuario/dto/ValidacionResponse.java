package com.petcare.usuario.dto;

import com.petcare.usuario.model.Rol;
import com.petcare.usuario.model.EstadoUsuario;
import lombok.Data;

@Data
public class ValidacionResponse {
    private boolean valido;
    private Integer idUsuario;
    private Rol rol;
    private EstadoUsuario estado;
}
