@echo off
echo 📦 Sistema Bancário NTT Data - Build Completo Maven
echo ===================================================
echo.

cd /d "%~dp0"

echo 🧹 Limpando builds anteriores...
call mvn clean
echo.

echo 📋 Compilando todos os módulos...
call mvn compile
if %errorlevel% neq 0 (
    echo ❌ Erro na compilação!
    pause
    exit /b 1
)
echo.

echo 🧪 Executando todos os testes...
call mvn test
if %errorlevel% neq 0 (
    echo ⚠️ Alguns testes falharam, mas continuando o build...
)
echo.

echo 📦 Gerando JARs executáveis...
call mvn package
if %errorlevel% neq 0 (
    echo ❌ Erro no empacotamento!
    pause
    exit /b 1
)
echo.

echo ✅ BUILD COMPLETO FINALIZADO!
echo.
echo 📁 ARQUIVOS GERADOS:
echo ==================
if exist "sistema-core\target\sistema-core-1.0.0.jar" (
    echo ✅ sistema-core\target\sistema-core-1.0.0.jar
    echo    📋 Execute: java -jar sistema-core\target\sistema-core-1.0.0.jar
)
if exist "sistema-gui\target\sistema-gui-1.0.0.jar" (
    echo ✅ sistema-gui\target\sistema-gui-1.0.0.jar
    echo    📋 Execute: java -jar sistema-gui\target\sistema-gui-1.0.0.jar
)
if exist "sistema-gui\target\sistema-bancario-gui-standalone.jar" (
    echo ✅ sistema-gui\target\sistema-bancario-gui-standalone.jar
    echo    📋 Execute: java -jar sistema-gui\target\sistema-bancario-gui-standalone.jar
)
echo.

echo 📊 RELATÓRIOS DISPONÍVEIS:
echo =========================
echo 📋 Testes: target\surefire-reports\
echo 📈 Cobertura: target\site\jacoco\index.html
echo 📚 JavaDoc: target\site\apidocs\
echo.

echo 🚀 COMANDOS MAVEN ÚTEIS:
echo =======================
echo mvn clean compile           - Compilar apenas
echo mvn test                     - Executar testes
echo mvn package                  - Gerar JARs
echo mvn exec:java -pl sistema-core -Dexec.mainClass="com.nttdata.banco.Main"
echo mvn exec:java -pl sistema-gui -Dexec.mainClass="com.nttdata.banco.gui.MainGUI"
echo.

pause