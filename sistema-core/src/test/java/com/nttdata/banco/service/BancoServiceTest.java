package com.nttdata.banco.service;

import com.nttdata.banco.TestRunner;
import com.nttdata.banco.enums.*;
import com.nttdata.banco.model.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Testes automatizados para a classe BancoService (Teste de Integração)
 * Verifica o funcionamento integrado de todo o sistema bancário
 */
public class BancoServiceTest {

    public void testCriarClienteComSucesso() {
        // Arrange
        BancoService bancoService = new BancoService();
        String nome = "João Teste";
        String cpf = "11111111111";
        String email = "joao.teste@email.com";
        String telefone = "11999999999";
        LocalDate dataNascimento = LocalDate.of(1990, 5, 15);

        // Act
        Cliente cliente = bancoService.criarCliente(nome, cpf, email, telefone, dataNascimento);

        // Assert
        TestRunner.assertNotNull(cliente, "Cliente não deve ser null");
        TestRunner.assertEquals(nome, cliente.getNome(), "Nome deve ser igual");
        TestRunner.assertEquals(cpf, cliente.getCpf(), "CPF deve ser igual");
        TestRunner.assertEquals(email, cliente.getEmail(), "Email deve ser igual");
    }

    public void testCriarClienteComCpfExistente() {
        // Arrange
        BancoService bancoService = new BancoService();
        String cpf = "12345678901"; // CPF já existe nos dados iniciais

        // Act & Assert
        try {
            bancoService.criarCliente("Novo João", cpf, "novo@email.com", "11888888888", LocalDate.of(1995, 3, 10));
            TestRunner.assertTrue(false, "Deve lançar exceção para CPF duplicado");
        } catch (IllegalArgumentException e) {
            TestRunner.assertTrue(e.getMessage().contains("já existe"), "Exceção deve mencionar que cliente já existe");
        }
    }

    public void testBuscarClientePorCpfExistente() {
        // Arrange
        BancoService bancoService = new BancoService();

        // Act
        Optional<Cliente> cliente = bancoService.buscarClientePorCpf("12345678901");

        // Assert
        TestRunner.assertTrue(cliente.isPresent(), "Cliente deve ser encontrado");
        TestRunner.assertEquals("João Silva", cliente.get().getNome(), "Nome deve ser João Silva");
    }

    public void testCriarContaCorrente() {
        // Arrange
        BancoService bancoService = new BancoService();
        String cpf = "12345678901";

        // Act
        Conta conta = bancoService.criarConta(cpf, TipoConta.CORRENTE);

        // Assert
        TestRunner.assertNotNull(conta, "Conta não deve ser null");
        TestRunner.assertEquals(TipoConta.CORRENTE, conta.getTipoConta(), "Tipo deve ser CORRENTE");
        TestRunner.assertEquals("0001", conta.getAgencia(), "Agência deve ser 0001");
        TestRunner.assertNotNull(conta.getNumero(), "Número da conta não deve ser null");
        TestRunner.assertEquals(cpf, conta.getTitular().getCpf(), "CPF do titular deve ser igual");
    }

    public void testCriarContaComCpfInexistente() {
        // Arrange
        BancoService bancoService = new BancoService();

        // Act & Assert
        try {
            bancoService.criarConta("99999999999", TipoConta.CORRENTE);
            TestRunner.assertTrue(false, "Deve lançar exceção para CPF inexistente");
        } catch (IllegalArgumentException e) {
            TestRunner.assertTrue(e.getMessage().contains("não encontrado"), "Exceção deve mencionar cliente não encontrado");
        }
    }

    public void testDepositarComSucesso() {
        // Arrange
        BancoService bancoService = new BancoService();
        String agencia = "0001";
        String numero = "001001";
        BigDecimal valor = new BigDecimal("500.00");

        // Act
        boolean resultado = bancoService.depositar(agencia, numero, valor);

        // Assert
        TestRunner.assertTrue(resultado, "Depósito deve ser bem-sucedido");
        
        Optional<Conta> conta = bancoService.buscarConta(agencia, numero);
        TestRunner.assertTrue(conta.isPresent(), "Conta deve existir");
        TestRunner.assertTrue(conta.get().getSaldo().compareTo(new BigDecimal("5500.00")) == 0, 
                             "Saldo deve ser R$ 5500 (5000 inicial + 500 depositado)");
    }

