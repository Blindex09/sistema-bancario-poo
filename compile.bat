@echo off
echo 🏦 Compilando Sistema Bancario NTT Data...
echo.

:: Cria diretorio de saida
if not exist "out" mkdir out

:: Compila todos os arquivos Java
javac -d out -cp src src/main/java/com/nttdata/banco/Main.java src/main/java/com/nttdata/banco/**/*.java

if %errorlevel% equ 0 (
    echo ✅ Compilacao realizada com sucesso!
    echo.
    echo 🚀 Para executar o sistema, use:
    echo java -cp out com.nttdata.banco.Main
    echo.
    echo Ou execute o arquivo run.bat
) else (
    echo ❌ Erro na compilacao!
)

pause