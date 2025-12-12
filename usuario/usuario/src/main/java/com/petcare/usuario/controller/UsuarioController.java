package com.petcare.usuario.controller;

import com.petcare.usuario.dto.LoginRequest;
import com.petcare.usuario.dto.LoginResponse;
import com.petcare.usuario.dto.RegisterRequest;
import com.petcare.usuario.dto.ValidacionResponse;
import com.petcare.usuario.model.Usuario;
import com.petcare.usuario.model.RolEntity;
import com.petcare.usuario.repository.UsuarioRepository;
import com.petcare.usuario.repository.RolRepository;
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
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.io.IOException;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin("*")
@Tag(name = "Usuarios", description = "Controlador para gestión de usuarios, autenticación y perfiles")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService service;
    private final RolRepository rolRepository;

    public UsuarioController(UsuarioService service, UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.service = service;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
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

        System.out.println("📥 LOGIN REQUEST - Email: " + req.getEmail());

        Usuario u = service.login(req.getEmail(), req.getPassword());

        if (u == null) {
            System.out.println("❌ LOGIN FAILED - Credenciales inválidas");
            return ResponseEntity.status(401).body(
                Map.of("success", false, "message", "Credenciales inválidas")
            );
        }

        LoginResponse resp = new LoginResponse();
        resp.setSuccess(true);
        resp.setUsuario(u);

        System.out.println("✅ LOGIN SUCCESS");
        System.out.println("📤 Enviando respuesta:");
        System.out.println("   - Usuario ID: " + u.getIdUsuario());
        System.out.println("   - Nombre: " + u.getNombreUsuario());
        System.out.println("   - Email: " + u.getEmail());
        System.out.println("   - Rol: " + u.getRolNombre());
        System.out.println("   - Estado: " + u.getEstado());

        return ResponseEntity.ok(resp);
    }

    // -----------------------------------------------------------
    // REGISTRO
    // -----------------------------------------------------------
    @PostMapping("/register")
    @Operation(summary = "Registrar un nuevo usuario", description = "Crea una nueva cuenta de usuario en el sistema. El rol por defecto será CLIENTE si no se especifica o es inválido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente", 
                     content = @Content(schema = @Schema(implementation = Usuario.class))),
        @ApiResponse(responseCode = "400", description = "Datos de registro inválidos")
    })
    public ResponseEntity<?> registrar(@RequestBody RegisterRequest req) {

        Usuario nuevo = new Usuario();
        nuevo.setNombreUsuario(req.getNombreUsuario());
        nuevo.setEmail(req.getEmail());
        nuevo.setTelefono(req.getTelefono());
        nuevo.setPassword(req.getPassword());
        
        // Obtener rol desde BD (por defecto CLIENTE)
        String nombreRol = (req.getRol() != null && !req.getRol().isEmpty()) ? req.getRol().toUpperCase() : "CLIENTE";
        RolEntity rol = rolRepository.findByNombreRol(nombreRol)
            .orElseGet(() -> rolRepository.findByNombreRol("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado en BD")));

        nuevo.setRol(rol);

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
    // CAMBIAR CONTRASEÑA
    // -----------------------------------------------------------
    @PostMapping("/cambiar-password/{id}")
    @Operation(summary = "Cambiar contraseña", description = "Permite cambiar la contraseña validando la contraseña actual")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contraseña cambiada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Contraseña actual incorrecta o contraseñas nuevas no coinciden"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<?> changePassword(
            @Parameter(description = "ID del usuario") @PathVariable Integer id,
            @RequestBody com.petcare.usuario.dto.ChangePasswordRequest request) {

        // Validar que las contraseñas nuevas coincidan
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            return ResponseEntity.status(400).body(
                Map.of("success", false, "message", "Las contraseñas nuevas no coinciden")
            );
        }

        // Intentar cambiar la contraseña
        boolean success = service.changePassword(id, request.getCurrentPassword(), request.getNewPassword());

        if (!success) {
            return ResponseEntity.status(400).body(
                Map.of("success", false, "message", "Contraseña actual incorrecta")
            );
        }

        return ResponseEntity.ok(
            Map.of("success", true, "message", "Contraseña cambiada exitosamente")
        );
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

        if (admin == null || !"ADMIN".equals(admin.getRol().getNombreRol())) {
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

        return ResponseEntity.ok(Map.of("rol", u.getRol().getNombreRol()));
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

    // -----------------------------------------------------------
    // SUBIR FOTO DE PERFIL
    // -----------------------------------------------------------
    @PostMapping(value = "/{id}/foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Subir foto de perfil", description = "Sube una imagen de perfil para el usuario especificado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Foto subida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "400", description = "Error al procesar la imagen")
    })
    public ResponseEntity<?> subirFotoPerfil(
            @Parameter(description = "ID del usuario") @PathVariable Integer id,
            @Parameter(description = "Archivo de imagen") @RequestParam("foto") MultipartFile foto) {

        try {
            Usuario usuario = usuarioRepository.findById(id)
                .orElse(null);

            if (usuario == null) {
                return ResponseEntity.status(404).body(
                    Map.of("success", false, "message", "Usuario no encontrado")
                );
            }

            // Validar que sea una imagen
            String contentType = foto.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.status(400).body(
                    Map.of("success", false, "message", "El archivo debe ser una imagen")
                );
            }

            // Guardar la foto
            usuario.setFotoPerfil(foto.getBytes());
            usuarioRepository.save(usuario);

            return ResponseEntity.ok(
                Map.of("success", true, "message", "Foto de perfil actualizada correctamente")
            );

        } catch (IOException e) {
            return ResponseEntity.status(400).body(
                Map.of("success", false, "message", "Error al procesar la imagen: " + e.getMessage())
            );
        }
    }

    // -----------------------------------------------------------
    // OBTENER FOTO DE PERFIL
    // -----------------------------------------------------------
    @GetMapping("/{id}/foto")
    @Operation(summary = "Obtener foto de perfil", description = "Descarga la foto de perfil del usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Foto encontrada"),
        @ApiResponse(responseCode = "404", description = "Usuario o foto no encontrada")
    })
    public ResponseEntity<byte[]> obtenerFotoPerfil(@Parameter(description = "ID del usuario") @PathVariable Integer id) {

        Usuario usuario = usuarioRepository.findById(id)
            .orElse(null);

        if (usuario == null || usuario.getFotoPerfil() == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(usuario.getFotoPerfil().length);

        return ResponseEntity.ok()
            .headers(headers)
            .body(usuario.getFotoPerfil());
    }

    // -----------------------------------------------------------
    // ELIMINAR FOTO DE PERFIL
    // -----------------------------------------------------------
    @DeleteMapping("/{id}/foto")
    @Operation(summary = "Eliminar foto de perfil", description = "Elimina la foto de perfil del usuario")
    @ApiResponse(responseCode = "200", description = "Foto eliminada correctamente")
    public ResponseEntity<?> eliminarFotoPerfil(@Parameter(description = "ID del usuario") @PathVariable Integer id) {

        Usuario usuario = usuarioRepository.findById(id)
            .orElse(null);

        if (usuario == null) {
            return ResponseEntity.status(404).body(
                Map.of("success", false, "message", "Usuario no encontrado")
            );
        }

        usuario.setFotoPerfil(null);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok(
            Map.of("success", true, "message", "Foto de perfil eliminada")
        );
    }
}

