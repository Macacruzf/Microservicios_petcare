package com.petcare.usuario.controller;

import com.petcare.usuario.dto.LoginRequest;
import com.petcare.usuario.dto.LoginResponse;
import com.petcare.usuario.model.EstadoUsuario;
import com.petcare.usuario.model.Rol;
import com.petcare.usuario.model.Usuario;
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
    private final UsuarioController controller = new UsuarioController(service);

    // ------------------------------------------------------------
    // 1. LOGIN
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

        assertEquals(200, resp.getStatusCodeValue());
        LoginResponse body = (LoginResponse) resp.getBody();
        assertTrue(body.isSuccess());
        assertEquals("Fran", body.getUsuario().getNombreUsuario());
    }

    @Test
    void loginInvalido() {
        Mockito.when(service.login(anyString(), anyString()))
                .thenReturn(null);

        LoginRequest req = new LoginRequest();
        req.setEmail("bad@test.com");
        req.setPassword("wrong");

        ResponseEntity<?> resp = controller.login(req);

        assertEquals(401, resp.getStatusCodeValue());
        Map<?, ?> body = (Map<?, ?>) resp.getBody();
        assertEquals("Credenciales inválidas", body.get("message"));
    }

    // ------------------------------------------------------------
    // 2. REGISTRO
    // ------------------------------------------------------------
    @Test
    void registrarUsuario() {

        Usuario nuevo = new Usuario(2, "Nuevo", "nuevo@test.com",
                "555", "pass", Rol.CLIENTE, EstadoUsuario.ACTIVO);

        Mockito.when(service.registrar(any(Usuario.class)))
                .thenReturn(nuevo);

        ResponseEntity<?> resp = controller.registrar(nuevo, Rol.CLIENTE);

        assertEquals(200, resp.getStatusCodeValue());
        Usuario body = (Usuario) resp.getBody();
        assertEquals("nuevo@test.com", body.getEmail());
    }

    // ------------------------------------------------------------
    // 3. VER PERFIL
    // ------------------------------------------------------------
    @Test
    void verPerfilOk() {

        Usuario user = new Usuario(1, "Fran", "fran@test.com",
                "123", "pass", Rol.CLIENTE, EstadoUsuario.ACTIVO);

        Mockito.when(service.getPerfil(1)).thenReturn(user);

        ResponseEntity<?> resp = controller.perfil(1);

        assertEquals(200, resp.getStatusCodeValue());
        Usuario body = (Usuario) resp.getBody();
        assertEquals("Fran", body.getNombreUsuario());
    }

    @Test
    void verPerfilNoEncontrado() {

        Mockito.when(service.getPerfil(1)).thenReturn(null);

        ResponseEntity<?> resp = controller.perfil(1);

        assertEquals(404, resp.getStatusCodeValue());
        assertEquals("No encontrado", resp.getBody());
    }

    // ------------------------------------------------------------
    // 4. EDITAR PERFIL
    // ------------------------------------------------------------
    @Test
    void editarPerfilOk() {

        Usuario actualizado = new Usuario(1, "Fran", "nuevo@test.com",
                "111", "pass", Rol.CLIENTE, EstadoUsuario.ACTIVO);

        Mockito.when(service.editarPerfil(eq(1), any(Usuario.class)))
                .thenReturn(actualizado);

        ResponseEntity<?> resp = controller.editarPerfil(1, actualizado);

        assertEquals(200, resp.getStatusCodeValue());
        Usuario body = (Usuario) resp.getBody();
        assertEquals("nuevo@test.com", body.getEmail());
    }

    @Test
    void editarPerfilNoEncontrado() {

        Mockito.when(service.editarPerfil(eq(1), any(Usuario.class)))
                .thenReturn(null);

        Usuario envio = new Usuario();
        envio.setEmail("fail@test.com");

        ResponseEntity<?> resp = controller.editarPerfil(1, envio);

        assertEquals(404, resp.getStatusCodeValue());
        assertEquals("No encontrado", resp.getBody());
    }

    // ------------------------------------------------------------
    // 5. LISTAR (ADMIN)
    // ------------------------------------------------------------
    @Test
    void listarUsuariosOk() {

        List<Usuario> lista = List.of(
                new Usuario(1, "Fran", "fran@test.com",
                        "111", "pass", Rol.CLIENTE, EstadoUsuario.ACTIVO),
                new Usuario(2, "Admin", "admin@test.com",
                        "222", "pass", Rol.ADMIN, EstadoUsuario.ACTIVO)
        );

        Mockito.when(service.listar()).thenReturn(lista);

        ResponseEntity<?> resp = controller.listar(Rol.ADMIN);

        assertEquals(200, resp.getStatusCodeValue());
        List<?> body = (List<?>) resp.getBody();
        assertEquals(2, body.size());
    }

    @Test
    void listarUsuariosNoAutorizado() {

        ResponseEntity<?> resp = controller.listar(Rol.CLIENTE);

        assertEquals(403, resp.getStatusCodeValue());
        assertEquals("No autorizado", resp.getBody());
    }

    // ------------------------------------------------------------
    // 6. LISTAR POR ESTADO
    // ------------------------------------------------------------
    @Test
    void listarPorEstado() {

        Usuario user = new Usuario(3, "Inactivo", "off@test.com",
                "333", "pass", Rol.CLIENTE, EstadoUsuario.INACTIVO);

        Mockito.when(service.listarPorEstado(EstadoUsuario.INACTIVO))
                .thenReturn(List.of(user));

        ResponseEntity<?> resp = controller.listarPorEstado(EstadoUsuario.INACTIVO);

        assertEquals(200, resp.getStatusCodeValue());
        List<?> body = (List<?>) resp.getBody();
        assertEquals(1, body.size());
    }

    // ------------------------------------------------------------
    // 7. BUSCAR
    // ------------------------------------------------------------
    @Test
    void buscarUsuario() {

        Usuario user = new Usuario(1, "F
