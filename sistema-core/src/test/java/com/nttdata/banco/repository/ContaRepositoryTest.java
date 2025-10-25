package com.nttdata.banco.repository;

import com.nttdata.banco.TestRunner;
import com.nttdata.banco.model.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Testes automatizados para a classe ContaRepository
 * Verifica operações CRUD e consultas de contas
 */
public class ContaRepositoryTest {

    public void testSalvarContaComNumero() {
        // Arrange
        ContaRepository repository = new ContaRepository();
        Cliente cliente = new Cliente("João Silva", "12345678901", "joao@email.com", "11999999999", LocalDate.of(1990, 5, 15));
        ContaCorrente conta = new ContaCorrente("001001", "0001", cliente);

        // Act
        Conta contaSalva = repository.salvar(conta);

        // Assert
        TestRunner.assertEquals(conta, contaSalva, "Conta salva deve ser igual à original");
        TestRunner.assertEquals(1, repository.getTotalContas(), "Deve ter 1 conta");
        TestRunner.assertTrue(repository.existe("0001", "001001"), "Conta deve existir no repositório");
    }

    public void testSalvarContaSemNumero() {
        // Arrange
        ContaRepository repository = new ContaRepository();
        Cliente cliente = new Cliente("João Silva", "12345678901", "joao@email.com", "11999999999", LocalDate.of(1990, 5, 15));
        ContaCorrente conta = new ContaCorrente();
        conta.setAgencia("0001");
        conta.setTitular(cliente);

        // Act
        Conta contaSalva = repository.salvar(conta);

        // Assert
        TestRunner.assertNotNull(contaSalva.getNumero(), "Número deve ser gerado automaticamente");
        TestRunner.assertEquals("001001", contaSalva.getNumero(), "Primeiro número gerado deve ser 001001");
        TestRunner.assertTrue(repository.existe("0001", "001001"), "Conta deve existir com número gerado");
    }

    public void testSalvarMultiplasContasComNumerosGerados() {
        // Arrange
        ContaRepository repository = new ContaRepository();
        Cliente cliente = new Cliente("João Silva", "12345678901", "joao@email.com", "11999999999", LocalDate.of(1990, 5, 15));
        
        ContaCorrente conta1 = new ContaCorrente();
        conta1.setAgencia("0001");
        conta1.setTitular(cliente);
        
        ContaPoupanca conta2 = new ContaPoupanca();
        conta2.setAgencia("0001");
        conta2.setTitular(cliente);

        // Act
        Conta conta1Salva = repository.salvar(conta1);
        Conta conta2Salva = repository.salvar(conta2);

        // Assert
        TestRunner.assertEquals("001001", conta1Salva.getNumero(), "Primeira conta deve ter número 001001");
        TestRunner.assertEquals("001002", conta2Salva.getNumero(), "Segunda conta deve ter número 001002");
        TestRunner.assertEquals(2, repository.getTotalContas(), "Deve ter 2 contas");
    }

    public void testBuscarPorNumeroExistente() {
        // Arrange
        ContaRepository repository = new ContaRepository();
        Cliente cliente = new Cliente("João Silva", "12345678901", "joao@email.com", "11999999999", LocalDate.of(1990, 5, 15));
        ContaCorrente conta = new ContaCorrente("001001", "0001", cliente);
        repository.salvar(conta);

        // Act
        Optional<Conta> resultado = repository.buscarPorNumero("0001", "001001");

        // Assert
        TestRunner.assertTrue(resultado.isPresent(), "Conta deve ser encontrada");
        TestRunner.assertEquals(conta, resultado.get(), "Conta encontrada deve ser igual à salva");
    }

    public void testBuscarPorNumeroInexistente() {
        // Arrange
        ContaRepository repository = new ContaRepository();

        // Act
        Optional<Conta> resultado = repository.buscarPorNumero("0001", "999999");

        // Assert
        TestRunner.assertFalse(resultado.isPresent(), "Conta inexistente não deve ser encontrada");
    }

