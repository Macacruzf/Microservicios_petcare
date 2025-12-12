package com.petcare.venta.repository;

import com.petcare.venta.model.DetalleCarrito;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DetalleCarritoRepository extends JpaRepository<DetalleCarrito, Long> {

    @Query("SELECT d FROM DetalleCarrito d WHERE d.carrito.idCarrito = :carritoId")
    List<DetalleCarrito> findByCarritoId(@Param("carritoId") Long carritoId);

}
