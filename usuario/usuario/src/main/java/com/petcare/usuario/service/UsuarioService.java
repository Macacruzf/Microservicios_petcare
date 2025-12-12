package com.petcare.usuario.service;

import com.petcare.usuario.model.Usuario;
import com.petcare.usuario.model.EstadoUsuario;
import com.petcare.usuario.model.RolEntity;
import com.petcare.usuario.repository.UsuarioRepository;
import com.petcare.usuario.repository.RolRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repo;
    private final RolRepository rolRepo;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repo, RolRepository rolRepo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.rolRepo = rolRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // ------------------------------------------------------
    // LOGIN SIMPLE
    // ------------------------------------------------------
    public Usuario login(String email, String password) {
        System.out.println("🔍 LOGIN - Email recibido: " + email);
        Usuario u = repo.findByEmail(email);

        if (u == null) {
            System.out.println("❌ LOGIN - Usuario no encontrado con email: " + email);
            return null;
        }

        System.out.println("✅ LOGIN - Usuario encontrado: " + u.getNombreUsuario());
        System.out.println("🔐 LOGIN - Contraseña recibida: " + password);
        System.out.println("🔐 LOGIN - Hash en BD: " + u.getPassword().substring(0, 20) + "...");

        boolean matches = passwordEncoder.matches(password, u.getPassword());
        System.out.println("🔍 LOGIN - Contraseñas coinciden: " + matches);

        // ✅ Validar contraseña encriptada con BCrypt
        if (!matches) {
            System.out.println("❌ LOGIN - Contraseña incorrecta");
            return null;
        }

        System.out.println("✅ LOGIN - Login exitoso para: " + u.getNombreUsuario());
        return u;
    }

    // ------------------------------------------------------
    // REGISTRO
    // ------------------------------------------------------
    public Usuario registrar(Usuario usuario) {
        // ✅ Encriptar contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setEstado(EstadoUsuario.ACTIVO); // estado inicial
        return repo.save(usuario);
    }

    // ------------------------------------------------------
    // VER PERFIL
    // ------------------------------------------------------
    public Usuario getPerfil(Integer id) {
        return repo.findById(id).orElse(null);
    }

    // ------------------------------------------------------
    // EDITAR PERFIL SEGÚN ROL
    // ------------------------------------------------------
    public Usuario editarPerfil(Integer id, Usuario datos) {
        Usuario u = repo.findById(id).orElse(null);
        if (u == null) return null;

        // ---------------------------
        // SI ES CLIENTE
        // ---------------------------
        if (u.getRol() != null && "CLIENTE".equals(u.getRol().getNombreRol())) {

            // SOLO puede modificar email, teléfono, contraseña, foto
            u.setEmail(datos.getEmail());
            u.setTelefono(datos.getTelefono());

            if (datos.getPassword() != null && !datos.getPassword().isBlank()) {
                // ✅ Encriptar nueva contraseña
                u.setPassword(passwordEncoder.encode(datos.getPassword()));
            }

            // si tienes un campo foto en la entidad, puedes permitir esto:
            try {
                var campoFoto = Usuario.class.getDeclaredField("foto");
                campoFoto.setAccessible(true);
                Object fotoActualizada = campoFoto.get(datos);
                if (fotoActualizada != null) {
                    campoFoto.set(u, fotoActualizada);
                }
            } catch (Exception ignored) {}

            return repo.save(u);
        }

        // ---------------------------
        // SI ES ADMIN
        // ---------------------------
        if (u.getRol() != null && "ADMIN".equals(u.getRol().getNombreRol())) {

            // ADMIN puede modificar todo
            u.setNombreUsuario(datos.getNombreUsuario());
            u.setEmail(datos.getEmail());
            u.setTelefono(datos.getTelefono());
            u.setRol(datos.getRol());
            u.setEstado(datos.getEstado());

            if (datos.getPassword() != null && !datos.getPassword().isBlank()) {
                // ✅ Encriptar nueva contraseña
                u.setPassword(passwordEncoder.encode(datos.getPassword()));
            }

            // foto opcional
            try {
                var campoFoto = Usuario.class.getDeclaredField("foto");
                campoFoto.setAccessible(true);
                Object fotoActualizada = campoFoto.get(datos);
                if (fotoActualizada != null) {
                    campoFoto.set(u, fotoActualizada);
                }
            } catch (Exception ignored) {}

            return repo.save(u);
        }

        return null;
    }

    // ------------------------------------------------------
    // LISTAR TODOS
    // ------------------------------------------------------
    public List<Usuario> listar() {
        return repo.findAll();
    }

    // ------------------------------------------------------
    // LISTAR POR ESTADO
    // ------------------------------------------------------
    public List<Usuario> listarPorEstado(EstadoUsuario estado) {
        return repo.findByEstado(estado);
    }

    // ------------------------------------------------------
    // BUSCAR (nombre o email)
    // ------------------------------------------------------
    public List<Usuario> buscar(String query) {
        return repo.buscar(query);
    }

    // ------------------------------------------------------
    // OBTENER POR EMAIL
    // ------------------------------------------------------
    public Usuario buscarPorEmail(String email) {
        return repo.findByEmail(email);
    }

    // ------------------------------------------------------
    // CAMBIAR CONTRASEÑA
    // ------------------------------------------------------
    public boolean changePassword(Integer userId, String currentPassword, String newPassword) {
        Usuario u = repo.findById(userId).orElse(null);
        if (u == null) return false;

        // ✅ Validar que la contraseña actual sea correcta
        if (!passwordEncoder.matches(currentPassword, u.getPassword())) {
            return false;
        }

        // ✅ Encriptar y guardar la nueva contraseña
        u.setPassword(passwordEncoder.encode(newPassword));
        repo.save(u);
        return true;
    }

    // ------------------------------------------------------
    // VALIDAR EXISTENCIA DE USUARIO
    // ------------------------------------------------------
    public boolean existe(Integer id) {
        return repo.existsById(id);
    }
}