    public void testBuscarPorTitular() {
        // Arrange
        ContaRepository repository = new ContaRepository();
        Cliente cliente1 = new Cliente("João Silva", "12345678901", "joao@email.com", "11999999999", LocalDate.of(1990, 5, 15));
        Cliente cliente2 = new Cliente("Maria Santos", "98765432100", "maria@email.com", "11888888888", LocalDate.of(1985, 3, 10));
        
        ContaCorrente conta1 = new ContaCorrente("001001", "0001", cliente1);
        ContaPoupanca conta2 = new ContaPoupanca("001002", "0001", cliente1);
        ContaCorrente conta3 = new ContaCorrente("001003", "0001", cliente2);
        
        repository.salvar(conta1);
        repository.salvar(conta2);
        repository.salvar(conta3);

        // Act
        List<Conta> contasCliente1 = repository.buscarPorTitular(cliente1);
        List<Conta> contasCliente2 = repository.buscarPorTitular(cliente2);

        // Assert
        TestRunner.assertEquals(2, contasCliente1.size(), "Cliente1 deve ter 2 contas");
        TestRunner.assertEquals(1, contasCliente2.size(), "Cliente2 deve ter 1 conta");
        TestRunner.assertTrue(contasCliente1.contains(conta1), "Lista deve conter conta1");
        TestRunner.assertTrue(contasCliente1.contains(conta2), "Lista deve conter conta2");
        TestRunner.assertTrue(contasCliente2.contains(conta3), "Lista deve conter conta3");
    }

    public void testBuscarPorCpf() {
        // Arrange
        ContaRepository repository = new ContaRepository();
        Cliente cliente1 = new Cliente("João Silva", "12345678901", "joao@email.com", "11999999999", LocalDate.of(1990, 5, 15));
        Cliente cliente2 = new Cliente("Maria Santos", "98765432100", "maria@email.com", "11888888888", LocalDate.of(1985, 3, 10));
        
        ContaCorrente conta1 = new ContaCorrente("001001", "0001", cliente1);
        ContaPoupanca conta2 = new ContaPoupanca("001002", "0001", cliente1);
        ContaCorrente conta3 = new ContaCorrente("001003", "0001", cliente2);
        
        repository.salvar(conta1);
        repository.salvar(conta2);
        repository.salvar(conta3);

        // Act
        List<Conta> contasCpf1 = repository.buscarPorCpf("12345678901");
        List<Conta> contasCpf2 = repository.buscarPorCpf("98765432100");

        // Assert
        TestRunner.assertEquals(2, contasCpf1.size(), "CPF 12345678901 deve ter 2 contas");
        TestRunner.assertEquals(1, contasCpf2.size(), "CPF 98765432100 deve ter 1 conta");
    }

    public void testListarTodas() {
        // Arrange
        ContaRepository repository = new ContaRepository();
        Cliente cliente = new Cliente("João Silva", "12345678901", "joao@email.com", "11999999999", LocalDate.of(1990, 5, 15));
        
        ContaCorrente conta1 = new ContaCorrente("001001", "0001", cliente);
        ContaPoupanca conta2 = new ContaPoupanca("001002", "0001", cliente);
        
        repository.salvar(conta1);
        repository.salvar(conta2);

        // Act
        List<Conta> todas = repository.listarTodas();

        // Assert
        TestRunner.assertEquals(2, todas.size(), "Deve retornar todas as contas");
        TestRunner.assertTrue(todas.contains(conta1), "Lista deve conter conta1");
        TestRunner.assertTrue(todas.contains(conta2), "Lista deve conter conta2");
    }

    public void testListarAtivas() {
        // Arrange
        ContaRepository repository = new ContaRepository();
        Cliente cliente = new Cliente("João Silva", "12345678901", "joao@email.com", "11999999999", LocalDate.of(1990, 5, 15));
        
        ContaCorrente conta1 = new ContaCorrente("001001", "0001", cliente);
        ContaPoupanca conta2 = new ContaPoupanca("001002", "0001", cliente);
        conta2.setAtiva(false); // Desativar conta2
        
        repository.salvar(conta1);
        repository.salvar(conta2);

        // Act
        List<Conta> ativas = repository.listarAtivas();

        // Assert
        TestRunner.assertEquals(1, ativas.size(), "Deve retornar apenas contas ativas");
        TestRunner.assertTrue(ativas.contains(conta1), "Lista deve conter apenas conta1");
        TestRunner.assertFalse(ativas.contains(conta2), "Lista não deve conter conta2 (inativa)");
    }

