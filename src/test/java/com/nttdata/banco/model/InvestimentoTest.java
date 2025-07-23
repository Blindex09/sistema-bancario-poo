package com.nttdata.banco.model;

import com.nttdata.banco.TestRunner;
import com.nttdata.banco.enums.TipoInvestimento;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Testes automatizados para a classe Investimento
 * Verifica cálculos financeiros, rendimentos e resgates
 */
public class InvestimentoTest {

    public void testCriacaoInvestimentoSimples() {
        // Arrange
        TipoInvestimento tipo = TipoInvestimento.CDB;
        BigDecimal valor = new BigDecimal("1000.00");
        String titular = "João Silva";

        // Act
        Investimento investimento = new Investimento(tipo, valor, titular);

        // Assert
        TestRunner.assertNotNull(investimento.getId(), "ID não deve ser null");
        TestRunner.assertEquals(tipo, investimento.getTipo(), "Tipo deve ser igual");
        TestRunner.assertEquals(valor, investimento.getValor(), "Valor deve ser igual");
        TestRunner.assertEquals(titular, investimento.getTitular(), "Titular deve ser igual");
        TestRunner.assertEquals(LocalDate.now(), investimento.getDataAplicacao(), "Data aplicação deve ser hoje");
        TestRunner.assertTrue(investimento.isAtivo(), "Investimento deve estar ativo");
        TestRunner.assertEquals("", investimento.getObservacoes(), "Observações devem estar vazias");
        TestRunner.assertNotNull(investimento.getDataVencimento(), "Data vencimento não deve ser null");
    }

    public void testCriacaoInvestimentoCompleto() {
        // Arrange
        TipoInvestimento tipo = TipoInvestimento.LCI;
        BigDecimal valor = new BigDecimal("5000.00");
        String titular = "Maria Santos";
        LocalDate dataVencimento = LocalDate.now().plusYears(1);
        String observacoes = "Investimento para reserva de emergência";

        // Act
        Investimento investimento = new Investimento(tipo, valor, titular, dataVencimento, observacoes);

        // Assert
        TestRunner.assertEquals(tipo, investimento.getTipo(), "Tipo deve ser LCI");
        TestRunner.assertEquals(valor, investimento.getValor(), "Valor deve ser igual");
        TestRunner.assertEquals(titular, investimento.getTitular(), "Titular deve ser igual");
        TestRunner.assertEquals(dataVencimento, investimento.getDataVencimento(), "Data vencimento deve ser igual");
        TestRunner.assertEquals(observacoes, investimento.getObservacoes(), "Observações devem ser iguais");
    }

    public void testDataVencimentoPoupanca() {
        // Arrange & Act
        Investimento investimento = new Investimento(TipoInvestimento.POUPANCA, new BigDecimal("1000.00"), "João");

        // Assert
        LocalDate vencimentoEsperado = LocalDate.now().plusMonths(1);
        TestRunner.assertEquals(vencimentoEsperado, investimento.getDataVencimento(), "Poupança deve vencer em 1 mês");
    }

    public void testDataVencimentoCDB() {
        // Arrange & Act
        Investimento investimento = new Investimento(TipoInvestimento.CDB, new BigDecimal("1000.00"), "João");

        // Assert
        LocalDate vencimentoEsperado = LocalDate.now().plusMonths(6);
        TestRunner.assertEquals(vencimentoEsperado, investimento.getDataVencimento(), "CDB deve vencer em 6 meses");
    }

    public void testDataVencimentoTesouroDireto() {
        // Arrange & Act
        Investimento investimento = new Investimento(TipoInvestimento.TESOURO_DIRETO, new BigDecimal("1000.00"), "João");

        // Assert
        LocalDate vencimentoEsperado = LocalDate.now().plusYears(2);
        TestRunner.assertEquals(vencimentoEsperado, investimento.getDataVencimento(), "Tesouro Direto deve vencer em 2 anos");
    }

    public void testCalcularValorAtualInicialmente() {
        // Arrange
        BigDecimal valorInicial = new BigDecimal("1000.00");
        Investimento investimento = new Investimento(TipoInvestimento.CDB, valorInicial, "João");

        // Act
        BigDecimal valorAtual = investimento.calcularValorAtual();

        // Assert
        TestRunner.assertEquals(valorInicial, valorAtual, "Valor atual deve ser igual ao inicial no dia da aplicação");
    }

    public void testCalcularRendimentoInicialmente() {
        // Arrange
        Investimento investimento = new Investimento(TipoInvestimento.CDB, new BigDecimal("1000.00"), "João");

        // Act
        BigDecimal rendimento = investimento.calcularRendimento();

        // Assert
        TestRunner.assertTrue(rendimento.compareTo(BigDecimal.ZERO) == 0, "Rendimento inicial deve ser zero");
    }

    public void testCalcularRentabilidadePercentualInicialmente() {
        // Arrange
        Investimento investimento = new Investimento(TipoInvestimento.CDB, new BigDecimal("1000.00"), "João");

        // Act
        double rentabilidade = investimento.calcularRentabilidadePercentual();

        // Assert
        TestRunner.assertEquals(0.0, rentabilidade, 0.01, "Rentabilidade inicial deve ser 0%");
    }

    public void testCalcularRentabilidadeComValorZero() {
        // Arrange
        Investimento investimento = new Investimento(TipoInvestimento.CDB, BigDecimal.ZERO, "João");

        // Act
        double rentabilidade = investimento.calcularRentabilidadePercentual();

        // Assert
        TestRunner.assertEquals(0.0, rentabilidade, 0.01, "Rentabilidade com valor zero deve ser 0%");
    }

