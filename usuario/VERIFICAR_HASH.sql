-- ========================================
-- VERIFICACIÓN DE HASHES BCrypt
-- ========================================
USE petcare_usuario;

-- Ver todos los usuarios con sus hashes
SELECT
    id_usuario,
    email,
    nombre_usuario,
    LENGTH(password) as longitud_hash,
    password as hash_completo
FROM usuario
ORDER BY id_usuario;

-- Verificación específica del cliente
SELECT
    '¿El hash del cliente es el correcto?' as pregunta,
    CASE
        WHEN password = '$2a$10$oeU9DjLVicK7oKk2vSArF.FQBhR1QbrHqT.X8M5NLRYz9EMzt6uBe'
        THEN '✅ SÍ - El hash coincide con el generado'
        ELSE '❌ NO - El hash NO coincide'
    END as resultado
FROM usuario
WHERE email = 'cliente@petcare.cl';

