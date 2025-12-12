package com.petcare.venta.controller;

import com.petcare.venta.model.Carrito;
import com.petcare.venta.model.Venta;
import com.petcare.venta.service.VentaCarritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/venta")
@CrossOrigin("*")
@RequiredArgsConstructor
@Tag(name = "Ventas & Carrito", description = "Endpoints para gestionar ventas y carrito de compras del usuario.")
public class VentaCarritoController {

    private final VentaCarritoService service;

    // ========================================================================
    // 🛒 CARRITO
    // ========================================================================

    @Operation(summary = "Obtener carrito del usuario", description = "Retorna el carrito del usuario, creándolo si no existe.")
    @ApiResponse(responseCode = "200", description = "Carrito obtenido o creado.")
    @GetMapping("/carrito/{usuarioId}")
    public Carrito obtenerCarrito(@PathVariable Long usuarioId) {
        return service.obtenerOCrearCarrito(usuarioId);
    }

    @Operation(summary = "Agregar producto al carrito", description = "Añade un producto específico al carrito del usuario.")
    @ApiResponse(responseCode = "200", description = "Producto añadido correctamente.")
    @PostMapping("/carrito/{usuarioId}/agregar/{productoId}/{cantidad}")
    public Carrito agregarProducto(
            @PathVariable Long usuarioId,
            @PathVariable Long productoId,
            @PathVariable Integer cantidad
    ) {
        return service.agregarProducto(usuarioId, productoId, cantidad);
    }

    @Operation(summary = "Vaciar carrito", description = "Elimina todos los productos del carrito del usuario.")
    @ApiResponse(responseCode = "204", description = "Carrito vaciado exitosamente.")
    @DeleteMapping("/carrito/{usuarioId}/limpiar")
    public ResponseEntity<Void> limpiarCarrito(@PathVariable Long usuarioId) {
        service.limpiarCarrito(usuarioId);
        return ResponseEntity.noContent().build();
    }

    // ========================================================================
    // 🛒 Eliminar item del carrito
    // ========================================================================
    @Operation(summary = "Eliminar producto del carrito", description = "Elimina un producto específico del carrito del usuario.")
    @ApiResponse(responseCode = "200", description = "Producto eliminado correctamente.")
    @DeleteMapping("/carrito/{usuarioId}/item/{idDetalleCarrito}")
    public ResponseEntity<Carrito> eliminarItem(
            @PathVariable Long usuarioId,
            @PathVariable Long idDetalleCarrito
    ) {
        return ResponseEntity.ok(service.eliminarItem(usuarioId, idDetalleCarrito));
    }

    // ========================================================================
    // 🛒 Actualizar cantidad de un producto
    // ========================================================================
    @Operation(summary = "Actualizar cantidad de un producto en el carrito", description = "Actualiza la cantidad de un producto en el carrito del usuario.")
    @ApiResponse(responseCode = "200", description = "Cantidad del producto actualizada correctamente.")
    @PutMapping("/carrito/{usuarioId}/item/{idDetalleCarrito}/{cantidad}")
    public ResponseEntity<Carrito> actualizarCantidad(
            @PathVariable Long usuarioId,
            @PathVariable Long idDetalleCarrito,
            @PathVariable Integer cantidad
    ) {
        return ResponseEntity.ok(service.actualizarCantidad(usuarioId, idDetalleCarrito, cantidad));
    }

    // ========================================================================
    // 💳 VENTAS
    // ========================================================================

    @Operation(summary = "Crear venta desde carrito", description = "Convierte el carrito actual en una venta confirmada por el usuario.")
    @ApiResponse(responseCode = "200", description = "Venta creada exitosamente.")
    @PostMapping("/crear-desde-carrito/{usuarioId}")
    public ResponseEntity<Venta> crearVentaDesdeCarrito(
            @PathVariable Long usuarioId,
            @RequestParam String metodoPago,
            @RequestParam Double totalCalculado
    ) {
        return ResponseEntity.ok(service.crearVentaDesdeCarrito(usuarioId, metodoPago, totalCalculado));
    }

    @Operation(summary = "Listar todas las ventas", description = "Recupera la lista de todas las ventas realizadas en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de ventas obtenida exitosamente.")
    @GetMapping("/listar")
    public List<Venta> listarTodas() {
        return service.listarTodas();
    }

    @Operation(summary = "Obtener una venta por ID", description = "Recupera los detalles de una venta específica por su ID.")
    @ApiResponse(responseCode = "200", description = "Venta encontrada.")
    @GetMapping("/{id}")
    public Venta obtenerVenta(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @Operation(summary = "Listar ventas por usuario", description = "Obtiene todas las ventas realizadas por un usuario específico.")
    @ApiResponse(responseCode = "200", description = "Ventas del usuario obtenidas.")
    @GetMapping("/usuario/{usuarioId}")
    public List<Venta> listarPorUsuario(@PathVariable Long usuarioId) {
        return service.listarPorUsuario(usuarioId);
    }
}
