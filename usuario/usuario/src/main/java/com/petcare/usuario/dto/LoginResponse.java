package com.petcare.usuario.dto;

import com.petcare.usuario.model.Usuario;
import lombok.Data;

@Data
public class LoginResponse {
    private boolean success;
    private Usuario usuario;
}
