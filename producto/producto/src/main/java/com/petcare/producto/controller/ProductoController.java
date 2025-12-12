package com.petcare.producto.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.petcare.producto.model.Producto;
import com.petcare.producto.model.Categoria;
import com.petcare.producto.service.ProductoService;

import com.petcare.producto.dto.ProductoUpdateDto;
import com.petcare.producto.dto.CategoriaSimpleDto;
import com.petcare.producto.dto.CategoriaRequest;
import com.petcare.producto.dto.EstadoRequest;

// Importaciones de Swagger (OpenAPI 3)
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Productos", description = "API para gestión de inventario, catálogo y categorías")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // ============================================================
    // ✔ PRODUCTOS PARA ANDROID (SIN HATEOAS)
    // ============================================================
    @GetMapping("/movil")
    @Operation(summary = "Listar productos (Versión Móvil)", description = "Retorna una lista optimizada de productos DTO para la app Android.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida con éxito", 
                     content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductoUpdateDto.class)))),
        @ApiResponse(responseCode = "204", description = "No hay productos disponibles")
    })
    public ResponseEntity<?> obtenerProductosMovil(
            @Parameter(description = "Filtrar por nombre") @RequestParam(required = false) String nombre,
            @Parameter(description = "Filtrar por ID de categoría") @RequestParam(required = false) Long categoriaId
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
                // Usar el método getter que devuelve String
                dto.setEstado(p.getEstadoNombre());
                // Agregar URL de imagen
                if (p.getImagen() != null && p.getImagen().length > 0) {
                    dto.setImagenUrl("/api/v1/productos/" + p.getIdProducto() + "/imagen");
                }

                if (p.getCategoria() != null) {
                    CategoriaSimpleDto cat = new CategoriaSimpleDto();
                    cat.setIdCategoria(p.getCategoria().getIdCategoria());
                    cat.setNombre(p.getCategoria().getNombre());
                    dto.setCategoria(cat);
                }
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
    @Operation(summary = "Listar todos los productos", description = "Obtiene la lista completa de entidades Producto.")
    @ApiResponse(responseCode = "200", description = "Éxito", 
                 content = @Content(array = @ArraySchema(schema = @Schema(implementation = Producto.class))))
    public ResponseEntity<?> obtenerProductos(
            @Parameter(description = "Nombre del producto") @RequestParam(required = false) String nombre,
            @Parameter(description = "ID de la categoría") @RequestParam(required = false) Long categoriaId) {

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
    @Operation(summary = "Obtener detalle de producto", description = "Busca un producto completo por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado", 
                     content = @Content(schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
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
    // ✔ OBTENER IMAGEN DEL PRODUCTO
    // ============================================================
    @GetMapping("/{id}/imagen")
    @Operation(summary = "Obtener imagen del producto", description = "Retorna la imagen almacenada en la BD como byte array.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Imagen encontrada"),
        @ApiResponse(responseCode = "404", description = "Producto o imagen no encontrada")
    })
    public ResponseEntity<byte[]> obtenerImagenProducto(@PathVariable Long id) {
        try {
            Producto producto = productoService.getProductoById(id);
            
            if (producto.getImagen() == null || producto.getImagen().length == 0) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .header("Content-Type", "image/png")
                    .header("Content-Type", "image/jpeg")
                    .body(producto.getImagen());

        } catch (ProductoService.ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // ============================================================
    // ✔ CREAR PRODUCTO
    // ============================================================
    @PostMapping
    @Operation(summary = "Crear nuevo producto", description = "Agrega un producto al inventario.")
    @ApiResponse(responseCode = "201", description = "Producto creado", content = @Content(schema = @Schema(implementation = Producto.class)))
    public ResponseEntity<?> agregarProducto(@RequestBody Producto producto) {
        try {
            // El estado ya viene seteado desde el frontend o será el default (FK a id_estado=1)
            Producto nuevo = productoService.agregarProducto(producto);
            return ResponseEntity.status(201).body(nuevo);
        } catch (Exception e) {
            return errorResponse(500, e.getMessage());
        }
    }

    // ============================================================
    // ✔ EDITAR PRODUCTO
    // ============================================================
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto", description = "Actualiza los datos básicos de un producto existente.")
    @ApiResponse(responseCode = "200", description = "Producto actualizado", content = @Content(schema = @Schema(implementation = Producto.class)))
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
    // ✔ CAMBIAR ESTADO
    // ============================================================
    @PutMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado del producto", description = "Actualiza el estado (DISPONIBLE, AGOTADO, ETC).")
    @ApiResponse(responseCode = "200", description = "Estado modificado correctamente")
    public ResponseEntity<?> cambiarEstadoProducto(
            @PathVariable Long id,
            @RequestBody EstadoRequest request
    ) {

        try {
            productoService.cambiarEstado(id, request.getEstado());

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
    // ✔ ELIMINAR PRODUCTO
    // ============================================================
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto del sistema por su ID.")
    @ApiResponse(responseCode = "200", description = "Producto eliminado")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return errorResponse(500, e.getMessage());
        }
    }
    // ============================================================
    // ✔ DESCONTAR STOCK (ANDROID - FINALIZAR COMPRA)
    // ============================================================
    @PutMapping("/{id}/descontar")
    @Operation(
        summary = "Descontar stock",
        description = "Descuenta una cantidad específica del stock si hay suficiente stock disponible."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Stock insuficiente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<?> descontarStock(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> body
    ) {
        try {
            int cantidad = body.get("cantidad");
            Producto producto = productoService.getProductoById(id);

            if (producto == null) {
                return errorResponse(404, "Producto no encontrado");
            }

            if (producto.getStock() < cantidad) {
                return errorResponse(400, "Stock insuficiente");
            }

            producto.setStock(producto.getStock() - cantidad);
            productoService.guardar(producto);

            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Stock descontado",
                "stockRestante", producto.getStock()
            ));

        } catch (Exception e) {
            return errorResponse(500, "Error al descontar stock: " + e.getMessage());
        }
    }


    // ============================================================
    // ✔ CRUD CATEGORÍAS
    // ============================================================
    @GetMapping("/categorias")
    @Operation(summary = "Listar categorías", description = "Obtiene todas las categorías disponibles.")
    @ApiResponse(responseCode = "200", description = "Éxito", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Categoria.class))))
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
    @Operation(summary = "Crear categoría", description = "Agrega una nueva categoría.")
    @ApiResponse(responseCode = "201", description = "Categoría creada", content = @Content(schema = @Schema(implementation = Categoria.class)))
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
    @Operation(summary = "Actualizar categoría")
    @ApiResponse(responseCode = "200", description = "Categoría actualizada", content = @Content(schema = @Schema(implementation = Categoria.class)))
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
    @Operation(summary = "Eliminar categoría")
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