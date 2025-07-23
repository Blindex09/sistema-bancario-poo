@echo off
echo 🏦 Executando Sistema Bancario NTT Data...
echo.

cd /d "C:\Users\olive\Documents\GitHub\sistema-bancario-poo"

:: Verifica se já foi compilado
if not exist "out\com\nttdata\banco\Main.class" (
    echo ⚠️  Sistema não compilado. Executando compilação...
    call compile-fixed.bat
    echo.
)

:: Executa o sistema
echo 🚀 Iniciando o sistema...
java -cp out com.nttdata.banco.Main

pause