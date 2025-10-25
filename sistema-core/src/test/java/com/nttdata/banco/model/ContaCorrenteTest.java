package com.nttdata.banco.model;

import com.nttdata.banco.TestRunner;
import com.nttdata.banco.enums.TipoConta;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Testes automatizados para a classe ContaCorrente
 * Verifica herança, polimorfismo e comportamentos específicos
 */
public class ContaCorrenteTest {

    public void testCriacaoContaCorrente() {
        // Arrange
        Cliente cliente = new Cliente("João Silva", "12345678901", "joao@email.com", "11999999999", LocalDate.of(1990, 5, 15));
        String numero = "001001";
        String agencia = "0001";

        // Act
        ContaCorrente conta = new ContaCorrente(numero, agencia, cliente);

        // Assert
        TestRunner.assertEquals(numero, conta.getNumero(), "Número da conta deve ser igual");
        TestRunner.assertEquals(agencia, conta.getAgencia(), "Agência deve ser igual");
        TestRunner.assertEquals(cliente, conta.getTitular(), "Titular deve ser igual");
        TestRunner.assertEquals(TipoConta.CORRENTE, conta.getTipoConta(), "Tipo deve ser CORRENTE");
        TestRunner.assertEquals(BigDecimal.ZERO, conta.getSaldo(), "Saldo inicial deve ser zero");
        TestRunner.assertTrue(conta.isAtiva(), "Conta deve estar ativa");
        TestRunner.assertNotNull(conta.getDataAbertura(), "Data de abertura não deve ser null");
    }

    public void testLimiteEtarifas() {
        // Arrange
        ContaCorrente conta = new ContaCorrente();

        // Assert
        TestRunner.assertEquals(new BigDecimal("1000.00"), conta.getLimite(), "Limite deve ser R$ 1000");
        TestRunner.assertEquals(new BigDecimal("15.00"), conta.getTarifaMensal(), "Tarifa mensal deve ser R$ 15");
        TestRunner.assertEquals(new BigDecimal("2.50"), ContaCorrente.getTarifaSaque(), "Tarifa saque deve ser R$ 2.50");
        TestRunner.assertEquals(new BigDecimal("1.50"), ContaCorrente.getTarifaTransferencia(), "Tarifa transferência deve ser R$ 1.50");
    }

    public void testDepositoContaCorrente() {
        // Arrange
        ContaCorrente conta = new ContaCorrente();
        BigDecimal valorDeposito = new BigDecimal("1000.00");

        // Act
        conta.depositar(valorDeposito);

        // Assert
        TestRunner.assertEquals(valorDeposito, conta.getSaldo(), "Saldo após depósito deve ser igual ao valor depositado");
    }

    public void testSaqueComSaldoSuficiente() {
        // Arrange
        ContaCorrente conta = new ContaCorrente();
        conta.depositar(new BigDecimal("1000.00"));
        BigDecimal valorSaque = new BigDecimal("500.00");
        BigDecimal tarifaSaque = ContaCorrente.getTarifaSaque();
        BigDecimal saldoEsperado = new BigDecimal("1000.00").subtract(valorSaque).subtract(tarifaSaque);

        // Act
        boolean resultado = conta.sacar(valorSaque);

        // Assert
        TestRunner.assertTrue(resultado, "Saque deve ser bem-sucedido");
        TestRunner.assertEquals(saldoEsperado, conta.getSaldo(), "Saldo deve descontar valor + tarifa");
    }

    public void testSaqueComLimite() {
        // Arrange
        ContaCorrente conta = new ContaCorrente();
        conta.depositar(new BigDecimal("100.00"));
        BigDecimal valorSaque = new BigDecimal("500.00");
        BigDecimal tarifaSaque = ContaCorrente.getTarifaSaque();
        
        // Act
        boolean resultado = conta.sacar(valorSaque);

        // Assert
        TestRunner.assertTrue(resultado, "Saque deve ser bem-sucedido usando limite");
        
        // Saldo esperado: 100 - 500 - 2.50 = -402.50
        BigDecimal saldoEsperado = new BigDecimal("100.00").subtract(valorSaque).subtract(tarifaSaque);
        TestRunner.assertEquals(saldoEsperado, conta.getSaldo(), "Saldo deve ficar negativo usando limite");
    }

