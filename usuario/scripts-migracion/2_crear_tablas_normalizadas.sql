-- =====================================================
-- SCRIPT 2: CREAR TABLAS NORMALIZADAS
-- =====================================================
-- Ejecutar en HeidiSQL (Laragon)
-- Ejecutar DESPUÉS de limpiar los datos con el script 1

-- =====================================================
-- PARTE A: MICROSERVICIO USUARIO - TABLA DE ROLES
-- =====================================================
USE petcare_usuario;

-- TABLA: roles
CREATE TABLE IF NOT EXISTS roles (
    id_rol INT PRIMARY KEY AUTO_INCREMENT,
    nombre_rol VARCHAR(50) NOT NULL UNIQUE COMMENT 'ADMIN, CLIENTE, etc.',
    descripcion TEXT COMMENT 'Descripción del rol',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE COMMENT 'Si el rol está activo'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Catálogo de roles de usuario';

-- Insertar roles iniciales
INSERT INTO roles (id_rol, nombre_rol, descripcion) VALUES
(1, 'ADMIN', 'Administrador con acceso completo al sistema'),
(2, 'CLIENTE', 'Cliente que puede realizar compras y dejar reseñas');

-- MODIFICAR TABLA usuario
-- Agregar columna para FK de rol
ALTER TABLE usuario
ADD COLUMN id_rol INT COMMENT 'FK a tabla roles' AFTER password;

-- Agregar índice
ALTER TABLE usuario
ADD INDEX idx_rol (id_rol);

-- Agregar Foreign Key
ALTER TABLE usuario
ADD CONSTRAINT fk_usuario_rol
    FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
    ON DELETE RESTRICT ON UPDATE CASCADE;

-- Eliminar columna ENUM antigua
ALTER TABLE usuario
DROP COLUMN IF EXISTS rol;

-- IMPORTANTE: También eliminar columna estado si existe (no la vamos a normalizar por ahora)
-- ALTER TABLE usuario DROP COLUMN IF EXISTS estado;

-- =====================================================
-- PARTE B: MICROSERVICIO PRODUCTO - TABLA DE ESTADOS
-- =====================================================
USE petcare_producto;

-- TABLA: estados_producto
CREATE TABLE IF NOT EXISTS estados_producto (
    id_estado INT PRIMARY KEY AUTO_INCREMENT,
    nombre_estado VARCHAR(50) NOT NULL UNIQUE COMMENT 'DISPONIBLE, NO_DISPONIBLE, SIN_STOCK',
    descripcion TEXT COMMENT 'Descripción del estado',
    visible_catalogo BOOLEAN DEFAULT TRUE COMMENT 'Si aparece en el catálogo',
    permite_venta BOOLEAN DEFAULT TRUE COMMENT 'Si permite realizar ventas',
    color_hex VARCHAR(7) COMMENT 'Color para UI (#4CAF50)',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Catálogo de estados de producto';

-- Insertar estados iniciales
INSERT INTO estados_producto (id_estado, nombre_estado, descripcion, visible_catalogo, permite_venta, color_hex) VALUES
(1, 'DISPONIBLE', 'Producto disponible para venta', TRUE, TRUE, '#4CAF50'),
(2, 'NO_DISPONIBLE', 'Producto no disponible temporalmente', FALSE, FALSE, '#FF9800'),
(3, 'SIN_STOCK', 'Producto sin stock', TRUE, FALSE, '#F44336');

-- MODIFICAR TABLA productos
-- Agregar columna para FK de estado
ALTER TABLE productos
ADD COLUMN id_estado INT DEFAULT 1 COMMENT 'FK a tabla estados_producto' AFTER stock;

-- Agregar índice
ALTER TABLE productos
ADD INDEX idx_estado (id_estado);

-- Agregar Foreign Key
ALTER TABLE productos
ADD CONSTRAINT fk_producto_estado
    FOREIGN KEY (id_estado) REFERENCES estados_producto(id_estado)
    ON DELETE RESTRICT ON UPDATE CASCADE;

-- Eliminar columna ENUM antigua
ALTER TABLE productos
DROP COLUMN IF EXISTS estado;

-- =====================================================
-- VERIFICACIÓN FINAL
-- =====================================================

-- Ver estructura de la tabla usuario modificada
USE petcare_usuario;
DESCRIBE usuario;
SELECT 'ROLES DISPONIBLES' as info;
SELECT * FROM roles;

-- Ver estructura de la tabla productos modificada
USE petcare_producto;
DESCRIBE productos;
SELECT 'ESTADOS DE PRODUCTO DISPONIBLES' as info;
SELECT * FROM estados_producto;

-- Resumen
SELECT
    'petcare_usuario - Roles creados' as verificacion,
    COUNT(*) as cantidad
FROM petcare_usuario.roles
UNION ALL
SELECT
    'petcare_producto - Estados creados',
    COUNT(*)
FROM petcare_producto.estados_producto;

-- Resultado esperado:
-- Roles creados: 2
-- Estados creados: 3

