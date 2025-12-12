-- =====================================================
-- SCRIPT 1: LIMPIAR DATOS - USUARIO Y PRODUCTO
-- =====================================================
-- Ejecutar en HeidiSQL (Laragon)
-- IMPORTANTE: Detén TODOS los microservicios antes de ejecutar

-- =====================================================
-- LIMPIAR: petcare_usuario
-- =====================================================
USE petcare_usuario;

-- Deshabilitar verificación de FK temporalmente
SET FOREIGN_KEY_CHECKS = 0;

-- Limpiar tabla de usuarios
TRUNCATE TABLE usuario;

-- Rehabilitar verificación de FK
SET FOREIGN_KEY_CHECKS = 1;

-- Verificar que esté vacía
SELECT 'petcare_usuario' as base_datos, COUNT(*) as total_usuarios FROM usuario;
-- Resultado esperado: 0

-- =====================================================
-- LIMPIAR: petcare_producto
-- =====================================================
USE petcare_producto;

-- Deshabilitar verificación de FK temporalmente
SET FOREIGN_KEY_CHECKS = 0;

-- Limpiar tabla de productos
TRUNCATE TABLE productos;

-- Limpiar tabla de categorías también (porque productos depende de ella)
TRUNCATE TABLE categorias;

-- Rehabilitar verificación de FK
SET FOREIGN_KEY_CHECKS = 1;

-- Verificar que estén vacías
SELECT 'petcare_producto - productos' as tabla, COUNT(*) as total FROM productos
UNION ALL
SELECT 'petcare_producto - categorias', COUNT(*) FROM categorias;
-- Resultado esperado: ambos en 0

