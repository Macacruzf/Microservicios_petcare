package com.petcare.venta.repository;

import com.petcare.venta.model.EstadoVentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadoVentaRepository extends JpaRepository<EstadoVentaEntity, Integer> {

    Optional<EstadoVentaEntity> findByNombreEstado(String nombreEstado);
}

