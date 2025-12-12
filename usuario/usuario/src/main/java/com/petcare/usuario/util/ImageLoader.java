package com.petcare.usuario.util;

import org.springframework.core.io.ClassPathResource;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utilidad para cargar imágenes desde resources
 */
public class ImageLoader {

    /**
     * Carga una imagen desde la carpeta resources/images/profiles
     *
     * @param filename Nombre del archivo (ej: "admin.jpg")
     * @return byte[] con el contenido de la imagen
     * @throws IOException si no se puede leer la imagen
     */
    public static byte[] loadImageFromResources(String filename) throws IOException {
        ClassPathResource resource = new ClassPathResource("images/profiles/" + filename);

        try (InputStream inputStream = resource.getInputStream()) {
            return inputStream.readAllBytes();
        }
    }

    /**
     * Intenta cargar una imagen, retorna null si no existe
     */
    public static byte[] loadImageSafely(String filename) {
        try {
            return loadImageFromResources(filename);
        } catch (IOException e) {
            System.err.println("⚠️ No se pudo cargar la imagen: " + filename + " - " + e.getMessage());
            return null;
        }
    }
}

