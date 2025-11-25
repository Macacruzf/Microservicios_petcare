package com.petcare.producto.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.petcare.producto.model.Categoria;
import com.petcare.producto.model.EstadoProducto;
import com.petcare.producto.model.Producto;
import com.petcare.producto.repository.CategoriaRepository;
import com.petcare.producto.repository.ProductoRepository;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(CategoriaRepository categoriaRepo, ProductoRepository productoRepo) {
        return args -> {

            System.out.println("=== Cargando datos iniciales para tienda de mascotas ===");

            // Evitar duplicados
            if (categoriaRepo.count() > 0 || productoRepo.count() > 0) {
                System.out.println("Datos encontrados. No se cargan datos iniciales.");
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
            // PRODUCTOS (sin imágenes)
            // -----------------------------
            List<Producto> productos = new ArrayList<>();

            // ALIMENTOS
            productos.add(new Producto(null, "Alimento Perro DogChow 3kg", 15990.0, 25, EstadoProducto.DISPONIBLE, alimentos));
            productos.add(new Producto(null, "Alimento Gato Whiskas 2,7kg", 13990.0, 30, EstadoProducto.DISPONIBLE, alimentos));
            productos.add(new Producto(null, "Snack Dental Pedigree 7un", 4990.0, 40, EstadoProducto.DISPONIBLE, alimentos));

            // ACCESORIOS
            productos.add(new Producto(null, "Correa Retráctil Azul", 8990.0, 15, EstadoProducto.DISPONIBLE, accesorios));
            productos.add(new Producto(null, "Collar Rojo Ajustable", 4990.0, 25, EstadoProducto.DISPONIBLE, accesorios));
            productos.add(new Producto(null, "Plato Doble Inoxidable", 6990.0, 20, EstadoProducto.DISPONIBLE, accesorios));

            // HIGIENE
            productos.add(new Producto(null, "Shampoo PelitoSuave Gatos", 7990.0, 20, EstadoProducto.DISPONIBLE, higiene));
            productos.add(new Producto(null, "Toallitas Húmedas PetClean 50u", 5990.0, 30, EstadoProducto.DISPONIBLE, higiene));
            productos.add(new Producto(null, "Cortaúñas Acero Inoxidable", 4990.0, 18, EstadoProducto.DISPONIBLE, higiene));

            // SALUD
            productos.add(new Producto(null, "Vitaminas Vita C", 12990.0, 25, EstadoProducto.DISPONIBLE, salud));
            productos.add(new Producto(null, "Antipulgas NexGard 10-25kg", 15990.0, 20, EstadoProducto.DISPONIBLE, salud));
            productos.add(new Producto(null, "Collar Antipulgas Bayer", 8990.0, 30, EstadoProducto.DISPONIBLE, salud));

            // JUGUETES
            productos.add(new Producto(null, "Pelota Masticable Goma", 3990.0, 40, EstadoProducto.DISPONIBLE, juguetes));
            productos.add(new Producto(null, "Ratón de Tela para Gatos", 2990.0, 35, EstadoProducto.DISPONIBLE, juguetes));
            productos.add(new Producto(null, "Cuerda Mordedora Grande", 5990.0, 25, EstadoProducto.DISPONIBLE, juguetes));

            // Guardar sin warnings
            productoRepo.saveAll(productos);

            System.out.println("Productos iniciales cargados correctamente.");
        };
    }
}
