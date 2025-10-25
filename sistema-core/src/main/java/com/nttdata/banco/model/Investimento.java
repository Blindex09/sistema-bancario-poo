package com.nttdata.banco.model;

import com.nttdata.banco.enums.TipoInvestimento;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Classe que representa um Investimento
 * Demonstra composição e cálculos financeiros
 */
public class Investimento {
    private final String id;
    private final TipoInvestimento tipo;
    private final BigDecimal valor;
    private final LocalDate dataAplicacao;
    private final String titular;
    private LocalDate dataVencimento;
    private boolean ativo;
    private String observacoes;

    public Investimento(TipoInvestimento tipo, BigDecimal valor, String titular) {
        this.id = UUID.randomUUID().toString();
        this.tipo = tipo;
        this.valor = valor;
        this.titular = titular;
        this.dataAplicacao = LocalDate.now();
        this.ativo = true;
        this.observacoes = "";
        
        // Define vencimento baseado no tipo de investimento
        this.dataVencimento = calcularDataVencimento();
    }

    public Investimento(TipoInvestimento tipo, BigDecimal valor, String titular, 
                       LocalDate dataVencimento, String observacoes) {
        this.id = UUID.randomUUID().toString();
        this.tipo = tipo;
        this.valor = valor;
        this.titular = titular;
        this.dataAplicacao = LocalDate.now();
        this.dataVencimento = dataVencimento;
        this.ativo = true;
        this.observacoes = observacoes != null ? observacoes : "";
    }

    private LocalDate calcularDataVencimento() {
        switch (tipo) {
            case POUPANCA:
                return dataAplicacao.plusMonths(1); // Liquidação mensal
            case CDB:
                return dataAplicacao.plusMonths(6); // 6 meses
            case LCI:
            case LCA:
                return dataAplicacao.plusMonths(12); // 12 meses
            case TESOURO_DIRETO:
                return dataAplicacao.plusYears(2); // 2 anos
            case ACOES:
                return dataAplicacao.plusYears(5); // 5 anos (investimento de longo prazo)
            default:
                return dataAplicacao.plusMonths(12);
        }
    }

    public BigDecimal calcularValorAtual() {
        if (!ativo) {
            return BigDecimal.ZERO;
        }

        long mesesDecorridos = ChronoUnit.MONTHS.between(dataAplicacao, LocalDate.now());
        if (mesesDecorridos <= 0) {
            return valor;
        }

        BigDecimal taxaMensal = BigDecimal.valueOf(tipo.getRentabilidadeMensal());
        BigDecimal fatorRendimento = BigDecimal.ONE.add(taxaMensal);
        
        // Cálculo de juros compostos: M = C * (1 + i)^t
        BigDecimal valorAtual = valor;
        for (int i = 0; i < mesesDecorridos; i++) {
            valorAtual = valorAtual.multiply(fatorRendimento);
        }
        
        return valorAtual.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularRendimento() {
        return calcularValorAtual().subtract(valor);
    }

    public double calcularRentabilidadePercentual() {
        if (valor.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        
        BigDecimal rendimento = calcularRendimento();
        return rendimento.divide(valor, 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"))
                        .doubleValue();
    }

    public boolean podeResgatar() {
        return ativo && LocalDate.now().isAfter(dataVencimento.minusDays(1));
    }

    public BigDecimal resgatar() {
        if (!podeResgatar()) {
            throw new IllegalStateException("Investimento não pode ser resgatado ainda");
        }
        
        BigDecimal valorResgate = calcularValorAtual();
        this.ativo = false;
        return valorResgate;
    }

    public long getDiasParaVencimento() {
        return ChronoUnit.DAYS.between(LocalDate.now(), dataVencimento);
    }

    // Getters
    public String getId() {
        return id;
    }

    public TipoInvestimento getTipo() {
        return tipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDate getDataAplicacao() {
        return dataAplicacao;
    }

    public String getTitular() {
        return titular;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format(
            "Investimento: %s\n" +
            "Valor Aplicado: R$ %.2f\n" +
            "Valor Atual: R$ %.2f\n" +
            "Rendimento: R$ %.2f (%.2f%%)\n" +
            "Data Aplicação: %s\n" +
            "Data Vencimento: %s\n" +
            "Status: %s\n" +
            "Dias para vencimento: %d",
            tipo.getNome(),
            valor,
            calcularValorAtual(),
            calcularRendimento(),
            calcularRentabilidadePercentual(),
            dataAplicacao.format(formatter),
            dataVencimento.format(formatter),
            ativo ? "Ativo" : "Resgatado",
            getDiasParaVencimento()
        );
    }
}