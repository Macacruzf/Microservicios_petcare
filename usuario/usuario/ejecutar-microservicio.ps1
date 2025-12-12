# Script para compilar y ejecutar el microservicio de Usuario
# Usando Java directamente sin Maven Wrapper

Write-Host "================================================" -ForegroundColor Cyan
Write-Host "  COMPILAR Y EJECUTAR MICROSERVICIO USUARIO" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

$projectDir = "C:\Users\bruno\Videos\PetCareConnect\Microservicios_petcare\usuario\usuario"
$javaHome = "C:\Program Files\Java\jdk-21"

cd $projectDir

Write-Host "PASO 1: Verificando Java..." -ForegroundColor Yellow
& "$javaHome\bin\java.exe" -version

Write-Host ""
Write-Host "PASO 2: Compilando proyecto con Maven..." -ForegroundColor Yellow

# Intentar con mvn del sistema
if (Get-Command mvn -ErrorAction SilentlyContinue) {
    Write-Host "Usando Maven del sistema..." -ForegroundColor Green
    mvn clean package -DskipTests
} else {
    Write-Host "ERROR: Maven no encontrado en el sistema" -ForegroundColor Red
    Write-Host ""
    Write-Host "SOLUCION ALTERNATIVA:" -ForegroundColor Yellow
    Write-Host "1. Abre Visual Studio Code" -ForegroundColor White
    Write-Host "2. Abre la carpeta: $projectDir" -ForegroundColor White
    Write-Host "3. Presiona F5 o ve a Run > Start Debugging" -ForegroundColor White
    Write-Host "4. Selecciona 'Spring Boot App'" -ForegroundColor White
    Write-Host ""
    Write-Host "O instala Maven:" -ForegroundColor Yellow
    Write-Host "choco install maven" -ForegroundColor White
    exit 1
}

Write-Host ""
Write-Host "PASO 3: Ejecutando microservicio..." -ForegroundColor Yellow
Write-Host "Puerto: 8081" -ForegroundColor Cyan
Write-Host "Presiona Ctrl+C para detener" -ForegroundColor Gray
Write-Host ""

& "$javaHome\bin\java.exe" -jar target\usuario-0.0.1-SNAPSHOT.jar

