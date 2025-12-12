package com.petcare.usuario.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Utilidad para generar avatares de perfil por defecto
 */
public class AvatarGenerator {

    /**
     * Genera un avatar circular con las iniciales del usuario
     * 
     * @param initials Iniciales del usuario (máximo 2 caracteres)
     * @param backgroundColor Color de fondo
     * @return byte[] representando la imagen JPEG
     */
    public static byte[] generateAvatar(String initials, Color backgroundColor) {
        int size = 200; // Tamaño de la imagen
        
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        
        // Habilitar antialiasing para mejor calidad
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Dibujar fondo blanco
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, size, size);
        
        // Dibujar círculo de color
        g2d.setColor(backgroundColor);
        g2d.fill(new Ellipse2D.Double(0, 0, size, size));
        
        // Dibujar las iniciales en blanco
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 80));
        
        // Centrar el texto
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(initials);
        int textHeight = fm.getAscent();
        int x = (size - textWidth) / 2;
        int y = (size - textHeight) / 2 + fm.getAscent();
        
        g2d.drawString(initials.toUpperCase(), x, y);
        g2d.dispose();
        
        // Convertir a byte array
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "jpg", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error generando avatar", e);
        }
    }
    
    /**
     * Genera un avatar con color basado en el rol del usuario
     */
    public static byte[] generateAvatarForRole(String initials, String rol) {
        Color color;
        
        switch (rol.toUpperCase()) {
            case "ADMIN":
                color = new Color(63, 81, 181); // Azul oscuro
                break;
            case "CLIENTE":
                color = new Color(76, 175, 80); // Verde
                break;
            default:
                color = new Color(158, 158, 158); // Gris
                break;
        }
        
        return generateAvatar(initials, color);
    }
}

