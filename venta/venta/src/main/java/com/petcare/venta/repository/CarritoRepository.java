package com.petcare.venta.repository;

import com.petcare.venta.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    // Buscar carrito del usuario
    Optional<Carrito> findByUsuarioId(Long usuarioId);
}
