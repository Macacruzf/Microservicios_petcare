package com.petcare.producto.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.petcare.producto.model.Categoria;
import com.petcare.producto.model.EstadoProductoEntity;
import com.petcare.producto.model.Producto;
import com.petcare.producto.repository.CategoriaRepository;
import com.petcare.producto.repository.EstadoProductoRepository;
import com.petcare.producto.repository.ProductoRepository;

@Configuration
public class LoadDatabase {

    /**
     * Método helper para cargar imágenes desde la carpeta uploads/images
     */
    private byte[] cargarImagen(String nombreArchivo) {
        try {
            Path imagePath = Paths.get("uploads/images/" + nombreArchivo);
            if (Files.exists(imagePath)) {
                return Files.readAllBytes(imagePath);
            } else {
                System.out.println("⚠️ Imagen no encontrada: " + nombreArchivo);
                return null;
            }
        } catch (IOException e) {
            System.out.println("❌ Error al cargar imagen " + nombreArchivo + ": " + e.getMessage());
            return null;
        }
    }

    @Bean
    CommandLineRunner initDatabase(CategoriaRepository categoriaRepo, ProductoRepository productoRepo, EstadoProductoRepository estadoRepo) {
        return args -> {

            System.out.println("=== Cargando datos iniciales para tienda de mascotas ===");

            // PASO 1: Crear estados si no existen
            EstadoProductoEntity estadoDisponible = estadoRepo.findByNombreEstado("DISPONIBLE").orElse(null);
            if (estadoDisponible == null) {
                estadoDisponible = new EstadoProductoEntity();
                estadoDisponible.setNombreEstado("DISPONIBLE");
                estadoDisponible.setDescripcion("Producto disponible para venta");
                estadoDisponible.setVisibleCatalogo(true);
                estadoDisponible.setPermiteVenta(true);
                estadoDisponible.setColorHex("#4CAF50");
                estadoDisponible = estadoRepo.save(estadoDisponible);
                System.out.println("✅ Estado DISPONIBLE creado");
            }

            EstadoProductoEntity estadoNoDisponible = estadoRepo.findByNombreEstado("NO_DISPONIBLE").orElse(null);
            if (estadoNoDisponible == null) {
                estadoNoDisponible = new EstadoProductoEntity();
                estadoNoDisponible.setNombreEstado("NO_DISPONIBLE");
                estadoNoDisponible.setDescripcion("Producto no disponible temporalmente");
                estadoNoDisponible.setVisibleCatalogo(false);
                estadoNoDisponible.setPermiteVenta(false);
                estadoNoDisponible.setColorHex("#FF9800");
                estadoRepo.save(estadoNoDisponible);
                System.out.println("✅ Estado NO_DISPONIBLE creado");
            }

            EstadoProductoEntity estadoSinStock = estadoRepo.findByNombreEstado("SIN_STOCK").orElse(null);
            if (estadoSinStock == null) {
                estadoSinStock = new EstadoProductoEntity();
                estadoSinStock.setNombreEstado("SIN_STOCK");
                estadoSinStock.setDescripcion("Producto sin stock");
                estadoSinStock.setVisibleCatalogo(true);
                estadoSinStock.setPermiteVenta(false);
                estadoSinStock.setColorHex("#F44336");
                estadoRepo.save(estadoSinStock);
                System.out.println("✅ Estado SIN_STOCK creado");
            }

            // PASO 2: Crear categorías y productos si no existen
            // Evitar duplicados
            if (categoriaRepo.count() > 0 || productoRepo.count() > 0) {
                System.out.println("Datos de productos encontrados. No se cargan datos iniciales.");
                return;
            }

            // -----------------------------
            // CATEGORÍAS
            // -----------------------------
            Categoria alimentos  = new Categoria(null, "Alimentos");
            Categoria accesorios = new Categoria(null, "Accesorios");
            Categoria higiene    = new Categoria(null, "Higiene");
            Categoria salud      = new Categoria(null, "Salud" );
            Categoria juguetes   = new Categoria(null, "Juguetes");

            List<Categoria> categorias = new ArrayList<>();
                categorias.add(alimentos);
                categorias.add(accesorios);
                categorias.add(higiene);
                categorias.add(salud);
                categorias.add(juguetes);

                categoriaRepo.saveAll(categorias);

            // -----------------------------
            // PRODUCTOS CON IMÁGENES
            // -----------------------------
            List<Producto> productos = new ArrayList<>();

            // ALIMENTOS
            Producto p1 = new Producto(null, "Alimento Perro DogChow 3kg", 15990.0, 25, estadoDisponible, alimentos, cargarImagen("comida_perrodogchow.png"));
            Producto p2 = new Producto(null, "Alimento Gato Whiskas 2,7kg", 13990.0, 30, estadoDisponible, alimentos, cargarImagen("comidawhiskas_gato.png"));
            Producto p3 = new Producto(null, "Snack Dental Pedigree 7un", 4990.0, 40, estadoDisponible, alimentos, cargarImagen("snack_dentalpedigree.png"));

            // ACCESORIOS
            Producto p4 = new Producto(null, "Correa Retráctil Azul", 8990.0, 15, estadoDisponible, accesorios, cargarImagen("correa_retractilazul.png"));
            Producto p5 = new Producto(null, "Collar Rojo Ajustable", 4990.0, 25, estadoDisponible, accesorios, cargarImagen("collar_rojo.png"));
            Producto p6 = new Producto(null, "Plato Doble Inoxidable", 6990.0, 20, estadoDisponible, accesorios, cargarImagen("plato_doble.png"));

            // HIGIENE
            Producto p7 = new Producto(null, "Shampoo PelitoSuave Gatos", 7990.0, 20, estadoDisponible, higiene, cargarImagen("shampoo_gato.png"));
            Producto p8 = new Producto(null, "Toallitas Húmedas PetClean 50u", 5990.0, 30, estadoDisponible, higiene, cargarImagen("toallitas_petclean.png"));
            Producto p9 = new Producto(null, "Cortaúñas Acero Inoxidable", 4990.0, 18, estadoDisponible, higiene, cargarImagen("cortaunias.png"));

            // SALUD
            Producto p10 = new Producto(null, "Vitaminas Vita C", 12990.0, 25, estadoDisponible, salud, cargarImagen("vitaminas_vitac.png"));
            Producto p11 = new Producto(null, "Antipulgas NexGard 10-25kg", 15990.0, 20, estadoDisponible, salud, cargarImagen("antipulgasnexgard.jpg"));
            Producto p12 = new Producto(null, "Collar Antipulgas Bayer", 8990.0, 30, estadoDisponible, salud, cargarImagen("collar_antipulgas.jpg"));

            // JUGUETES
            Producto p13 = new Producto(null, "Pelota Masticable Goma", 3990.0, 40, estadoDisponible, juguetes, cargarImagen("juguete_goma.png"));
            Producto p14 = new Producto(null, "Ratón de Tela para Gatos", 2990.0, 35, estadoDisponible, juguetes, cargarImagen("raton_tela.png"));
            Producto p15 = new Producto(null, "Cuerda Mordedora Grande", 5990.0, 25, estadoDisponible, juguetes, cargarImagen("cuerda_mordedora.png"));

            productos.add(p1); productos.add(p2); productos.add(p3);
            productos.add(p4); productos.add(p5); productos.add(p6);
            productos.add(p7); productos.add(p8); productos.add(p9);
            productos.add(p10); productos.add(p11); productos.add(p12);
            productos.add(p13); productos.add(p14); productos.add(p15);

            // Guardar productos con imágenes
            productoRepo.saveAll(productos);

            System.out.println("✅ Productos iniciales cargados correctamente con imágenes desde BD.");
        };
    }
}
