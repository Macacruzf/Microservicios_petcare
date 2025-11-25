package com.petcare.usuario.repository;

import com.petcare.usuario.model.Usuario;
import com.petcare.usuario.model.Rol;
import com.petcare.usuario.model.EstadoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Usuario findByEmail(String email);

    // Buscar por nombre o correo
    @Query("SELECT u FROM Usuario u WHERE " +
            "LOWER(u.nombreUsuario) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Usuario> buscar(String query);

    List<Usuario> findByEstado(EstadoUsuario estado);

    List<Usuario> findByRol(Rol rol);
}
