package com.nttdata.banco.menu;

import com.nttdata.banco.model.Cliente;
import com.nttdata.banco.service.BancoService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Menu para gerenciamento de clientes
 */
public class MenuCliente {
    private final BancoService bancoService;
    private final Scanner scanner;

    public MenuCliente(BancoService bancoService, Scanner scanner) {
        this.bancoService = bancoService;
        this.scanner = scanner;
    }

    public void exibir() {
        boolean voltar = false;
        
        while (!voltar) {
            mostrarOpcoes();
            int opcao = lerOpcao();
            
            switch (opcao) {
                case 1 -> criarCliente();
                case 2 -> buscarCliente();
                case 3 -> listarClientes();
                case 0 -> voltar = true;
                default -> System.out.println("⚠️ Opção inválida!");
            }
            
            if (!voltar) {
                pausar();
            }
        }
    }

    private void mostrarOpcoes() {
        System.out.println("\n" + "─".repeat(50));
        System.out.println("👥 GERENCIAMENTO DE CLIENTES");
        System.out.println("─".repeat(50));
        System.out.println("➕ 1. Criar Cliente");
        System.out.println("🔍 2. Buscar Cliente (CPF)");
        System.out.println("📋 3. Listar Todos os Clientes");
        System.out.println("⬅️ 0. Voltar");
        System.out.println("─".repeat(50));
        System.out.print("🔴 Escolha uma opção: ");
    }

    private void criarCliente() {
        System.out.println("\n➕ CRIAR NOVO CLIENTE");
        System.out.println("─".repeat(30));
        
        try {
            System.out.print("📝 Nome completo: ");
            String nome = scanner.nextLine().trim();
            
            System.out.print("🏗️ CPF (apenas números): ");
            String cpf = scanner.nextLine().trim();
            
            if (bancoService.buscarClientePorCpf(cpf).isPresent()) {
                System.out.println("⚠️ Cliente já existe com este CPF!");
                return;
            }
            
            System.out.print("📧 Email: ");
            String email = scanner.nextLine().trim();
            
            System.out.print("📞 Telefone: ");
            String telefone = scanner.nextLine().trim();
            
            System.out.print("🎂 Data de nascimento (dd/MM/yyyy): ");
            String dataStr = scanner.nextLine().trim();
            LocalDate dataNascimento = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            
            Cliente cliente = bancoService.criarCliente(nome, cpf, email, telefone, dataNascimento);
            
            System.out.println("✅ Cliente criado com sucesso!");
            System.out.println("🎉 " + cliente);
            
        } catch (Exception e) {
            System.out.println("❌ Erro ao criar cliente: " + e.getMessage());
        }
    }

    private void buscarCliente() {
        System.out.println("\n🔍 BUSCAR CLIENTE");
        System.out.println("─".repeat(25));
        
        System.out.print("🏗️ Digite o CPF: ");
        String cpf = scanner.nextLine().trim();
        
        Optional<Cliente> clienteOpt = bancoService.buscarClientePorCpf(cpf);
        
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            System.out.println("✅ Cliente encontrado:");
            exibirDetalhesCliente(cliente);
        } else {
            System.out.println("❌ Cliente não encontrado!");
        }
    }

    private void listarClientes() {
        System.out.println("\n📋 LISTA DE CLIENTES");
        System.out.println("─".repeat(30));
        
        List<Cliente> clientes = bancoService.listarClientes();
        
        if (clientes.isEmpty()) {
            System.out.println("😔 Nenhum cliente cadastrado.");
        } else {
            System.out.println("📈 Total de clientes: " + clientes.size());
            System.out.println();
            
            for (int i = 0; i < clientes.size(); i++) {
                System.out.println((i + 1) + ". " + clientes.get(i));
            }
        }
    }

    private void exibirDetalhesCliente(Cliente cliente) {
        System.out.println("┌" + "─".repeat(40) + "┐");
        System.out.println("│ " + String.format("%-38s", "DADOS DO CLIENTE") + " │");
        System.out.println("├" + "─".repeat(40) + "┤");
        System.out.println("│ Nome: " + String.format("%-30s", cliente.getNome()) + " │");
        System.out.println("│ CPF:  " + String.format("%-30s", cliente.getCpf()) + " │");
        System.out.println("│ Email: " + String.format("%-29s", cliente.getEmail()) + " │");
        System.out.println("│ Telefone: " + String.format("%-25s", cliente.getTelefone()) + " │");
        
        if (cliente.getDataNascimento() != null) {
            String data = cliente.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            System.out.println("│ Nascimento: " + String.format("%-22s", data) + " │");
        }
        
        System.out.println("└" + "─".repeat(40) + "┘");
    }

    private int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void pausar() {
        System.out.println("\n🔄 Pressione ENTER para continuar...");
        scanner.nextLine();
    }
}