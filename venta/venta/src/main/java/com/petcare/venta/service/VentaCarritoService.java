package com.petcare.venta.service;

import com.petcare.venta.model.Carrito;
import com.petcare.venta.model.DetalleCarrito;
import com.petcare.venta.model.DetalleVenta;
import com.petcare.venta.model.EstadoVentaEntity;
import com.petcare.venta.model.Venta;
import com.petcare.venta.repository.CarritoRepository;
import com.petcare.venta.repository.DetalleCarritoRepository;
import com.petcare.venta.repository.DetalleVentaRepository;
import com.petcare.venta.repository.EstadoVentaRepository;
import com.petcare.venta.repository.VentaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VentaCarritoService {

    private final VentaRepository ventaRepo;
    private final DetalleVentaRepository detVentaRepo;
    private final CarritoRepository carritoRepo;
    private final DetalleCarritoRepository detCarritoRepo;
    private final EstadoVentaRepository estadoVentaRepo;


    // ===============================================================
    // 🛒 OBTENER / CREAR CARRITO
    // ===============================================================
    public Carrito obtenerOCrearCarrito(Long usuarioId) {
        return carritoRepo.findByUsuarioId(usuarioId)
                .orElseGet(() -> {
                    Carrito c = new Carrito();
                    c.setUsuarioId(usuarioId);
                    c.setTotal(0.0);
                    return carritoRepo.save(c);
                });
    }


    // ===============================================================
    // 🛒 AGREGAR PRODUCTO AL CARRITO
    // ===============================================================
    @Transactional
    public Carrito agregarProducto(Long usuarioId, Long productoId, Integer cantidad) {

        Carrito carrito = obtenerOCrearCarrito(usuarioId);

        double precioUnitario = 2990.0; // MOCK

        DetalleCarrito det = new DetalleCarrito();
        det.setCarrito(carrito);
        det.setProductoId(productoId);
        det.setCantidad(cantidad);
        det.setPrecio(precioUnitario);
        det.setSubtotal(precioUnitario * cantidad);

        detCarritoRepo.save(det);

        carrito.getDetalles().add(det);
        carrito.setTotal(
                carrito.getDetalles().stream().mapToDouble(DetalleCarrito::getSubtotal).sum()
        );

        return carritoRepo.save(carrito);
    }


    // ===============================================================
    // 🛒 LIMPIAR CARRITO
    // ===============================================================
    @Transactional
    public void limpiarCarrito(Long usuarioId) {

        Carrito carrito = obtenerOCrearCarrito(usuarioId);

        detCarritoRepo.deleteAll(carrito.getDetalles());
        carrito.getDetalles().clear();
        carrito.setTotal(0.0);

        carritoRepo.save(carrito);
    }


    // ===============================================================
    // 💳 CREAR VENTA — USANDO TOTAL CALCULADO DEL FRONTEND
    // ===============================================================
    @Transactional
    public Venta crearVentaDesdeCarrito(Long usuarioId, String metodoPago, Double totalCalculado) {

        Carrito carrito = obtenerOCrearCarrito(usuarioId);

        if (carrito.getDetalles().isEmpty()) {
            throw new RuntimeException("El carrito está vacío.");
        }

        // Obtener estado CONFIRMADA
        EstadoVentaEntity estadoConfirmada = estadoVentaRepo.findByNombreEstado("CONFIRMADA")
            .orElseThrow(() -> new RuntimeException("Estado CONFIRMADA no encontrado en BD"));

        // Crear venta
        Venta venta = new Venta();
        venta.setUsuarioId(usuarioId);
        venta.setMetodoPago(metodoPago);
        venta.setEstado(estadoConfirmada);  // ← Asignar estado

        // ⚡ TOTAL QUE VIENE DESDE ANDROID
        venta.setTotal(totalCalculado);

        ventaRepo.save(venta);

        // Crear detalle de venta
        for (DetalleCarrito dc : carrito.getDetalles()) {
            DetalleVenta dv = new DetalleVenta();
            dv.setVenta(venta);
            dv.setProductoId(dc.getProductoId());
            dv.setCantidad(dc.getCantidad());
            dv.setPrecioUnitario(dc.getPrecio());
            dv.setSubtotal(dc.getSubtotal());
            detVentaRepo.save(dv);
        }

        // Limpiar carrito
        detCarritoRepo.deleteAll(carrito.getDetalles());
        carrito.getDetalles().clear();
        carrito.setTotal(0.0);
        carritoRepo.save(carrito);

        return venta;
    }


    // ===============================================================
    // 🛒 ELIMINAR ITEM
    // ===============================================================
    @Transactional
    public Carrito eliminarItem(Long usuarioId, Long idDetalleCarrito) {

        Carrito carrito = obtenerOCrearCarrito(usuarioId);

        DetalleCarrito item = detCarritoRepo.findById(idDetalleCarrito)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));

        if (!item.getCarrito().getIdCarrito().equals(carrito.getIdCarrito())) {
            throw new RuntimeException("El item no pertenece al carrito del usuario.");
        }

        detCarritoRepo.delete(item);

        double total = detCarritoRepo.findByCarritoId(carrito.getIdCarrito())
                .stream()
                .mapToDouble(DetalleCarrito::getSubtotal)
                .sum();

        carrito.setTotal(total);
        carritoRepo.save(carrito);

        return carrito;
    }


    // ===============================================================
    // 🛒 ACTUALIZAR CANTIDAD
    // ===============================================================
    @Transactional
    public Carrito actualizarCantidad(Long usuarioId, Long idDetalleCarrito, Integer cantidad) {

        Carrito carrito = obtenerOCrearCarrito(usuarioId);

        DetalleCarrito item = detCarritoRepo.findById(idDetalleCarrito)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));

        if (!item.getCarrito().getIdCarrito().equals(carrito.getIdCarrito())) {
            throw new RuntimeException("El item no pertenece al carrito del usuario.");
        }

        item.setCantidad(cantidad);
        item.setSubtotal(item.getPrecio() * cantidad);

        detCarritoRepo.save(item);

        double total = detCarritoRepo.findByCarritoId(carrito.getIdCarrito())
                .stream()
                .mapToDouble(DetalleCarrito::getSubtotal)
                .sum();

        carrito.setTotal(total);
        carritoRepo.save(carrito);

        return carrito;
    }


    // ===============================================================
    // 📄 CONSULTAS
    // ===============================================================
    public List<Venta> listarTodas() {
        return ventaRepo.findAll();
    }

    public Venta obtenerPorId(Long id) {
        return ventaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
    }

    public List<Venta> listarPorUsuario(Long usuarioId) {
        return ventaRepo.findByUsuarioId(usuarioId);
    }
}
