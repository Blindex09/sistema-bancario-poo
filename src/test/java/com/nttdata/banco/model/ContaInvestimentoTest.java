package com.nttdata.banco.model;

import com.nttdata.banco.TestRunner;
import com.nttdata.banco.enums.TipoConta;
import com.nttdata.banco.enums.TipoInvestimento;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Testes automatizados para a classe ContaInvestimento
 * Verifica gestão de investimentos e cálculos específicos
 */
public class ContaInvestimentoTest {

    public void testCriacaoContaInvestimento() {
        // Arrange
        Cliente cliente = new Cliente("Pedro Investidor", "11122233344", "pedro@email.com", "11777777777", LocalDate.of(1980, 8, 25));
        String numero = "001003";
        String agencia = "0001";

        // Act
        ContaInvestimento conta = new ContaInvestimento(numero, agencia, cliente);

        // Assert
        TestRunner.assertEquals(numero, conta.getNumero(), "Número da conta deve ser igual");
        TestRunner.assertEquals(agencia, conta.getAgencia(), "Agência deve ser igual");
        TestRunner.assertEquals(cliente, conta.getTitular(), "Titular deve ser igual");
        TestRunner.assertEquals(TipoConta.INVESTIMENTO, conta.getTipoConta(), "Tipo deve ser INVESTIMENTO");
        TestRunner.assertEquals(BigDecimal.ZERO, conta.getSaldo(), "Saldo inicial deve ser zero");
        TestRunner.assertTrue(conta.isAtiva(), "Conta deve estar ativa");
        TestRunner.assertEquals(0, conta.getInvestimentos().size(), "Lista de investimentos deve estar vazia");
        TestRunner.assertEquals(BigDecimal.ZERO, conta.getValorTotalInvestido(), "Valor total investido deve ser zero");
    }

    public void testValorMinimoSaque() {
        // Assert
        TestRunner.assertEquals(new BigDecimal("100.00"), ContaInvestimento.getValorMinimoSaque(), "Valor mínimo saque deve ser R$ 100");
    }

    public void testDepositoContaInvestimento() {
        // Arrange
        ContaInvestimento conta = new ContaInvestimento();
        BigDecimal valorDeposito = new BigDecimal("5000.00");

        // Act
        conta.depositar(valorDeposito);

        // Assert
        TestRunner.assertEquals(valorDeposito, conta.getSaldo(), "Saldo após depósito deve ser igual ao valor depositado");
    }

    public void testSaqueComSaldoSuficiente() {
        // Arrange
        ContaInvestimento conta = new ContaInvestimento();
        conta.depositar(new BigDecimal("5000.00"));
        BigDecimal valorSaque = new BigDecimal("1000.00");
        BigDecimal saldoEsperado = new BigDecimal("4000.00");

        // Act
        boolean resultado = conta.sacar(valorSaque);

        // Assert
        TestRunner.assertTrue(resultado, "Saque deve ser bem-sucedido");
        TestRunner.assertEquals(saldoEsperado, conta.getSaldo(), "Saldo deve ser R$ 4000.00");
    }

    public void testSaqueMenorQueMinimo() {
        // Arrange
        ContaInvestimento conta = new ContaInvestimento();
        conta.depositar(new BigDecimal("5000.00"));
        BigDecimal valorSaque = new BigDecimal("50.00"); // Menor que o mínimo
        BigDecimal saldoInicial = conta.getSaldo();

        // Act
        boolean resultado = conta.sacar(valorSaque);

        // Assert
        TestRunner.assertFalse(resultado, "Saque deve ser negado por ser menor que o mínimo");
        TestRunner.assertEquals(saldoInicial, conta.getSaldo(), "Saldo deve permanecer inalterado");
    }

    public void testSaqueSemSaldoSuficiente() {
        // Arrange
        ContaInvestimento conta = new ContaInvestimento();
        conta.depositar(new BigDecimal("500.00"));
        BigDecimal valorSaque = new BigDecimal("1000.00");
        BigDecimal saldoInicial = conta.getSaldo();

        // Act
        boolean resultado = conta.sacar(valorSaque);

        // Assert
        TestRunner.assertFalse(resultado, "Saque deve ser negado por falta de saldo");
        TestRunner.assertEquals(saldoInicial, conta.getSaldo(), "Saldo deve permanecer inalterado");
    }

    public void testAdicionarInvestimento() {
        // Arrange
        ContaInvestimento conta = new ContaInvestimento();
        Investimento investimento = new Investimento(TipoInvestimento.CDB, new BigDecimal("1000.00"), "Pedro Investidor");

        // Act
        conta.adicionarInvestimento(investimento);

        // Assert
        TestRunner.assertEquals(1, conta.getInvestimentos().size(), "Deve ter 1 investimento");
        TestRunner.assertEquals(new BigDecimal("1000.00"), conta.getValorTotalInvestido(), "Valor total investido deve ser R$ 1000");
        TestRunner.assertTrue(conta.getInvestimentos().contains(investimento), "Lista deve conter o investimento");
    }

