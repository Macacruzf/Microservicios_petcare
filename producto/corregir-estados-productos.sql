-- ========================================
-- SCRIPT PARA CORREGIR ESTADOS DE PRODUCTOS
-- ========================================

USE petcare_producto;

-- 1. Ver qué estados existen actualmente
SELECT * FROM estados_producto;

-- 2. Ver productos con estados inválidos o NULL
SELECT p.idproducto, p.nombre, p.id_estado,
       CASE
           WHEN p.id_estado IS NULL THEN 'NULL'
           WHEN NOT EXISTS (SELECT 1 FROM estados_producto WHERE id_estado = p.id_estado) THEN 'NO EXISTE'
           ELSE 'VÁLIDO'
       END as validez
FROM productos p;

-- 3. Eliminar la foreign key constraint si existe (para poder actualizar)
-- Primero verificamos qué constraints existen
SELECT CONSTRAINT_NAME
FROM information_schema.TABLE_CONSTRAINTS
WHERE TABLE_SCHEMA = 'petcare_producto'
  AND TABLE_NAME = 'productos'
  AND CONSTRAINT_TYPE = 'FOREIGN KEY';

-- Intentar eliminar las constraints que puedan existir (ignora errores si no existen)
SET @sql1 = IF((SELECT COUNT(*) FROM information_schema.TABLE_CONSTRAINTS
    WHERE CONSTRAINT_SCHEMA = 'petcare_producto'
    AND TABLE_NAME = 'productos'
    AND CONSTRAINT_NAME = 'FK4v34qgpy3vmng42gvfckouudm') > 0,
    'ALTER TABLE productos DROP FOREIGN KEY FK4v34qgpy3vmng42gvfckouudm',
    'SELECT "FK4v34qgpy3vmng42gvfckouudm no existe"');
PREPARE stmt1 FROM @sql1;
EXECUTE stmt1;
DEALLOCATE PREPARE stmt1;

SET @sql2 = IF((SELECT COUNT(*) FROM information_schema.TABLE_CONSTRAINTS
    WHERE CONSTRAINT_SCHEMA = 'petcare_producto'
    AND TABLE_NAME = 'productos'
    AND CONSTRAINT_NAME = 'fk_productos_estado') > 0,
    'ALTER TABLE productos DROP FOREIGN KEY fk_productos_estado',
    'SELECT "fk_productos_estado no existe"');
PREPARE stmt2 FROM @sql2;
EXECUTE stmt2;
DEALLOCATE PREPARE stmt2;

-- 4. Actualizar productos con estado NULL o inválido al estado DISPONIBLE (id=1)
UPDATE productos
SET id_estado = 1
WHERE id_estado IS NULL
   OR NOT EXISTS (SELECT 1 FROM estados_producto WHERE id_estado = productos.id_estado);

-- 5. Verificar que todos los productos ahora tienen estados válidos
SELECT p.idproducto, p.nombre, p.id_estado, e.nombre_estado
FROM productos p
LEFT JOIN estados_producto e ON p.id_estado = e.id_estado;

-- 6. Recrear la foreign key constraint
ALTER TABLE productos
ADD CONSTRAINT fk_productos_estado
FOREIGN KEY (id_estado)
REFERENCES estados_producto (id_estado);

-- 7. Verificación final
SELECT 'CORRECCIÓN COMPLETADA' as mensaje;
SELECT COUNT(*) as total_productos FROM productos;
SELECT COUNT(*) as productos_con_estado_valido
FROM productos p
WHERE EXISTS (SELECT 1 FROM estados_producto WHERE id_estado = p.id_estado);

