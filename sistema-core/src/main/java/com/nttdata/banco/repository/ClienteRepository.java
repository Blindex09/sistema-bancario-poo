package com.nttdata.banco.repository;

import com.nttdata.banco.model.Cliente;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Repository para gerenciar os clientes
 * Simula um banco de dados em memória
 */
public class ClienteRepository {
    private final Map<String, Cliente> clientes;

    public ClienteRepository() {
        this.clientes = new HashMap<>();
    }

    public Cliente salvar(Cliente cliente) {
        clientes.put(cliente.getCpf(), cliente);
        return cliente;
    }

    public Optional<Cliente> buscarPorCpf(String cpf) {
        return Optional.ofNullable(clientes.get(cpf));
    }

    public List<Cliente> buscarPorNome(String nome) {
        return clientes.values().stream()
                .filter(cliente -> cliente.getNome().toLowerCase()
                        .contains(nome.toLowerCase()))
                .toList();
    }

    public Optional<Cliente> buscarPorEmail(String email) {
        return clientes.values().stream()
                .filter(cliente -> cliente.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public List<Cliente> listarTodos() {
        return new ArrayList<>(clientes.values());
    }

    public boolean excluir(String cpf) {
        return clientes.remove(cpf) != null;
    }

    public boolean existe(String cpf) {
        return clientes.containsKey(cpf);
    }

    public int getTotalClientes() {
        return clientes.size();
    }

    public void limpar() {
        clientes.clear();
    }
}