    public void testPodeResgatar() {
        // Arrange
        LocalDate dataVencimento = LocalDate.now().minusDays(1); // Vencido
        Investimento investimento = new Investimento(TipoInvestimento.CDB, 
                                                   new BigDecimal("1000.00"), 
                                                   "João", 
                                                   dataVencimento, 
                                                   "");

        // Act & Assert
        TestRunner.assertTrue(investimento.podeResgatar(), "Investimento vencido deve poder ser resgatado");
    }

    public void testNaoPodeResgatar() {
        // Arrange
        LocalDate dataVencimento = LocalDate.now().plusDays(30); // Ainda não vencido
        Investimento investimento = new Investimento(TipoInvestimento.CDB, 
                                                   new BigDecimal("1000.00"), 
                                                   "João", 
                                                   dataVencimento, 
                                                   "");

        // Act & Assert
        TestRunner.assertFalse(investimento.podeResgatar(), "Investimento não vencido não deve poder ser resgatado");
    }

    public void testResgatar() {
        // Arrange
        LocalDate dataVencimento = LocalDate.now().minusDays(1);
        Investimento investimento = new Investimento(TipoInvestimento.CDB, 
                                                   new BigDecimal("1000.00"), 
                                                   "João", 
                                                   dataVencimento, 
                                                   "");

        // Act
        BigDecimal valorResgate = investimento.resgatar();

        // Assert
        TestRunner.assertNotNull(valorResgate, "Valor resgate não deve ser null");
        TestRunner.assertTrue(valorResgate.compareTo(BigDecimal.ZERO) > 0, "Valor resgate deve ser positivo");
        TestRunner.assertFalse(investimento.isAtivo(), "Investimento deve ficar inativo após resgate");
    }

    public void testResgatarInvestimentoNaoVencido() {
        // Arrange
        LocalDate dataVencimento = LocalDate.now().plusDays(30);
        Investimento investimento = new Investimento(TipoInvestimento.CDB, 
                                                   new BigDecimal("1000.00"), 
                                                   "João", 
                                                   dataVencimento, 
                                                   "");

        // Act & Assert
        try {
            investimento.resgatar();
            TestRunner.assertTrue(false, "Deve lançar exceção ao tentar resgatar investimento não vencido");
        } catch (IllegalStateException e) {
            TestRunner.assertTrue(e.getMessage().contains("não pode ser resgatado"), 
                                 "Exceção deve mencionar que não pode ser resgatado");
        }
    }

    public void testGetDiasParaVencimento() {
        // Arrange
        LocalDate dataVencimento = LocalDate.now().plusDays(30);
        Investimento investimento = new Investimento(TipoInvestimento.CDB, 
                                                   new BigDecimal("1000.00"), 
                                                   "João", 
                                                   dataVencimento, 
                                                   "");

        // Act
        long diasParaVencimento = investimento.getDiasParaVencimento();

        // Assert
        TestRunner.assertEquals(30L, diasParaVencimento, "Deve calcular corretamente os dias para vencimento");
    }

    public void testSetDataVencimento() {
        // Arrange
        Investimento investimento = new Investimento(TipoInvestimento.CDB, new BigDecimal("1000.00"), "João");
        LocalDate novaData = LocalDate.now().plusYears(1);

        // Act
        investimento.setDataVencimento(novaData);

        // Assert
        TestRunner.assertEquals(novaData, investimento.getDataVencimento(), "Data vencimento deve ser alterada");
    }

    public void testSetAtivo() {
        // Arrange
        Investimento investimento = new Investimento(TipoInvestimento.CDB, new BigDecimal("1000.00"), "João");

        // Act
        investimento.setAtivo(false);

        // Assert
        TestRunner.assertFalse(investimento.isAtivo(), "Investimento deve ficar inativo");
    }

    public void testSetObservacoes() {
        // Arrange
        Investimento investimento = new Investimento(TipoInvestimento.CDB, new BigDecimal("1000.00"), "João");
        String novasObservacoes = "Alteração de observações";

        // Act
        investimento.setObservacoes(novasObservacoes);

        // Assert
        TestRunner.assertEquals(novasObservacoes, investimento.getObservacoes(), "Observações devem ser alteradas");
    }

    public void testToString() {
        // Arrange
        Investimento investimento = new Investimento(TipoInvestimento.CDB, new BigDecimal("1000.00"), "João Silva");

        // Act
        String toString = investimento.toString();

        // Assert
        TestRunner.assertTrue(toString.contains("CDB"), "ToString deve conter tipo do investimento");
        TestRunner.assertTrue(toString.contains("1000,00"), "ToString deve conter valor aplicado");
        TestRunner.assertTrue(toString.contains("Ativo"), "ToString deve conter status");
        TestRunner.assertTrue(toString.contains("Valor Atual"), "ToString deve conter valor atual");
        TestRunner.assertTrue(toString.contains("Rendimento"), "ToString deve conter rendimento");
    }
}

// Método auxiliar para assertEquals com double
class TestRunnerExtension {
    public static void assertEquals(double expected, double actual, double delta, String message) {
        if (Math.abs(expected - actual) > delta) {
            throw new AssertionError(message + " - Esperado: " + expected + ", Atual: " + actual);
        }
    }
}
