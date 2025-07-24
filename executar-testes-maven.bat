@echo off
echo 🧪 Sistema Bancário NTT Data - Testes Automatizados Maven
echo =========================================================
echo.

cd /d "%~dp0"

echo 📋 Compilando e executando testes...
echo.

call mvn clean test
if %errorlevel% neq 0 (
    echo ❌ Alguns testes falharam!
    echo.
    echo 📊 Para ver relatórios detalhados:
    echo    - Surefire Reports: target\surefire-reports\
    echo    - Cobertura JaCoCo: target\site\jacoco\index.html
    pause
    exit /b 1
)

echo.
echo ✅ Todos os testes passaram com sucesso!
echo.
echo 📊 RELATÓRIOS DISPONÍVEIS:
echo    📋 Relatórios de Teste: target\surefire-reports\
echo    📈 Cobertura de Código: target\site\jacoco\index.html
echo.
echo 💡 Para executar testes com cobertura detalhada:
echo    mvn clean test jacoco:report
echo.
echo 🎯 Para executar apenas testes específicos:
echo    mvn test -Dtest=ClienteTest
echo    mvn test -Dtest=BancoServiceTest
echo.

pause