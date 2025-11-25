package com.petcare.venta.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petcare.venta.model.Carrito;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Optional<Carrito> findByUsuarioId(Long usuarioId);
}
