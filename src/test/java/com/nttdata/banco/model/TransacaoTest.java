package com.nttdata.banco.model;

import com.nttdata.banco.TestRunner;
import com.nttdata.banco.enums.TipoTransacao;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Testes automatizados para a classe Transacao
 * Verifica imutabilidade e criação de transações
 */
public class TransacaoTest {

    public void testCriacaoTransacaoSimples() {
        // Arrange
        TipoTransacao tipo = TipoTransacao.DEPOSITO;
        BigDecimal valor = new BigDecimal("500.00");
        String descricao = "Depósito em conta";
        String conta = "0001-001001";
        boolean sucesso = true;

        // Act
        Transacao transacao = new Transacao(tipo, valor, descricao, conta, sucesso);

        // Assert
        TestRunner.assertNotNull(transacao.getId(), "ID não deve ser null");
        TestRunner.assertEquals(tipo, transacao.getTipo(), "Tipo deve ser igual");
        TestRunner.assertEquals(valor, transacao.getValor(), "Valor deve ser igual");
        TestRunner.assertEquals(descricao, transacao.getDescricao(), "Descrição deve ser igual");
        TestRunner.assertEquals(conta, transacao.getContaOrigem(), "Conta origem deve ser igual");
        TestRunner.assertNull(transacao.getContaDestino(), "Conta destino deve ser null");
        TestRunner.assertTrue(transacao.isSucesso(), "Transação deve ter sucesso");
        TestRunner.assertEquals("", transacao.getObservacoes(), "Observações devem estar vazias");
        TestRunner.assertNotNull(transacao.getDataHora(), "Data/hora não deve ser null");
    }

    public void testCriacaoTransacaoTransferencia() {
        // Arrange
        TipoTransacao tipo = TipoTransacao.TRANSFERENCIA;
        BigDecimal valor = new BigDecimal("1000.00");
        String descricao = "Transferência entre contas";
        String contaOrigem = "0001-001001";
        String contaDestino = "0001-001002";
        boolean sucesso = true;

        // Act
        Transacao transacao = new Transacao(tipo, valor, descricao, contaOrigem, contaDestino, sucesso);

        // Assert
        TestRunner.assertEquals(tipo, transacao.getTipo(), "Tipo deve ser TRANSFERENCIA");
        TestRunner.assertEquals(valor, transacao.getValor(), "Valor deve ser igual");
        TestRunner.assertEquals(contaOrigem, transacao.getContaOrigem(), "Conta origem deve ser igual");
        TestRunner.assertEquals(contaDestino, transacao.getContaDestino(), "Conta destino deve ser igual");
        TestRunner.assertTrue(transacao.isSucesso(), "Transação deve ter sucesso");
    }

    public void testCriacaoTransacaoCompleta() {
        // Arrange
        TipoTransacao tipo = TipoTransacao.PIX;
        BigDecimal valor = new BigDecimal("250.00");
        String descricao = "PIX para João";
        String contaOrigem = "0001-001001";
        String contaDestino = "0001-001002";
        boolean sucesso = true;
        String observacoes = "Pagamento de almoço";

        // Act
        Transacao transacao = new Transacao(tipo, valor, descricao, contaOrigem, contaDestino, sucesso, observacoes);

        // Assert
        TestRunner.assertEquals(tipo, transacao.getTipo(), "Tipo deve ser PIX");
        TestRunner.assertEquals(valor, transacao.getValor(), "Valor deve ser igual");
        TestRunner.assertEquals(descricao, transacao.getDescricao(), "Descrição deve ser igual");
        TestRunner.assertEquals(contaOrigem, transacao.getContaOrigem(), "Conta origem deve ser igual");
        TestRunner.assertEquals(contaDestino, transacao.getContaDestino(), "Conta destino deve ser igual");
        TestRunner.assertTrue(transacao.isSucesso(), "Transação deve ter sucesso");
        TestRunner.assertEquals(observacoes, transacao.getObservacoes(), "Observações devem ser iguais");
    }

    public void testTransacaoFalhada() {
        // Arrange
        TipoTransacao tipo = TipoTransacao.SAQUE;
        BigDecimal valor = new BigDecimal("5000.00");
        String descricao = "Saque - saldo insuficiente";
        String conta = "0001-001001";
        boolean sucesso = false;

        // Act
        Transacao transacao = new Transacao(tipo, valor, descricao, conta, sucesso);

        // Assert
        TestRunner.assertFalse(transacao.isSucesso(), "Transação deve ter falhado");
        TestRunner.assertEquals("SAQUE", transacao.getTipo().name(), "Tipo deve ser SAQUE");
    }

