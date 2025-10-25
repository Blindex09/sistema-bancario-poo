# 🏗️ Estrutura do Projeto - Sistema Bancário POO

## Visão Geral

Este projeto está organizado como um **multi-módulo Maven**, dividindo o código em módulos lógicos e reutilizáveis.

## Módulos

### 📦 `sistema-core`
**Descrição:** Módulo principal contendo toda a lógica de negócio do sistema bancário.

**Conteúdo:**
- **Models**: Classes de domínio (Cliente, Conta, Transacao, Investimento)
- **Services**: Lógica de negócio (BancoService)
- **Repositories**: Camada de acesso a dados
- **Enums**: Tipos enumerados (TipoConta, TipoTransacao, TipoInvestimento)
- **Menu**: Interface de console para usuário
- **Tests**: Testes automatizados completos

**Executar:**
```bash
# Console mode
mvn exec:java -pl sistema-core

# Testes
mvn test -pl sistema-core
```

**Gerar JAR:**
```bash
mvn package -pl sistema-core
java -jar sistema-core/target/sistema-core-1.0.0.jar
```

---

### 🖥️ `sistema-gui`
**Descrição:** Interface gráfica acessível com recursos completos de usabilidade.

**Conteúdo:**
- **Views**: Telas da aplicação (ClienteView, ContaView, TransacaoView, etc.)
- **Components**: Componentes acessíveis customizados
- **Theme**: Gerenciamento de temas e aparência
- **Utils**: Utilitários e recursos de acessibilidade (som, leitura de tela)

**Dependências:**
- `sistema-core` - Para lógica de negócio
- `FlatLaf` - Look and Feel moderno

**Executar:**
```bash
# Com Maven
mvn exec:java -pl sistema-gui

# JAR standalone (recomendado)
mvn package
java -jar sistema-gui/target/sistema-bancario-gui-standalone.jar
```

---

## Estrutura de Diretórios

```
sistema-bancario-poo/
│
├── pom.xml                          # POM parent (gerencia versões e plugins)
│
├── sistema-core/                    # ⭐ Módulo Core
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/nttdata/banco/
│       │   ├── Main.java            # Entry point console
│       │   ├── model/               # Entidades de domínio
│       │   ├── service/             # Serviços de negócio
│       │   ├── repository/          # Repositórios de dados
│       │   ├── enums/               # Enumerações
│       │   └── menu/                # Interface de console
│       └── test/java/               # Testes automatizados
│
└── sistema-gui/                     # 🎨 Módulo GUI
    ├── pom.xml
    └── src/
        └── main/java/com/nttdata/banco/gui/
            ├── MainGUI.java         # Entry point GUI
            ├── view/                # Telas
            ├── components/          # Componentes customizados
            ├── theme/               # Temas e cores
            └── utils/               # Utilitários
```

## Comandos Maven Úteis

### Build Completo
```bash
# Compilar tudo
mvn clean compile

# Compilar e executar testes
mvn clean test

# Gerar JARs
mvn clean package

# Pular testes (mais rápido)
mvn clean package -DskipTests
```

### Executar Módulos Específicos
```bash
# Executar apenas o core
mvn exec:java -pl sistema-core

# Executar apenas a GUI
mvn exec:java -pl sistema-gui

# Testar apenas o core
mvn test -pl sistema-core
```

### Limpeza
```bash
# Limpar builds
mvn clean

# Limpar recursivamente
mvn clean -pl sistema-core,sistema-gui
```

## Dependências entre Módulos

```
sistema-gui
    │
    └──► sistema-core
```

O módulo `sistema-gui` **depende** do módulo `sistema-core`, pois utiliza todos os modelos, serviços e repositórios definidos lá.

## Tecnologias Utilizadas

### Sistema Core
- **Java 17** - Linguagem principal
- **JUnit 5** - Framework de testes
- **Mockito** - Mocking para testes
- **AssertJ** - Assertions fluentes
- **SLF4J + Logback** - Logging

### Sistema GUI
- **FlatLaf** - Look and Feel moderno
- **Swing** - Framework GUI
- **Java AWT** - APIs gráficas

## Padrões de Design Implementados

1. **Repository Pattern** - Abstração de acesso a dados
2. **Service Pattern** - Lógica de negócio centralizada
3. **Strategy Pattern** - Diferentes comportamentos por tipo de conta
4. **Factory Pattern** - Criação de objetos
5. **MVC Pattern** - Separação na GUI (Model-View-Controller)

## Vantagens da Estrutura Multi-Módulo

✅ **Separação de responsabilidades** - Cada módulo tem um propósito claro
✅ **Reutilização** - O core pode ser usado por outros front-ends
✅ **Manutenibilidade** - Código organizado e fácil de manter
✅ **Testabilidade** - Módulos podem ser testados independentemente
✅ **Escalabilidade** - Fácil adicionar novos módulos (API REST, Mobile, etc.)

## Roadmap

Módulos futuros que podem ser adicionados:

- 🌐 **sistema-api** - API REST para integração
- 📱 **sistema-mobile** - Aplicação móvel
- 💾 **sistema-persistence** - Persistência em banco de dados
- 📊 **sistema-reports** - Geração de relatórios em PDF/Excel
- 🔐 **sistema-security** - Autenticação e autorização

## Recursos Adicionais

- [Maven Multi-Module Projects](https://maven.apache.org/guides/mini/guide-multiple-modules.html)
- [Java 17 Documentation](https://docs.oracle.com/en/java/javase/17/)
- [FlatLaf Documentation](https://www.formdev.com/flatlaf/)

---

**Desenvolvido por:** Blindex09  
**Bootcamp:** NTT Data - Java para Iniciantes  
**Versão:** 1.0.0
