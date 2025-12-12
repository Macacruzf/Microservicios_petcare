package com.petcare.producto.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.petcare.producto.model.Categoria;
import com.petcare.producto.model.EstadoProductoEntity;
import com.petcare.producto.model.Producto;
import com.petcare.producto.repository.CategoriaRepository;
import com.petcare.producto.repository.EstadoProductoRepository;
import com.petcare.producto.repository.ProductoRepository;

import com.petcare.producto.dto.ProductoUpdateDto;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final EstadoProductoRepository estadoProductoRepository;

    public ProductoService(ProductoRepository productoRepository,
                           CategoriaRepository categoriaRepository,
                           EstadoProductoRepository estadoProductoRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.estadoProductoRepository = estadoProductoRepository;
    }

    // =============================================================
    // ✔ OBTENER PRODUCTOS (CON FILTROS)
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
    public List<Producto> getProductosPorEstado(String nombreEstado) {
        EstadoProductoEntity estado = estadoProductoRepository.findByNombreEstado(nombreEstado)
                .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado: " + nombreEstado));
        return productoRepository.findByEstado(estado);
    }

    // =============================================================
    // ✔ OBTENER POR ID (detalle completo)
    // =============================================================
    public Producto getProductoConDetalles(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
    }

    // =============================================================
    // ✔ OBTENER POR ID (Android / edición)
    // =============================================================
    public Producto getProductoById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con ID: " + id
                ));
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
    // ✔ ACTUALIZAR PRODUCTO DESDE DTO
    // =============================================================
    public Producto actualizarProducto(Long id, ProductoUpdateDto request) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        if (request.getNombre() != null) producto.setNombre(request.getNombre());
        if (request.getPrecio() != null) producto.setPrecio(request.getPrecio());
        if (request.getStock() != null) producto.setStock(request.getStock());
        if (request.getEstado() != null) {
            EstadoProductoEntity nuevoEstado = estadoProductoRepository.findByNombreEstado(request.getEstado().toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado: " + request.getEstado()));
            producto.setEstado(nuevoEstado);
        }

        if (request.getCategoria() != null && request.getCategoria().getIdCategoria() != null) {

            Categoria categoria = categoriaRepository.findById(request.getCategoria().getIdCategoria())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Categoría no encontrada con ID: " + request.getCategoria().getIdCategoria()
                    ));

            producto.setCategoria(categoria);
        }

        return productoRepository.save(producto);
    }

    // =============================================================
    // ✔ CAMBIAR SOLO ESTADO
    // =============================================================
    public void cambiarEstado(Long id, String nombreEstado) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        EstadoProductoEntity nuevoEstado = estadoProductoRepository.findByNombreEstado(nombreEstado.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado: " + nombreEstado));

        producto.setEstado(nuevoEstado);
        productoRepository.save(producto);
    }

    // =============================================================
    // ✔ ACTUALIZAR STOCK
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

    // =============================================================
    // ⭐ MÉTODOS QUE FALTABAN
    // =============================================================

    /** Obtener producto simple sin detalles */
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElse(null);
    }

    /** Guardar producto (crear o actualizar) */
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }
}
