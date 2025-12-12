@echo off
cd /d "%~dp0"
echo ================================
echo   MICROSERVICIO DE USUARIO
echo ================================
echo.
echo Iniciando en puerto 8081...
echo.
java -jar target\usuario-0.0.1-SNAPSHOT.jar
pause