    public void testDepositarContaInexistente() {
        // Arrange
        BancoService bancoService = new BancoService();

        // Act
        boolean resultado = bancoService.depositar("0001", "999999", new BigDecimal("500.00"));

        // Assert
        TestRunner.assertFalse(resultado, "Depósito deve falhar para conta inexistente");
    }

    public void testSacarComSaldoSuficiente() {
        // Arrange
        BancoService bancoService = new BancoService();
        String agencia = "0001";
        String numero = "001001";
        BigDecimal valor = new BigDecimal("1000.00");

        // Act
        boolean resultado = bancoService.sacar(agencia, numero, valor);

        // Assert
        TestRunner.assertTrue(resultado, "Saque deve ser bem-sucedido");
        
        Optional<Conta> conta = bancoService.buscarConta(agencia, numero);
        TestRunner.assertTrue(conta.isPresent(), "Conta deve existir");
        // Saldo esperado: 5000 - 1000 - 2.50 (tarifa) = 3997.50
        TestRunner.assertTrue(conta.get().getSaldo().compareTo(new BigDecimal("3997.50")) == 0, 
                             "Saldo deve descontar valor e tarifa");
    }

    public void testTransferenciaComSucesso() {
        // Arrange
        BancoService bancoService = new BancoService();
        String agenciaOrigem = "0001";
        String numeroOrigem = "001001";
        String agenciaDestino = "0001";
        String numeroDestino = "001002";
        BigDecimal valor = new BigDecimal("1000.00");

        // Buscar saldos iniciais
        Optional<Conta> contaOrigemAntes = bancoService.buscarConta(agenciaOrigem, numeroOrigem);
        Optional<Conta> contaDestinoAntes = bancoService.buscarConta(agenciaDestino, numeroDestino);
        BigDecimal saldoOrigemAntes = contaOrigemAntes.get().getSaldo();
        BigDecimal saldoDestinoAntes = contaDestinoAntes.get().getSaldo();

        // Act
        boolean resultado = bancoService.transferir(agenciaOrigem, numeroOrigem, agenciaDestino, numeroDestino, valor);

        // Assert
        TestRunner.assertTrue(resultado, "Transferência deve ser bem-sucedida");
        
        Optional<Conta> contaOrigemDepois = bancoService.buscarConta(agenciaOrigem, numeroOrigem);
        Optional<Conta> contaDestinoDepois = bancoService.buscarConta(agenciaDestino, numeroDestino);
        
        TestRunner.assertTrue(contaOrigemDepois.isPresent(), "Conta origem deve existir");
        TestRunner.assertTrue(contaDestinoDepois.isPresent(), "Conta destino deve existir");
        
        // Verifica se o valor foi transferido corretamente (conta corrente tem tarifa de saque)
        BigDecimal saldoOrigemEsperado = saldoOrigemAntes.subtract(valor).subtract(new BigDecimal("2.50"));
        BigDecimal saldoDestinoEsperado = saldoDestinoAntes.add(valor);
        
        TestRunner.assertEquals(saldoOrigemEsperado, contaOrigemDepois.get().getSaldo(), "Saldo origem deve ser debitado");
        TestRunner.assertEquals(saldoDestinoEsperado, contaDestinoDepois.get().getSaldo(), "Saldo destino deve ser creditado");
    }

    public void testPixComSucesso() {
        // Arrange
        BancoService bancoService = new BancoService();
        String agenciaOrigem = "0001";
        String numeroOrigem = "001001";
        String chavePixDestino = "98765432100"; // CPF da Maria
        BigDecimal valor = new BigDecimal("250.00");

        // Act
        boolean resultado = bancoService.pix(agenciaOrigem, numeroOrigem, chavePixDestino, valor);

        // Assert
        TestRunner.assertTrue(resultado, "PIX deve ser bem-sucedido");
        
        // Verifica se as contas foram atualizadas
        Optional<Conta> contaOrigem = bancoService.buscarConta(agenciaOrigem, numeroOrigem);
        List<Conta> contasDestino = bancoService.buscarContasPorCpf(chavePixDestino);
        
        TestRunner.assertTrue(contaOrigem.isPresent(), "Conta origem deve existir");
        TestRunner.assertFalse(contasDestino.isEmpty(), "Conta destino deve existir");
    }

