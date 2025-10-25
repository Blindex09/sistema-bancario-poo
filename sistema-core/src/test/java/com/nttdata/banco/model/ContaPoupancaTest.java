package com.nttdata.banco.model;

import com.nttdata.banco.TestRunner;
import com.nttdata.banco.enums.TipoConta;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Testes automatizados para a classe ContaPoupanca
 * Verifica rendimentos, saques e comportamentos específicos
 */
public class ContaPoupancaTest {

    public void testCriacaoContaPoupanca() {
        // Arrange
        Cliente cliente = new Cliente("Maria Santos", "98765432100", "maria@email.com", "11888888888", LocalDate.of(1985, 3, 10));
        String numero = "001002";
        String agencia = "0001";

        // Act
        ContaPoupanca conta = new ContaPoupanca(numero, agencia, cliente);

        // Assert
        TestRunner.assertEquals(numero, conta.getNumero(), "Número da conta deve ser igual");
        TestRunner.assertEquals(agencia, conta.getAgencia(), "Agência deve ser igual");
        TestRunner.assertEquals(cliente, conta.getTitular(), "Titular deve ser igual");
        TestRunner.assertEquals(TipoConta.POUPANCA, conta.getTipoConta(), "Tipo deve ser POUPANCA");
        TestRunner.assertEquals(BigDecimal.ZERO, conta.getSaldo(), "Saldo inicial deve ser zero");
        TestRunner.assertTrue(conta.isAtiva(), "Conta deve estar ativa");
        TestRunner.assertNotNull(conta.getDataAniversario(), "Data aniversário não deve ser null");
        TestRunner.assertEquals(BigDecimal.ZERO, conta.getRendimentoMensal(), "Rendimento mensal inicial deve ser zero");
    }

    public void testTaxaRendimento() {
        // Assert
        TestRunner.assertEquals(new BigDecimal("0.005"), ContaPoupanca.getTaxaRendimento(), "Taxa de rendimento deve ser 0.5%");
    }

    public void testDepositoContaPoupanca() {
        // Arrange
        ContaPoupanca conta = new ContaPoupanca();
        BigDecimal valorDeposito = new BigDecimal("1000.00");

        // Act
        conta.depositar(valorDeposito);

        // Assert
        TestRunner.assertEquals(valorDeposito, conta.getSaldo(), "Saldo após depósito deve ser igual ao valor depositado");
    }

    public void testSaqueComSaldoSuficiente() {
        // Arrange
        ContaPoupanca conta = new ContaPoupanca();
        conta.depositar(new BigDecimal("1000.00"));
        BigDecimal valorSaque = new BigDecimal("500.00");
        BigDecimal saldoEsperado = new BigDecimal("500.00");

        // Act
        boolean resultado = conta.sacar(valorSaque);

        // Assert
        TestRunner.assertTrue(resultado, "Saque deve ser bem-sucedido");
        TestRunner.assertEquals(saldoEsperado, conta.getSaldo(), "Saldo deve ser R$ 500.00");
    }

    public void testSaqueSemSaldoSuficiente() {
        // Arrange
        ContaPoupanca conta = new ContaPoupanca();
        conta.depositar(new BigDecimal("100.00"));
        BigDecimal valorSaque = new BigDecimal("500.00");
        BigDecimal saldoInicial = conta.getSaldo();

        // Act
        boolean resultado = conta.sacar(valorSaque);

        // Assert
        TestRunner.assertFalse(resultado, "Saque deve ser negado por falta de saldo");
        TestRunner.assertEquals(saldoInicial, conta.getSaldo(), "Saldo deve permanecer inalterado");
    }

    public void testSaqueValorNegativo() {
        // Arrange
        ContaPoupanca conta = new ContaPoupanca();
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
        ContaPoupanca conta = new ContaPoupanca();

        // Act
        BigDecimal tarifas = conta.calcularTarifas();

        // Assert
        TestRunner.assertEquals(BigDecimal.ZERO, tarifas, "Poupança não deve ter tarifas");
    }

