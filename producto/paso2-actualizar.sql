-- ========================================
-- PASO 2: ACTUALIZAR PRODUCTOS
-- ========================================
USE petcare_producto;

-- Actualizar todos los productos con estado NULL o inválido al estado DISPONIBLE (id=1)
UPDATE productos
SET id_estado = 1
WHERE id_estado IS NULL
   OR id_estado NOT IN (SELECT id_estado FROM estados_producto);

-- Ver cuántos se actualizaron
SELECT ROW_COUNT() as productos_actualizados;

-- Verificar que todos tienen estados válidos ahora
SELECT p.idproducto, p.nombre, p.id_estado, e.nombre_estado
FROM productos p
LEFT JOIN estados_producto e ON p.id_estado = e.id_estado;

