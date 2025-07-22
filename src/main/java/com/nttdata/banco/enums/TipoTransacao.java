package com.nttdata.banco.enums;

/**
 * Enum que define os tipos de transação disponíveis
 */
public enum TipoTransacao {
    DEPOSITO("Depósito"),
    SAQUE("Saque"),
    TRANSFERENCIA("Transferência"),
    PIX("PIX"),
    INVESTIMENTO("Investimento"),
    RESGATE_INVESTIMENTO("Resgate de Investimento");

    private final String descricao;

    TipoTransacao(String descricao) {
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