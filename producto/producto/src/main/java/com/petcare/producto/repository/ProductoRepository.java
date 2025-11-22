package com.petcare.producto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petcare.producto.model.EstadoProducto;
import com.petcare.producto.model.Producto;


@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Buscar productos que contengan un nombre (ignorando mayúsculas/minúsculas)
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // Buscar productos por ID de categoría
    List<Producto> findByCategoria_IdCategoria(Long idCategoria);

    // Buscar productos por nombre y categoría (ambos filtros aplicados)
    List<Producto> findByNombreContainingIgnoreCaseAndCategoria_IdCategoria(String nombre, Long idCategoria);

    // Buscar productos cuyo ID esté en una lista (útil para pedidos o filtros múltiples)
    List<Producto> findByIdProductoIn(List<Long> idProductos);

    // Verificar si existen productos asociados a una categoría específica
    boolean existsByCategoria_IdCategoria(Long idCategoria);

    // Buscar productos por estado (ej. ACTIVO, AGOTADO, etc.)
    List<Producto> findByEstado(EstadoProducto estado);
}