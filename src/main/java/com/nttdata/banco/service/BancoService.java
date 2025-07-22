package com.nttdata.banco.service;

import com.nttdata.banco.enums.TipoConta;
import com.nttdata.banco.enums.TipoInvestimento;
import com.nttdata.banco.enums.TipoTransacao;
import com.nttdata.banco.model.*;
import com.nttdata.banco.repository.ClienteRepository;
import com.nttdata.banco.repository.ContaRepository;
import com.nttdata.banco.repository.InvestimentoRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Serviço principal do banco que coordena todas as operações
 * Demonstra reuso de código e separação de responsabilidades
 */
public class BancoService {
    private final ClienteRepository clienteRepository;
    private final ContaRepository contaRepository;
    private final InvestimentoRepository investimentoRepository;
    private static final String AGENCIA_PADRAO = "0001";

    public BancoService() {
        this.clienteRepository = new ClienteRepository();
        this.contaRepository = new ContaRepository();
        this.investimentoRepository = new InvestimentoRepository();
        carregarDadosIniciais();
    }

    // ================== OPERAÇÕES DE CLIENTE ==================
    
    public Cliente criarCliente(String nome, String cpf, String email, String telefone, LocalDate dataNascimento) {
        if (clienteRepository.existe(cpf)) {
            throw new IllegalArgumentException("Cliente já existe com este CPF");
        }
        
        Cliente cliente = new Cliente(nome, cpf, email, telefone, dataNascimento);
        return clienteRepository.salvar(cliente);
    }

    public Optional<Cliente> buscarClientePorCpf(String cpf) {
        return clienteRepository.buscarPorCpf(cpf);
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.listarTodos();
    }

    // ================== OPERAÇÕES DE CONTA ==================
    
    public Conta criarConta(String cpfTitular, TipoConta tipoConta) {
        Optional<Cliente> clienteOpt = clienteRepository.buscarPorCpf(cpfTitular);
        if (clienteOpt.isEmpty()) {
            throw new IllegalArgumentException("Cliente não encontrado");
        }
        
        Cliente cliente = clienteOpt.get();
        Conta conta = criarContaPorTipo(tipoConta, cliente);
        
        return contaRepository.salvar(conta);
    }

    private Conta criarContaPorTipo(TipoConta tipoConta, Cliente cliente) {
        return switch (tipoConta) {
            case CORRENTE -> new ContaCorrente(null, AGENCIA_PADRAO, cliente);
            case POUPANCA -> new ContaPoupanca(null, AGENCIA_PADRAO, cliente);
            case INVESTIMENTO -> new ContaInvestimento(null, AGENCIA_PADRAO, cliente);
        };
    }

    public Optional<Conta> buscarConta(String agencia, String numero) {
        return contaRepository.buscarPorNumero(agencia, numero);
    }

    public List<Conta> buscarContasPorCpf(String cpf) {
        return contaRepository.buscarPorCpf(cpf);
    }

    public List<Conta> listarContas() {
        return contaRepository.listarTodas();
    }

    // ================== OPERAÇÕES BANCÁRIAS ==================
    
