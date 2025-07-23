@echo off
echo 🎨 Compilando Interface Gráfica - Sistema Bancário NTT Data...
echo.

:: Verifica se o sistema principal está compilado
if not exist "..\out\com\nttdata\banco\Main.class" (
    echo ⚠️  Sistema principal não compilado. Compilando primeiro...
    cd ..
    call compile.bat
    cd gui
    echo.
)

:: Cria diretório de saída para GUI
if not exist "out" mkdir out

:: Remove arquivos compilados anteriores
if exist "out\*" del /Q /S "out\*"

:: Compila a interface gráfica
echo 📋 Compilando interface gráfica...
javac -d out -cp "..\out" ^
src/main/java/com/nttdata/banco/gui/MainGUI.java ^
src/main/java/com/nttdata/banco/gui/theme/*.java ^
src/main/java/com/nttdata/banco/gui/utils/*.java ^
src/main/java/com/nttdata/banco/gui/components/*.java ^
src/main/java/com/nttdata/banco/gui/view/*.java

if %errorlevel% neq 0 (
    echo ❌ Erro na compilação da interface gráfica!
    pause
    exit /b 1
)

echo ✅ Interface gráfica compilada com sucesso!
echo.

:: Executa a interface gráfica
echo 🚀 Iniciando interface gráfica...
echo.
java -cp "out;..\out" com.nttdata.banco.gui.MainGUI

echo.
echo 👋 Interface gráfica finalizada.
pause
