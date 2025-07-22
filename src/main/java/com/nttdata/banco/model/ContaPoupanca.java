package com.nttdata.banco.model;

import com.nttdata.banco.enums.TipoConta;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Classe que representa uma Conta Poupança
 * Herda de Conta e implementa comportamentos específicos
 */
public class ContaPoupanca extends Conta {
    private LocalDate dataAniversario;
    private BigDecimal rendimentoMensal;
    private static final BigDecimal TAXA_RENDIMENTO = new BigDecimal("0.005");

    public ContaPoupanca() {
        super();
        setTipoConta(TipoConta.POUPANCA);
        this.dataAniversario = LocalDate.now();
        this.rendimentoMensal = BigDecimal.ZERO;
    }

    public ContaPoupanca(String numero, String agencia, Cliente titular) {
        super(numero, agencia, titular, TipoConta.POUPANCA);
        this.dataAniversario = LocalDate.now();
        this.rendimentoMensal = BigDecimal.ZERO;
    }

    @Override
    public boolean sacar(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
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
        return BigDecimal.ZERO; // Poupança não tem tarifas
    }

    @Override
    public String getDetalhesEspecificos() {
        return String.format("Data Aniversário: %s | Rendimento: %.2f%% a.m.", 
                           dataAniversario, TAXA_RENDIMENTO.multiply(new BigDecimal("100")));
    }

    public void aplicarRendimento() {
        BigDecimal rendimento = getSaldo().multiply(TAXA_RENDIMENTO);
        setSaldo(getSaldo().add(rendimento));
        this.rendimentoMensal = this.rendimentoMensal.add(rendimento);
    }

    public LocalDate getDataAniversario() {
        return dataAniversario;
    }

    public void setDataAniversario(LocalDate dataAniversario) {
        this.dataAniversario = dataAniversario;
    }

    public BigDecimal getRendimentoMensal() {
        return rendimentoMensal;
    }

    public static BigDecimal getTaxaRendimento() {
        return TAXA_RENDIMENTO;
    }
}