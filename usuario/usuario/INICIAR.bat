@echo off
echo ================================================
echo   INICIAR MICROSERVICIO DE USUARIO
echo   Puerto: 8081
echo ================================================
echo.

cd /d "%~dp0"

echo Ejecutando microservicio...
echo Presiona Ctrl+C para detener
echo.

java -jar target\usuario-0.0.1-SNAPSHOT.jar

pause

