package com.petcare.venta.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petcare.venta.model.Carrito;
import com.petcare.venta.service.CarritoService;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @PostMapping("/{usuarioId}/agregar/{productoId}/{cantidad}")
    public Carrito agregarProducto(
            @PathVariable Long usuarioId,
            @PathVariable Long productoId,
            @PathVariable Integer cantidad) {

        return carritoService.agregarProducto(usuarioId, productoId, cantidad);
    }

    @DeleteMapping("/{usuarioId}/limpiar")
    public void limpiarCarrito(@PathVariable Long usuarioId) {
        carritoService.limpiarCarrito(usuarioId);
    }

    @GetMapping("/{usuarioId}")
    public Carrito obtenerCarrito(@PathVariable Long usuarioId) {
        return carritoService.obtenerOCrearCarrito(usuarioId);
    }
}
