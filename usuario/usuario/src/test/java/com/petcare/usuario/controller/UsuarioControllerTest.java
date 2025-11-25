package com.petcare.usuario.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petcare.usuario.dto.LoginRequest;
import com.petcare.usuario.dto.RegisterRequest;
import com.petcare.usuario.model.EstadoUsuario;
import com.petcare.usuario.model.Rol;
import com.petcare.usuario.model.Usuario;
import com.petcare.usuario.repository.UsuarioRepository;
import com.petcare.usuario.service.UsuarioService;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false) // ⚠️ Importante: Ignora seguridad (tokens) para probar solo la lógica
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // ============================================================
    // TEST: LOGIN
    // ============================================================
    @Test
    @DisplayName("POST /login - Exito: Retorna usuario y success true")
    void login_Exito() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setEmail("juan@mail.com");
        req.setPassword("1234");

        Usuario u = new Usuario();
        u.setIdUsuario(1);
        u.setEmail("juan@mail.com");
        u.setNombreUsuario("Juan");

        when(usuarioService.login("juan@mail.com", "1234")).thenReturn(u);

        mockMvc.perform(post("/usuario/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.usuario.email").value("juan@mail.com"));
    }

    @Test
    @DisplayName("POST /login - Fallo: Credenciales inválidas (401)")
    void login_Fallo() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setEmail("bad@mail.com");
        req.setPassword("wrong");

        when(usuarioService.login("bad@mail.com", "wrong")).thenReturn(null);

        mockMvc.perform(post("/usuario/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized()) // Espera 401
                .andExpect(jsonPath("$.success").value(false));
    }

    // ============================================================
    // TEST: REGISTRO
    // ============================================================
    @Test
    @DisplayName("POST /register - Exito: Crea nuevo usuario")
    void registrar_Exito() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setNombreUsuario("nuevoUser");
        req.setEmail("nuevo@mail.com");
        req.setPassword("pass");
        req.setRol("CLIENTE");

        Usuario creado = new Usuario();
        creado.setIdUsuario(10);
        creado.setNombreUsuario("nuevoUser");
        creado.setRol(Rol.CLIENTE);

        when(usuarioService.registrar(any(Usuario.class))).thenReturn(creado);

        mockMvc.perform(post("/usuario/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(10))
                .andExpect(jsonPath("$.rol").value("CLIENTE"));
    }

    // ============================================================
    // TEST: PERFIL (GET & PUT)
    // ============================================================
    @Test
    @DisplayName("GET /perfil/{id} - Exito: Retorna perfil")
    void perfil_Exito() throws Exception {
        Usuario u = new Usuario();
        u.setIdUsuario(1);
        u.setNombreUsuario("Pepe");

        when(usuarioService.getPerfil(1)).thenReturn(u);

        mockMvc.perform(get("/usuario/perfil/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreUsuario").value("Pepe"));
    }

    @Test
    @DisplayName("GET /perfil/{id} - Fallo: 404 No Encontrado")
    void perfil_NoEncontrado() throws Exception {
        when(usuarioService.getPerfil(99)).thenReturn(null);

        mockMvc.perform(get("/usuario/perfil/{id}", 99))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /perfil/{id} - Exito: Actualiza perfil")
    void editarPerfil_Exito() throws Exception {
        Usuario datos = new Usuario();
        datos.setTelefono("9999999");

        Usuario actualizado = new Usuario();
        actualizado.setIdUsuario(1);
        actualizado.setTelefono("9999999");

        when(usuarioService.editarPerfil(eq(1), any(Usuario.class))).thenReturn(actualizado);

        mockMvc.perform(put("/usuario/perfil/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(datos)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.telefono").value("9999999"));
    }

    // ============================================================
    // TEST: LISTAR (ADMIN)
    // ============================================================
    @Test
    @DisplayName("GET /listar/{idAdmin} - Exito: Admin autorizado")
    void listar_ExitoAdmin() throws Exception {
        Usuario admin = new Usuario();
        admin.setIdUsuario(1);
        admin.setRol(Rol.ADMIN);

        Usuario u2 = new Usuario();
        u2.setIdUsuario(2);

        when(usuarioService.getPerfil(1)).thenReturn(admin);
        when(usuarioService.listar()).thenReturn(Arrays.asList(admin, u2));

        mockMvc.perform(get("/usuario/listar/{idAdmin}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("GET /listar/{idAdmin} - Fallo: 403 No autorizado")
    void listar_FalloNoAdmin() throws Exception {
        Usuario cliente = new Usuario();
        cliente.setIdUsuario(2);
        cliente.setRol(Rol.CLIENTE);

        when(usuarioService.getPerfil(2)).thenReturn(cliente);

        mockMvc.perform(get("/usuario/listar/{idAdmin}", 2))
                .andExpect(status().isForbidden());
    }

    // ============================================================
    // TEST: OTROS ENDPOINTS (Estados, Buscar, Validar, Roles)
    // ============================================================
    @Test
    @DisplayName("GET /estados - Listar por estado")
    void listarPorEstado_Exito() throws Exception {
        when(usuarioService.listarPorEstado(EstadoUsuario.ACTIVO))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/usuario/estados").param("estado", "ACTIVO"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /buscar - Buscar usuario")
    void buscar_Exito() throws Exception {
        when(usuarioService.buscar("algo")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/usuario/buscar").param("q", "algo"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /validar-credenciales - Exito: Valido")
    void validarCredenciales_Valido() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setEmail("valid@mail.com");
        req.setPassword("pass");

        Usuario u = new Usuario();
        u.setIdUsuario(5);
        u.setRol(Rol.CLIENTE);
        u.setEstado(EstadoUsuario.ACTIVO);

        when(usuarioService.login("valid@mail.com", "pass")).thenReturn(u);

        mockMvc.perform(post("/usuario/validar-credenciales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valido").value(true))
                .andExpect(jsonPath("$.rol").value("CLIENTE"));
    }

    @Test
    @DisplayName("GET /{id}/rol - Obtener Rol")
    void obtenerRol_Exito() throws Exception {
        Usuario u = new Usuario();
        u.setRol(Rol.ADMIN);

        when(usuarioService.getPerfil(10)).thenReturn(u);

        mockMvc.perform(get("/usuario/{id}/rol", 10))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rol").value("ADMIN"));
    }

    @Test
    @DisplayName("DELETE /{id} - Eliminar usuario")
    void eliminar_Exito() throws Exception {
        doNothing().when(usuarioRepository).deleteById(1);

        mockMvc.perform(delete("/usuario/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario eliminado"));
    }
}