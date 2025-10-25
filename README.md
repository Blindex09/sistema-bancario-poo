🏦 Sistema Bancário POO - NTT Data Bootcamp

Sistema bancário desenvolvido em Java com Programação Orientada a Objetos para o bootcamp NTT Data

📋 Descrição

Este é um sistema bancário completo desenvolvido em Java que demonstra os conceitos fundamentais da Programação Orientada a Objetos (POO), incluindo:

- ✅ Herança - Classes de conta que herdam de uma classe abstrata base
- ✅ Encapsulamento - Propriedades privadas com getters/setters
- ✅ Polimorfismo - Métodos abstratos implementados de forma específica
- ✅ Abstração - Classes abstratas e interfaces bem definidas
- ✅ Reuso de código - Padrões Repository e Service

🚀 Funcionalidades

👥 Gerenciamento de Clientes
- Cadastro de novos clientes
- Busca por CPF
- Listagem de todos os clientes

💳 Gerenciamento de Contas
- Criação de contas (Corrente, Poupança, Investimento)
- Busca de contas por agência/número
- Listagem de contas por CPF
- Consulta de saldo

💰 Transações Bancárias
- Depósito - Adicionar dinheiro à conta
- Saque - Retirar dinheiro (com validações de limite)
- Transferência - Entre contas do mesmo banco
- PIX - Transferência instantânea usando CPF como chave

📈 Sistema de Investimentos
- 6 tipos de investimento disponíveis:
  - Poupança (0.5% a.m.)
  - CDB (0.8% a.m.)
  - LCI (0.7% a.m.)
  - LCA (0.7% a.m.)
  - Tesouro Direto (0.9% a.m.)
  - Ações (1.2% a.m.)
- Simulador de investimentos
- Resgate com cálculo de rendimentos
- Portfólio consolidado

📊 Relatórios e Consultas
- Extrato detalhado de contas
- Relatório geral do banco
- Relatório de investimentos
- Posição consolidada por cliente
- Ranking de clientes por patrimônio

🎯 Conceitos de POO Demonstrados

1. Herança

public abstract class Conta {
    // Classe base abstrata
}

public class ContaCorrente extends Conta {
    // Implementação específica para conta corrente
}

public class ContaPoupanca extends Conta {
    // Implementação específica para poupança
}

2. Polimorfismo

// Método abstrato implementado de forma diferente em cada subclasse
public abstract boolean sacar(BigDecimal valor);
public abstract BigDecimal calcularTarifas();

3. Encapsulamento

private BigDecimal saldo;
private Cliente titular;

public BigDecimal getSaldo() { return saldo; }
protected void setSaldo(BigDecimal saldo) { this.saldo = saldo; }

4. Abstração
- Interface clara entre serviços e repositórios
- Separação de responsabilidades
- Métodos abstratos bem definidos

🏗️ Arquitetura do Projeto

**Estrutura Multi-Módulo Maven**

```
sistema-bancario-poo/
├── pom.xml                         # POM parent
├── sistema-core/                   # Módulo Core (Console)
│   └── src/main/java/com/nttdata/banco/
│       ├── Main.java               # Classe principal console
│       ├── model/                  # Modelos de domínio
│       │   ├── Cliente.java
│       │   ├── Endereco.java
│       │   ├── Conta.java          # Classe abstrata
│       │   ├── ContaCorrente.java  # Herança
│       │   ├── ContaPoupanca.java  # Herança
│       │   ├── ContaInvestimento.java
│       │   ├── Transacao.java      # Imutável
│       │   └── Investimento.java
│       ├── enums/                  # Enumerações
│       │   ├── TipoConta.java
│       │   ├── TipoTransacao.java
│       │   └── TipoInvestimento.java
│       ├── repository/             # Camada de dados
│       │   ├── ClienteRepository.java
│       │   ├── ContaRepository.java
│       │   └── InvestimentoRepository.java
│       ├── service/                # Lógica de negócio
│       │   └── BancoService.java
│       └── menu/                   # Interface console
│           ├── MenuPrincipal.java
│           ├── MenuCliente.java
│           ├── MenuConta.java
│           ├── MenuTransacao.java
│           ├── MenuInvestimento.java
│           └── MenuRelatorio.java
└── sistema-gui/                    # Módulo GUI (Interface Gráfica)
    └── src/main/java/com/nttdata/banco/gui/
        ├── MainGUI.java            # Classe principal GUI
        ├── view/                   # Telas da interface
        ├── components/             # Componentes acessíveis
        ├── theme/                  # Gerenciamento de temas
        └── utils/                  # Utilitários de acessibilidade
```

