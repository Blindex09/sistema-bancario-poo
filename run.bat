@echo off
echo 🏦 Executando Sistema Bancario NTT Data...
echo.

:: Verifica se esta compilado
if not exist "out\com\nttdata\banco\Main.class" (
    echo ⚠️  Sistema nao compilado. Compilando primeiro...
    call compile.bat
    echo.
)

:: Executa o sistema
java -cp out com.nttdata.banco.Main

echo.
echo 👋 Sistema finalizado.
pause