    public void testAplicarRendimento() {
        // Arrange
        ContaPoupanca conta = new ContaPoupanca();
        BigDecimal saldoInicial = new BigDecimal("1000.00");
        conta.depositar(saldoInicial);
        
        BigDecimal rendimentoEsperado = saldoInicial.multiply(ContaPoupanca.getTaxaRendimento());
        BigDecimal saldoEsperado = saldoInicial.add(rendimentoEsperado);

        // Act
        conta.aplicarRendimento();

        // Assert
        TestRunner.assertEquals(saldoEsperado, conta.getSaldo(), "Saldo deve incluir rendimento");
        TestRunner.assertEquals(rendimentoEsperado, conta.getRendimentoMensal(), "Rendimento mensal deve ser calculado");
    }

    public void testAplicarRendimentoMultiplasVezes() {
        // Arrange
        ContaPoupanca conta = new ContaPoupanca();
        conta.depositar(new BigDecimal("1000.00"));
        
        // Act - Aplicar rendimento duas vezes
        conta.aplicarRendimento();
        BigDecimal rendimentoPrimeiraAplicacao = conta.getRendimentoMensal();
        conta.aplicarRendimento();

        // Assert
        TestRunner.assertTrue(conta.getRendimentoMensal().compareTo(rendimentoPrimeiraAplicacao) > 0, 
                             "Rendimento mensal deve acumular");
        TestRunner.assertTrue(conta.getSaldo().compareTo(new BigDecimal("1000.00")) > 0, 
                             "Saldo deve ser maior que o inicial");
    }

    public void testDetalhesEspecificos() {
        // Arrange
        ContaPoupanca conta = new ContaPoupanca();

        // Act
        String detalhes = conta.getDetalhesEspecificos();

        // Assert
        TestRunner.assertTrue(detalhes.contains("Data Aniversário"), "Detalhes devem conter 'Data Aniversário'");
        TestRunner.assertTrue(detalhes.contains("Rendimento"), "Detalhes devem conter 'Rendimento'");
        TestRunner.assertTrue(detalhes.contains("0,50"), "Detalhes devem conter a taxa em percentual");
        TestRunner.assertTrue(detalhes.contains("a.m."), "Detalhes devem indicar que é ao mês");
    }

    public void testDataAniversario() {
        // Arrange
        ContaPoupanca conta = new ContaPoupanca();
        LocalDate novaData = LocalDate.of(2024, 6, 15);

        // Act
        conta.setDataAniversario(novaData);

        // Assert
        TestRunner.assertEquals(novaData, conta.getDataAniversario(), "Data aniversário deve ser alterada");
    }

    public void testTransferencia() {
        // Arrange
        Cliente cliente1 = new Cliente("João", "12345678901", "joao@email.com", "11999999999", LocalDate.of(1990, 5, 15));
        Cliente cliente2 = new Cliente("Maria", "98765432100", "maria@email.com", "11888888888", LocalDate.of(1985, 3, 10));
        
        ContaPoupanca contaOrigem = new ContaPoupanca("001001", "0001", cliente1);
        ContaPoupanca contaDestino = new ContaPoupanca("001002", "0001", cliente2);
        
        contaOrigem.depositar(new BigDecimal("1000.00"));
        BigDecimal valorTransferencia = new BigDecimal("300.00");
        BigDecimal saldoEsperadoOrigem = new BigDecimal("700.00");

        // Act
        boolean resultado = contaOrigem.transferir(valorTransferencia, contaDestino);

        // Assert
        TestRunner.assertTrue(resultado, "Transferência deve ser bem-sucedida");
        TestRunner.assertEquals(valorTransferencia, contaDestino.getSaldo(), "Conta destino deve receber o valor");
        TestRunner.assertEquals(saldoEsperadoOrigem, contaOrigem.getSaldo(), "Conta origem deve ter saldo reduzido");
    }
}
