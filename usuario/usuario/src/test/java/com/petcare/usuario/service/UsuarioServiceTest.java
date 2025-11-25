package com.petcare.usuario.service;

import com.petcare.usuario.model.EstadoUsuario;
import com.petcare.usuario.model.Rol;
import com.petcare.usuario.model.Usuario;
import com.petcare.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

class UsuarioServiceTest {

    private final UsuarioRepository repo = Mockito.mock(UsuarioRepository.class);
    private final UsuarioService service = new UsuarioService(repo);

    // ------------------------------------------------------------
    // 1. LOGIN
    // ------------------------------------------------------------
    @Test
    void loginOk() {
        Usuario u = new Usuario(1, "Fran", "fran@test.com",
                "111", "pass", Rol.CLIENTE, EstadoUsuario.ACTIVO);

        Mockito.when(repo.findByEmail("fran@test.com")).thenReturn(u);

        Usuario result = service.login("fran@test.com", "pass");

        assertNotNull(result);
        assertEquals("Fran", result.getNombreUsuario());
    }

    @Test
    void loginEmailNoExiste() {
        Mockito.when(repo.findByEmail(anyString())).thenReturn(null);

        Usuario result = service.login("no@test.com", "pass");
        assertNull(result);
    }

    @Test
    void loginPasswordIncorrecta() {
        Usuario u = new Usuario(1, "Fran", "fran@test.com",
                "111", "pass", Rol.CLIENTE, EstadoUsuario.ACTIVO);

        Mockito.when(repo.findByEmail("fran@test.com")).thenReturn(u);

        Usuario result = service.login("fran@test.com", "badpass");
        assertNull(result);
    }

    // ------------------------------------------------------------
    // 2. REGISTRAR
    // ------------------------------------------------------------
    @Test
    void registrarUsuario() {
        Usuario nuevo = new Usuario(null, "Nuevo", "nuevo@test.com",
                "222", "pass", Rol.CLIENTE, null);

        Usuario guardado = new Usuario(1, "Nuevo", "nuevo@test.com",
                "222", "pass", Rol.CLIENTE, EstadoUsuario.ACTIVO);

        // Cuando se guarda, retorna el usuario completo
        Mockito.when(repo.save(any(Usuario.class))).thenReturn(guardado);

        Usuario result = service.registrar(nuevo);

        assertEquals(EstadoUsuario.ACTIVO, result.getEstado());
        assertEquals("nuevo@test.com", result.getEmail());
    }

    // ------------------------------------------------------------
    // 3. GET PERFIL
    // ------------------------------------------------------------
    @Test
    void getPerfilEncontrado() {
        Usuario u = new Usuario(1, "Fran", "f@test.com",
                "111", "pass", Rol.CLIENTE, EstadoUsuario.ACTIVO);

        Mockito.when(repo.findById(1)).thenReturn(Optional.of(u));

        Usuario result = service.getPerfil(1);

        assertNotNull(result);
        assertEquals("Fran", result.getNombreUsuario());
    }

    @Test
    void getPerfilNoEncontrado() {
        Mockito.when(repo.findById(1)).thenReturn(Optional.empty());

        Usuario result = service.getPerfil(1);

        assertNull(result);
    }

    // ------------------------------------------------------------
    // 4. EDITAR PERFIL
    // ------------------------------------------------------------
    @Test
    void editarPerfilOk() {
        Usuario original = new Usuario(1, "Fran", "old@test.com",
                "111", "pass", Rol.CLIENTE, EstadoUsuario.ACTIVO);

        Usuario cambios = new Usuario(null, null, "new@test.com",
                "999", "newpass", null, null);

        Usuario actualizado = new Usuario(1, "Fran", "new@test.com",
                "999", "newpass", Rol.CLIENTE, EstadoUsuario.ACTIVO);

        Mockito.when(repo.findById(1)).thenReturn(Optional.of(original));
        Mockito.when(repo.save(any(Usuario.class))).thenReturn(actualizado);

        Usuario result = service.editarPerfil(1, cambios);

        assertEquals("new@test.com", result.getEmail());
        assertEquals("999", result.getTelefono());
        assertEquals("newpass", result.getPassword());
    }

    @Test
    void editarPerfilNoExiste() {
        Mockito.when(repo.findById(1)).thenReturn(Optional.empty());

        Usuario cambios = new Usuario();
        Usuario result = service.editarPerfil(1, cambios);

        assertNull(result);
    }

    // ------------------------------------------------------------
    // 5. LISTAR TODOS
    // ------------------------------------------------------------
    @Test
    void listarUsuarios() {
        List<Usuario> lista = List.of(
                new Usuario(1, "A", "a@test.com", "111", "pass", Rol.CLIENTE, EstadoUsuario.ACTIVO),
                new Usuario(2, "B", "b@test.com", "222", "pass", Rol.CLIENTE, EstadoUsuario.ACTIVO)
        );

        Mockito.when(repo.findAll()).thenReturn(lista);

        List<Usuario> result = service.listar();

        assertEquals(2, result.size());
    }

    // ------------------------------------------------------------
    // 6. LISTAR POR ESTADO
    // ------------------------------------------------------------
    @Test
    void listarPorEstado() {
        Usuario u = new Usuario(3, "C", "c@test.com",
                "333", "pass", Rol.CLIENTE, EstadoUsuario.INACTIVO);

        Mockito.when(repo.findByEstado(EstadoUsuario.INACTIVO))
                .thenReturn(List.of(u));

        List<Usuario> result = service.listarPorEstado(EstadoUsuario.INACTIVO);

        assertEquals(1, result.size());
        assertEquals(EstadoUsuario.INACTIVO, result.get(0).getEstado());
    }

    // ------------------------------------------------------------
    // 7. BUSCAR
    // ------------------------------------------------------------
    @Test
    void buscar() {
        Usuario u = new Usuario(1, "Fran", "fran@test.com",
                "111", "pass", Rol.CLIENTE, EstadoUsuario.ACTIVO);

        Mockito.when(repo.buscar("fran")).thenReturn(List.of(u));

        List<Usuario> result = service.buscar("fran");

        assertEquals(1, result.size());
        assertEquals("Fran", result.get(0).getNombreUsuario());
    }
}
