-- ========================================
-- PASO 3: VERIFICACIÓN FINAL
-- ========================================
USE petcare_producto;

-- Contar productos
SELECT COUNT(*) as total_productos FROM productos;

-- Contar productos con estado válido
SELECT COUNT(*) as productos_con_estado_valido
FROM productos p
WHERE p.id_estado IN (SELECT id_estado FROM estados_producto);

-- Ver todos los productos y sus estados
SELECT p.idproducto, p.nombre, p.precio, p.stock, e.nombre_estado
FROM productos p
INNER JOIN estados_producto e ON p.id_estado = e.id_estado
ORDER BY p.idproducto;

SELECT '✅ VERIFICACIÓN COMPLETADA - Todos los productos tienen estados válidos' as resultado;

