package com.nttdata.banco.model;

import com.nttdata.banco.enums.TipoConta;
import java.math.BigDecimal;

/**
 * Classe que representa uma Conta Corrente
 * Herda de Conta e implementa comportamentos específicos
 */
public class ContaCorrente extends Conta {
    private BigDecimal limite;
    private BigDecimal tarifaMensal;
    private static final BigDecimal TARIFA_SAQUE = new BigDecimal("2.50");
    private static final BigDecimal TARIFA_TRANSFERENCIA = new BigDecimal("1.50");

    public ContaCorrente() {
        super();
        setTipoConta(TipoConta.CORRENTE);
        this.limite = new BigDecimal("1000.00");
        this.tarifaMensal = new BigDecimal("15.00");
    }

    public ContaCorrente(String numero, String agencia, Cliente titular) {
        super(numero, agencia, titular, TipoConta.CORRENTE);
        this.limite = new BigDecimal("1000.00");
        this.tarifaMensal = new BigDecimal("15.00");
    }

    @Override
    public boolean sacar(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        
        BigDecimal saldoDisponivel = getSaldo().add(limite);
        BigDecimal valorComTarifa = valor.add(TARIFA_SAQUE);
        
        if (valorComTarifa.compareTo(saldoDisponivel) <= 0) {
            setSaldo(getSaldo().subtract(valorComTarifa));
            return true;
        }
        return false;
    }

    @Override
    public BigDecimal calcularTarifas() {
        return tarifaMensal.add(TARIFA_SAQUE).add(TARIFA_TRANSFERENCIA);
    }

    @Override
    public String getDetalhesEspecificos() {
        return String.format("Limite: R$ %.2f | Tarifa Mensal: R$ %.2f", limite, tarifaMensal);
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public void setLimite(BigDecimal limite) {
        this.limite = limite;
    }

    public BigDecimal getTarifaMensal() {
        return tarifaMensal;
    }

    public void setTarifaMensal(BigDecimal tarifaMensal) {
        this.tarifaMensal = tarifaMensal;
    }

    public static BigDecimal getTarifaSaque() {
        return TARIFA_SAQUE;
    }

    public static BigDecimal getTarifaTransferencia() {
        return TARIFA_TRANSFERENCIA;
    }
}