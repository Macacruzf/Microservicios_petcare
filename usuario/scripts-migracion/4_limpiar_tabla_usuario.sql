-- =====================================================
-- LIMPIAR TABLA USUARIO PARA ARREGLAR FOREIGN KEY
-- =====================================================
USE petcare_usuario;

-- Deshabilitar verificación de FK temporalmente
SET FOREIGN_KEY_CHECKS = 0;

-- Limpiar tabla usuario (tiene datos con id_rol = 0 inválido)
TRUNCATE TABLE usuario;

-- Rehabilitar verificación de FK
SET FOREIGN_KEY_CHECKS = 1;

-- Verificar que esté vacía
SELECT 'Usuarios en la tabla:' as info, COUNT(*) as total FROM usuario;

-- Verificar que los roles existan
SELECT 'Roles disponibles:' as info;
SELECT * FROM roles;

-- RESULTADO ESPERADO:
-- - Usuarios en la tabla: 0
-- - Roles disponibles: ADMIN (id=1), CLIENTE (id=2)

