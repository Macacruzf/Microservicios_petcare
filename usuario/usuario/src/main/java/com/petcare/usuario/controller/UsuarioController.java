package com.petcare.usuario.controller;

import com.petcare.usuario.dto.LoginRequest;
import com.petcare.usuario.dto.LoginResponse;
import com.petcare.usuario.model.Usuario;
import com.petcare.usuario.model.Rol;
import com.petcare.usuario.model.EstadoUsuario;
import com.petcare.usuario.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/usuario")
@CrossOrigin("*")
@Tag(name = "Usuarios", description = "Controlador de usuarios sin token")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    // LOGIN
    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Usuario u = service.login(req.getEmail(), req.getPassword());

        if (u == null) {
            return ResponseEntity.status(401).body(
                    Map.of("success", false, "message", "Credenciales inválidas")
            );
        }

        LoginResponse resp = new LoginResponse();
        resp.setSuccess(true);
        resp.setUsuario(u);

        return ResponseEntity.ok(resp);
    }

    // REGISTRO
    @PostMapping("/register")
    @Operation(summary = "Registrar un usuario")
    public ResponseEntity<?> registrar(
            @RequestBody Usuario usuario,
            @RequestParam Rol rol
    ) {
        usuario.setRol(rol);
        return ResponseEntity.ok(service.registrar(usuario));
    }

    // VER PERFIL
    @GetMapping("/perfil/{id}")
    @Operation(summary = "Ver perfil de usuario")
    public ResponseEntity<?> perfil(@PathVariable Integer id) {
        Usuario u = service.getPerfil(id);
        if (u == null) return ResponseEntity.status(404).body("No encontrado");
        return ResponseEntity.ok(u);
    }

    // EDITAR PERFIL
    @PutMapping("/perfil/{id}")
    @Operation(summary = "Editar perfil del usuario")
    public ResponseEntity<?> editarPerfil(
            @PathVariable Integer id,
            @RequestBody Usuario datos
    ) {
        Usuario u = service.editarPerfil(id, datos);
        if (u == null) return ResponseEntity.status(404).body("No encontrado");
        return ResponseEntity.ok(u);
    }

    // LISTAR USUARIOS (solo admin)
    @GetMapping("/listar")
    @Operation(summary = "Listar todos los usuarios (solo ADMIN)")
    public ResponseEntity<?> listar(@RequestParam Rol rol) {
        if (rol != Rol.ADMIN) {
            return ResponseEntity.status(403).body("No autorizado");
        }
        return ResponseEntity.ok(service.listar());
    }

    // LISTAR POR ESTADO
    @GetMapping("/estados")
    @Operation(summary = "Listar usuarios por estado")
    public ResponseEntity<?> listarPorEstado(@RequestParam EstadoUsuario estado) {
        return ResponseEntity.ok(service.listarPorEstado(estado));
    }

    // BUSCAR
    @GetMapping("/buscar")
    @Operation(summary = "Buscar usuario por nombre o email")
    public ResponseEntity<?> buscar(@RequestParam String q) {
        return ResponseEntity.ok(service.buscar(q));
    }
}
