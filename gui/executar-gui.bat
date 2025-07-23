@echo off
echo 🎨 Executando Interface Gráfica - Sistema Bancário NTT Data...
echo.

cd /d "C:\Users\olive\Documents\GitHub\sistema-bancario-poo\gui"

:: Verifica se o sistema principal está compilado
if not exist "..\out\com\nttdata\banco\Main.class" (
    echo ⚠️  Sistema principal não compilado. Compilando primeiro...
    cd ..
    javac -d out -cp src src/main/java/com/nttdata/banco/enums/*.java
    javac -d out -cp src;out src/main/java/com/nttdata/banco/model/*.java
    javac -d out -cp src;out src/main/java/com/nttdata/banco/repository/*.java
    javac -d out -cp src;out src/main/java/com/nttdata/banco/service/*.java
    javac -d out -cp src;out src/main/java/com/nttdata/banco/menu/*.java
    javac -d out -cp src;out src/main/java/com/nttdata/banco/Main.java
    cd gui
    echo ✅ Sistema principal compilado!
    echo.
)

:: Cria diretório de saída para GUI
if not exist "out" mkdir out

:: Compila a interface gráfica se necessário
if not exist "out\com\nttdata\banco\gui\MainGUI.class" (
    echo 📋 Compilando interface gráfica...
    javac -d out -cp "..\out" src/main/java/com/nttdata/banco/gui/theme/*.java
    javac -d out -cp "..\out;out" src/main/java/com/nttdata/banco/gui/utils/*.java
    javac -d out -cp "..\out;out" src/main/java/com/nttdata/banco/gui/components/*.java
    javac -d out -cp "..\out;out" src/main/java/com/nttdata/banco/gui/view/*.java
    javac -d out -cp "..\out;out" src/main/java/com/nttdata/banco/gui/MainGUI.java
    echo ✅ Interface gráfica compilada!
    echo.
)

:: Executa a interface gráfica
echo 🚀 Iniciando interface gráfica acessível...
echo.
echo 💡 RECURSOS DISPONÍVEIS:
echo    🎨 Menu Visualizar → Temas (Claro, Escuro, Alto Contraste)
echo    📏 Menu Visualizar → Tamanho da Fonte (5 opções)
echo    🔊 Menu Acessibilidade → Feedback Sonoro
echo    ⌨️  Navegação completa por teclado (Tab, Enter, Alt+Letras)
echo    👁️  Suporte a leitores de tela
echo.

java -cp "out;..\out" com.nttdata.banco.gui.MainGUI

echo.
echo 👋 Interface gráfica finalizada.
pause