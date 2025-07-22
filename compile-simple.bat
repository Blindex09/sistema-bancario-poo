@echo off
echo 🏦 Compilando Sistema Bancario NTT Data (Versao Simples)...
echo.

:: Cria diretorio de saida
if not exist "out" mkdir out

:: Compila usando find (mais compatível)
echo Localizando e compilando todos os arquivos Java...

:: Gera lista de arquivos e compila
dir /S /B src\*.java > temp_files.txt
javac -d out -cp src @temp_files.txt

if %errorlevel% equ 0 (
    echo ✅ Compilacao realizada com sucesso!
    echo.
    echo 🚀 Para executar: java -cp out com.nttdata.banco.Main
    del temp_files.txt
) else (
    echo ❌ Erro na compilacao!
    del temp_files.txt
)

pause