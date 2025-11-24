package com.petcare.usuario.service;

import com.petcare.usuario.model.Usuario;
import com.petcare.usuario.model.EstadoUsuario;
import com.petcare.usuario.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repo;

    public UsuarioService(UsuarioRepository repo) {
        this.repo = repo;
    }

    // LOGIN
    public Usuario login(String email, String password) {
        Usuario u = repo.findByEmail(email);
        if (u == null) return null;
        if (!u.getPassword().equals(password)) return null;
        return u;
    }

    // REGISTRO
    public Usuario registrar(Usuario usuario) {
        usuario.setEstado(EstadoUsuario.ACTIVO); // por defecto
        return repo.save(usuario);
    }

    // VER PERFIL
    public Usuario getPerfil(Integer id) {
        return repo.findById(id).orElse(null);
    }

    // EDITAR PERFIL (solo datos propios)
    public Usuario editarPerfil(Integer id, Usuario datos) {
        Usuario u = repo.findById(id).orElse(null);
        if (u == null) return null;

        u.setEmail(datos.getEmail());
        u.setTelefono(datos.getTelefono());

        if (datos.getPassword() != null && !datos.getPassword().isBlank())
            u.setPassword(datos.getPassword());

        return repo.save(u);
    }

    // LISTAR TODOS (solo admin)
    public List<Usuario> listar() {
        return repo.findAll();
    }

    // LISTAR POR ESTADO
    public List<Usuario> listarPorEstado(EstadoUsuario estado) {
        return repo.findByEstado(estado);
    }

    // BUSCAR
    public List<Usuario> buscar(String query) {
        return repo.buscar(query);
    }

    
}