    public void testSaqueExcedendoLimite() {
        // Arrange
        ContaCorrente conta = new ContaCorrente();
        conta.depositar(new BigDecimal("100.00"));
        BigDecimal valorSaque = new BigDecimal("1200.00"); // Excede saldo + limite + tarifa
        BigDecimal saldoInicial = conta.getSaldo();

        // Act
        boolean resultado = conta.sacar(valorSaque);

        // Assert
        TestRunner.assertFalse(resultado, "Saque deve ser negado");
        TestRunner.assertEquals(saldoInicial, conta.getSaldo(), "Saldo deve permanecer inalterado");
    }

    public void testSaqueValorNegativo() {
        // Arrange
        ContaCorrente conta = new ContaCorrente();
        conta.depositar(new BigDecimal("1000.00"));
        BigDecimal valorSaque = new BigDecimal("-100.00");
        BigDecimal saldoInicial = conta.getSaldo();

        // Act
        boolean resultado = conta.sacar(valorSaque);

        // Assert
        TestRunner.assertFalse(resultado, "Saque com valor negativo deve ser negado");
        TestRunner.assertEquals(saldoInicial, conta.getSaldo(), "Saldo deve permanecer inalterado");
    }

    public void testCalcularTarifas() {
        // Arrange
        ContaCorrente conta = new ContaCorrente();
        BigDecimal tarifaEsperada = new BigDecimal("15.00") // Tarifa mensal
                .add(new BigDecimal("2.50"))  // Tarifa saque
                .add(new BigDecimal("1.50")); // Tarifa transferência

        // Act
        BigDecimal tarifaCalculada = conta.calcularTarifas();

        // Assert
        TestRunner.assertEquals(tarifaEsperada, tarifaCalculada, "Tarifa total deve somar todas as tarifas");
    }

    public void testDetalhesEspecificos() {
        // Arrange
        ContaCorrente conta = new ContaCorrente();

        // Act
        String detalhes = conta.getDetalhesEspecificos();

        // Assert
        TestRunner.assertTrue(detalhes.contains("1000,00"), "Detalhes devem conter o limite");
        TestRunner.assertTrue(detalhes.contains("15,00"), "Detalhes devem conter a tarifa mensal");
        TestRunner.assertTrue(detalhes.contains("Limite"), "Detalhes devem mencionar 'Limite'");
        TestRunner.assertTrue(detalhes.contains("Tarifa"), "Detalhes devem mencionar 'Tarifa'");
    }

    public void testTransferencia() {
        // Arrange
        Cliente cliente1 = new Cliente("João", "12345678901", "joao@email.com", "11999999999", LocalDate.of(1990, 5, 15));
        Cliente cliente2 = new Cliente("Maria", "98765432100", "maria@email.com", "11888888888", LocalDate.of(1985, 3, 10));
        
        ContaCorrente contaOrigem = new ContaCorrente("001001", "0001", cliente1);
        ContaCorrente contaDestino = new ContaCorrente("001002", "0001", cliente2);
        
        contaOrigem.depositar(new BigDecimal("1000.00"));
        BigDecimal valorTransferencia = new BigDecimal("500.00");

        // Act
        boolean resultado = contaOrigem.transferir(valorTransferencia, contaDestino);

        // Assert
        TestRunner.assertTrue(resultado, "Transferência deve ser bem-sucedida");
        TestRunner.assertEquals(valorTransferencia, contaDestino.getSaldo(), "Conta destino deve receber o valor");
    }
}