    public void testInvestirComSucesso() {
        // Arrange
        BancoService bancoService = new BancoService();
        String agencia = "0001";
        String numero = "001001";
        TipoInvestimento tipo = TipoInvestimento.CDB;
        BigDecimal valor = new BigDecimal("1000.00");

        // Buscar saldo inicial
        Optional<Conta> contaAntes = bancoService.buscarConta(agencia, numero);
        BigDecimal saldoAntes = contaAntes.get().getSaldo();

        // Act
        boolean resultado = bancoService.investir(agencia, numero, tipo, valor);

        // Assert
        TestRunner.assertTrue(resultado, "Investimento deve ser bem-sucedido");
        
        Optional<Conta> contaDepois = bancoService.buscarConta(agencia, numero);
        TestRunner.assertTrue(contaDepois.isPresent(), "Conta deve existir");
        
        // O saldo deve ser reduzido pelo valor investido + tarifa de saque (conta corrente)
        BigDecimal saldoEsperado = saldoAntes.subtract(valor).subtract(new BigDecimal("2.50"));
        TestRunner.assertEquals(saldoEsperado, contaDepois.get().getSaldo(), 
                               "Saldo deve ser debitado pelo valor investido + tarifa");
    }

    public void testInvestirSemSaldoSuficiente() {
        // Arrange
        BancoService bancoService = new BancoService();
        String agencia = "0001";
        String numero = "001001";
        TipoInvestimento tipo = TipoInvestimento.CDB;
        BigDecimal valor = new BigDecimal("10000.00"); // Valor maior que o saldo disponível

        // Act
        boolean resultado = bancoService.investir(agencia, numero, tipo, valor);

        // Assert
        TestRunner.assertFalse(resultado, "Investimento deve falhar por falta de saldo");
    }

    public void testBuscarContasPorCpf() {
        // Arrange
        BancoService bancoService = new BancoService();
        String cpf = "12345678901"; // João Silva tem 2 contas pré-carregadas

        // Act
        List<Conta> contas = bancoService.buscarContasPorCpf(cpf);

        // Assert
        TestRunner.assertEquals(2, contas.size(), "João deve ter 2 contas");
        TestRunner.assertTrue(contas.stream().anyMatch(c -> c.getTipoConta() == TipoConta.CORRENTE), 
                             "Deve ter uma conta corrente");
        TestRunner.assertTrue(contas.stream().anyMatch(c -> c.getTipoConta() == TipoConta.POUPANCA), 
                             "Deve ter uma conta poupança");
    }

    public void testListarClientes() {
        // Arrange
        BancoService bancoService = new BancoService();

        // Act
        List<Cliente> clientes = bancoService.listarClientes();

        // Assert
        TestRunner.assertTrue(clientes.size() >= 2, "Deve ter pelo menos 2 clientes pré-carregados");
        TestRunner.assertTrue(clientes.stream().anyMatch(c -> c.getNome().equals("João Silva")), 
                             "Deve conter João Silva");
        TestRunner.assertTrue(clientes.stream().anyMatch(c -> c.getNome().equals("Maria Santos")), 
                             "Deve conter Maria Santos");
    }

    public void testListarContas() {
        // Arrange
        BancoService bancoService = new BancoService();

        // Act
        List<Conta> contas = bancoService.listarContas();

        // Assert
        TestRunner.assertTrue(contas.size() >= 3, "Deve ter pelo menos 3 contas pré-carregadas");
        TestRunner.assertTrue(contas.stream().anyMatch(c -> c.getTipoConta() == TipoConta.CORRENTE), 
                             "Deve ter conta corrente");
        TestRunner.assertTrue(contas.stream().anyMatch(c -> c.getTipoConta() == TipoConta.POUPANCA), 
                             "Deve ter conta poupança");
        TestRunner.assertTrue(contas.stream().anyMatch(c -> c.getTipoConta() == TipoConta.INVESTIMENTO), 
                             "Deve ter conta investimento");
    }

