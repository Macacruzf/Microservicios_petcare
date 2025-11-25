package com.petcare.producto.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.petcare.producto.model.Producto;
import com.petcare.producto.model.Categoria;
import com.petcare.producto.model.EstadoProducto;
import com.petcare.producto.service.ProductoService;

import com.petcare.producto.dto.ProductoUpdateDto;
import com.petcare.producto.dto.CategoriaSimpleDto;
import com.petcare.producto.dto.CategoriaRequest;
import com.petcare.producto.dto.EstadoRequest;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // ============================================================
    // ✔ PRODUCTOS PARA ANDROID (SIN HATEOAS)
    // ============================================================
    @GetMapping("/movil")
    @Operation(summary = "Versión liviana para Android sin HATEOAS")
    public ResponseEntity<?> obtenerProductosMovil(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Long categoriaId
    ) {
        try {
            List<Producto> productos = productoService.getProductos(nombre, categoriaId);

            if (productos.isEmpty()) return ResponseEntity.noContent().build();

            List<ProductoUpdateDto> response = productos.stream().map(p -> {
                ProductoUpdateDto dto = new ProductoUpdateDto();
                dto.setIdProducto(p.getIdProducto());
                dto.setNombre(p.getNombre());
                dto.setPrecio(p.getPrecio());
                dto.setStock(p.getStock());
                dto.setEstado(p.getEstado().name());

                CategoriaSimpleDto cat = new CategoriaSimpleDto();
                cat.setIdCategoria(p.getCategoria().getIdCategoria());
                cat.setNombre(p.getCategoria().getNombre());
                dto.setCategoria(cat);

                return dto;
            }).toList();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return errorResponse(500, "Error al obtener productos: " + e.getMessage());
        }
    }

    // ============================================================
    // ✔ LISTAR PRODUCTOS GENERAL
    // ============================================================
    @GetMapping
    public ResponseEntity<?> obtenerProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Long categoriaId) {

        try {
            List<Producto> productos = productoService.getProductos(nombre, categoriaId);
            return productos.isEmpty()
                    ? ResponseEntity.noContent().build()
                    : ResponseEntity.ok(productos);

        } catch (Exception e) {
            return errorResponse(500, "Error interno: " + e.getMessage());
        }
    }

    // ============================================================
    // ✔ OBTENER PRODUCTO POR ID
    // ============================================================
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerProductoConDetalles(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(productoService.getProductoById(id));

        } catch (ProductoService.ResourceNotFoundException e) {
            return errorResponse(404, e.getMessage());
        } catch (Exception e) {
            return errorResponse(500, "Error inesperado: " + e.getMessage());
        }
    }

    // ============================================================
    // ✔ CREAR PRODUCTO  (SIN ADMIN)
    // ============================================================
    @PostMapping
    public ResponseEntity<?> agregarProducto(@RequestBody Producto producto) {
        try {
            if (producto.getEstado() == null) {
                producto.setEstado(EstadoProducto.DISPONIBLE);
            }

            Producto nuevo = productoService.agregarProducto(producto);
            return ResponseEntity.status(201).body(nuevo);

        } catch (Exception e) {
            return errorResponse(500, e.getMessage());
        }
    }

    // ============================================================
    // ✔ EDITAR PRODUCTO  (SIN ADMIN)
    // ============================================================
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(
            @PathVariable Long id,
            @RequestBody ProductoUpdateDto dto) {

        try {
            Producto actualizado = productoService.actualizarProducto(id, dto);
            return ResponseEntity.ok(actualizado);

        } catch (Exception e) {
            return errorResponse(500, e.getMessage());
        }
    }

    // ============================================================
    // ✔ CAMBIAR ESTADO  (SIN ADMIN)
    // ============================================================
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstadoProducto(
            @PathVariable Long id,
            @RequestBody EstadoRequest request
    ) {

        try {
            EstadoProducto estado = request.getEstadoAsEnum();
            productoService.cambiarEstado(id, estado);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Estado actualizado"
            ));

        } catch (ProductoService.ResourceNotFoundException e) {
            return errorResponse(404, e.getMessage());
        } catch (Exception e) {
            return errorResponse(500, e.getMessage());
        }
    }

    // ============================================================
    // ✔ ELIMINAR PRODUCTO  (SIN ADMIN)
    // ============================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return errorResponse(500, e.getMessage());
        }
    }

    // ============================================================
    // ✔ CRUD CATEGORÍAS — ANDROID COMPATIBLE (SIN ADMIN)
    // ============================================================
    @GetMapping("/categorias")
    public ResponseEntity<?> obtenerCategorias() {
        try {
            List<Categoria> categorias = productoService.getCategorias();
            return categorias.isEmpty()
                    ? ResponseEntity.noContent().build()
                    : ResponseEntity.ok(categorias);

        } catch (Exception e) {
            return errorResponse(500, e.getMessage());
        }
    }

    @PostMapping("/categorias")
    public ResponseEntity<?> agregarCategoria(@RequestBody CategoriaRequest request) {
        try {
            Categoria nueva = new Categoria();
            nueva.setNombre(request.getNombre());

            return ResponseEntity.status(201).body(productoService.agregarCategoria(nueva));

        } catch (Exception e) {
            return errorResponse(500, e.getMessage());
        }
    }

    @PutMapping("/categorias/{id}")
    public ResponseEntity<?> actualizarCategoria(
            @PathVariable Long id,
            @RequestBody CategoriaRequest request) {

        try {
            Categoria datos = new Categoria();
            datos.setNombre(request.getNombre());

            return ResponseEntity.ok(productoService.actualizarCategoria(id, datos));

        } catch (Exception e) {
            return errorResponse(500, e.getMessage());
        }
    }

    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Long id) {
        try {
            productoService.eliminarCategoria(id);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return errorResponse(500, e.getMessage());
        }
    }

    // ============================================================
    // AUXILIAR
    // ============================================================
    private ResponseEntity<Map<String, Object>> errorResponse(int status, String message) {
        return ResponseEntity.status(status).body(Map.of(
                "status", "error",
                "message", message
        ));
    }
}
