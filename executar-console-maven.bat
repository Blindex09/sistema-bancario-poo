@echo off
echo 🚀 Sistema Bancário NTT Data - Maven Edition
echo ============================================
echo.

cd /d "%~dp0"

echo 📋 Compilando projeto...
call mvn clean compile
if %errorlevel% neq 0 (
    echo ❌ Erro na compilação!
    pause
    exit /b 1
)

echo ✅ Compilação concluída!
echo.
echo 🏦 Executando Sistema Bancário (Console)...
echo.

call mvn -pl sistema-core exec:java -Dexec.mainClass="com.nttdata.banco.Main"

echo.
echo 👋 Sistema finalizado.
pause