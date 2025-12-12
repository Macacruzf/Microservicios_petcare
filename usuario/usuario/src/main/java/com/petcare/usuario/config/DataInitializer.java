package com.petcare.usuario.config;

import com.petcare.usuario.model.RolEntity;
import com.petcare.usuario.model.EstadoUsuario;
import com.petcare.usuario.model.Usuario;
import com.petcare.usuario.repository.UsuarioRepository;
import com.petcare.usuario.repository.RolRepository;
import com.petcare.usuario.util.ImageLoader;
import com.petcare.usuario.util.AvatarGenerator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initUsuarios(UsuarioRepository usuarioRepo, RolRepository rolRepo, PasswordEncoder passwordEncoder) {
        return args -> {

            System.out.println("=== Inicializando datos de Usuario ===");

            // 🔧 GENERAR HASHES BCRYPT CORRECTOS (temporal)
            System.out.println("\n========== HASHES BCRYPT GENERADOS ==========");
            String adminHash = passwordEncoder.encode("Admin.123");
            String clienteHash = passwordEncoder.encode("Cliente.123");
            System.out.println("Admin.123 -> " + adminHash);
            System.out.println("Cliente.123 -> " + clienteHash);
            System.out.println("Longitud Admin: " + adminHash.length());
            System.out.println("Longitud Cliente: " + clienteHash.length());
            System.out.println("============================================\n");

            // PASO 1: Crear roles si no existen
            RolEntity rolAdmin = rolRepo.findByNombreRol("ADMIN").orElse(null);
            if (rolAdmin == null) {
                rolAdmin = new RolEntity();
                rolAdmin.setNombreRol("ADMIN");
                rolAdmin.setDescripcion("Administrador con acceso completo al sistema");
                rolAdmin.setActivo(true);
                rolAdmin = rolRepo.save(rolAdmin);
                System.out.println("✅ Rol ADMIN creado");
            }

            RolEntity rolCliente = rolRepo.findByNombreRol("CLIENTE").orElse(null);
            if (rolCliente == null) {
                rolCliente = new RolEntity();
                rolCliente.setNombreRol("CLIENTE");
                rolCliente.setDescripcion("Cliente que puede realizar compras y dejar reseñas");
                rolCliente.setActivo(true);
                rolCliente = rolRepo.save(rolCliente);
                System.out.println("✅ Rol CLIENTE creado");
            }

            // PASO 2: Crear usuarios si no existen

            if (usuarioRepo.findByEmail("admin@petcare.cl") == null) {
                Usuario admin = new Usuario();
                admin.setNombreUsuario("Admin PetCare");
                admin.setEmail("admin@petcare.cl");
                admin.setTelefono("912345678");
                // ✅ Usar hash generado dinámicamente
                admin.setPassword(adminHash);
                admin.setRol(rolAdmin);
                admin.setEstado(EstadoUsuario.ACTIVO);

                // ✅ Cargar imagen real de admin.jpg
                byte[] adminFoto = ImageLoader.loadImageSafely("admin.jpg");
                if (adminFoto != null) {
                    admin.setFotoPerfil(adminFoto);
                    System.out.println("✅ Usuario ADMIN creado con foto de perfil (admin.jpg)");
                } else {
                    // Fallback: generar avatar si no existe la imagen
                    admin.setFotoPerfil(AvatarGenerator.generateAvatarForRole("A", "ADMIN"));
                    System.out.println("✅ Usuario ADMIN creado con avatar generado");
                }

                usuarioRepo.save(admin);
            }

            if (usuarioRepo.findByEmail("cliente@petcare.cl") == null) {
                Usuario clienteDemo = new Usuario();
                clienteDemo.setNombreUsuario("Cliente Demo");
                clienteDemo.setEmail("cliente@petcare.cl");
                clienteDemo.setTelefono("987654321");
                // ✅ Usar hash generado dinámicamente
                clienteDemo.setPassword(clienteHash);
                clienteDemo.setRol(rolCliente);
                clienteDemo.setEstado(EstadoUsuario.ACTIVO);

                // ✅ Cargar imagen real de cliente.png
                byte[] clienteFoto = ImageLoader.loadImageSafely("cliente.png");
                if (clienteFoto != null) {
                    clienteDemo.setFotoPerfil(clienteFoto);
                    System.out.println("✅ Usuario CLIENTE creado con foto de perfil (cliente.png)");
                } else {
                    // Fallback: generar avatar si no existe la imagen
                    clienteDemo.setFotoPerfil(AvatarGenerator.generateAvatarForRole("CD", "CLIENTE"));
                    System.out.println("✅ Usuario CLIENTE creado con avatar generado");
                }

                usuarioRepo.save(clienteDemo);
            }

            // PASO 3: Actualizar usuarios existentes sin foto
            usuarioRepo.findAll().forEach(usuario -> {
                if (usuario.getFotoPerfil() == null) {
                    String rolNombre = usuario.getRol() != null ? usuario.getRol().getNombreRol() : "CLIENTE";
                    byte[] foto = null;

                    // Intentar cargar imagen específica según el rol
                    if ("ADMIN".equals(rolNombre)) {
                        foto = ImageLoader.loadImageSafely("admin.jpg");
                    } else {
                        foto = ImageLoader.loadImageSafely("cliente.png");
                    }

                    // Si no existe imagen específica, generar avatar
                    if (foto == null) {
                        String initials = obtenerIniciales(usuario.getNombreUsuario());
                        foto = AvatarGenerator.generateAvatarForRole(initials, rolNombre);
                    }

                    usuario.setFotoPerfil(foto);
                    usuarioRepo.save(usuario);
                    System.out.println("✅ Foto de perfil actualizada para: " + usuario.getNombreUsuario());
                }
            });

            System.out.println("=== Inicialización de usuarios completada ===");
        };
    }

    /**
     * Obtiene las iniciales de un nombre completo
     */
    private String obtenerIniciales(String nombreCompleto) {
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            return "U";
        }

        String[] partes = nombreCompleto.trim().split("\\s+");
        if (partes.length == 1) {
            return partes[0].substring(0, Math.min(2, partes[0].length()));
        } else {
            return "" + partes[0].charAt(0) + partes[partes.length - 1].charAt(0);
        }
    }
}
