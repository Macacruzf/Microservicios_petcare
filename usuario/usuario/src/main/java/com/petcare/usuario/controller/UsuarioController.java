package com.petcare.usuario.controller;

import com.petcare.usuario.dto.LoginRequest;
import com.petcare.usuario.dto.LoginResponse;
import com.petcare.usuario.dto.RegisterRequest;
import com.petcare.usuario.dto.ValidacionResponse;
import com.petcare.usuario.model.Usuario;
import com.petcare.usuario.repository.UsuarioRepository;
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

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService service;

    public UsuarioController(UsuarioService service, UsuarioRepository usuarioRepository) {
        this.service = service;
        this.usuarioRepository = usuarioRepository;
    }

    // -----------------------------------------------------------
    // LOGIN
    // -----------------------------------------------------------
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

    // -----------------------------------------------------------
    // REGISTRO
    // -----------------------------------------------------------
    @PostMapping("/register")
    @Operation(summary = "Registrar un nuevo usuario")
    public ResponseEntity<?> registrar(@RequestBody RegisterRequest req) {

        Usuario nuevo = new Usuario();
        nuevo.setNombreUsuario(req.getNombreUsuario());
        nuevo.setEmail(req.getEmail());
        nuevo.setTelefono(req.getTelefono());
        nuevo.setPassword(req.getPassword());
        nuevo.setRol(Rol.valueOf(req.getRol()));

        return ResponseEntity.ok(service.registrar(nuevo));
    }

    // -----------------------------------------------------------
    // VER PERFIL
    // -----------------------------------------------------------
    @GetMapping("/perfil/{id}")
    @Operation(summary = "Obtener el perfil de un usuario")
    public ResponseEntity<?> perfil(@PathVariable Integer id) {
        Usuario u = service.getPerfil(id);
        if (u == null) return ResponseEntity.status(404).body("No encontrado");
        return ResponseEntity.ok(u);
    }

    // -----------------------------------------------------------
    // EDITAR PERFIL
    // CLIENTE: email, telefono, password, foto
    // ADMIN: todo
    // -----------------------------------------------------------
    @PutMapping("/perfil/{id}")
    @Operation(summary = "Editar datos del perfil")
    public ResponseEntity<?> editarPerfil(
            @PathVariable Integer id,
            @RequestBody Usuario datos) {

        Usuario actualizado = service.editarPerfil(id, datos);
        if (actualizado == null)
            return ResponseEntity.status(404).body("No encontrado");

        return ResponseEntity.ok(actualizado);
    }

    // -----------------------------------------------------------
    // LISTAR USUARIOS (solo admin)
    // -----------------------------------------------------------
    @GetMapping("/listar/{idAdmin}")
    @Operation(summary = "Listar todos los usuarios (ADMIN)")
    public ResponseEntity<?> listar(@PathVariable Integer idAdmin) {

        Usuario admin = service.getPerfil(idAdmin);

        if (admin == null || admin.getRol() != Rol.ADMIN) {
            return ResponseEntity.status(403).body("No autorizado");
        }

        return ResponseEntity.ok(service.listar());
    }

    // -----------------------------------------------------------
    // LISTAR POR ESTADO
    // -----------------------------------------------------------
    @GetMapping("/estados")
    @Operation(summary = "Listar usuarios por estado")
    public ResponseEntity<?> listarPorEstado(@RequestParam EstadoUsuario estado) {
        return ResponseEntity.ok(service.listarPorEstado(estado));
    }

    // -----------------------------------------------------------
    // BUSCAR POR NOMBRE/EMAIL
    // -----------------------------------------------------------
    @GetMapping("/buscar")
    @Operation(summary = "Buscar usuario por nombre/email")
    public ResponseEntity<?> buscar(@RequestParam String q) {
        return ResponseEntity.ok(service.buscar(q));
    }

    // -----------------------------------------------------------
    // VALIDACIÓN DE CREDENCIALES para otros microservicios
    // -----------------------------------------------------------
    @PostMapping("/validar-credenciales")
    @Operation(summary = "Validar usuario y contraseña para otros MS")
    public ResponseEntity<?> validarCredenciales(@RequestBody LoginRequest req) {

        Usuario u = service.login(req.getEmail(), req.getPassword());

        ValidacionResponse resp = new ValidacionResponse();

        if (u == null) {
            resp.setValido(false);
            return ResponseEntity.ok(resp);
        }

        resp.setValido(true);
        resp.setIdUsuario(u.getIdUsuario());
        resp.setRol(u.getRol());
        resp.setEstado(u.getEstado());

        return ResponseEntity.ok(resp);
    }

    // -----------------------------------------------------------
    // OBTENER SOLO ROL
    // -----------------------------------------------------------
    @GetMapping("/{id}/rol")
    public ResponseEntity<?> obtenerRol(@PathVariable Integer id) {
        Usuario u = service.getPerfil(id);
        if (u == null)
            return ResponseEntity.status(404).body("No encontrado");

        return ResponseEntity.ok(Map.of("rol", u.getRol().name()));
    }

    // -----------------------------------------------------------
    // OBTENER SOLO ESTADO
    // -----------------------------------------------------------
    @GetMapping("/{id}/estado")
    public ResponseEntity<?> obtenerEstado(@PathVariable Integer id) {
        Usuario u = service.getPerfil(id);
        if (u == null)
            return ResponseEntity.status(404).body("No encontrado");

        return ResponseEntity.ok(Map.of("estado", u.getEstado().name()));
    }

    // -----------------------------------------------------------
    // ELIMINAR USUARIO
    // -----------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        usuarioRepository.deleteById(id);
        return ResponseEntity.ok("Usuario eliminado");
    }
}
