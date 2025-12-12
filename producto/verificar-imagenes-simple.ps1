# Script de verificacion - sin caracteres especiales

Write-Host "========================================"
Write-Host "VERIFICACION DE IMAGENES EN BD"
Write-Host "========================================"
Write-Host ""

# 1. Verificar MySQL
Write-Host "1. Verificando MySQL..."
$mysql = Get-Process -Name mysqld -ErrorAction SilentlyContinue
if ($mysql) {
    Write-Host "   OK - MySQL ejecutandose (PID: $($mysql.Id))"
} else {
    Write-Host "   ERROR - MySQL NO esta ejecutandose"
    exit
}

# 2. Verificar microservicio
Write-Host ""
Write-Host "2. Verificando microservicio producto..."
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8086/api/v1/productos/movil" -Method Get -ErrorAction Stop
    Write-Host "   OK - Microservicio respondiendo en puerto 8086"
    Write-Host "   Productos encontrados: $($response.Count)"
} catch {
    Write-Host "   ERROR - Microservicio NO responde"
    exit
}

# 3. Verificar URLs de imagenes
Write-Host ""
Write-Host "3. Verificando URLs de imagenes..."
$productosConImagen = $response | Where-Object { $_.imagenUrl -ne $null }
if ($productosConImagen.Count -gt 0) {
    Write-Host "   OK - $($productosConImagen.Count) productos tienen imagenUrl"
    Write-Host "   Ejemplo: $($productosConImagen[0].imagenUrl)"
} else {
    Write-Host "   ADVERTENCIA - Ningun producto tiene imagenUrl"
}

# 4. Probar descarga de imagen
Write-Host ""
Write-Host "4. Probando descarga de imagen..."
$idProducto = $productosConImagen[0].idProducto
try {
    $imagen = Invoke-WebRequest -Uri "http://localhost:8086/api/v1/productos/$idProducto/imagen" -Method Get -ErrorAction Stop
    $tamanioKB = [math]::Round($imagen.Content.Length/1024, 2)
    Write-Host "   OK - Imagen descargada correctamente"
    Write-Host "   Tamanio: $tamanioKB KB"
} catch {
    Write-Host "   ERROR - No se pudo descargar imagen: $($_.Exception.Message)"
}

# 5. Verificar en MySQL
Write-Host ""
Write-Host "5. Verificando en MySQL..."
$mysqlPath = "C:\laragon\bin\mysql\mysql-8.4.3-winx64\bin\mysql.exe"

if (Test-Path $mysqlPath) {
    $query = "USE petcare_producto; SELECT idproducto, nombre, LENGTH(imagen) as tamanio_bytes FROM productos LIMIT 5;"
    $resultado = & $mysqlPath -u root -e $query 2>&1 | Out-String

    Write-Host "   Primeros 5 productos con tamano de imagen:"
    Write-Host $resultado
} else {
    Write-Host "   ADVERTENCIA - mysql.exe no encontrado"
}

Write-Host ""
Write-Host "========================================"
Write-Host "VERIFICACION COMPLETADA"
Write-Host "========================================"

