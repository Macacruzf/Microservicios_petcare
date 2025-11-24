package com.petcare.producto.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.petcare.producto.model.Categoria;
import com.petcare.producto.model.EstadoProducto;
import com.petcare.producto.model.Producto;
import com.petcare.producto.repository.CategoriaRepository;
import com.petcare.producto.repository.ProductoRepository;
import com.petcare.producto.dto.ProductoUpdateDto;
// IMPORT AGREGADO PARA LA SOBRECARGA OPCIONAL
import com.petcare.producto.dto.ProductoUpdateDto;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository,
                           CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // =============================================================
    // ✔ OBTENER PRODUCTOS (CON FILTROS OPCIONALES)
    // =============================================================
    public List<Producto> getProductos(String nombre, Long categoriaId) {
        if (nombre != null && categoriaId != null) {
            return productoRepository
                    .findByNombreContainingIgnoreCaseAndCategoria_IdCategoria(nombre, categoriaId);
        } else if (nombre != null) {
            return productoRepository.findByNombreContainingIgnoreCase(nombre);
        } else if (categoriaId != null) {
            return productoRepository.findByCategoria_IdCategoria(categoriaId);
        } else {
            return productoRepository.findAll();
        }
    }

    // =============================================================
    // ✔ FILTRAR POR ESTADO
    // =============================================================
    public List<Producto> getProductosPorEstado(EstadoProducto estado) {
        return productoRepository.findByEstado(estado);
    }

    // =============================================================
    // ✔ OBTENER POR ID
    // =============================================================
    public Producto getProductoConDetalles(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
    }

    // =============================================================
    // ✔ CREAR PRODUCTO
    // =============================================================
    public Producto agregarProducto(Producto producto) {

        if (producto.getCategoria() == null || producto.getCategoria().getIdCategoria() == null) {
            throw new IllegalArgumentException("La categoría es obligatoria");
        }

        Categoria categoria = categoriaRepository.findById(producto.getCategoria().getIdCategoria())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

        producto.setCategoria(categoria);

        return productoRepository.save(producto);
    }

    // =============================================================
    // ✔ ACTUALIZAR PRODUCTO (ORIGINAL)
    // =============================================================
    public Producto actualizarProducto(Long id, Producto cambios) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        producto.setNombre(cambios.getNombre());
        producto.setPrecio(cambios.getPrecio());
        producto.setStock(cambios.getStock());
        producto.setEstado(cambios.getEstado());

        if (cambios.getCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(cambios.getCategoria().getIdCategoria())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
            producto.setCategoria(categoria);
        }

        return productoRepository.save(producto);
    }

    // =============================================================
    // ⭐ SOBRECARGA OPCIONAL PARA DTO (AGREGADO)
    // =============================================================
    // Sobrecarga opcional para DTO
    public Producto actualizarProducto(Long id, ProductoUpdateDto request) {
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        // Actualiza solo campos no nulos
        if (request.getNombre() != null) producto.setNombre(request.getNombre());
        if (request.getPrecio() != null) producto.setPrecio(request.getPrecio());
        if (request.getStock() != null) producto.setStock(request.getStock());
        if (request.getEstado() != null) producto.setEstado(request.getEstadoEnum());  // Convierte automáticamente

        // Nota: No actualiza categoría aquí, ya que ProductoUpdateRequest no la incluye
        return productoRepository.save(producto);
    }

    // =============================================================
    // ⭐ NUEVO: CAMBIAR SOLO EL ESTADO
    // =============================================================
    public void cambiarEstado(Long id, EstadoProducto nuevoEstado) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        producto.setEstado(nuevoEstado);

        productoRepository.save(producto);
    }

    // =============================================================
    // ✔ ACTUALIZAR SOLO STOCK
    // =============================================================
    public Producto actualizarStock(Long id, Integer cantidad) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        producto.setStock(cantidad);
        return productoRepository.save(producto);
    }

    // =============================================================
    // ✔ ACTUALIZAR STOCK EN LOTE
    // =============================================================
    public void actualizarStockBulk(List<Map<String, Object>> updates) {
        for (Map<String, Object> update : updates) {
            Long idProducto = Long.valueOf(update.get("idProducto").toString());
            Integer cantidad = Integer.valueOf(update.get("cantidad").toString());
            actualizarStock(idProducto, cantidad);
        }
    }

    // =============================================================
    // ✔ ELIMINAR PRODUCTO
    // =============================================================
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
    }

    // =============================================================
    // ✔ CRUD CATEGORÍAS
    // =============================================================
    public List<Categoria> getCategorias() {
        return categoriaRepository.findAll();
    }

    public Categoria agregarCategoria(Categoria categoria) {
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizarCategoria(Long id, Categoria datos) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));

        categoria.setNombre(datos.getNombre());
        return categoriaRepository.save(categoria);
    }

    public void eliminarCategoria(Long id) {
        categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));

        if (productoRepository.existsByCategoria_IdCategoria(id)) {
            throw new IllegalStateException("No se puede eliminar la categoría, tiene productos asociados");
        }

        categoriaRepository.deleteById(id);
    }

    // =============================================================
    // ✔ EXCEPCIÓN PERSONALIZADA
    // =============================================================
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }
}
