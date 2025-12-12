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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Productos", description = "API para gestión de inventario, catálogo y categorías")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // ============================================================
    // ✔ PRODUCTOS PARA ANDROID
    // ============================================================
    @GetMapping("/movil")
    @Operation(
        summary = "Listar productos (Versión Móvil)",
        description = "Retorna una lista optimizada de productos DTO para la app Android."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida con éxito",
            content = @Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = ProductoUpdateDto.class)))),
        @ApiResponse(responseCode = "204", description = "No hay productos disponibles"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
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
                dto.setEstado(p.getEstadoNombre());

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
    @Operation(summary = "Listar todos los productos")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Producto.class)))),
        @ApiResponse(responseCode = "204", description = "No existen productos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
    @Operation(summary = "Obtener detalle de producto")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto encontrado",
            content = @Content(schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
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
    // ✔ OBTENER IMAGEN
    // ============================================================
    @GetMapping("/{id}/imagen")
    @Operation(summary = "Obtener imagen del producto")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Imagen encontrada",
            content = @Content(mediaType = "image/png")),
        @ApiResponse(responseCode = "404", description = "Producto o imagen no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<byte[]> obtenerImagenProducto(@PathVariable Long id) {
        try {
            Producto producto = productoService.getProductoById(id);

            if (producto.getImagen() == null || producto.getImagen().length == 0)
                return ResponseEntity.status(404).build();

            return ResponseEntity.ok()
                    .header("Content-Type", "image/jpeg")
                    .body(producto.getImagen());

        } catch (ProductoService.ResourceNotFoundException e) {
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // ============================================================
    // ✔ CREAR PRODUCTO
    // ============================================================
    @PostMapping
    @Operation(
        summary = "Crear nuevo producto",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del nuevo producto",
            required = true,
            content = @Content(schema = @Schema(implementation = Producto.class),
                examples = @ExampleObject(
                    name = "Ejemplo de creación",
                    value = """
                    {
                      "nombre": "Dog Chow Adulto 15kg",
                      "precio": 23990,
                      "stock": 20,
                      "categoria": { "idCategoria": 1 }
                    }
                    """
                )
            )
        )
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Producto creado",
            content = @Content(schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> agregarProducto(@RequestBody Producto producto) {
        try {
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
    @Operation(summary = "Actualizar producto")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto actualizado",
            content = @Content(schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> actualizarProducto(
            @PathVariable Long id,
            @RequestBody ProductoUpdateDto dto) {

        try {
            return ResponseEntity.ok(productoService.actualizarProducto(id, dto));

        } catch (ProductoService.ResourceNotFoundException e) {
            return errorResponse(404, e.getMessage());
        } catch (Exception e) {
            return errorResponse(500, e.getMessage());
        }
    }

    // ============================================================
    // ✔ CAMBIAR ESTADO
    // ============================================================
    @PutMapping("/{id}/estado")
    @Operation(
        summary = "Cambiar estado del producto",
        description = "Actualiza el estado (DISPONIBLE, NO_DISPONIBLE, SIN_STOCK)."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado actualizado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
    @Operation(summary = "Eliminar producto")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.ok(Map.of("message", "Producto eliminado"));

        } catch (ProductoService.ResourceNotFoundException e) {
            return errorResponse(404, e.getMessage());
        } catch (Exception e) {
            return errorResponse(500, e.getMessage());
        }
    }

    // ============================================================
    // ✔ DESCONTAR STOCK
    // ============================================================
    @PutMapping("/{id}/descontar")
    @Operation(
        summary = "Descontar stock",
        description = "Descuenta una cantidad específica del stock si hay disponibilidad suficiente."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stock actualizado"),
        @ApiResponse(responseCode = "400", description = "Stock insuficiente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> descontarStock(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> body
    ) {
        try {
            int cantidad = body.get("cantidad");
            Producto producto = productoService.getProductoById(id);

            if (producto.getStock() < cantidad)
                return errorResponse(400, "Stock insuficiente");

            producto.setStock(producto.getStock() - cantidad);
            productoService.guardar(producto);

            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Stock descontado",
                "stockRestante", producto.getStock()
            ));

        } catch (ProductoService.ResourceNotFoundException e) {
            return errorResponse(404, e.getMessage());
        } catch (Exception e) {
            return errorResponse(500, e.getMessage());
        }
    }

    // ============================================================
    // ✔ CRUD CATEGORÍAS
    // ============================================================
    @GetMapping("/categorias")
    @Operation(summary = "Listar categorías")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Categoria.class)))),
        @ApiResponse(responseCode = "204", description = "No existen categorías"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
    @Operation(
        summary = "Crear nueva categoría",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de la nueva categoría",
            required = true,
            content = @Content(schema = @Schema(implementation = CategoriaRequest.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "nombre": "Alimentos"
                    }
                    """
                )
            )
        )
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Categoría creada",
            content = @Content(schema = @Schema(implementation = Categoria.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categoría actualizada",
            content = @Content(schema = @Schema(implementation = Categoria.class))),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> actualizarCategoria(
            @PathVariable Long id,
            @RequestBody CategoriaRequest request) {

        try {
            Categoria datos = new Categoria();
            datos.setNombre(request.getNombre());

            return ResponseEntity.ok(productoService.actualizarCategoria(id, datos));

        } catch (ProductoService.ResourceNotFoundException e) {
            return errorResponse(404, e.getMessage());
        } catch (Exception e) {
            return errorResponse(500, e.getMessage());
        }
    }

    @DeleteMapping("/categorias/{id}")
    @Operation(summary = "Eliminar categoría")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categoría eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> eliminarCategoria(@PathVariable Long id) {
        try {
            productoService.eliminarCategoria(id);
            return ResponseEntity.ok(Map.of("message", "Categoría eliminada"));

        } catch (ProductoService.ResourceNotFoundException e) {
            return errorResponse(404, e.getMessage());
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
