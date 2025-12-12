-- =====================================================
-- SCRIPT 3: VERIFICACIÓN RÁPIDA
-- =====================================================
-- Ejecutar DESPUÉS de crear las tablas normalizadas
-- Para verificar que todo está correcto

-- =====================================================
-- VERIFICAR: petcare_usuario
-- =====================================================
USE petcare_usuario;

SELECT '========== VERIFICACIÓN petcare_usuario ==========' as ' ';

-- 1. ¿Existe la tabla roles?
SELECT 'Tabla roles existe' as verificacion,
       COUNT(*) as filas
FROM roles;

-- 2. Ver datos de roles
SELECT 'Datos de roles:' as ' ';
SELECT * FROM roles;

-- 3. Estructura de la tabla usuario
SELECT 'Estructura de usuario:' as ' ';
DESCRIBE usuario;

-- 4. ¿La columna id_rol existe en usuario?
SELECT 'Columna id_rol en usuario' as verificacion,
       COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_KEY
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'petcare_usuario'
  AND TABLE_NAME = 'usuario'
  AND COLUMN_NAME = 'id_rol';

-- 5. ¿Existe la FK?
SELECT 'Foreign Key fk_usuario_rol' as verificacion,
       CONSTRAINT_NAME, REFERENCED_TABLE_NAME, REFERENCED_COLUMN_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'petcare_usuario'
  AND TABLE_NAME = 'usuario'
  AND CONSTRAINT_NAME = 'fk_usuario_rol';

-- =====================================================
-- VERIFICAR: petcare_producto
-- =====================================================
USE petcare_producto;

SELECT '========== VERIFICACIÓN petcare_producto ==========' as ' ';

-- 1. ¿Existe la tabla estados_producto?
SELECT 'Tabla estados_producto existe' as verificacion,
       COUNT(*) as filas
FROM estados_producto;

-- 2. Ver datos de estados
SELECT 'Datos de estados_producto:' as ' ';
SELECT * FROM estados_producto;

-- 3. Estructura de la tabla productos
SELECT 'Estructura de productos:' as ' ';
DESCRIBE productos;

-- 4. ¿La columna id_estado existe en productos?
SELECT 'Columna id_estado en productos' as verificacion,
       COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_KEY, COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'petcare_producto'
  AND TABLE_NAME = 'productos'
  AND COLUMN_NAME = 'id_estado';

-- 5. ¿Existe la FK?
SELECT 'Foreign Key fk_producto_estado' as verificacion,
       CONSTRAINT_NAME, REFERENCED_TABLE_NAME, REFERENCED_COLUMN_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'petcare_producto'
  AND TABLE_NAME = 'productos'
  AND CONSTRAINT_NAME = 'fk_producto_estado';

-- =====================================================
-- RESUMEN FINAL
-- =====================================================

SELECT '========== RESUMEN FINAL ==========' as ' ';

SELECT
    'petcare_usuario.roles' as tabla,
    COUNT(*) as registros,
    'DEBE SER 2' as esperado
FROM petcare_usuario.roles
UNION ALL
SELECT
    'petcare_producto.estados_producto',
    COUNT(*),
    'DEBE SER 3'
FROM petcare_producto.estados_producto;

-- =====================================================
-- RESULTADO ESPERADO
-- =====================================================
--
-- Tabla roles existe: 2 filas
-- Datos de roles:
--   1 | ADMIN   | Administrador con acceso completo
--   2 | CLIENTE | Cliente que puede realizar compras
--
-- Columna id_rol en usuario: EXISTS
-- Foreign Key fk_usuario_rol: EXISTS
--
-- Tabla estados_producto existe: 3 filas
-- Datos de estados_producto:
--   1 | DISPONIBLE    | ...
--   2 | NO_DISPONIBLE | ...
--   3 | SIN_STOCK     | ...
--
-- Columna id_estado en productos: EXISTS
-- Foreign Key fk_producto_estado: EXISTS
--
-- RESUMEN FINAL:
--   petcare_usuario.roles: 2 registros
--   petcare_producto.estados_producto: 3 registros
--
-- =====================================================

