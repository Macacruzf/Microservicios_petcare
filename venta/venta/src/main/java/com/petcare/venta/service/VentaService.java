package com.petcare.venta.service;

import com.petcare.venta.dto.VentaRequest;
import com.petcare.venta.model.DetalleVenta;
import com.petcare.venta.model.Venta;
import com.petcare.venta.repository.DetalleVentaRepository;
import com.petcare.venta.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;

    public Venta crearVenta(VentaRequest request) {

        Venta venta = new Venta();
        venta.setFecha(LocalDateTime.now());
        venta.setCliente(request.getCliente());
        venta.setMetodoPago(request.getMetodoPago());
        venta.setTotal(request.getTotal());

        venta = ventaRepository.save(venta);

        if (request.getItems() != null) {
            for (VentaRequest.DetalleVentaRequest item : request.getItems()) {
                DetalleVenta detalle = new DetalleVenta();
                detalle.setVenta(venta);
                detalle.setProductoId(item.getProductoId());
                detalle.setNombre(item.getNombre());
                detalle.setCantidad(item.getCantidad());
                detalle.setSubtotal(item.getSubtotal());
                detalleVentaRepository.save(detalle);
            }
        }

        return venta;
    }

    // ⭐ ESTE es el método que tu controlador usa
    public List<Venta> listarTodas() {
        return ventaRepository.findAll();
    }

    public Venta obtenerPorId(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con id: " + id));
    }
}
