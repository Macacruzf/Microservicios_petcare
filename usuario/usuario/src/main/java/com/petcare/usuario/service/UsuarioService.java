package com.petcare.usuario.service;

import com.petcare.usuario.model.Usuario;
import com.petcare.usuario.model.EstadoUsuario;
import com.petcare.usuario.model.Rol;
import com.petcare.usuario.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repo;

    public UsuarioService(UsuarioRepository repo) {
        this.repo = repo;
    }

    // ------------------------------------------------------
    // LOGIN SIMPLE
    // ------------------------------------------------------
    public Usuario login(String email, String password) {
        Usuario u = repo.findByEmail(email);
        if (u == null) return null;
        if (!u.getPassword().equals(password)) return null;
        return u;
    }

    // ------------------------------------------------------
    // REGISTRO
    // ------------------------------------------------------
    public Usuario registrar(Usuario usuario) {
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
        if (u.getRol() == Rol.CLIENTE) {

            // SOLO puede modificar email, teléfono, contraseña, foto
            u.setEmail(datos.getEmail());
            u.setTelefono(datos.getTelefono());

            if (datos.getPassword() != null && !datos.getPassword().isBlank()) {
                u.setPassword(datos.getPassword());
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
        if (u.getRol() == Rol.ADMIN) {

            // ADMIN puede modificar todo
            u.setNombreUsuario(datos.getNombreUsuario());
            u.setEmail(datos.getEmail());
            u.setTelefono(datos.getTelefono());
            u.setRol(datos.getRol());
            u.setEstado(datos.getEstado());

            if (datos.getPassword() != null && !datos.getPassword().isBlank()) {
                u.setPassword(datos.getPassword());
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
    // VALIDAR EXISTENCIA DE USUARIO
    // ------------------------------------------------------
    public boolean existe(Integer id) {
        return repo.existsById(id);
    }
}
