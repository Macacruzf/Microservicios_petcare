-- ========================================================================
-- SCRIPT DE LIMPIEZA: Microservicio VENTA
-- ========================================================================
-- Este script elimina todas las tablas del microservicio de venta
-- en el ORDEN CORRECTO para evitar errores de foreign key constraints
-- ========================================================================

USE petcare_venta;

-- ========================================================================
-- PASO 1: Desactivar verificación de foreign keys temporalmente
-- ========================================================================
SET FOREIGN_KEY_CHECKS = 0;

-- ========================================================================
-- PASO 2: Eliminar tablas en cualquier orden (foreign keys desactivadas)
-- ========================================================================

-- Tablas dependientes (con foreign keys)
DROP TABLE IF EXISTS detalle_venta;
DROP TABLE IF EXISTS detalle_carrito;
DROP TABLE IF EXISTS carrito_detalle;  -- Por si tiene otro nombre

-- Tablas principales
DROP TABLE IF EXISTS venta;
DROP TABLE IF EXISTS carrito;

-- Tabla de catálogo
DROP TABLE IF EXISTS estados_venta;

-- ========================================================================
-- PASO 3: Reactivar verificación de foreign keys
-- ========================================================================
SET FOREIGN_KEY_CHECKS = 1;

-- ========================================================================
-- PASO 4: Verificación
-- ========================================================================
SHOW TABLES;

-- Si todo salió bien, no debería mostrar ninguna tabla
-- o solo las tablas que NO son del microservicio de venta