    public void testFluxoCompletoClienteContaTransacoes() {
        // Arrange
        BancoService bancoService = new BancoService();
        
        // Criar novo cliente
        Cliente cliente = bancoService.criarCliente("Pedro Testador", "33333333333", 
                                                   "pedro@test.com", "11777777777", 
                                                   LocalDate.of(1985, 8, 15));

        // Act & Assert - Fluxo completo
        
        // 1. Criar conta corrente
        Conta conta = bancoService.criarConta(cliente.getCpf(), TipoConta.CORRENTE);
        TestRunner.assertNotNull(conta, "Conta deve ser criada");
        TestRunner.assertEquals(TipoConta.CORRENTE, conta.getTipoConta(), "Deve ser conta corrente");

        // 2. Fazer depósito
        boolean depositoOk = bancoService.depositar(conta.getAgencia(), conta.getNumero(), new BigDecimal("2000.00"));
        TestRunner.assertTrue(depositoOk, "Depósito deve ser bem-sucedido");

        // 3. Verificar saldo
        Optional<Conta> contaAtualizada = bancoService.buscarConta(conta.getAgencia(), conta.getNumero());
        TestRunner.assertTrue(contaAtualizada.isPresent(), "Conta deve existir");
        TestRunner.assertEquals(new BigDecimal("2000.00"), contaAtualizada.get().getSaldo(), 
                               "Saldo deve ser R$ 2000");

        // 4. Fazer saque
        boolean saqueOk = bancoService.sacar(conta.getAgencia(), conta.getNumero(), new BigDecimal("500.00"));
        TestRunner.assertTrue(saqueOk, "Saque deve ser bem-sucedido");

        // 5. Verificar saldo final
        Optional<Conta> contaFinal = bancoService.buscarConta(conta.getAgencia(), conta.getNumero());
        TestRunner.assertTrue(contaFinal.isPresent(), "Conta deve existir");
        // Saldo esperado: 2000 - 500 - 2.50 (tarifa) = 1497.50
        TestRunner.assertEquals(new BigDecimal("1497.50"), contaFinal.get().getSaldo(), 
                               "Saldo final deve descontar valor e tarifa");

        // 6. Verificar histórico de transações
        TestRunner.assertTrue(contaFinal.get().getHistorico().size() >= 2, 
                             "Deve ter pelo menos 2 transações no histórico");
    }

    public void testIntegracaoCompletaSistema() {
        // Arrange
        BancoService bancoService = new BancoService();

        // Act & Assert - Teste de integração completa
        
        // Verificar dados iniciais
        List<Cliente> clientes = bancoService.listarClientes();
        List<Conta> contas = bancoService.listarContas();
        
        TestRunner.assertTrue(clientes.size() >= 2, "Sistema deve ter clientes pré-carregados");
        TestRunner.assertTrue(contas.size() >= 3, "Sistema deve ter contas pré-carregadas");

        // Verificar se João Silva tem suas contas
        Optional<Cliente> joao = bancoService.buscarClientePorCpf("12345678901");
        TestRunner.assertTrue(joao.isPresent(), "João Silva deve existir");
        
        List<Conta> contasJoao = bancoService.buscarContasPorCpf("12345678901");
        TestRunner.assertEquals(2, contasJoao.size(), "João deve ter 2 contas");

        // Verificar se Maria Santos tem sua conta
        Optional<Cliente> maria = bancoService.buscarClientePorCpf("98765432100");
        TestRunner.assertTrue(maria.isPresent(), "Maria Santos deve existir");
        
        List<Conta> contasMaria = bancoService.buscarContasPorCpf("98765432100");
        TestRunner.assertEquals(1, contasMaria.size(), "Maria deve ter 1 conta");

        // Verificar saldos iniciais
        Optional<Conta> contaCorrenteJoao = bancoService.buscarConta("0001", "001001");
        Optional<Conta> contaPoupancaJoao = bancoService.buscarConta("0001", "001002");
        Optional<Conta> contaInvestimentoMaria = bancoService.buscarConta("0001", "001003");

        TestRunner.assertTrue(contaCorrenteJoao.isPresent(), "Conta corrente do João deve existir");
        TestRunner.assertTrue(contaPoupancaJoao.isPresent(), "Conta poupança do João deve existir");
        TestRunner.assertTrue(contaInvestimentoMaria.isPresent(), "Conta investimento da Maria deve existir");

        TestRunner.assertEquals(new BigDecimal("5000.00"), contaCorrenteJoao.get().getSaldo(), 
                               "Saldo inicial conta corrente deve ser R$ 5000");
        TestRunner.assertEquals(new BigDecimal("10000.00"), contaPoupancaJoao.get().getSaldo(), 
                               "Saldo inicial conta poupança deve ser R$ 10000");
        TestRunner.assertEquals(new BigDecimal("25000.00"), contaInvestimentoMaria.get().getSaldo(), 
                               "Saldo inicial conta investimento deve ser R$ 25000");
    }
}