    public boolean depositar(String agencia, String numero, BigDecimal valor) {
        Optional<Conta> contaOpt = buscarConta(agencia, numero);
        if (contaOpt.isEmpty()) {
            return false;
        }
        
        Conta conta = contaOpt.get();
        try {
            conta.depositar(valor);
            
            Transacao transacao = new Transacao(
                TipoTransacao.DEPOSITO, 
                valor, 
                "Depósito em conta", 
                agencia + "-" + numero, 
                true
            );
            conta.adicionarTransacao(transacao);
            
            contaRepository.salvar(conta);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean sacar(String agencia, String numero, BigDecimal valor) {
        Optional<Conta> contaOpt = buscarConta(agencia, numero);
        if (contaOpt.isEmpty()) {
            return false;
        }
        
        Conta conta = contaOpt.get();
        boolean sucesso = conta.sacar(valor);
        
        Transacao transacao = new Transacao(
            TipoTransacao.SAQUE, 
            valor, 
            "Saque em conta", 
            agencia + "-" + numero, 
            sucesso
        );
        conta.adicionarTransacao(transacao);
        
        contaRepository.salvar(conta);
        return sucesso;
    }

    public boolean transferir(String agenciaOrigem, String numeroOrigem, 
                             String agenciaDestino, String numeroDestino, 
                             BigDecimal valor) {
        Optional<Conta> contaOrigemOpt = buscarConta(agenciaOrigem, numeroOrigem);
        Optional<Conta> contaDestinoOpt = buscarConta(agenciaDestino, numeroDestino);
        
        if (contaOrigemOpt.isEmpty() || contaDestinoOpt.isEmpty()) {
            return false;
        }
        
        Conta contaOrigem = contaOrigemOpt.get();
        Conta contaDestino = contaDestinoOpt.get();
        
        boolean sucesso = contaOrigem.transferir(valor, contaDestino);
        
        // Registra transação na conta origem
        Transacao transacaoOrigem = new Transacao(
            TipoTransacao.TRANSFERENCIA, 
            valor, 
            "Transferência enviada", 
            agenciaOrigem + "-" + numeroOrigem,
            agenciaDestino + "-" + numeroDestino,
            sucesso
        );
        contaOrigem.adicionarTransacao(transacaoOrigem);
        
        if (sucesso) {
            // Registra transação na conta destino
            Transacao transacaoDestino = new Transacao(
                TipoTransacao.TRANSFERENCIA, 
                valor, 
                "Transferência recebida", 
                agenciaOrigem + "-" + numeroOrigem,
                agenciaDestino + "-" + numeroDestino,
                true
            );
            contaDestino.adicionarTransacao(transacaoDestino);
            
            contaRepository.salvar(contaDestino);
        }
        
        contaRepository.salvar(contaOrigem);
        return sucesso;
    }

    public boolean pix(String agenciaOrigem, String numeroOrigem, 
                      String chavePixDestino, BigDecimal valor) {
        // Simulação do PIX - busca conta por "chave PIX" (usando CPF)
        Optional<Conta> contaOrigemOpt = buscarConta(agenciaOrigem, numeroOrigem);
        if (contaOrigemOpt.isEmpty()) {
            return false;
        }
        
        // Busca conta destino pelo CPF (simulando chave PIX)
        List<Conta> contasDestino = buscarContasPorCpf(chavePixDestino);
        if (contasDestino.isEmpty()) {
            return false;
        }
        
        Conta contaOrigem = contaOrigemOpt.get();
        Conta contaDestino = contasDestino.get(0); // Pega a primeira conta encontrada
        
        boolean sucesso = contaOrigem.transferir(valor, contaDestino);
        
        // Registra PIX na conta origem
        Transacao pixOrigem = new Transacao(
            TipoTransacao.PIX, 
            valor, 
            "PIX enviado para " + chavePixDestino, 
            agenciaOrigem + "-" + numeroOrigem,
            contaDestino.getAgencia() + "-" + contaDestino.getNumero(),
            sucesso
        );
        contaOrigem.adicionarTransacao(pixOrigem);
        
        if (sucesso) {
            // Registra PIX na conta destino
            Transacao pixDestino = new Transacao(
                TipoTransacao.PIX, 
                valor, 
                "PIX recebido de " + contaOrigem.getTitular().getNome(), 
                agenciaOrigem + "-" + numeroOrigem,
                contaDestino.getAgencia() + "-" + contaDestino.getNumero(),
                true
            );
            contaDestino.adicionarTransacao(pixDestino);
            
            contaRepository.salvar(contaDestino);
        }
        
        contaRepository.salvar(contaOrigem);
        return sucesso;
    }

    // ================== OPERAÇÕES DE INVESTIMENTO ==================
    
    public boolean investir(String agencia, String numero, TipoInvestimento tipo, BigDecimal valor) {
        Optional<Conta> contaOpt = buscarConta(agencia, numero);
        if (contaOpt.isEmpty()) {
            return false;
        }
        
        Conta conta = contaOpt.get();
        
        // Verifica se tem saldo suficiente
        if (conta.getSaldo().compareTo(valor) < 0) {
            return false;
        }
        
        // Debita da conta
        if (!conta.sacar(valor)) {
            return false;
        }
        
        // Cria o investimento
        Investimento investimento = new Investimento(tipo, valor, conta.getTitular().getCpf());
        investimentoRepository.salvar(investimento);
        
        // Adiciona à conta de investimento se for do tipo
        if (conta instanceof ContaInvestimento contaInv) {
            contaInv.adicionarInvestimento(investimento);
        }
        
        // Registra transação
        Transacao transacao = new Transacao(
            TipoTransacao.INVESTIMENTO, 
            valor, 
            "Investimento em " + tipo.getNome(), 
            agencia + "-" + numero, 
            true
        );
        conta.adicionarTransacao(transacao);
        
        contaRepository.salvar(conta);
        return true;
    }

    public boolean resgatarInvestimento(String investimentoId, String agencia, String numero) {
        Optional<Investimento> investimentoOpt = investimentoRepository.buscarPorId(investimentoId);
        Optional<Conta> contaOpt = buscarConta(agencia, numero);
        
        if (investimentoOpt.isEmpty() || contaOpt.isEmpty()) {
            return false;
        }
        
        Investimento investimento = investimentoOpt.get();
        Conta conta = contaOpt.get();
        
        if (!investimento.podeResgatar()) {
            return false;
        }
        
        BigDecimal valorResgate = investimento.resgatar();
        conta.depositar(valorResgate);
        
        // Remove da conta de investimento se for do tipo
        if (conta instanceof ContaInvestimento contaInv) {
            contaInv.removerInvestimento(investimento);
        }
        
        // Registra transação
        Transacao transacao = new Transacao(
            TipoTransacao.RESGATE_INVESTIMENTO, 
            valorResgate, 
            "Resgate de investimento " + investimento.getTipo().getNome(), 
            agencia + "-" + numero, 
            true
        );
        conta.adicionarTransacao(transacao);
        
        investimentoRepository.salvar(investimento);
        contaRepository.salvar(conta);
        return true;
    }

    public List<Investimento> listarInvestimentosPorCpf(String cpf) {
        return investimentoRepository.buscarPorTitular(cpf);
    }

    public List<Investimento> listarTodosInvestimentos() {
        return investimentoRepository.listarTodos();
    }

    // ================== RELATÓRIOS E CONSULTAS ==================
    
    public void exibirExtrato(String agencia, String numero) {
        Optional<Conta> contaOpt = buscarConta(agencia, numero);
        if (contaOpt.isEmpty()) {
            System.out.println("Conta não encontrada!");
            return;
        }
        
        Conta conta = contaOpt.get();
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                    EXTRATO BANCÁRIO");
        System.out.println("=".repeat(60));
        System.out.println(conta);
        System.out.println("Titular: " + conta.getTitular().getNome());
        System.out.println("=".repeat(60));
        
        List<Transacao> historico = conta.getHistorico();
        if (historico.isEmpty()) {
            System.out.println("Nenhuma transação encontrada.");
        } else {
            System.out.println("HISTÓRICO DE TRANSAÇÕES:");
            System.out.println("-".repeat(60));
            
            historico.stream()
                    .sorted((t1, t2) -> t2.getDataHora().compareTo(t1.getDataHora()))
                    .limit(10)
                    .forEach(transacao -> {
                        System.out.println(transacao);
                        System.out.println("-".repeat(60));
                    });
        }
        System.out.println("=".repeat(60));
    }

    // ================== DADOS INICIAIS ==================
    
    private void carregarDadosIniciais() {
        // Cria clientes de exemplo
        Cliente cliente1 = criarCliente("João Silva", "12345678901", "joao@email.com", "11999999999", 
                                      LocalDate.of(1990, 5, 15));
        Cliente cliente2 = criarCliente("Maria Santos", "98765432100", "maria@email.com", "11888888888", 
                                      LocalDate.of(1985, 10, 20));
        
        // Cria contas de exemplo
        Conta contaCorrente1 = criarConta("12345678901", TipoConta.CORRENTE);
        Conta contaPoupanca1 = criarConta("12345678901", TipoConta.POUPANCA);
        Conta contaInvestimento1 = criarConta("98765432100", TipoConta.INVESTIMENTO);
        
        // Adiciona saldo inicial
        depositar(contaCorrente1.getAgencia(), contaCorrente1.getNumero(), new BigDecimal("5000.00"));
        depositar(contaPoupanca1.getAgencia(), contaPoupanca1.getNumero(), new BigDecimal("10000.00"));
        depositar(contaInvestimento1.getAgencia(), contaInvestimento1.getNumero(), new BigDecimal("25000.00"));
        
        System.out.println("📋 Dados iniciais carregados com sucesso!");
        System.out.println("💳 Contas criadas para demonstração:");
        System.out.println("   - Ag: 0001, Conta: " + contaCorrente1.getNumero() + " (Corrente - João)");
        System.out.println("   - Ag: 0001, Conta: " + contaPoupanca1.getNumero() + " (Poupança - João)");
        System.out.println("   - Ag: 0001, Conta: " + contaInvestimento1.getNumero() + " (Investimento - Maria)");
    }

    // ================== GETTERS ==================
    
    public ClienteRepository getClienteRepository() {
        return clienteRepository;
    }

    public ContaRepository getContaRepository() {
        return contaRepository;
    }

    public InvestimentoRepository getInvestimentoRepository() {
        return investimentoRepository;
    }
}