    public void testExcluirContaExistente() {
        // Arrange
        ContaRepository repository = new ContaRepository();
        Cliente cliente = new Cliente("João Silva", "12345678901", "joao@email.com", "11999999999", LocalDate.of(1990, 5, 15));
        ContaCorrente conta = new ContaCorrente("001001", "0001", cliente);
        repository.salvar(conta);

        // Act
        boolean resultado = repository.excluir("0001", "001001");

        // Assert
        TestRunner.assertTrue(resultado, "Exclusão deve ser bem-sucedida");
        TestRunner.assertEquals(0, repository.getTotalContas(), "Deve ter 0 contas após exclusão");
        TestRunner.assertFalse(repository.existe("0001", "001001"), "Conta não deve mais existir");
    }

    public void testExcluirContaInexistente() {
        // Arrange
        ContaRepository repository = new ContaRepository();

        // Act
        boolean resultado = repository.excluir("0001", "999999");

        // Assert
        TestRunner.assertFalse(resultado, "Exclusão de conta inexistente deve falhar");
    }

    public void testExiste() {
        // Arrange
        ContaRepository repository = new ContaRepository();
        Cliente cliente = new Cliente("João Silva", "12345678901", "joao@email.com", "11999999999", LocalDate.of(1990, 5, 15));
        ContaCorrente conta = new ContaCorrente("001001", "0001", cliente);
        repository.salvar(conta);

        // Act & Assert
        TestRunner.assertTrue(repository.existe("0001", "001001"), "Conta deve existir");
        TestRunner.assertFalse(repository.existe("0001", "999999"), "Conta inexistente não deve existir");
    }

    public void testGetTotalContas() {
        // Arrange
        ContaRepository repository = new ContaRepository();
        Cliente cliente = new Cliente("João Silva", "12345678901", "joao@email.com", "11999999999", LocalDate.of(1990, 5, 15));

        // Assert inicial
        TestRunner.assertEquals(0, repository.getTotalContas(), "Total inicial deve ser 0");

        // Act
        ContaCorrente conta1 = new ContaCorrente("001001", "0001", cliente);
        ContaPoupanca conta2 = new ContaPoupanca("001002", "0001", cliente);
        repository.salvar(conta1);
        repository.salvar(conta2);

        // Assert
        TestRunner.assertEquals(2, repository.getTotalContas(), "Total deve ser 2 após adicionar 2 contas");
    }

    public void testGetTotalContasAtivas() {
        // Arrange
        ContaRepository repository = new ContaRepository();
        Cliente cliente = new Cliente("João Silva", "12345678901", "joao@email.com", "11999999999", LocalDate.of(1990, 5, 15));
        
        ContaCorrente conta1 = new ContaCorrente("001001", "0001", cliente);
        ContaPoupanca conta2 = new ContaPoupanca("001002", "0001", cliente);
        conta2.setAtiva(false);
        
        repository.salvar(conta1);
        repository.salvar(conta2);

        // Act
        int totalAtivas = repository.getTotalContasAtivas();

        // Assert
        TestRunner.assertEquals(1, totalAtivas, "Deve ter 1 conta ativa");
    }

    public void testAtualizarConta() {
        // Arrange
        ContaRepository repository = new ContaRepository();
        Cliente cliente = new Cliente("João Silva", "12345678901", "joao@email.com", "11999999999", LocalDate.of(1990, 5, 15));
        ContaCorrente conta = new ContaCorrente("001001", "0001", cliente);
        repository.salvar(conta);

        // Act - depositar e salvar novamente
        conta.depositar(new BigDecimal("1000.00"));
        Conta contaAtualizada = repository.salvar(conta);

        // Assert
        TestRunner.assertEquals(new BigDecimal("1000.00"), contaAtualizada.getSaldo(), "Saldo deve estar atualizado");
        TestRunner.assertEquals(1, repository.getTotalContas(), "Deve continuar tendo 1 conta");
        
        Optional<Conta> contaBuscada = repository.buscarPorNumero("0001", "001001");
        TestRunner.assertTrue(contaBuscada.isPresent(), "Conta deve existir");
        TestRunner.assertEquals(new BigDecimal("1000.00"), contaBuscada.get().getSaldo(), "Saldo no repositório deve estar atualizado");
    }
}
