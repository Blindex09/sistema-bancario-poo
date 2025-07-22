#!/bin/bash
echo "🏦 Executando Sistema Bancário NTT Data..."
echo

# Verifica se está compilado
if [ ! -f "out/com/nttdata/banco/Main.class" ]; then
    echo "⚠️  Sistema não compilado. Compilando primeiro..."
    ./compile.sh
    echo
fi

# Executa o sistema
java -cp out com.nttdata.banco.Main

echo
echo "👋 Sistema finalizado."