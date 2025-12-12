-- ========================================
-- PASO 1: VERIFICAR ESTADO ACTUAL
-- ========================================
USE petcare_producto;

-- Ver estados existentes
SELECT * FROM estados_producto;

-- Ver productos y sus estados actuales
SELECT p.idproducto, p.nombre, p.id_estado,
       CASE
           WHEN p.id_estado IS NULL THEN 'NULL'
           WHEN p.id_estado NOT IN (SELECT id_estado FROM estados_producto) THEN 'NO EXISTE'
           ELSE 'VÁLIDO'
       END as validez
FROM productos p;

-- Ver qué foreign keys existen
SELECT CONSTRAINT_NAME
FROM information_schema.TABLE_CONSTRAINTS
WHERE TABLE_SCHEMA = 'petcare_producto'
  AND TABLE_NAME = 'productos'
  AND CONSTRAINT_TYPE = 'FOREIGN KEY';

