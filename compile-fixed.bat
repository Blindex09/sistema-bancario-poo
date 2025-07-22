@echo off
echo 🏦 Compilando Sistema Bancario NTT Data...
echo.

:: Cria diretorio de saida
if not exist "out" mkdir out

:: Remove arquivos compilados anteriores
if exist "out\*" del /Q "out\*"

:: Compila todas as classes Java explicitamente
echo Compilando classes...
javac -d out -cp src ^
src/main/java/com/nttdata/banco/Main.java ^
src/main/java/com/nttdata/banco/enums/*.java ^
src/main/java/com/nttdata/banco/model/*.java ^
src/main/java/com/nttdata/banco/service/*.java ^
src/main/java/com/nttdata/banco/repository/*.java ^
src/main/java/com/nttdata/banco/menu/*.java

if %errorlevel% equ 0 (
    echo ✅ Compilacao realizada com sucesso!
    echo.
    echo 🚀 Para executar o sistema, use:
    echo java -cp out com.nttdata.banco.Main
    echo.
    echo Ou execute o arquivo run.bat
) else (
    echo ❌ Erro na compilacao!
    echo Verifique se o Java está instalado e configurado corretamente
    echo java -version
)

pause