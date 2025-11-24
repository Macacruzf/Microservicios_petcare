package com.petcare.producto.controller;

import java.security.Principal;
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
import com.petcare.producto.dto.EstadoRequest;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // ============================================================
    // ✔ PRODUCTOS — ANDROID SIN HATEOAS
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
    // ✔ PRODUCTOS — GET GENERAL
    // ============================================================
    @GetMapping
    @Operation(summary = "Obtiene todos los productos")
    public ResponseEntity<?> obtenerProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Long categoriaId) {

        try {
            List<Producto> productos = productoService.getProductos(nombre, categoriaId);
            return productos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(productos);

        } catch (Exception e) {
            return errorResponse(500, "Error interno: " + e.getMessage());
        }
    }

    // ============================================================
    // ✔ PRODUCTO POR ID
    // ============================================================
    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un producto por su ID")
    public ResponseEntity<?> obtenerProductoConDetalles(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(productoService.getProductoConDetalles(id));

        } catch (ProductoService.ResourceNotFoundException e) {
            return errorResponse(404, e.getMessage());
        } catch (Exception e) {
            return errorResponse(500, "Error inesperado: " + e.getMessage());
        }
    }

    // ============================================================
    // ✔ CREAR PRODUCTO
    // ============================================================
    @PostMapping
    public ResponseEntity<?> agregarProducto(@RequestBody Producto producto, Principal principal) {

        if (!esAdmin(principal)) return accesoDenegado();

        try {
            Producto nuevo = productoService.agregarProducto(producto);
            return ResponseEntity.status(201).body(nuevo);

        } catch (Exception e) {
            return errorResponse(500, e.getMessage());
        }
    }

    // ============================================================
    // ✔ ACTUALIZAR PRODUCTO
    // ============================================================
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(
            @PathVariable Long id,
            @RequestBody Producto producto,
            Principal principal) {

        if (!esAdmin(principal)) return accesoDenegado();

        try {
            return ResponseEntity.ok(productoService.actualizarProducto(id, producto));

        } catch (Exception e) {
            return errorResponse(500, e.getMessage());
        }
    }

    // ============================================================
    // ✔ CAMBIAR SOLO EL ESTADO
    // ============================================================
    @PutMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado del producto")
    public ResponseEntity<?> cambiarEstadoProducto(
            @PathVariable Long id,
            @RequestBody EstadoRequest request,
            Principal principal
    ) {
        if (!esAdmin(principal)) return accesoDenegado();

        try {
            EstadoProducto estado = request.getEstadoAsEnum();
            productoService.cambiarEstado(id, estado);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Estado actualizado"
            ));

        } catch (IllegalArgumentException e) {
            return errorResponse(400, e.getMessage());
        } catch (ProductoService.ResourceNotFoundException e) {
            return errorResponse(404, e.getMessage());
        } catch (Exception e) {
            return errorResponse(500, "Error interno: " + e.getMessage());
        }
    }

    // ============================================================
    // ✔ ELIMINAR PRODUCTO
    // ============================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id, Principal principal) {

        if (!esAdmin(principal)) return accesoDenegado();

        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return errorResponse(500, e.getMessage());
        }
    }

    // ============================================================
    // ✔ CATEGORÍAS
    // ============================================================
    @GetMapping("/categorias")
    public ResponseEntity<?> obtenerCategorias() {
        try {
            List<Categoria> categorias = productoService.getCategorias();
            return categorias.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(categorias);

        } catch (Exception e) {
            return errorResponse(500, e.getMessage());
        }
    }

    @PostMapping("/categorias")
    public ResponseEntity<?> agregarCategoria(@RequestBody Categoria categoria, Principal principal) {

        if (!esAdmin(principal)) return accesoDenegado();

        try {
            return ResponseEntity.status(201).body(productoService.agregarCategoria(categoria));

        } catch (Exception e) {
            return errorResponse(500, e.getMessage());
        }
    }

    @PutMapping("/categorias/{id}")
    public ResponseEntity<?> actualizarCategoria(
            @PathVariable Long id,
            @RequestBody Categoria categoria,
            Principal principal) {

        if (!esAdmin(principal)) return accesoDenegado();

        try {
            return ResponseEntity.ok(productoService.actualizarCategoria(id, categoria));

        } catch (Exception e) {
            return errorResponse(500, e.getMessage());
        }
    }

    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Long id, Principal principal) {

        if (!esAdmin(principal)) return accesoDenegado();

        try {
            productoService.eliminarCategoria(id);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return errorResponse(500, e.getMessage());
        }
    }

    // ============================================================
    // ⭐ ANDROID: CONTROLLER INTERNO
    // ============================================================
    @RestController
    @RequestMapping("/categoria")
    public class CategoriaAndroidController {

        @Autowired
        private ProductoService productoService;

        @GetMapping
        public ResponseEntity<?> getCategorias() {
            try {
                List<Categoria> categorias = productoService.getCategorias();
                return categorias.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(categorias);

            } catch (Exception e) {
                return errorResponse(500, "Error al obtener categorías: " + e.getMessage());
            }
        }

        @PostMapping
        public ResponseEntity<?> agregarCategoria(@RequestBody Categoria categoria) {
            try {
                return ResponseEntity.status(201).body(productoService.agregarCategoria(categoria));

            } catch (Exception e) {
                return errorResponse(500, e.getMessage());
            }
        }

        @PutMapping("/{id}")
        public ResponseEntity<?> editarCategoria(@PathVariable Long id, @RequestBody Categoria categoria) {
            try {
                return ResponseEntity.ok(productoService.actualizarCategoria(id, categoria));

            } catch (Exception e) {
                return errorResponse(500, e.getMessage());
            }
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<?> eliminarCategoriaAndroid(@PathVariable Long id) {
            try {
                productoService.eliminarCategoria(id);
                return ResponseEntity.ok().build();

            } catch (Exception e) {
                return errorResponse(500, e.getMessage());
            }
        }
    }

    // ============================================================
    // MÉTODOS AUXILIARES
    // ============================================================
    private boolean esAdmin(Principal principal) {
        return principal != null && principal.getName().equals("admin");
    }

    private ResponseEntity<Map<String, Object>> accesoDenegado() {
        return ResponseEntity.status(403).body(Map.of(
                "status", "error",
                "message", "Acceso denegado"
        ));
    }

    private ResponseEntity<Map<String, Object>> errorResponse(int status, String message) {
        return ResponseEntity.status(status).body(Map.of(
                "status", "error",
                "message", message
        ));
    }
}