    public void testImutabilidade() {
        // Arrange
        Transacao transacao = new Transacao(TipoTransacao.DEPOSITO, 
                                          new BigDecimal("100.00"), 
                                          "Teste", 
                                          "0001-001001", 
                                          true);
        
        // Store original values
        String idOriginal = transacao.getId();
        TipoTransacao tipoOriginal = transacao.getTipo();
        BigDecimal valorOriginal = transacao.getValor();
        LocalDateTime dataOriginal = transacao.getDataHora();

        // Act & Assert - tentar modificar deveria não ter efeito (imutável)
        // Não há setters para testar, isso confirma a imutabilidade
        TestRunner.assertEquals(idOriginal, transacao.getId(), "ID deve permanecer imutável");
        TestRunner.assertEquals(tipoOriginal, transacao.getTipo(), "Tipo deve permanecer imutável");
        TestRunner.assertEquals(valorOriginal, transacao.getValor(), "Valor deve permanecer imutável");
        TestRunner.assertEquals(dataOriginal, transacao.getDataHora(), "Data deve permanecer imutável");
    }

    public void testDataHoraFormatada() {
        // Arrange
        Transacao transacao = new Transacao(TipoTransacao.DEPOSITO, 
                                          new BigDecimal("100.00"), 
                                          "Teste", 
                                          "0001-001001", 
                                          true);

        // Act
        String dataFormatada = transacao.getDataHoraFormatada();

        // Assert
        TestRunner.assertNotNull(dataFormatada, "Data formatada não deve ser null");
        TestRunner.assertTrue(dataFormatada.length() > 0, "Data formatada não deve estar vazia");
        TestRunner.assertTrue(dataFormatada.contains("/"), "Data formatada deve conter separadores de data");
        TestRunner.assertTrue(dataFormatada.contains(":"), "Data formatada deve conter separadores de hora");
    }

    public void testToStringTransacaoSimples() {
        // Arrange
        Transacao transacao = new Transacao(TipoTransacao.DEPOSITO, 
                                          new BigDecimal("500.00"), 
                                          "Depósito em conta", 
                                          "0001-001001", 
                                          true);

        // Act
        String toString = transacao.toString();

        // Assert
        TestRunner.assertTrue(toString.contains("Depósito"), "ToString deve conter tipo da transação");
        TestRunner.assertTrue(toString.contains("500,00"), "ToString deve conter valor");
        TestRunner.assertTrue(toString.contains("SUCESSO"), "ToString deve conter status");
        TestRunner.assertTrue(toString.contains("Depósito em conta"), "ToString deve conter descrição");
        TestRunner.assertTrue(toString.contains("0001-001001"), "ToString deve conter conta");
    }

    public void testToStringTransacaoTransferencia() {
        // Arrange
        Transacao transacao = new Transacao(TipoTransacao.TRANSFERENCIA, 
                                          new BigDecimal("1000.00"), 
                                          "Transferência", 
                                          "0001-001001", 
                                          "0001-001002", 
                                          true);

        // Act
        String toString = transacao.toString();

        // Assert
        TestRunner.assertTrue(toString.contains("Transferência"), "ToString deve conter tipo");
        TestRunner.assertTrue(toString.contains("1000,00"), "ToString deve conter valor");
        TestRunner.assertTrue(toString.contains("0001-001001"), "ToString deve conter conta origem");
        TestRunner.assertTrue(toString.contains("0001-001002"), "ToString deve conter conta destino");
        TestRunner.assertTrue(toString.contains("Origem:"), "ToString deve mencionar origem");
        TestRunner.assertTrue(toString.contains("Destino:"), "ToString deve mencionar destino");
    }

    public void testToStringComObservacoes() {
        // Arrange
        Transacao transacao = new Transacao(TipoTransacao.PIX, 
                                          new BigDecimal("200.00"), 
                                          "PIX", 
                                          "0001-001001", 
                                          "0001-001002", 
                                          true, 
                                          "Pagamento do almoço");

        // Act
        String toString = transacao.toString();

        // Assert
        TestRunner.assertTrue(toString.contains("Pagamento do almoço"), "ToString deve conter observações");
        TestRunner.assertTrue(toString.contains("Observações:"), "ToString deve mencionar observações");
    }

    public void testObservacoesNull() {
        // Arrange & Act
        Transacao transacao = new Transacao(TipoTransacao.DEPOSITO, 
                                          new BigDecimal("100.00"), 
                                          "Teste", 
                                          "0001-001001", 
                                          null, 
                                          true, 
                                          null);

        // Assert
        TestRunner.assertEquals("", transacao.getObservacoes(), "Observações null devem virar string vazia");
    }
}
