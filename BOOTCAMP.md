📚 Conceitos de POO Demonstrados - Bootcamp NTT Data

🎯 Objetivos do Projeto

Este projeto foi desenvolvido para demonstrar na prática os 4 pilares fundamentais da Programação Orientada a Objetos:

1️⃣ HERANÇA

📍 Onde está demonstrada:
- Classe abstrata Conta como base
- Classes filhas: ContaCorrente, ContaPoupanca, ContaInvestimento

💡 Como funciona:

// Classe pai (abstrata)
public abstract class Conta {
    protected BigDecimal saldo;
    protected Cliente titular;
    
    // Métodos comuns a todas as contas
    public void depositar(BigDecimal valor) { /* implementação */ }
    
    // Métodos que cada tipo implementa diferente
    public abstract boolean sacar(BigDecimal valor);
}

// Classe filha - herda comportamentos e atributos
public class ContaCorrente extends Conta {
    private BigDecimal limite; // Atributo específico
    
    @Override
    public boolean sacar(BigDecimal valor) {
        // Implementação específica com limite
    }
}

🔍 Benefícios demonstrados:
- Reuso de código: Métodos comuns ficam na classe pai
- Especialização: Cada tipo de conta tem comportamentos únicos
- Manutenibilidade: Mudanças na classe pai afetam todas as filhas

---

2️⃣ ENCAPSULAMENTO

📍 Onde está demonstrado:
- Atributos privados em todas as classes
- Métodos públicos controlam o acesso (getters/setters)
- Validações nos métodos de acesso

💡 Como funciona:

public class Cliente {
    private String nome;     // ❌ Não pode ser acessado diretamente
    private String cpf;      // ❌ Não pode ser acessado diretamente
    
    // ✅ Acesso controlado
    public String getNome() {
        return nome;
    }
    
    // ✅ Validação na entrada
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        this.nome = nome;
    }
}

🔍 Benefícios demonstrados:
- Proteção de dados: Impossível alterar diretamente
- Validação: Dados sempre consistentes
- Flexibilidade: Implementação interna pode mudar sem afetar o código que usa

---

3️⃣ POLIMORFISMO

📍 Onde está demonstrado:
- Método sacar() implementado diferente em cada tipo de conta
- Método calcularTarifas() com comportamentos específicos
- Tratamento uniforme de diferentes tipos de conta

💡 Como funciona:

// Mesmo método, comportamentos diferentes
Conta contaCorrente = new ContaCorrente();
Conta contaPoupanca = new ContaPoupanca();

// Polimorfismo em ação!
contaCorrente.sacar(100);  // ✅ Pode usar limite
contaPoupanca.sacar(100);  // ❌ Só se tiver saldo

// No serviço, tratamos todas iguais:
public boolean sacar(Conta conta, BigDecimal valor) {
    return conta.sacar(valor); // Chama a implementação correta!
}

🔍 Benefícios demonstrados:
- Interface uniforme: Mesmo método para diferentes objetos
- Comportamentos específicos: Cada classe implementa como precisa
- Extensibilidade: Novos tipos de conta sem mudar código existente

---

4️⃣ ABSTRAÇÃO

📍 Onde está demonstrada:
- Classe abstrata Conta define o "contrato"
- Interfaces claras entre camadas (Repository, Service, Menu)
- Enums com comportamentos (TipoInvestimento)

💡 Como funciona:

// Abstração da persistência
public interface ContaRepository {
    Conta salvar(Conta conta);
    Optional<Conta> buscarPorNumero(String agencia, String numero);
}

// Abstração da lógica de negócio
public class BancoService {
    public boolean transferir(String origem, String destino, BigDecimal valor) {
        // Esconde a complexidade da transferência
    }
}

// Menu só conhece o serviço, não os detalhes
public class MenuTransacao {
    private BancoService service;
    
    private void transferir() {
        // Não precisa saber COMO a transferência funciona
        boolean sucesso = service.transferir(origem, destino, valor);
    }
}

🔍 Benefícios demonstrados:
- Simplicidade: Interfaces simples escondem complexidade
- Separação de responsabilidades: Cada camada tem sua função
- Testabilidade: Fácil de testar cada parte isoladamente

---

🏗️ PADRÕES DE DESIGN UTILIZADOS

Repository Pattern

public class ContaRepository {
    // Abstrai como os dados são armazenados
    private Map<String, Conta> contas = new HashMap<>();
    
    public Conta salvar(Conta conta) { /* */ }
    public Optional<Conta> buscar(String chave) { /* */ }
}

Service Pattern

public class BancoService {
    // Concentra a lógica de negócio
    public boolean transferir(/* parâmetros */) {
        // Validações, regras de negócio, etc.
    }
}

Factory Pattern (implícito)

public Conta criarConta(TipoConta tipo, Cliente cliente) {
    return switch (tipo) {
        case CORRENTE -> new ContaCorrente(cliente);
        case POUPANCA -> new ContaPoupanca(cliente);
        case INVESTIMENTO -> new ContaInvestimento(cliente);
    };
}

---

📈 EXEMPLOS PRÁTICOS NO SISTEMA

Cenário: Saque em Diferentes Tipos de Conta

// Conta Corrente - pode usar limite
public boolean sacar(BigDecimal valor) {
    BigDecimal disponivel = saldo.add(limite);
    if (valor.compareTo(disponivel) <= 0) {
        saldo = saldo.subtract(valor.add(TARIFA_SAQUE));
        return true;
    }
    return false;
}

// Conta Poupança - só saldo disponível
public boolean sacar(BigDecimal valor) {
    if (valor.compareTo(saldo) <= 0) {
        saldo = saldo.subtract(valor);
        return true;
    }
    return false;
}

// Conta Investimento - valor mínimo
public boolean sacar(BigDecimal valor) {
    if (valor.compareTo(VALOR_MINIMO) >= 0 && valor.compareTo(saldo) <= 0) {
        saldo = saldo.subtract(valor);
        return true;
    }
    return false;
}

Resultado: O mesmo comando "sacar" se comporta diferente para cada tipo de conta!

---

🎓 LIÇÕES APRENDIDAS

✅ Vantagens da POO demonstradas:
1. Organização: Código bem estruturado e fácil de entender
2. Manutenibilidade: Mudanças isoladas em classes específicas
3. Reutilização: Código comum aproveitado por herança
4. Extensibilidade: Fácil adicionar novos tipos sem quebrar existentes
5. Testabilidade: Cada classe pode ser testada independentemente

🚀 Aplicação Real:
Esse padrão é usado em sistemas bancários reais:
- Bancos têm dezenas de tipos de conta
- Cada uma com regras específicas
- Interface uniforme para o cliente
- Facilita manutenção e evolução do sistema

---

🎯 CONCLUSÃO

Este projeto demonstra que POO não é apenas teoria - é uma ferramenta fundamental para:

- 🏗️ Construir sistemas robustos e organizados
- 🔧 Facilitar manutenção e evolução
- 🎯 Modelar problemas do mundo real
- 👥 Trabalhar em equipe com código claro

Cada linha de código neste projeto tem um propósito educacional e demonstra na prática os conceitos fundamentais da Programação Orientada a Objetos!