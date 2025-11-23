package com.petcare.usuario.config;

import com.petcare.usuario.model.Rol;
import com.petcare.usuario.model.EstadoUsuario;
import com.petcare.usuario.model.Usuario;
import com.petcare.usuario.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initUsuarios(UsuarioRepository repo) {
        return args -> {

            if (repo.findByEmail("admin@petcare.com") == null) {
                Usuario admin = new Usuario();
                admin.setNombreUsuario("Admin");
                admin.setEmail("admin@petcare.com");
                admin.setTelefono("12345678");
                admin.setPassword("admin123");
                admin.setRol(Rol.ADMIN);
                admin.setEstado(EstadoUsuario.ACTIVO);
                repo.save(admin);
            }

            if (repo.findByEmail("cliente@petcare.com") == null) {
                Usuario cliente = new Usuario();
                cliente.setNombreUsuario("Cliente Demo");
                cliente.setEmail("cliente@petcare.com");
                cliente.setTelefono("98765432");
                cliente.setPassword("cliente123");
                cliente.setRol(Rol.CLIENTE);
                cliente.setEstado(EstadoUsuario.ACTIVO);
                repo.save(cliente);
            }
        };
    }
}
