-- =====================================================
-- NORMALIZACIÓN DE ESTADOS DE VENTA
-- =====================================================
USE petcare_venta;

-- Crear tabla de estados de venta
CREATE TABLE IF NOT EXISTS estados_venta (
    id_estado INT PRIMARY KEY AUTO_INCREMENT,
    nombre_estado VARCHAR(50) NOT NULL UNIQUE,
    descripcion TEXT,
    permite_cancelacion BOOLEAN DEFAULT TRUE,
    es_final BOOLEAN DEFAULT FALSE,
    color_hex VARCHAR(7),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Insertar estados
INSERT INTO estados_venta (id_estado, nombre_estado, descripcion, permite_cancelacion, es_final, color_hex) VALUES
(1, 'PENDIENTE', 'Venta pendiente de confirmación', TRUE, FALSE, '#FFA500'),
(2, 'CONFIRMADA', 'Venta confirmada y en proceso', TRUE, FALSE, '#2196F3'),
(3, 'COMPLETADA', 'Venta completada exitosamente', FALSE, TRUE, '#4CAF50'),
(4, 'CANCELADA', 'Venta cancelada por el usuario o sistema', FALSE, TRUE, '#F44336');

-- Verificar datos insertados
SELECT * FROM estados_venta;

-- NOTA: La columna id_estado en la tabla venta se creará automáticamente
-- cuando inicies el microservicio gracias a JPA con ddl-auto=update

