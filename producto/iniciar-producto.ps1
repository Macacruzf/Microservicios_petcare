# Script para iniciar el microservicio de producto
# Saltando la compilación de tests

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Iniciando Microservicio de Producto" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Verificar y liberar puerto 8086 si está ocupado
Write-Host "Verificando puerto 8086..." -ForegroundColor Yellow
$connection = Get-NetTCPConnection -LocalPort 8086 -ErrorAction SilentlyContinue
if ($connection) {
    $processId = $connection.OwningProcess
    Write-Host "⚠️  Puerto 8086 está en uso por proceso $processId" -ForegroundColor Yellow
    Write-Host "Deteniendo proceso..." -ForegroundColor Yellow
    Stop-Process -Id $processId -Force -ErrorAction SilentlyContinue
    Start-Sleep -Seconds 2
    Write-Host "✅ Puerto liberado" -ForegroundColor Green
} else {
    Write-Host "✅ Puerto 8086 disponible" -ForegroundColor Green
}
Write-Host ""

Set-Location "C:\Users\bruno\Videos\PetCareConnect\Microservicios_petcare\producto\producto"

Write-Host "Compilando código fuente (sin tests)..." -ForegroundColor Yellow
mvn clean compile -DskipTests

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "✅ Compilación exitosa" -ForegroundColor Green
    Write-Host ""
    Write-Host "Iniciando microservicio en puerto 8086..." -ForegroundColor Yellow
    Write-Host "Presiona Ctrl+C para detener el microservicio" -ForegroundColor Cyan
    Write-Host ""

    # Ejecutar con Maven Spring Boot
    mvn spring-boot:run '-Dmaven.test.skip=true'
} else {
    Write-Host ""
    Write-Host "❌ Error en la compilación" -ForegroundColor Red
    Write-Host "Por favor, revisa los errores anteriores" -ForegroundColor Red
    pause
}

