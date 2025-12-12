# Script para verificar que las imágenes se cargaron correctamente en la BD

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "VERIFICACIÓN DE IMÁGENES EN BD" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# 1. Verificar MySQL
Write-Host "1. Verificando MySQL..." -ForegroundColor Yellow
$mysql = Get-Process -Name mysqld -ErrorAction SilentlyContinue
if ($mysql) {
    Write-Host "   ✅ MySQL está ejecutándose (PID: $($mysql.Id))" -ForegroundColor Green
} else {
    Write-Host "   ❌ MySQL NO está ejecutándose. Inicia Laragon primero." -ForegroundColor Red
    exit
}

# 2. Verificar microservicio
Write-Host ""
Write-Host "2. Verificando microservicio producto..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8086/api/v1/productos/movil" -Method Get -ErrorAction Stop
    Write-Host "   ✅ Microservicio respondiendo en puerto 8086" -ForegroundColor Green
    Write-Host "   📦 Productos encontrados: $($response.Count)" -ForegroundColor Green
} catch {
    Write-Host "   ❌ Microservicio NO está respondiendo. Ejecuta ProductoApplication desde IntelliJ." -ForegroundColor Red
    exit
}

# 3. Verificar URLs de imágenes
Write-Host ""
Write-Host "3. Verificando URLs de imágenes en el DTO..." -ForegroundColor Yellow
$productosConImagen = $response | Where-Object { $_.imagenUrl -ne $null }
if ($productosConImagen.Count -gt 0) {
    Write-Host "   ✅ $($productosConImagen.Count) productos tienen imagenUrl" -ForegroundColor Green
    Write-Host "   Ejemplo: $($productosConImagen[0].imagenUrl)" -ForegroundColor Gray
} else {
    Write-Host "   ⚠️ Ningún producto tiene imagenUrl" -ForegroundColor Yellow
}

# 4. Probar descarga de imagen
Write-Host ""
Write-Host "4. Probando descarga de imagen del producto 1..." -ForegroundColor Yellow
try {
    $imagen = Invoke-WebRequest -Uri "http://localhost:8086/api/v1/productos/1/imagen" -Method Get -ErrorAction Stop
    $tamaño = $imagen.Content.Length
    Write-Host "   ✅ Imagen descargada correctamente" -ForegroundColor Green
    Write-Host "   📊 Tamaño: $([math]::Round($tamaño/1024, 2)) KB" -ForegroundColor Green
    Write-Host "   📝 Content-Type: $($imagen.Headers['Content-Type'])" -ForegroundColor Gray
} catch {
    Write-Host "   ❌ Error al descargar imagen: $($_.Exception.Message)" -ForegroundColor Red
}

# 5. Verificar en MySQL (requiere mysql.exe en PATH o especificar ruta completa)
Write-Host ""
Write-Host "5. Verificando en MySQL..." -ForegroundColor Yellow
$mysqlPath = "C:\laragon\bin\mysql\mysql-8.4.3-winx64\bin\mysql.exe"

if (Test-Path $mysqlPath) {
    $query = "USE petcare_producto; SELECT idproducto, nombre, LENGTH(imagen) as tamanio_bytes FROM productos LIMIT 5;"
    $resultado = & $mysqlPath -u root -e $query 2>&1

    if ($LASTEXITCODE -eq 0) {
        Write-Host "   ✅ Consulta exitosa a MySQL" -ForegroundColor Green
        Write-Host ""
        Write-Host "   Primeros 5 productos con tamaño de imagen:" -ForegroundColor Cyan
        $resultado | ForEach-Object { Write-Host "   $_" -ForegroundColor Gray }
    } else {
        Write-Host "   ⚠️ No se pudo consultar MySQL directamente" -ForegroundColor Yellow
    }
} else {
    Write-Host "   ⚠️ mysql.exe no encontrado en la ruta esperada" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "VERIFICACIÓN COMPLETADA" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

