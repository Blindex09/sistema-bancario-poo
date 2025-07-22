package com.nttdata.banco.model;

import com.nttdata.banco.enums.TipoTransacao;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Classe que representa uma Transação bancária
 * Implementa encapsulamento e imutabilidade dos dados da transação
 */
public class Transacao {
    private final String id;
    private final TipoTransacao tipo;
    private final BigDecimal valor;
    private final LocalDateTime dataHora;
    private final String descricao;
    private final String contaOrigem;
    private final String contaDestino;
    private final boolean sucesso;
    private final String observacoes;

    // Construtor para transações simples (depósito, saque)
    public Transacao(TipoTransacao tipo, BigDecimal valor, String descricao, String conta, boolean sucesso) {
        this.id = UUID.randomUUID().toString();
        this.tipo = tipo;
        this.valor = valor;
        this.dataHora = LocalDateTime.now();
        this.descricao = descricao;
        this.contaOrigem = conta;
        this.contaDestino = null;
        this.sucesso = sucesso;
        this.observacoes = "";
    }

    // Construtor para transferências e PIX
    public Transacao(TipoTransacao tipo, BigDecimal valor, String descricao, 
                    String contaOrigem, String contaDestino, boolean sucesso) {
        this.id = UUID.randomUUID().toString();
        this.tipo = tipo;
        this.valor = valor;
        this.dataHora = LocalDateTime.now();
        this.descricao = descricao;
        this.contaOrigem = contaOrigem;
        this.contaDestino = contaDestino;
        this.sucesso = sucesso;
        this.observacoes = "";
    }

    // Construtor completo
    public Transacao(TipoTransacao tipo, BigDecimal valor, String descricao, 
                    String contaOrigem, String contaDestino, boolean sucesso, String observacoes) {
        this.id = UUID.randomUUID().toString();
        this.tipo = tipo;
        this.valor = valor;
        this.dataHora = LocalDateTime.now();
        this.descricao = descricao;
        this.contaOrigem = contaOrigem;
        this.contaDestino = contaDestino;
        this.sucesso = sucesso;
        this.observacoes = observacoes != null ? observacoes : "";
    }

    // Getters (sem setters para manter imutabilidade)
    public String getId() {
        return id;
    }

    public TipoTransacao getTipo() {
        return tipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getContaOrigem() {
        return contaOrigem;
    }

    public String getContaDestino() {
        return contaDestino;
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public String getDataHoraFormatada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dataHora.format(formatter);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[%s] %s - %s", 
                getDataHoraFormatada(), tipo.getDescricao(), 
                sucesso ? "SUCESSO" : "FALHOU"));
        sb.append(String.format("\nValor: R$ %.2f", valor));
        sb.append(String.format("\nDescrição: %s", descricao));
        
        if (contaDestino != null) {
            sb.append(String.format("\nOrigem: %s | Destino: %s", contaOrigem, contaDestino));
        } else {
            sb.append(String.format("\nConta: %s", contaOrigem));
        }
        
        if (!observacoes.isEmpty()) {
            sb.append(String.format("\nObservações: %s", observacoes));
        }
        
        return sb.toString();
    }
}