    public void testAdicionarMultiplosInvestimentos() {
        // Arrange
        ContaInvestimento conta = new ContaInvestimento();
        Investimento investimento1 = new Investimento(TipoInvestimento.CDB, new BigDecimal("1000.00"), "Pedro");
        Investimento investimento2 = new Investimento(TipoInvestimento.LCI, new BigDecimal("2000.00"), "Pedro");

        // Act
        conta.adicionarInvestimento(investimento1);
        conta.adicionarInvestimento(investimento2);

        // Assert
        TestRunner.assertEquals(2, conta.getInvestimentos().size(), "Deve ter 2 investimentos");
        TestRunner.assertEquals(new BigDecimal("3000.00"), conta.getValorTotalInvestido(), "Valor total investido deve ser R$ 3000");
    }

    public void testRemoverInvestimento() {
        // Arrange
        ContaInvestimento conta = new ContaInvestimento();
        Investimento investimento = new Investimento(TipoInvestimento.CDB, new BigDecimal("1000.00"), "Pedro");
        conta.adicionarInvestimento(investimento);

        // Act
        boolean resultado = conta.removerInvestimento(investimento);

        // Assert
        TestRunner.assertTrue(resultado, "Remoção deve ser bem-sucedida");
        TestRunner.assertEquals(0, conta.getInvestimentos().size(), "Lista deve estar vazia");
        TestRunner.assertTrue(conta.getValorTotalInvestido().compareTo(BigDecimal.ZERO) == 0, "Valor total investido deve ser zero");
    }

    public void testRemoverInvestimentoInexistente() {
        // Arrange
        ContaInvestimento conta = new ContaInvestimento();
        Investimento investimento = new Investimento(TipoInvestimento.CDB, new BigDecimal("1000.00"), "Pedro");

        // Act
        boolean resultado = conta.removerInvestimento(investimento);

        // Assert
        TestRunner.assertFalse(resultado, "Remoção deve falhar para investimento inexistente");
    }

    public void testCalcularTarifas() {
        // Arrange
        ContaInvestimento conta = new ContaInvestimento();
        Investimento investimento = new Investimento(TipoInvestimento.CDB, new BigDecimal("10000.00"), "Pedro");
        conta.adicionarInvestimento(investimento);
        
        BigDecimal tarifaEsperada = new BigDecimal("10000.00").multiply(new BigDecimal("0.001")); // 0.1% de 10000 = 10

        // Act
        BigDecimal tarifa = conta.calcularTarifas();

        // Assert
        TestRunner.assertEquals(tarifaEsperada, tarifa, "Tarifa deve ser 0.1% do valor investido");
    }

    public void testCalcularPatrimonioTotal() {
        // Arrange
        ContaInvestimento conta = new ContaInvestimento();
        conta.depositar(new BigDecimal("5000.00")); // Saldo em conta
        
        Investimento investimento = new Investimento(TipoInvestimento.CDB, new BigDecimal("2000.00"), "Pedro");
        conta.adicionarInvestimento(investimento);

        // Act
        BigDecimal patrimonioTotal = conta.calcularPatrimonioTotal();

        // Assert
        TestRunner.assertTrue(patrimonioTotal.compareTo(new BigDecimal("7000.00")) >= 0, 
                             "Patrimônio total deve ser pelo menos R$ 7000 (saldo + investimento + rendimentos)");
    }

    public void testDetalhesEspecificos() {
        // Arrange
        ContaInvestimento conta = new ContaInvestimento();
        Investimento investimento1 = new Investimento(TipoInvestimento.CDB, new BigDecimal("1000.00"), "Pedro");
        Investimento investimento2 = new Investimento(TipoInvestimento.LCI, new BigDecimal("2000.00"), "Pedro");
        conta.adicionarInvestimento(investimento1);
        conta.adicionarInvestimento(investimento2);

        // Act
        String detalhes = conta.getDetalhesEspecificos();

        // Assert
        TestRunner.assertTrue(detalhes.contains("2"), "Detalhes devem mostrar 2 investimentos");
        TestRunner.assertTrue(detalhes.contains("3000,00"), "Detalhes devem mostrar valor total investido");
        TestRunner.assertTrue(detalhes.contains("100,00"), "Detalhes devem mostrar saque mínimo");
        TestRunner.assertTrue(detalhes.contains("Investimentos"), "Detalhes devem mencionar 'Investimentos'");
    }
}
