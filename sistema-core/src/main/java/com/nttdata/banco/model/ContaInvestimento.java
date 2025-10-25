package com.nttdata.banco.model;

import com.nttdata.banco.enums.TipoConta;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa uma Conta de Investimento
 * Herda de Conta e implementa comportamentos específicos
 */
public class ContaInvestimento extends Conta {
    private List<Investimento> investimentos;
    private BigDecimal valorTotalInvestido;
    private static final BigDecimal VALOR_MINIMO_SAQUE = new BigDecimal("100.00");

    public ContaInvestimento() {
        super();
        setTipoConta(TipoConta.INVESTIMENTO);
        this.investimentos = new ArrayList<>();
        this.valorTotalInvestido = BigDecimal.ZERO;
    }

    public ContaInvestimento(String numero, String agencia, Cliente titular) {
        super(numero, agencia, titular, TipoConta.INVESTIMENTO);
        this.investimentos = new ArrayList<>();
        this.valorTotalInvestido = BigDecimal.ZERO;
    }

    @Override
    public boolean sacar(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0 || valor.compareTo(VALOR_MINIMO_SAQUE) < 0) {
            return false;
        }
        
        if (valor.compareTo(getSaldo()) <= 0) {
            setSaldo(getSaldo().subtract(valor));
            return true;
        }
        return false;
    }

    @Override
    public BigDecimal calcularTarifas() {
        // Taxa de administração baseada no valor investido
        return valorTotalInvestido.multiply(new BigDecimal("0.001"));
    }

    @Override
    public String getDetalhesEspecificos() {
        return String.format("Investimentos: %d | Valor Total Investido: R$ %.2f | Saque Mínimo: R$ %.2f", 
                           investimentos.size(), valorTotalInvestido, VALOR_MINIMO_SAQUE);
    }

    public void adicionarInvestimento(Investimento investimento) {
        this.investimentos.add(investimento);
        this.valorTotalInvestido = this.valorTotalInvestido.add(investimento.getValor());
    }

    public boolean removerInvestimento(Investimento investimento) {
        if (this.investimentos.remove(investimento)) {
            this.valorTotalInvestido = this.valorTotalInvestido.subtract(investimento.getValor());
            return true;
        }
        return false;
    }

    public List<Investimento> getInvestimentos() {
        return new ArrayList<>(investimentos);
    }

    public BigDecimal getValorTotalInvestido() {
        return valorTotalInvestido;
    }

    public static BigDecimal getValorMinimoSaque() {
        return VALOR_MINIMO_SAQUE;
    }

    public BigDecimal calcularPatrimonioTotal() {
        BigDecimal patrimonioInvestimentos = investimentos.stream()
            .map(Investimento::calcularValorAtual)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return getSaldo().add(patrimonioInvestimentos);
    }
}