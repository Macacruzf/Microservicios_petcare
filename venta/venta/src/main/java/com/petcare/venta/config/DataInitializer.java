package com.petcare.venta.config;

import com.petcare.venta.model.EstadoVentaEntity;
import com.petcare.venta.repository.EstadoVentaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initEstadosVenta(EstadoVentaRepository estadoRepo) {
        return args -> {

            System.out.println("=== Inicializando estados de venta ===");

            // ESTADO 1: PENDIENTE
            EstadoVentaEntity estadoPendiente = estadoRepo.findByNombreEstado("PENDIENTE").orElse(null);
            if (estadoPendiente == null) {
                estadoPendiente = new EstadoVentaEntity();
                estadoPendiente.setNombreEstado("PENDIENTE");
                estadoPendiente.setDescripcion("Venta pendiente de confirmación");
                estadoPendiente.setPermiteCancelacion(true);
                estadoPendiente.setEsFinal(false);
                estadoPendiente.setColorHex("#FFA500");
                estadoRepo.save(estadoPendiente);
                System.out.println("✅ Estado PENDIENTE creado");
            }

            // ESTADO 2: CONFIRMADA
            EstadoVentaEntity estadoConfirmada = estadoRepo.findByNombreEstado("CONFIRMADA").orElse(null);
            if (estadoConfirmada == null) {
                estadoConfirmada = new EstadoVentaEntity();
                estadoConfirmada.setNombreEstado("CONFIRMADA");
                estadoConfirmada.setDescripcion("Venta confirmada y en proceso");
                estadoConfirmada.setPermiteCancelacion(true);
                estadoConfirmada.setEsFinal(false);
                estadoConfirmada.setColorHex("#2196F3");
                estadoRepo.save(estadoConfirmada);
                System.out.println("✅ Estado CONFIRMADA creado");
            }

            // ESTADO 3: COMPLETADA
            EstadoVentaEntity estadoCompletada = estadoRepo.findByNombreEstado("COMPLETADA").orElse(null);
            if (estadoCompletada == null) {
                estadoCompletada = new EstadoVentaEntity();
                estadoCompletada.setNombreEstado("COMPLETADA");
                estadoCompletada.setDescripcion("Venta completada exitosamente");
                estadoCompletada.setPermiteCancelacion(false);
                estadoCompletada.setEsFinal(true);
                estadoCompletada.setColorHex("#4CAF50");
                estadoRepo.save(estadoCompletada);
                System.out.println("✅ Estado COMPLETADA creado");
            }

            // ESTADO 4: CANCELADA
            EstadoVentaEntity estadoCancelada = estadoRepo.findByNombreEstado("CANCELADA").orElse(null);
            if (estadoCancelada == null) {
                estadoCancelada = new EstadoVentaEntity();
                estadoCancelada.setNombreEstado("CANCELADA");
                estadoCancelada.setDescripcion("Venta cancelada por el usuario o sistema");
                estadoCancelada.setPermiteCancelacion(false);
                estadoCancelada.setEsFinal(true);
                estadoCancelada.setColorHex("#F44336");
                estadoRepo.save(estadoCancelada);
                System.out.println("✅ Estado CANCELADA creado");
            }

            System.out.println("=== Estados de venta inicializados correctamente ===");
        };
    }
}

