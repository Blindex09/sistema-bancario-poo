package com.nttdata.banco.repository;

import com.nttdata.banco.model.Investimento;
import com.nttdata.banco.enums.TipoInvestimento;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Repository para gerenciar os investimentos
 * Simula um banco de dados em memória
 */
public class InvestimentoRepository {
    private final Map<String, Investimento> investimentos;

    public InvestimentoRepository() {
        this.investimentos = new HashMap<>();
    }

    public Investimento salvar(Investimento investimento) {
        investimentos.put(investimento.getId(), investimento);
        return investimento;
    }

    public Optional<Investimento> buscarPorId(String id) {
        return Optional.ofNullable(investimentos.get(id));
    }

    public List<Investimento> buscarPorTitular(String titular) {
        return investimentos.values().stream()
                .filter(inv -> inv.getTitular().equals(titular))
                .toList();
    }

    public List<Investimento> buscarPorTipo(TipoInvestimento tipo) {
        return investimentos.values().stream()
                .filter(inv -> inv.getTipo().equals(tipo))
                .toList();
    }

    public List<Investimento> buscarAtivos() {
        return investimentos.values().stream()
                .filter(Investimento::isAtivo)
                .toList();
    }

    public List<Investimento> buscarVencendoEm(int dias) {
        return investimentos.values().stream()
                .filter(Investimento::isAtivo)
                .filter(inv -> inv.getDiasParaVencimento() <= dias)
                .toList();
    }

    public List<Investimento> buscarVencidos() {
        return investimentos.values().stream()
                .filter(Investimento::isAtivo)
                .filter(inv -> inv.getDiasParaVencimento() < 0)
                .toList();
    }

    public List<Investimento> listarTodos() {
        return new ArrayList<>(investimentos.values());
    }

    public boolean excluir(String id) {
        return investimentos.remove(id) != null;
    }

    public boolean existe(String id) {
        return investimentos.containsKey(id);
    }

    public int getTotalInvestimentos() {
        return investimentos.size();
    }

    public int getTotalInvestimentosAtivos() {
        return (int) investimentos.values().stream()
                .filter(Investimento::isAtivo)
                .count();
    }

    public BigDecimal getValorTotalInvestido() {
        return investimentos.values().stream()
                .filter(Investimento::isAtivo)
                .map(Investimento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getValorTotalAtual() {
        return investimentos.values().stream()
                .filter(Investimento::isAtivo)
                .map(Investimento::calcularValorAtual)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void limpar() {
        investimentos.clear();
    }
}