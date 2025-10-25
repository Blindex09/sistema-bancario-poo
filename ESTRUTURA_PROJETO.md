# 📁 Estrutura do Projeto - Sistema Bancário POO

## 📋 Visão Geral

Este documento descreve a estrutura do projeto após correções da configuração Maven multi-módulo.

## 🏗️ Arquitetura Multi-Módulo

O projeto utiliza Maven com estrutura multi-módulo:

```
sistema-bancario-poo/
├── pom.xml                    # POM parent (aggregator)
├── sistema-core/              # Módulo Core (console)
│   ├── pom.xml
│   └── src/
│       ├── main/java/         # Código principal
│       └── test/java/         # Testes
└── sistema-gui/               # Módulo GUI (interface gráfica)
    ├── pom.xml
    └── src/
        └── main/java/         # Código da interface
```

## 📦 Módulos

### 1. sistema-core

**Descrição**: Módulo core com toda a lógica de negócio e interface console

**Conteúdo**:
- 📝 22 arquivos Java (main)
- 🧪 10 arquivos de teste
- ✅ 136 testes automatizados (95.6% de sucesso)

**Estrutura**:
```
src/main/java/com/nttdata/banco/
├── Main.java                  # Entrada da aplicação console
├── model/                     # Modelos de domínio (POO)
│   ├── Cliente.java
│   ├── Endereco.java
│   ├── Conta.java            # Classe abstrata
│   ├── ContaCorrente.java
│   ├── ContaPoupanca.java
│   ├── ContaInvestimento.java
│   ├── Transacao.java
│   └── Investimento.java
├── enums/                     # Enumerações
│   ├── TipoConta.java
│   ├── TipoTransacao.java
│   └── TipoInvestimento.java
├── repository/                # Camada de dados
│   ├── ClienteRepository.java
│   ├── ContaRepository.java
│   └── InvestimentoRepository.java
├── service/                   # Lógica de negócio
│   └── BancoService.java
└── menu/                      # Interface console
    ├── MenuPrincipal.java
    ├── MenuCliente.java
    ├── MenuConta.java
    ├── MenuTransacao.java
    ├── MenuInvestimento.java
    └── MenuRelatorio.java
```

**Testes**:
```
src/test/java/com/nttdata/banco/
├── TestRunner.java            # Framework de testes customizado
├── model/                     # Testes de modelo
├── repository/                # Testes de repositório
└── service/                   # Testes de integração
```

### 2. sistema-gui

**Descrição**: Interface gráfica acessível com recursos WCAG 2.1

**Conteúdo**:
- 📝 14 arquivos Java
- 🎨 Temas e componentes acessíveis
- ♿ Recursos de acessibilidade

**Estrutura**:
```
src/main/java/com/nttdata/banco/gui/
├── MainGUI.java               # Entrada da aplicação GUI
├── view/                      # Telas da interface
│   ├── MainWindow.java
│   ├── ClienteView.java
│   ├── ContaView.java
│   ├── TransacaoView.java
│   ├── InvestimentoView.java
│   └── RelatorioView.java
├── components/                # Componentes acessíveis
│   ├── AccessibleButton.java
│   └── AccessibleTextField.java
├── theme/                     # Gerenciamento de temas
│   ├── ThemeManager.java
│   ├── FontManager.java
│   └── TemaConfig.java
└── utils/                     # Utilitários
    ├── AccessibilityUtils.java
    └── SoundUtils.java
```

**Dependências**:
- sistema-core (módulo principal)
- FlatLaf 3.2.5 (Look and Feel moderno)
- Logback (logging)

## 🔧 Comandos Maven

### Compilar
```bash
mvn clean compile
```

### Executar Testes
```bash
# Maven (sem resultados visíveis - testes usam framework customizado)
mvn test

# Executar TestRunner diretamente
java -cp sistema-core/target/classes:sistema-core/target/test-classes \
     com.nttdata.banco.TestRunner
```

### Executar Aplicações

**Console**:
```bash
mvn exec:java -pl sistema-core
```

**GUI**:
```bash
mvn exec:java -pl sistema-gui
```

### Gerar JAR Executável (GUI)
```bash
mvn clean package -pl sistema-gui
java -jar sistema-gui/target/sistema-bancario-gui-standalone.jar
```

## 📊 Estatísticas do Projeto

| Métrica | Valor |
|---------|-------|
| Módulos | 2 |
| Classes Java | 36 |
| Testes Automatizados | 136 |
| Taxa de Sucesso Testes | 95.6% |
| Linhas de Código (aprox.) | ~9,500 |

## ✅ Status do Build

- ✅ **Compilação**: Sucesso
- ✅ **Testes**: 130/136 passando
- ✅ **Segurança**: Sem vulnerabilidades (CodeQL)
- ✅ **Console App**: Funcionando
- ✅ **GUI App**: Funcionando

## 🎯 Conceitos POO Demonstrados

### Herança
```java
public abstract class Conta { }
public class ContaCorrente extends Conta { }
public class ContaPoupanca extends Conta { }
```

### Polimorfismo
```java
public abstract boolean sacar(BigDecimal valor);
public abstract BigDecimal calcularTarifas();
```

### Encapsulamento
```java
private BigDecimal saldo;
public BigDecimal getSaldo() { return saldo; }
protected void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
```

### Abstração
- Classes abstratas (Conta)
- Interfaces claras entre camadas
- Padrões Repository e Service

## 📝 Notas Importantes

1. **Framework de Testes Customizado**: O projeto não usa JUnit, mas um framework próprio (`TestRunner`)
2. **Dados de Demonstração**: Sistema carrega dados de exemplo automaticamente
3. **Acessibilidade**: GUI implementa recursos completos de acessibilidade (WCAG 2.1)

## 🔗 Referências

- README.md - Documentação principal
- ACESSIBILIDADE.md - Recursos de acessibilidade
- BOOTCAMP.md - Informações do bootcamp
- COMPILACAO.md - Instruções de compilação alternativas

---

**Última atualização**: 2025-10-25  
**Status**: ✅ Projeto funcionando corretamente
