package com.nttdata.banco.model;

import com.nttdata.banco.enums.TipoConta;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Classe abstrata que representa uma Conta bancária
 * Demonstra abstração e serve como base para outros tipos de conta
 */
public abstract class Conta {
    private String numero;
    private String agencia;
    private BigDecimal saldo;
    private Cliente titular;
    private TipoConta tipoConta;
    private LocalDateTime dataAbertura;
    private List<Transacao> historico;
    private boolean ativa;

    public Conta() {
        this.saldo = BigDecimal.ZERO;
        this.historico = new ArrayList<>();
        this.dataAbertura = LocalDateTime.now();
        this.ativa = true;
    }

    public Conta(String numero, String agencia, Cliente titular, TipoConta tipoConta) {
        this();
        this.numero = numero;
        this.agencia = agencia;
        this.titular = titular;
        this.tipoConta = tipoConta;
    }

    // Métodos abstratos que serão implementados pelas subclasses
    public abstract boolean sacar(BigDecimal valor);
    public abstract BigDecimal calcularTarifas();
    public abstract String getDetalhesEspecificos();

    // Métodos concretos comuns a todas as contas
    public void depositar(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo");
        }
        this.saldo = this.saldo.add(valor);
    }

    public boolean transferir(BigDecimal valor, Conta contaDestino) {
        if (this.sacar(valor)) {
            contaDestino.depositar(valor);
            return true;
        }
        return false;
    }

    public void adicionarTransacao(Transacao transacao) {
        this.historico.add(transacao);
    }

    // Getters e Setters
    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    protected void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Cliente getTitular() {
        return titular;
    }

    public void setTitular(Cliente titular) {
        this.titular = titular;
    }

    public TipoConta getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(TipoConta tipoConta) {
        this.tipoConta = tipoConta;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public List<Transacao> getHistorico() {
        return new ArrayList<>(historico);
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conta conta = (Conta) o;
        return Objects.equals(numero, conta.numero) && Objects.equals(agencia, conta.agencia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero, agencia);
    }

    @Override
    public String toString() {
        return String.format("%s - Ag: %s, Conta: %s, Saldo: R$ %.2f", 
                           tipoConta.getDescricao(), agencia, numero, saldo);
    }
}