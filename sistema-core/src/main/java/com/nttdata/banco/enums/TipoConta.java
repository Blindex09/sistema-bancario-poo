package com.nttdata.banco.enums;

/**
 * Enum que define os tipos de conta disponíveis
 * Demonstra o uso de enums para constantes
 */
public enum TipoConta {
    CORRENTE("Conta Corrente"),
    POUPANCA("Conta Poupança"),
    INVESTIMENTO("Conta Investimento");

    private final String descricao;

    TipoConta(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}