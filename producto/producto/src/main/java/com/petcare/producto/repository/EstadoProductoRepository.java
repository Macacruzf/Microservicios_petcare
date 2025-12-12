package com.petcare.producto.repository;

import com.petcare.producto.model.EstadoProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadoProductoRepository extends JpaRepository<EstadoProductoEntity, Integer> {

    Optional<EstadoProductoEntity> findByNombreEstado(String nombreEstado);
}

