package com.nttdata.banco.menu;

import com.nttdata.banco.enums.TipoConta;
import com.nttdata.banco.model.Conta;
import com.nttdata.banco.service.BancoService;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Menu para gerenciamento de contas
 */
public class MenuConta {
    private final BancoService bancoService;
    private final Scanner scanner;

    public MenuConta(BancoService bancoService, Scanner scanner) {
        this.bancoService = bancoService;
        this.scanner = scanner;
    }

    public void exibir() {
        boolean voltar = false;
        
        while (!voltar) {
            mostrarOpcoes();
            int opcao = lerOpcao();
            
            switch (opcao) {
                case 1 -> criarConta();
                case 2 -> buscarConta();
                case 3 -> listarContas();
                case 4 -> buscarContasPorCpf();
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
        System.out.println("💳 GERENCIAMENTO DE CONTAS");
        System.out.println("─".repeat(50));
        System.out.println("➕ 1. Criar Nova Conta");
        System.out.println("🔍 2. Buscar Conta (Agência/Número)");
        System.out.println("📋 3. Listar Todas as Contas");
        System.out.println("👤 4. Buscar Contas por CPF");
        System.out.println("⬅️ 0. Voltar");
        System.out.println("─".repeat(50));
        System.out.print("🔴 Escolha uma opção: ");
    }

    private void criarConta() {
        System.out.println("\n➕ CRIAR NOVA CONTA");
        System.out.println("─".repeat(25));
        
        try {
            System.out.print("🏷️ CPF do titular: ");
            String cpf = scanner.nextLine().trim();
            
            if (bancoService.buscarClientePorCpf(cpf).isEmpty()) {
                System.out.println("❌ Cliente não encontrado! Cadastre o cliente primeiro.");
                return;
            }
            
            System.out.println("\n📋 Tipos de conta disponíveis:");
            TipoConta[] tipos = TipoConta.values();
            for (int i = 0; i < tipos.length; i++) {
                System.out.println((i + 1) + ". " + tipos[i].getDescricao());
            }
            
            System.out.print("\n🔢 Escolha o tipo (1-" + tipos.length + "): ");
            int tipoEscolhido = Integer.parseInt(scanner.nextLine().trim()) - 1;
            
            if (tipoEscolhido < 0 || tipoEscolhido >= tipos.length) {
                System.out.println("⚠️ Tipo inválido!");
                return;
            }
            
            TipoConta tipoConta = tipos[tipoEscolhido];
            Conta conta = bancoService.criarConta(cpf, tipoConta);
            
            System.out.println("\n✅ Conta criada com sucesso!");
            exibirDetalhesConta(conta);
            
        } catch (Exception e) {
            System.out.println("❌ Erro ao criar conta: " + e.getMessage());
        }
    }

    private void buscarConta() {
        System.out.println("\n🔍 BUSCAR CONTA");
        System.out.println("─".repeat(20));
        
        try {
            System.out.print("🏢 Agência: ");
            String agencia = scanner.nextLine().trim();
            
            System.out.print("🔢 Número da conta: ");
            String numero = scanner.nextLine().trim();
            
            Optional<Conta> contaOpt = bancoService.buscarConta(agencia, numero);
            
            if (contaOpt.isPresent()) {
                System.out.println("\n✅ Conta encontrada:");
                exibirDetalhesConta(contaOpt.get());
            } else {
                System.out.println("❌ Conta não encontrada!");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Erro na busca: " + e.getMessage());
        }
    }

    private void listarContas() {
        System.out.println("\n📋 LISTA DE CONTAS");
        System.out.println("─".repeat(25));
        
        List<Conta> contas = bancoService.listarContas();
        
        if (contas.isEmpty()) {
            System.out.println("😔 Nenhuma conta cadastrada.");
        } else {
            System.out.println("📊 Total de contas: " + contas.size());
            System.out.println();
            
            for (int i = 0; i < contas.size(); i++) {
                Conta conta = contas.get(i);
                System.out.println((i + 1) + ". " + conta + " - " + conta.getTitular().getNome());
            }
        }
    }

    private void buscarContasPorCpf() {
        System.out.println("\n👤 BUSCAR CONTAS POR CPF");
        System.out.println("─".repeat(30));
        
        System.out.print("🏷️ CPF: ");
        String cpf = scanner.nextLine().trim();
        
        List<Conta> contas = bancoService.buscarContasPorCpf(cpf);
        
        if (contas.isEmpty()) {
            System.out.println("❌ Nenhuma conta encontrada para este CPF!");
        } else {
            System.out.println("\n✅ Contas encontradas: " + contas.size());
            System.out.println();
            
            for (int i = 0; i < contas.size(); i++) {
                System.out.println((i + 1) + ". ");
                exibirDetalhesConta(contas.get(i));
                System.out.println();
            }
        }
    }

    private void exibirDetalhesConta(Conta conta) {
        System.out.println("┌" + "─".repeat(50) + "┐");
        System.out.println("│ " + String.format("%-48s", "DADOS DA CONTA") + " │");
        System.out.println("├" + "─".repeat(50) + "┤");
        System.out.println("│ Tipo: " + String.format("%-41s", conta.getTipoConta().getDescricao()) + " │");
        System.out.println("│ Agência: " + String.format("%-37s", conta.getAgencia()) + " │");
        System.out.println("│ Número: " + String.format("%-38s", conta.getNumero()) + " │");
        System.out.println("│ Saldo: R$ " + String.format("%33.2f", conta.getSaldo()) + " │");
        System.out.println("│ Titular: " + String.format("%-36s", conta.getTitular().getNome()) + " │");
        System.out.println("│ Status: " + String.format("%-37s", conta.isAtiva() ? "Ativa" : "Inativa") + " │");
        
        String detalhes = conta.getDetalhesEspecificos();
        if (!detalhes.isEmpty()) {
            System.out.println("│ Detalhes: " + String.format("%-34s", detalhes) + " │");
        }
        
        System.out.println("└" + "─".repeat(50) + "┘");
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