🔧 Guia de Compilação - Sistema Bancário NTT Data

📋 Pré-requisitos

1. Java JDK 8 ou superior instalado
2. JAVA_HOME configurado
3. PATH incluindo o diretório bin do Java

Verificar instalação:

java -version
javac -version

🚀 Opções de Compilação

Opção 1: Scripts Corrigidos (Recomendado)

# Windows
compile-fixed.bat

# Linux/Mac
chmod +x compile-fixed.sh
./compile-fixed.sh

Opção 2: Script Simples

# Windows
compile-simple.bat

Opção 3: Linha de Comando Manual

# Criar diretório de saída
mkdir out

# Compilar manualmente
javac -d out -cp src src/main/java/com/nttdata/banco/Main.java src/main/java/com/nttdata/banco/*/*.java

Opção 4: Compilação Individual por Pacote

# Compilar por pacotes (mais seguro)
javac -d out -cp src src/main/java/com/nttdata/banco/enums/*.java
javac -d out -cp src:out src/main/java/com/nttdata/banco/model/*.java
javac -d out -cp src:out src/main/java/com/nttdata/banco/repository/*.java
javac -d out -cp src:out src/main/java/com/nttdata/banco/service/*.java
javac -d out -cp src:out src/main/java/com/nttdata/banco/menu/*.java
javac -d out -cp src:out src/main/java/com/nttdata/banco/Main.java

🐛 Problemas Comuns e Soluções

Erro: "javac não é reconhecido"
Causa: Java não está no PATH
Solução:
- Windows: Adicionar C:\Program Files\Java\jdk-XX\bin ao PATH
- Linux/Mac: Adicionar ao .bashrc ou .zshrc:

  export JAVA_HOME=/usr/lib/jvm/java-XX-openjdk
  export PATH=$PATH:$JAVA_HOME/bin

Erro: "cannot find symbol"
Causa: Dependências entre classes não resolvidas
Solução: Usar a opção 4 (compilação por pacotes) ou verificar imports

Erro: "package does not exist"
Causa: Estrutura de pacotes incorreta
Solução: Verificar se a estrutura está correta:

src/main/java/com/nttdata/banco/
├── Main.java
├── enums/
├── model/
├── repository/
├── service/
└── menu/

Erro: "access denied" ou "permission denied"
Causa: Permissões de escrita no diretório
Solução:
- Windows: Executar como administrador
- Linux/Mac: chmod 755 no diretório

💡 Dicas

1. Sempre limpe o diretório out antes de recompilar
2. Use caminhos absolutos se relativos não funcionarem
3. Verifique se todos os arquivos .java estão salvos
4. IDEs como IntelliJ IDEA ou Eclipse facilitam a compilação

🏃‍♂️ Execução

Após compilar com sucesso:

# Windows
java -cp out com.nttdata.banco.Main

# Linux/Mac
java -cp out com.nttdata.banco.Main

📞 Se nada funcionar

1. Tente compilar apenas o Main.java primeiro
2. Verifique se há erros de sintaxe no código
3. Use uma IDE para identificar problemas
4. Considere usar Maven ou Gradle para projetos maiores

---
📝 Nota: Este projeto foi desenvolvido para o Bootcamp NTT Data - Java para Iniciantes