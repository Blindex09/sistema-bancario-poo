package com.nttdata.banco.repository;

import com.nttdata.banco.model.Conta;
import com.nttdata.banco.model.Cliente;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Repository para gerenciar as contas
 * Simula um banco de dados em memória
 */
public class ContaRepository {
    private final Map<String, Conta> contas;
    private int proximoNumero;

    public ContaRepository() {
        this.contas = new HashMap<>();
        this.proximoNumero = 1001;
    }

    public Conta salvar(Conta conta) {
        if (conta.getNumero() == null || conta.getNumero().isEmpty()) {
            conta.setNumero(gerarNumero());
        }
        
        String chave = gerarChave(conta.getAgencia(), conta.getNumero());
        contas.put(chave, conta);
        return conta;
    }

    public Optional<Conta> buscarPorNumero(String agencia, String numero) {
        String chave = gerarChave(agencia, numero);
        return Optional.ofNullable(contas.get(chave));
    }

    public List<Conta> buscarPorTitular(Cliente titular) {
        return contas.values().stream()
                .filter(conta -> conta.getTitular().equals(titular))
                .toList();
    }

    public List<Conta> buscarPorCpf(String cpf) {
        return contas.values().stream()
                .filter(conta -> conta.getTitular().getCpf().equals(cpf))
                .toList();
    }

    public List<Conta> listarTodas() {
        return new ArrayList<>(contas.values());
    }

    public List<Conta> listarAtivas() {
        return contas.values().stream()
                .filter(Conta::isAtiva)
                .toList();
    }

    public boolean excluir(String agencia, String numero) {
        String chave = gerarChave(agencia, numero);
        return contas.remove(chave) != null;
    }

    public boolean existe(String agencia, String numero) {
        String chave = gerarChave(agencia, numero);
        return contas.containsKey(chave);
    }

    public int getTotalContas() {
        return contas.size();
    }

    public int getTotalContasAtivas() {
        return (int) contas.values().stream().filter(Conta::isAtiva).count();
    }

    private String gerarNumero() {
        return String.format("%06d", proximoNumero++);
    }

    private String gerarChave(String agencia, String numero) {
        return agencia + "-" + numero;
    }
}