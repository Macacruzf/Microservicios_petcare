package com.petcare.usuario.controller;

import com.petcare.usuario.dto.LoginRequest;
import com.petcare.usuario.dto.LoginResponse;
import com.petcare.usuario.model.EstadoUsuario;
import com.petcare.usuario.model.Rol;
import com.petcare.usuario.model.Usuario;
import com.petcare.usuario.repository.UsuarioRepository;
import com.petcare.usuario.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

class UsuarioControllerTest {

private final UsuarioService service = Mockito.mock(UsuarioService.class);
private final UsuarioRepository repo = Mockito.mock(UsuarioRepository.class);

private final UsuarioController controller = new UsuarioController(service, repo);


    // ------------------------------------------------------------
    // LOGIN
    // ------------------------------------------------------------
    @Test
    void loginOk() {
        Usuario user = new Usuario(1, "Fran", "fran@test.com",
                "123", "pass", Rol.CLIENTE, EstadoUsuario.ACTIVO);

        Mockito.when(service.login("fran@test.com", "pass"))
                .thenReturn(user);

        LoginRequest req = new LoginRequest();
        req.setEmail("fran@test.com");
        req.setPassword("pass");

        ResponseEntity<?> resp = controller.login(req);

        assertEquals(200, resp.getStatusCode().value());
        LoginResponse body = (LoginResponse) resp.getBody();
        assertTrue(body.isSuccess());
        assertEquals("Fran", body.getUsuario().getNombreUsuario());
    }

    @Test
    void loginCredencialesInvalidas() {
        Mockito.when(service.login(anyString(), anyString()))
                .thenReturn(null);

        LoginRequest req = new LoginRequest();
        req.setEmail("bad@test.com");
        req.setPassword("wrong");

        ResponseEntity<?> resp = controller.login(req);

        assertEquals(401, resp.getStatusCode().value());
        Map<?, ?> body = (Map<?, ?>) resp.getBody();
        assertEquals("Credenciales inválidas", body.get("message"));
    }

    // ------------------------------------------------------------
    // VER PERFIL
    // ------------------------------------------------------------
    @Test
    void verPerfilOk() {
        Usuario user = new Usuario(1, "Fran", "fran@test.com",
                "123", "pass", Rol.CLIENTE, EstadoUsuario.ACTIVO);

        Mockito.when(service.getPerfil(1)).thenReturn(user);

        ResponseEntity<?> resp = controller.perfil(1);

        assertEquals(200, resp.getStatusCode().value());
        Usuario body = (Usuario) resp.getBody();
        assertEquals("Fran", body.getNombreUsuario());
    }

    @Test
    void verPerfilNoEncontrado() {
        Mockito.when(service.getPerfil(1)).thenReturn(null);

        ResponseEntity<?> resp = controller.perfil(1);

        assertEquals(404, resp.getStatusCode().value());
        assertEquals("No encontrado", resp.getBody());
    }

    // ------------------------------------------------------------
    // EDITAR PERFIL
    // ------------------------------------------------------------
    @Test
    void editarPerfilOk() {
        Usuario original = new Usuario(1, "Fran", "old@test.com",
                "123", "pass", Rol.CLIENTE, EstadoUsuario.ACTIVO);

        Usuario cambios = new Usuario(null, null, "nuevo@test.com",
                "999", "newpass", null, null);

        Usuario actualizado = new Usuario(1, "Fran", "nuevo@test.com",
                "999", "newpass", Rol.CLIENTE, EstadoUsuario.ACTIVO);

        Mockito.when(service.editarPerfil(eq(1), any(Usuario.class)))
                .thenReturn(actualizado);

        ResponseEntity<?> resp = controller.editarPerfil(1, cambios);

        assertEquals(200, resp.getStatusCode().value());
        Usuario body = (Usuario) resp.getBody();
        assertEquals("nuevo@test.com", body.getEmail());
    }

    @Test
    void editarPerfilNoEncontrado() {
        Mockito.when(service.editarPerfil(eq(1), any(Usuario.class)))
                .thenReturn(null);

        Usuario cambios = new Usuario();
        cambios.setEmail("fail@test.com");

        ResponseEntity<?> resp = controller.editarPerfil(1, cambios);

        assertEquals(404, resp.getStatusCode().value());
        assertEquals("No encontrado", resp.getBody());
    }

    // ------------------------------------------------------------
    //  LISTAR POR ESTADO
    // ------------------------------------------------------------
    @Test
    void listarPorEstado() {
        Usuario u = new Usuario(1, "Fran", "f@test.com",
                "123", "pass", Rol.CLIENTE, EstadoUsuario.INACTIVO);

        Mockito.when(service.listarPorEstado(EstadoUsuario.INACTIVO))
                .thenReturn(List.of(u));

        ResponseEntity<?> resp = controller.listarPorEstado(EstadoUsuario.INACTIVO);

        assertEquals(200, resp.getStatusCode().value());
        List<?> body = (List<?>) resp.getBody();
        assertEquals(1, body.size());
    }

    // ------------------------------------------------------------
    // BUSCAR
    // ------------------------------------------------------------
    @Test
    void buscarUsuario() {
        Usuario u = new Usuario(1, "Fran", "fran@test.com",
                "123", "pass", Rol.CLIENTE, EstadoUsuario.ACTIVO);

        Mockito.when(service.buscar("fran"))
                .thenReturn(List.of(u));

        ResponseEntity<?> resp = controller.buscar("fran");

        assertEquals(200, resp.getStatusCode().value());
        List<?> body = (List<?>) resp.getBody();
        assertEquals(1, body.size());
        assertEquals("Fran", ((Usuario) body.get(0)).getNombreUsuario());
    }
}
