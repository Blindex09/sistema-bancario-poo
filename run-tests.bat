@echo off
echo 🧪 Compilando e Executando Testes Automatizados...
echo.

:: Cria diretório de saída para testes
if not exist "test-out" mkdir test-out

:: Remove arquivos compilados anteriores
if exist "test-out\*" del /Q /S "test-out\*"

:: Compila as classes principais primeiro (necessárias para os testes)
echo 📋 Compilando classes principais...
javac -d out -cp src ^
src/main/java/com/nttdata/banco/Main.java ^
src/main/java/com/nttdata/banco/enums/*.java ^
src/main/java/com/nttdata/banco/model/*.java ^
src/main/java/com/nttdata/banco/service/*.java ^
src/main/java/com/nttdata/banco/repository/*.java ^
src/main/java/com/nttdata/banco/menu/*.java

if %errorlevel% neq 0 (
    echo ❌ Erro na compilação das classes principais!
    pause
    exit /b 1
)

:: Compila as classes de teste
echo 🧪 Compilando classes de teste...
javac -d test-out -cp "src;out" ^
src/test/java/com/nttdata/banco/TestRunner.java ^
src/test/java/com/nttdata/banco/model/*.java ^
src/test/java/com/nttdata/banco/repository/*.java ^
src/test/java/com/nttdata/banco/service/*.java

if %errorlevel% neq 0 (
    echo ❌ Erro na compilação dos testes!
    pause
    exit /b 1
)

echo ✅ Compilação concluída com sucesso!
echo.

:: Executa os testes
echo 🚀 Executando testes automatizados...
echo.
java -cp "test-out;out" com.nttdata.banco.TestRunner

echo.
echo 📋 Execução de testes concluída!
pause
