package com.nttdata.banco.menu;

import com.nttdata.banco.service.BancoService;
import java.util.Scanner;

/**
 * Menu principal do sistema bancário
 * Demonstra organização e navegação entre funcionalidades
 */
public class MenuPrincipal {
    private final BancoService bancoService;
    private final Scanner scanner;
    private final MenuCliente menuCliente;
    private final MenuConta menuConta;
    private final MenuTransacao menuTransacao;
    private final MenuInvestimento menuInvestimento;
    private final MenuRelatorio menuRelatorio;
    private boolean executando;

    public MenuPrincipal(BancoService bancoService) {
        this.bancoService = bancoService;
        this.scanner = new Scanner(System.in);
        this.menuCliente = new MenuCliente(bancoService, scanner);
        this.menuConta = new MenuConta(bancoService, scanner);
        this.menuTransacao = new MenuTransacao(bancoService, scanner);
        this.menuInvestimento = new MenuInvestimento(bancoService, scanner);
        this.menuRelatorio = new MenuRelatorio(bancoService, scanner);
        this.executando = true;
    }

    public void exibirMenu() {
        while (executando) {
            mostrarOpcoes();
            int opcao = lerOpcao();
            processarOpcao(opcao);
        }
        
        System.out.println("\n👋 Obrigado por usar o Sistema Bancário NTT Data!");
        scanner.close();
    }

    private void mostrarOpcoes() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("🏦 SISTEMA BANCÁRIO NTT DATA - MENU PRINCIPAL");
        System.out.println("═".repeat(60));
        System.out.println("👥 1. Gerenciar Clientes");
        System.out.println("💳 2. Gerenciar Contas");
        System.out.println("💰 3. Transações Bancárias");
        System.out.println("📈 4. Investimentos");
        System.out.println("📋 5. Relatórios e Consultas");
        System.out.println("❌ 0. Sair");
        System.out.println("═".repeat(60));
        System.out.print("🔴 Escolha uma opção: ");
    }

    private int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void processarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> menuCliente.exibir();
            case 2 -> menuConta.exibir();
            case 3 -> menuTransacao.exibir();
            case 4 -> menuInvestimento.exibir();
            case 5 -> menuRelatorio.exibir();
            case 0 -> {
                executando = false;
                return;
            }
            default -> {
                System.out.println("⚠️ Opção inválida! Tente novamente.");
                return;
            }
        }
        
        pausar();
    }

    private void pausar() {
        System.out.println("\n🔄 Pressione ENTER para continuar...");
        scanner.nextLine();
    }
}