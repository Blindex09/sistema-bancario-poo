#!/bin/bash
echo "🏦 Compilando Sistema Bancário NTT Data..."
echo

# Cria diretório de saída
mkdir -p out

# Remove arquivos compilados anteriores
rm -f out/*.class 2>/dev/null

# Compila todas as classes Java explicitamente
echo "Compilando classes..."
javac -d out -cp src \
src/main/java/com/nttdata/banco/Main.java \
src/main/java/com/nttdata/banco/enums/*.java \
src/main/java/com/nttdata/banco/model/*.java \
src/main/java/com/nttdata/banco/service/*.java \
src/main/java/com/nttdata/banco/repository/*.java \
src/main/java/com/nttdata/banco/menu/*.java

if [ $? -eq 0 ]; then
    echo "✅ Compilação realizada com sucesso!"
    echo
    echo "🚀 Para executar o sistema, use:"
    echo "java -cp out com.nttdata.banco.Main"
    echo
    echo "Ou execute o arquivo run.sh"
    chmod +x run.sh
else
    echo "❌ Erro na compilação!"
    echo "Verifique se o Java está instalado e configurado corretamente"
    echo "java -version"
fi