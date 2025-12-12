-- ========================================================================
-- VERIFICACIÓN COMPLETA: Tablas del Microservicio VENTA
-- ========================================================================

USE petcare_venta;

-- Listar todas las tablas
SELECT '=== TABLAS EXISTENTES ===' AS Info;
SHOW TABLES;

-- Estructura de la tabla CARRITO
SELECT '=== ESTRUCTURA: CARRITO ===' AS Info;
DESCRIBE carrito;

-- Estructura de la tabla DETALLE_CARRITO
SELECT '=== ESTRUCTURA: DETALLE_CARRITO ===' AS Info;
DESCRIBE detalle_carrito;

-- Estructura de la tabla VENTA
SELECT '=== ESTRUCTURA: VENTA ===' AS Info;
DESCRIBE venta;

-- Estructura de la tabla DETALLE_VENTA
SELECT '=== ESTRUCTURA: DETALLE_VENTA ===' AS Info;
DESCRIBE detalle_venta;

-- Estructura de la tabla ESTADOS_VENTA
SELECT '=== ESTRUCTURA: ESTADOS_VENTA ===' AS Info;
DESCRIBE estados_venta;

-- Contar registros en cada tabla
SELECT '=== CONTEO DE REGISTROS ===' AS Info;
SELECT 'carrito' AS tabla, COUNT(*) AS registros FROM carrito
UNION ALL
SELECT 'detalle_carrito', COUNT(*) FROM detalle_carrito
UNION ALL
SELECT 'venta', COUNT(*) FROM venta
UNION ALL
SELECT 'detalle_venta', COUNT(*) FROM detalle_venta
UNION ALL
SELECT 'estados_venta', COUNT(*) FROM estados_venta;

-- Verificar datos de estados_venta (debe tener 4 estados)
SELECT '=== DATOS: ESTADOS_VENTA ===' AS Info;
SELECT * FROM estados_venta;

