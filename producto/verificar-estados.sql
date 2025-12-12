-- Verificar productos sin estado asignado
SELECT idproducto, nombre, id_estado 
FROM petcare_producto.productos 
WHERE id_estado IS NULL;
-- Actualizar productos sin estado al estado DISPONIBLE (id=1)
UPDATE petcare_producto.productos 
SET id_estado = 1 
WHERE id_estado IS NULL;
