-- ========================================
-- LIMPIEZA COMPLETA DE USUARIOS
-- ========================================
USE petcare_usuario;

-- PASO 1: Eliminar TODOS los usuarios
DELETE FROM usuario;

-- PASO 2: Reiniciar AUTO_INCREMENT
ALTER TABLE usuario AUTO_INCREMENT = 1;

-- PASO 3: Verificar que password sea VARCHAR(255)
ALTER TABLE usuario MODIFY COLUMN password VARCHAR(255) NOT NULL;

-- PASO 4: Verificación
SELECT '✅ Base de datos limpia. Ahora reinicia el microservicio.' as resultado;

