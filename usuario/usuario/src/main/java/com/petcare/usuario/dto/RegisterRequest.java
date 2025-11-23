package com.petcare.usuario.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String nombreUsuario;
    private String email;
    private String telefono;
    private String password;
    private String rol;     // CLIENTE o ADMIN
    private String fotoUri; // opcional
}
