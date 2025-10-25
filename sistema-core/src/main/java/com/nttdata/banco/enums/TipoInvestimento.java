package com.nttdata.banco.enums;

/**
 * Enum que define os tipos de investimento disponíveis
 */
public enum TipoInvestimento {
    POUPANCA(0.005, "Poupança", "Baixo risco, rentabilidade baixa"),
    CDB(0.008, "CDB", "Médio risco, rentabilidade média"),
    LCI(0.007, "LCI", "Baixo risco, isento de IR"),
    LCA(0.007, "LCA", "Baixo risco, isento de IR"),
    TESOURO_DIRETO(0.009, "Tesouro Direto", "Médio risco, boa rentabilidade"),
    ACOES(0.012, "Ações", "Alto risco, alta rentabilidade potencial");

    private final double rentabilidadeMensal;
    private final String nome;
    private final String descricao;

    TipoInvestimento(double rentabilidadeMensal, String nome, String descricao) {
        this.rentabilidadeMensal = rentabilidadeMensal;
        this.nome = nome;
        this.descricao = descricao;
    }

    public double getRentabilidadeMensal() {
        return rentabilidadeMensal;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f%% a.m.) - %s", nome, rentabilidadeMensal * 100, descricao);
    }
}