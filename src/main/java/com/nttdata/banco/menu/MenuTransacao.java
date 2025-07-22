package com.nttdata.banco.menu;

import com.nttdata.banco.service.BancoService;
import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Menu para transações bancárias
 */
public class MenuTransacao {
    private final BancoService bancoService;
    private final Scanner scanner;

    public MenuTransacao(BancoService bancoService, Scanner scanner) {
        this.bancoService = bancoService;
        this.scanner = scanner;
    }

    public void exibir() {
        boolean voltar = false;
        
        while (!voltar) {
            mostrarOpcoes();
            int opcao = lerOpcao();
            
            switch (opcao) {
                case 1 -> depositar();
                case 2 -> sacar();
                case 3 -> transferir();
                case 4 -> pix();
                case 5 -> consultarSaldo();
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
        System.out.println("💰 TRANSAÇÕES BANCÁRIAS");
        System.out.println("─".repeat(50));
        System.out.println("📥 1. Depósito");
        System.out.println("📤 2. Saque");
        System.out.println("🔄 3. Transferência");
        System.out.println("⚡ 4. PIX");
        System.out.println("💳 5. Consultar Saldo");
        System.out.println("⬅️ 0. Voltar");
        System.out.println("─".repeat(50));
        System.out.print("🔴 Escolha uma opção: ");
    }

    private void depositar() {
        System.out.println("\n📥 DEPÓSITO");
        System.out.println("─".repeat(15));
        
        try {
            String[] dadosConta = lerDadosConta();
            if (dadosConta == null) return;
            
            System.out.print("💵 Valor do depósito: R$ ");
            BigDecimal valor = new BigDecimal(scanner.nextLine().trim());
            
            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("⚠️ Valor deve ser positivo!");
                return;
            }
            
            boolean sucesso = bancoService.depositar(dadosConta[0], dadosConta[1], valor);
            
            if (sucesso) {
                System.out.println("✅ Depósito realizado com sucesso!");
                System.out.printf("💰 Valor depositado: R$ %.2f%n", valor);
            } else {
                System.out.println("❌ Erro ao realizar depósito!");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }

    private void sacar() {
        System.out.println("\n📤 SAQUE");
        System.out.println("─".repeat(12));
        
        try {
            String[] dadosConta = lerDadosConta();
            if (dadosConta == null) return;
            
            System.out.print("💵 Valor do saque: R$ ");
            BigDecimal valor = new BigDecimal(scanner.nextLine().trim());
            
            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("⚠️ Valor deve ser positivo!");
                return;
            }
            
            boolean sucesso = bancoService.sacar(dadosConta[0], dadosConta[1], valor);
            
            if (sucesso) {
                System.out.println("✅ Saque realizado com sucesso!");
                System.out.printf("💰 Valor sacado: R$ %.2f%n", valor);
                System.out.println("⚠️ Lembre-se: Contas correntes podem ter tarifas!");
            } else {
                System.out.println("❌ Saque não autorizado! Verifique o saldo ou limite.");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }

    private void transferir() {
        System.out.println("\n🔄 TRANSFERÊNCIA");
        System.out.println("─".repeat(20));
        
        try {
            System.out.println("📤 CONTA ORIGEM:");
            String[] dadosOrigem = lerDadosConta();
            if (dadosOrigem == null) return;
            
            System.out.println("\n📥 CONTA DESTINO:");
            String[] dadosDestino = lerDadosConta();
            if (dadosDestino == null) return;
            
            System.out.print("💵 Valor da transferência: R$ ");
            BigDecimal valor = new BigDecimal(scanner.nextLine().trim());
            
            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("⚠️ Valor deve ser positivo!");
                return;
            }
            
            boolean sucesso = bancoService.transferir(
                dadosOrigem[0], dadosOrigem[1],
                dadosDestino[0], dadosDestino[1],
                valor
            );
            
            if (sucesso) {
                System.out.println("✅ Transferência realizada com sucesso!");
                System.out.printf("💰 Valor transferido: R$ %.2f%n", valor);
                System.out.printf("📤 De: %s-%s para %s-%s%n", 
                    dadosOrigem[0], dadosOrigem[1], dadosDestino[0], dadosDestino[1]);
            } else {
                System.out.println("❌ Transferência não autorizada! Verifique os dados e saldo.");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }

    private void pix() {
        System.out.println("\n⚡ PIX - TRANSFERÊNCIA INSTANTÂNEA");
        System.out.println("─".repeat(40));
        
        try {
            System.out.println("📤 CONTA ORIGEM:");
            String[] dadosOrigem = lerDadosConta();
            if (dadosOrigem == null) return;
            
            System.out.print("🔑 Chave PIX (CPF do destinatário): ");
            String chavePix = scanner.nextLine().trim();
            
            System.out.print("💵 Valor do PIX: R$ ");
            BigDecimal valor = new BigDecimal(scanner.nextLine().trim());
            
            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("⚠️ Valor deve ser positivo!");
                return;
            }
            
            boolean sucesso = bancoService.pix(dadosOrigem[0], dadosOrigem[1], chavePix, valor);
            
            if (sucesso) {
                System.out.println("✅ PIX realizado com sucesso!");
                System.out.printf("💰 Valor enviado: R$ %.2f%n", valor);
                System.out.printf("🔑 Para a chave PIX: %s%n", chavePix);
                System.out.println("⚡ Transferência instantânea concluída!");
            } else {
                System.out.println("❌ PIX não autorizado! Verifique os dados, chave PIX e saldo.");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }

    private void consultarSaldo() {
        System.out.println("\n💳 CONSULTAR SALDO");
        System.out.println("─".repeat(22));
        
        try {
            String[] dadosConta = lerDadosConta();
            if (dadosConta == null) return;
            
            var contaOpt = bancoService.buscarConta(dadosConta[0], dadosConta[1]);
            
            if (contaOpt.isPresent()) {
                var conta = contaOpt.get();
                System.out.println("\n✅ Conta encontrada:");
                System.out.println("┌" + "─".repeat(40) + "┐");
                System.out.println("│ " + String.format("%-38s", "SALDO ATUAL") + " │");
                System.out.println("├" + "─".repeat(40) + "┤");
                System.out.println("│ Titular: " + String.format("%-27s", conta.getTitular().getNome()) + " │");
                System.out.println("│ Conta: " + String.format("%-29s", dadosConta[0] + "-" + dadosConta[1]) + " │");
                System.out.println("│ Tipo: " + String.format("%-30s", conta.getTipoConta().getDescricao()) + " │");
                System.out.printf("│ Saldo: R$ %25.2f │%n", conta.getSaldo());
                System.out.println("└" + "─".repeat(40) + "┘");
                
                // Mostra detalhes específicos do tipo de conta
                String detalhes = conta.getDetalhesEspecificos();
                if (!detalhes.isEmpty()) {
                    System.out.println("\n📋 Detalhes: " + detalhes);
                }
            } else {
                System.out.println("❌ Conta não encontrada!");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }

    private String[] lerDadosConta() {
        try {
            System.out.print("🏢 Agência: ");
            String agencia = scanner.nextLine().trim();
            
            System.out.print("🔢 Número da conta: ");
            String numero = scanner.nextLine().trim();
            
            return new String[]{agencia, numero};
        } catch (Exception e) {
            System.out.println("❌ Erro ao ler dados da conta: " + e.getMessage());
            return null;
        }
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