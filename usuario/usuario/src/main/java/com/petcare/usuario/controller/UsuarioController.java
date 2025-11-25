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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/usuario")
@CrossOrigin("*")
@Tag(name = "Usuarios", description = "Controlador para gestión de usuarios, autenticación y perfiles")
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
    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario verificando su email y contraseña.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login exitoso", 
                     content = @Content(schema = @Schema(implementation = LoginResponse.class))),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas (Email o contraseña incorrectos)")
    })
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
    @Operation(summary = "Registrar un nuevo usuario", description = "Crea una nueva cuenta de usuario en el sistema. El rol por defecto será CLIENTE si no se especifica o es inválido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente", 
                     content = @Content(schema = @Schema(implementation = Usuario.class))),
        @ApiResponse(responseCode = "400", description = "Datos de registro inválidos")
    })
    public ResponseEntity<?> registrar(@RequestBody RegisterRequest req) {

        Usuario nuevo = new Usuario();
        nuevo.setNombreUsuario(req.getNombreUsuario());
        nuevo.setEmail(req.getEmail());
        nuevo.setTelefono(req.getTelefono());
        nuevo.setPassword(req.getPassword());
        
        try {
            nuevo.setRol(Rol.valueOf(req.getRol()));
        } catch (IllegalArgumentException | NullPointerException e) {
            // Si el rol es inválido o nulo, asignamos CLIENTE por seguridad
            nuevo.setRol(Rol.CLIENTE);
        }

        return ResponseEntity.ok(service.registrar(nuevo));
    }

    // -----------------------------------------------------------
    // VER PERFIL
    // -----------------------------------------------------------
    @GetMapping("/perfil/{id}")
    @Operation(summary = "Obtener perfil", description = "Recupera la información detallada de un usuario por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perfil encontrado",
                     content = @Content(schema = @Schema(implementation = Usuario.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<?> perfil(@Parameter(description = "ID único del usuario") @PathVariable Integer id) {
        Usuario u = service.getPerfil(id);
        if (u == null) return ResponseEntity.status(404).body("No encontrado");
        return ResponseEntity.ok(u);
    }

    // -----------------------------------------------------------
    // EDITAR PERFIL
    // -----------------------------------------------------------
    @PutMapping("/perfil/{id}")
    @Operation(summary = "Editar datos del perfil", description = "Permite actualizar información del usuario (email, teléfono, contraseña, etc).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perfil actualizado correctamente",
                     content = @Content(schema = @Schema(implementation = Usuario.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado para editar")
    })
    public ResponseEntity<?> editarPerfil(
            @Parameter(description = "ID del usuario a editar") @PathVariable Integer id,
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
    @Operation(summary = "Listar todos los usuarios (ADMIN)", description = "Endpoint protegido. Solo un usuario con rol ADMIN puede listar a todos los usuarios registrados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista completa de usuarios",
                     content = @Content(schema = @Schema(implementation = Usuario.class))),
        @ApiResponse(responseCode = "403", description = "Acceso denegado: El usuario solicitante no es ADMIN"),
        @ApiResponse(responseCode = "404", description = "Usuario administrador no encontrado")
    })
    public ResponseEntity<?> listar(@Parameter(description = "ID del administrador que realiza la solicitud") @PathVariable Integer idAdmin) {

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
    @Operation(summary = "Listar usuarios por estado", description = "Filtra usuarios según su estado actual (ej. ACTIVO, INACTIVO).")
    public ResponseEntity<?> listarPorEstado(
            @Parameter(description = "Estado por el cual filtrar") @RequestParam EstadoUsuario estado) {
        return ResponseEntity.ok(service.listarPorEstado(estado));
    }

    // -----------------------------------------------------------
    // BUSCAR POR NOMBRE/EMAIL
    // -----------------------------------------------------------
    @GetMapping("/buscar")
    @Operation(summary = "Buscar usuarios", description = "Busca usuarios por coincidencia parcial en nombre de usuario o email.")
    public ResponseEntity<?> buscar(
            @Parameter(description = "Término de búsqueda (nombre o email)") @RequestParam String q) {
        return ResponseEntity.ok(service.buscar(q));
    }

    // -----------------------------------------------------------
    // VALIDACIÓN DE CREDENCIALES
    // -----------------------------------------------------------
    @PostMapping("/validar-credenciales")
    @Operation(summary = "Validar credenciales (Interno)", description = "Endpoint de utilidad para que otros microservicios validen si un usuario y contraseña son correctos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resultado de la validación",
                     content = @Content(schema = @Schema(implementation = ValidacionResponse.class)))
    })
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
    @Operation(summary = "Obtener Rol", description = "Devuelve únicamente el rol del usuario especificado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rol obtenido"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
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
    @Operation(summary = "Obtener Estado", description = "Devuelve únicamente el estado del usuario especificado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado obtenido"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
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
    @Operation(summary = "Eliminar usuario", description = "Elimina permanentemente un usuario de la base de datos.")
    @ApiResponse(responseCode = "200", description = "Usuario eliminado correctamente")
    public ResponseEntity<?> eliminar(@Parameter(description = "ID del usuario a eliminar") @PathVariable Integer id) {
        usuarioRepository.deleteById(id);
        return ResponseEntity.ok("Usuario eliminado");
    }
}