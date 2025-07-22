#!/bin/bash
echo "🏦 Compilando Sistema Bancário NTT Data..."
echo

# Cria diretório de saída
mkdir -p out

# Compila todos os arquivos Java
javac -d out -cp src src/main/java/com/nttdata/banco/Main.java src/main/java/com/nttdata/banco/**/*.java

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
fi