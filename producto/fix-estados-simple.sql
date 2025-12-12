-- ========================================
-- SCRIPT SIMPLIFICADO PARA CORREGIR ESTADOS
-- ========================================

USE petcare_producto;

-- PASO 1: Ver estados existentes
SELECT '=== ESTADOS EXISTENTES ===' as paso;
SELECT * FROM estados_producto;

-- PASO 2: Ver productos y sus estados actuales
SELECT '=== PRODUCTOS Y SUS ESTADOS ===' as paso;
SELECT p.idproducto, p.nombre, p.id_estado,
       CASE
           WHEN p.id_estado IS NULL THEN 'NULL'
           WHEN p.id_estado NOT IN (SELECT id_estado FROM estados_producto) THEN 'NO EXISTE'
           ELSE 'VÁLIDO'
       END as validez
FROM productos p;

-- PASO 3: Eliminar constraints existentes (ejecutar una por una)
-- Si da error, es porque no existe, puedes ignorarlo
ALTER TABLE productos DROP FOREIGN KEY FK4v34qgpy3vmng42gvfckouudm;
-- Si la anterior da error, intenta con esta:
-- ALTER TABLE productos DROP FOREIGN KEY fk_productos_estado;

-- PASO 4: Actualizar productos con estado NULL o inválido
SELECT '=== ACTUALIZANDO PRODUCTOS ===' as paso;
UPDATE productos
SET id_estado = 1
WHERE id_estado IS NULL
   OR id_estado NOT IN (SELECT id_estado FROM estados_producto);

-- Verificar cuántos se actualizaron
SELECT ROW_COUNT() as productos_actualizados;

-- PASO 5: Verificar que todos tienen estados válidos
SELECT '=== VERIFICACIÓN POST-UPDATE ===' as paso;
SELECT p.idproducto, p.nombre, p.id_estado, e.nombre_estado
FROM productos p
LEFT JOIN estados_producto e ON p.id_estado = e.id_estado;

-- PASO 6: Recrear la foreign key
SELECT '=== CREANDO FOREIGN KEY ===' as paso;
ALTER TABLE productos
ADD CONSTRAINT fk_productos_estado
FOREIGN KEY (id_estado)
REFERENCES estados_producto (id_estado);

-- PASO 7: Verificación final
SELECT '=== VERIFICACIÓN FINAL ===' as paso;
SELECT COUNT(*) as total_productos FROM productos;
SELECT COUNT(*) as productos_validos
FROM productos p
WHERE p.id_estado IN (SELECT id_estado FROM estados_producto);

SELECT '✅ CORRECCIÓN COMPLETADA' as resultado;

