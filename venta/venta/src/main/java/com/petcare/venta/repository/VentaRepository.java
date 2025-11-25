package com.petcare.venta.repository;

import com.petcare.venta.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    // Listar ventas de un usuario específico
    List<Venta> findByUsuarioId(Long usuarioId);
}
