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
@Tag(name = "Ventas & Carrito", description = "Endpoints para ventas y gestión del carrito.")
public class VentaCarritoController {

    private final VentaCarritoService service;

    // ========================================================================
    // 🛒 CARRITO
    // ========================================================================

    @Operation(summary = "Obtener carrito del usuario")
    @ApiResponse(responseCode = "200", description = "Carrito obtenido o creado.")
    @GetMapping("/carrito/{usuarioId}")
    public Carrito obtenerCarrito(@PathVariable Long usuarioId) {
        return service.obtenerOCrearCarrito(usuarioId);
    }

    @Operation(summary = "Agregar producto al carrito")
    @ApiResponse(responseCode = "200", description = "Producto añadido correctamente.")
    @PostMapping("/carrito/{usuarioId}/agregar/{productoId}/{cantidad}")
    public Carrito agregarProducto(
            @PathVariable Long usuarioId,
            @PathVariable Long productoId,
            @PathVariable Integer cantidad
    ) {
        return service.agregarProducto(usuarioId, productoId, cantidad);
    }

    @Operation(summary = "Vaciar carrito")
    @ApiResponse(responseCode = "204", description = "Carrito vaciado.")
    @DeleteMapping("/carrito/{usuarioId}/limpiar")
    public ResponseEntity<Void> limpiarCarrito(@PathVariable Long usuarioId) {
        service.limpiarCarrito(usuarioId);
        return ResponseEntity.noContent().build();
    }

    // ========================================================================
    // 🛒 NUEVO: Eliminar item del carrito
    // ========================================================================
    @Operation(
            summary = "Eliminar producto del carrito",
            description = "Elimina un producto específico del carrito del usuario."
    )
    @ApiResponse(responseCode = "200", description = "Producto eliminado correctamente.")
    @DeleteMapping("/carrito/{usuarioId}/item/{idDetalleCarrito}")
    public ResponseEntity<Carrito> eliminarItem(
            @PathVariable Long usuarioId,
            @PathVariable Long idDetalleCarrito
    ) {
        return ResponseEntity.ok(
                service.eliminarItem(usuarioId, idDetalleCarrito)
        );
    }

    // ========================================================================
    // 🛒 NUEVO: Actualizar cantidad
    // ========================================================================
    @Operation(
            summary = "Actualizar cantidad de un producto en el carrito",
            description = "Modifica la cantidad de un producto dentro del carrito."
    )
    @ApiResponse(responseCode = "200", description = "Cantidad actualizada correctamente.")
    @PutMapping("/carrito/{usuarioId}/item/{idDetalleCarrito}/{cantidad}")
    public ResponseEntity<Carrito> actualizarCantidad(
            @PathVariable Long usuarioId,
            @PathVariable Long idDetalleCarrito,
            @PathVariable Integer cantidad
    ) {
        return ResponseEntity.ok(
                service.actualizarCantidad(usuarioId, idDetalleCarrito, cantidad)
        );
    }

    // ========================================================================
    // 💳 VENTAS
    // ========================================================================

    @Operation(
            summary = "Crear venta desde carrito",
            description = "Convierte el carrito en una venta usando el total calculado desde la app."
    )
    @ApiResponse(responseCode = "200", description = "Venta creada exitosamente.")
    @PostMapping("/crear-desde-carrito/{usuarioId}")
    public ResponseEntity<Venta> crearVentaDesdeCarrito(
            @PathVariable Long usuarioId,
            @RequestParam String metodoPago,
            @RequestParam Double totalCalculado
    ) {
        return ResponseEntity.ok(
                service.crearVentaDesdeCarrito(usuarioId, metodoPago, totalCalculado)
        );
    }

    @Operation(summary = "Listar todas las ventas")
    @GetMapping("/listar")
    public List<Venta> listarTodas() {
        return service.listarTodas();
    }

    @Operation(summary = "Obtener una venta por ID")
    @GetMapping("/{id}")
    public Venta obtenerVenta(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @Operation(summary = "Listar ventas por usuario")
    @GetMapping("/usuario/{usuarioId}")
    public List<Venta> listarPorUsuario(@PathVariable Long usuarioId) {
        return service.listarPorUsuario(usuarioId);
    }

}
