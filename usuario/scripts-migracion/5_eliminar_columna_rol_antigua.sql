-- =====================================================
-- ELIMINAR COLUMNA ROL ANTIGUA Y VERIFICAR
-- =====================================================
USE petcare_usuario;

-- Ver estructura actual de la tabla
DESCRIBE usuario;

-- Eliminar columna rol antigua (ENUM) si existe
-- MySQL no soporta IF EXISTS en DROP COLUMN, usamos un procedimiento
SET @exist := (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
               WHERE TABLE_SCHEMA = 'petcare_usuario'
               AND TABLE_NAME = 'usuario'
               AND COLUMN_NAME = 'rol');

SET @sqlstmt := IF(@exist > 0, 'ALTER TABLE usuario DROP COLUMN rol', 'SELECT ''Columna rol ya no existe'' AS Info');
PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Ver estructura después de eliminar
DESCRIBE usuario;

-- Verificar los usuarios con sus roles correctos
SELECT
    u.id_usuario,
    u.nombre_usuario,
    u.email,
    u.id_rol,
    r.nombre_rol,
    u.estado
FROM usuario u
LEFT JOIN roles r ON u.id_rol = r.id_rol;

-- RESULTADO ESPERADO:
-- admin@petcare.cl -> id_rol = 1 -> nombre_rol = ADMIN
-- cliente@petcare.cl -> id_rol = 2 -> nombre_rol = CLIENTE

