package com.petcare.venta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petcare.venta.model.CarritoDetalle;

public interface CarritoDetalleRepository extends JpaRepository<CarritoDetalle, Long> {
}
