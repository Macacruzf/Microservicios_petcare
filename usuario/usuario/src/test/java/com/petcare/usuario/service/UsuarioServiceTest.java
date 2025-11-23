package com.petcare.usuario.service;

import com.petcare.usuario.dto.RegisterRequest;
import com.petcare.usuario.model.Usuario;
import com.petcare.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    UsuarioServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registrar_UsuarioNuevo_CreaCorrectamente() {
        // Arrange
        RegisterRequest dto = new RegisterRequest();
        dto.setNombreUsuario("Francisca");
        dto.setEmail("fran@test.com");
        dto.setTelefono("12345678");
        dto.setPassword("pass123");
        dto.setRol("CLIENTE");

        when(usuarioRepository.existsByEmail("fran@test.com")).thenReturn(false);
        when(passwordEncoder.encode("pass123")).thenReturn("pass_encoded");

        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);

        // Act
        usuarioService.registrar(dto);

        // Assert
        verify(usuarioRepository).save(usuarioCaptor.capture());
        Usuario u = usuarioCaptor.getValue();

        assertEquals("Francisca", u.getNombreUsuario());
        assertEquals("fran@test.com", u.getEmail());
        assertEquals("pass_encoded", u.getPassword());
        assertEquals("CLIENTE", u.getRol());
    }

    @Test
    void registrar_CorreoDuplicado_LanzaError() {
        RegisterRequest dto = new RegisterRequest();
        dto.setEmail("repetido@test.com");

        when(usuarioRepository.existsByEmail("repetido@test.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> usuarioService.registrar(dto));
    }

    @Test
    void buscarPorEmail_ExisteDevuelveUsuario() {
        Usuario u = new Usuario();
        u.setEmail("test@test.com");

        when(usuarioRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(u));

        Usuario encontrado = usuarioService.buscarPorEmail("test@test.com");

        assertNotNull(encontrado);
        assertEquals("test@test.com", encontrado.getEmail());
    }

    @Test
    void buscarPorEmail_NoExisteDevuelveNull() {
        when(usuarioRepository.findByEmail("x@test.com"))
                .thenReturn(Optional.empty());

        Usuario encontrado = usuarioService.buscarPorEmail("x@test.com");

        assertNull(encontrado);
    }
}