🔧 Como Executar

Pré-requisitos
- Java 17 ou superior
- Maven 3.6+ (para build)
- IDE de sua preferência (IntelliJ IDEA, Eclipse, VS Code)

Compilação e Execução com Maven

1. Clone o repositório:

```bash
git clone https://github.com/Blindex09/sistema-bancario-poo.git
cd sistema-bancario-poo
```

2. Compile o projeto:

```bash
mvn clean compile
```

3. Execute o sistema console:

```bash
mvn exec:java -pl sistema-core
```

4. Execute a interface gráfica:

```bash
mvn exec:java -pl sistema-gui
```

Usando IDE
1. Importe o projeto Maven na sua IDE
2. Configure o JDK 17+
3. Execute `com.nttdata.banco.Main` (console) ou `com.nttdata.banco.gui.MainGUI` (GUI)

📖 Documentação Adicional
- [ESTRUTURA_PROJETO.md](ESTRUTURA_PROJETO.md) - Estrutura detalhada do projeto Maven multi-módulo

🎮 Como Usar

Dados Pré-Carregados
O sistema já vem com dados de exemplo:
- Cliente 1: João Silva (CPF: 12345678901)
  - Conta Corrente: 0001-001001 (R$ 5.000,00)
  - Conta Poupança: 0001-001002 (R$ 10.000,00)
- Cliente 2: Maria Santos (CPF: 98765432100)
  - Conta Investimento: 0001-001003 (R$ 25.000,00)

Fluxo de Uso Recomendado
1. Explore o menu principal e familiarize-se com as opções
2. Teste as transações com as contas pré-carregadas
3. Experimente os investimentos - use a simulação primeiro
4. Consulte os relatórios para ver o resultado das operações
5. Crie novos clientes e contas para testar completamente

📝 Exemplos de Uso

Realizando um PIX

1. Acesse "Transações Bancárias" > "PIX"
2. Conta origem: Agência 0001, Conta 001001
3. Chave PIX (CPF): 98765432100
4. Valor: R$ 500,00

Investindo

1. Acesse "Investimentos" > "Realizar Investimento"
2. Conta: Agência 0001, Conta 001001
3. Tipo: Tesouro Direto (0.9% a.m.)
4. Valor: R$ 1.000,00

Consultando Posição Consolidada

1. Acesse "Relatórios" > "Posição Consolidada"
2. Digite o CPF: 12345678901
3. Veja todas as contas e investimentos do cliente

🧪 Características Técnicas

Padrões de Design Utilizados
- Repository Pattern - Abstração da camada de dados
- Service Pattern - Lógica de negócio centralizada
- Strategy Pattern - Diferentes comportamentos por tipo de conta
- Factory Pattern - Criação de contas por tipo

Boas Práticas Implementadas
- Uso de BigDecimal para valores monetários
- Imutabilidade nas classes de transação
- Validações de negócio robustas
- Tratamento de exceções
- Código limpo e bem documentado
- Nomes descritivos em português

Recursos Java Utilizados
- Java 17+ com record e switch expressions
- Collections Framework (List, Map, Stream)
- LocalDate e LocalDateTime para datas
- Enums com comportamentos
- Generics e Optional

🎓 Objetivos Pedagógicos Alcançados

✅ Herança - Demonstrada nas classes de conta
✅ Encapsulamento - Propriedades privadas com acesso controlado
✅ Polimorfismo - Métodos que se comportam diferente por tipo
✅ Abstração - Classes abstratas e interfaces bem definidas
✅ Reuso de código - Padrões e estruturas reutilizáveis
✅ Separação de responsabilidades - Arquitetura em camadas
✅ Tratamento de dados - Validações e conversões adequadas

🚀 Possíveis Melhorias

- [ ] Persistência em banco de dados
- [ ] Interface gráfica (JavaFX/Swing)
- [ ] API REST
- [ ] Testes unitários automatizados
- [ ] Logs de sistema
- [ ] Configurações externas
- [ ] Autenticação e autorização
- [ ] Relatórios em PDF

👨‍💻 Autor

Blindex09
- Bootcamp: NTT Data - Java para Iniciantes
- GitHub: @Blindex09

📄 Licença

Este projeto foi desenvolvido para fins educacionais como parte do bootcamp NTT Data.

---

⭐ Dica: Explore todas as funcionalidades do sistema! Cada menu tem surpresas e demonstra conceitos importantes de POO.

🎯 Meta: Este projeto demonstra que a Programação Orientada a Objetos não é apenas teoria - é uma ferramenta poderosa para criar sistemas robustos e organizados!