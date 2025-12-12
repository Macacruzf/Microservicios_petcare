# Script para ejecutar el microservicio de producto

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "EJECUTANDO MICROSERVICIO PRODUCTO" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Verificar que MySQL esté ejecutándose
Write-Host "1. Verificando MySQL..." -ForegroundColor Yellow
$mysql = Get-Process -Name mysqld -ErrorAction SilentlyContinue
if ($mysql) {
    Write-Host "   ✅ MySQL está ejecutándose" -ForegroundColor Green
} else {
    Write-Host "   ❌ MySQL NO está ejecutándose" -ForegroundColor Red
    Write-Host "   Por favor, inicia Laragon primero." -ForegroundColor Yellow
    pause
    exit
}

Write-Host ""
Write-Host "2. Compilando y ejecutando microservicio..." -ForegroundColor Yellow
Write-Host ""

# Cambiar al directorio del proyecto
Set-Location -Path "C:\Users\bruno\Videos\PetCareConnect\Microservicios_petcare\producto\producto"

# Ejecutar Maven wrapper
& cmd /c "mvnw.cmd spring-boot:run"

