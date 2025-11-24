package com.petcare.venta.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.petcare.venta.model.Carrito;
import com.petcare.venta.model.CarritoDetalle;
import com.petcare.venta.repository.CarritoDetalleRepository;
import com.petcare.venta.repository.CarritoRepository;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final CarritoDetalleRepository detalleRepository;

    public CarritoService(CarritoRepository carritoRepository, CarritoDetalleRepository detalleRepository) {
        this.carritoRepository = carritoRepository;
        this.detalleRepository = detalleRepository;
    }

    public Carrito obtenerOCrearCarrito(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> {
                    Carrito nuevo = new Carrito();
                    nuevo.setUsuarioId(usuarioId);
                    return carritoRepository.save(nuevo);
                });
    }

    public Carrito agregarProducto(Long usuarioId, Long productoId, Integer cantidad) {
        Carrito carrito = obtenerOCrearCarrito(usuarioId);

        CarritoDetalle detalle = new CarritoDetalle();
        detalle.setProductoId(productoId);
        detalle.setCantidad(cantidad);
        detalle.setCarrito(carrito);

        detalleRepository.save(detalle);
        return carritoRepository.findByUsuarioId(usuarioId).get();
    }

    public void limpiarCarrito(Long usuarioId) {
        Optional<Carrito> carrito = carritoRepository.findByUsuarioId(usuarioId);
        carrito.ifPresent(carritoRepository::delete);
    }
}
