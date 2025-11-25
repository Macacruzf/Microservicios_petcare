package com.petcare.venta.repository;

import com.petcare.venta.model.DetalleCarrito;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleCarritoRepository extends JpaRepository<DetalleCarrito, Long> {
    List<DetalleCarrito> findByCarritoId(Long carritoId);

}
