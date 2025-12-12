package com.petcare.usuario.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utilidad para generar hashes BCrypt
 * Ejecuta este main para obtener los hashes correctos
 */
public class BCryptHashGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Generar hash para Admin.123
        String adminPassword = "Admin.123";
        String adminHash = encoder.encode(adminPassword);
        System.out.println("=== HASH PARA ADMIN ===");
        System.out.println("Contraseña: " + adminPassword);
        System.out.println("Hash BCrypt: " + adminHash);
        System.out.println();

        // Generar hash para Cliente.123
        String clientePassword = "Cliente.123";
        String clienteHash = encoder.encode(clientePassword);
        System.out.println("=== HASH PARA CLIENTE ===");
        System.out.println("Contraseña: " + clientePassword);
        System.out.println("Hash BCrypt: " + clienteHash);
        System.out.println();

        // Verificar que los hashes funcionan
        System.out.println("=== VERIFICACIÓN ===");
        System.out.println("Admin - Hash válido: " + encoder.matches(adminPassword, adminHash));
        System.out.println("Cliente - Hash válido: " + encoder.matches(clientePassword, clienteHash));
    }
}

