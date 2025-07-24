@echo off
echo 🎨 Sistema Bancário NTT Data - Interface Gráfica Maven
echo =====================================================
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
echo 💡 RECURSOS DE ACESSIBILIDADE DISPONÍVEIS:
echo    🎨 Menu Visualizar → Temas (5 opções)
echo    📏 Menu Visualizar → Tamanho da Fonte (5 tamanhos)
echo    🔊 Menu Acessibilidade → Feedback Sonoro
echo    ⌨️  Navegação completa por teclado
echo    👁️  Suporte a leitores de tela (NVDA, JAWS)
echo.
echo 🚀 Executando Interface Gráfica Acessível...
echo.

call mvn -pl sistema-gui exec:java -Dexec.mainClass="com.nttdata.banco.gui.MainGUI"

echo.
echo 👋 Interface gráfica finalizada.
pause