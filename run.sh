#!/bin/bash
# Script para executar o Sistema Bancário NTT Data

echo "════════════════════════════════════════════════════════"
echo "🏦 Sistema Bancário NTT Data - Launcher"
echo "════════════════════════════════════════════════════════"
echo ""
echo "Escolha uma opção:"
echo "  1) Executar versão Console"
echo "  2) Executar versão GUI (Interface Gráfica)"
echo "  3) Executar testes"
echo "  4) Compilar projeto"
echo "  5) Gerar JARs"
echo "  0) Sair"
echo ""
read -p "Digite sua opção: " opcao

case $opcao in
    1)
        echo ""
        echo "🖥️  Iniciando versão Console..."
        mvn exec:java -pl sistema-core
        ;;
    2)
        echo ""
        echo "🎨 Iniciando versão GUI..."
        if [ -f "sistema-gui/target/sistema-bancario-gui-standalone.jar" ]; then
            java -jar sistema-gui/target/sistema-bancario-gui-standalone.jar
        else
            echo "⚠️  JAR não encontrado. Compilando primeiro..."
            if mvn package -DskipTests; then
                java -jar sistema-gui/target/sistema-bancario-gui-standalone.jar
            else
                echo "❌ Erro ao compilar o projeto. Verifique os erros acima."
                exit 1
            fi
        fi
        ;;
    3)
        echo ""
        echo "🧪 Executando testes..."
        mvn test
        ;;
    4)
        echo ""
        echo "🔨 Compilando projeto..."
        mvn clean compile
        ;;
    5)
        echo ""
        echo "📦 Gerando JARs..."
        mvn clean package
        echo ""
        echo "✅ JARs gerados em:"
        echo "   - sistema-core/target/sistema-core-1.0.0.jar"
        echo "   - sistema-gui/target/sistema-bancario-gui-standalone.jar"
        ;;
    0)
        echo "👋 Até logo!"
        exit 0
        ;;
    *)
        echo "❌ Opção inválida!"
        exit 1
        ;;
esac
