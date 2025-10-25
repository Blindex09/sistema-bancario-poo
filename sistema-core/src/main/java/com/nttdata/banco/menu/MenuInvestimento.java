package com.nttdata.banco.menu;

import com.nttdata.banco.enums.TipoInvestimento;
import com.nttdata.banco.model.Investimento;
import com.nttdata.banco.service.BancoService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

/**
 * Menu para gerenciamento de investimentos
 */
public class MenuInvestimento {
    private final BancoService bancoService;
    private final Scanner scanner;

    public MenuInvestimento(BancoService bancoService, Scanner scanner) {
        this.bancoService = bancoService;
        this.scanner = scanner;
    }

    public void exibir() {
        boolean voltar = false;
        
        while (!voltar) {
            mostrarOpcoes();
            int opcao = lerOpcao();
            
            switch (opcao) {
                case 1 -> realizarInvestimento();
                case 2 -> resgatarInvestimento();
                case 3 -> consultarInvestimentos();
                case 4 -> listarTiposInvestimento();
                case 5 -> simularInvestimento();
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
        System.out.println("📈 GERENCIAMENTO DE INVESTIMENTOS");
        System.out.println("─".repeat(50));
        System.out.println("💰 1. Realizar Investimento");
        System.out.println("📤 2. Resgatar Investimento");
        System.out.println("🔍 3. Consultar Meus Investimentos");
        System.out.println("📋 4. Tipos de Investimento Disponíveis");
        System.out.println("🎯 5. Simular Investimento");
        System.out.println("⬅️ 0. Voltar");
        System.out.println("─".repeat(50));
        System.out.print("🔴 Escolha uma opção: ");
    }

    private void realizarInvestimento() {
        System.out.println("\n💰 REALIZAR INVESTIMENTO");
        System.out.println("─".repeat(30));
        
        try {
            String[] dadosConta = lerDadosConta();
            if (dadosConta == null) return;
            
            // Consulta saldo da conta
            var contaOpt = bancoService.buscarConta(dadosConta[0], dadosConta[1]);
            if (contaOpt.isEmpty()) {
                System.out.println("❌ Conta não encontrada!");
                return;
            }
            
            var conta = contaOpt.get();
            System.out.printf("💳 Saldo disponível: R$ %.2f%n", conta.getSaldo());
            
            // Lista tipos de investimento
            System.out.println("\n📋 Tipos de investimento disponíveis:");
            TipoInvestimento[] tipos = TipoInvestimento.values();
            for (int i = 0; i < tipos.length; i++) {
                System.out.printf("%d. %s%n", (i + 1), tipos[i]);
            }
            
            System.out.print("\n🔢 Escolha o tipo (1-" + tipos.length + "): ");
            int tipoEscolhido = Integer.parseInt(scanner.nextLine().trim()) - 1;
            
            if (tipoEscolhido < 0 || tipoEscolhido >= tipos.length) {
                System.out.println("⚠️ Tipo inválido!");
                return;
            }
            
            TipoInvestimento tipo = tipos[tipoEscolhido];
            
            System.out.print("💵 Valor a investir: R$ ");
            BigDecimal valor = new BigDecimal(scanner.nextLine().trim());
            
            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("⚠️ Valor deve ser positivo!");
                return;
            }
            
            if (valor.compareTo(conta.getSaldo()) > 0) {
                System.out.println("⚠️ Saldo insuficiente!");
                return;
            }
            
            boolean sucesso = bancoService.investir(dadosConta[0], dadosConta[1], tipo, valor);
            
            if (sucesso) {
                System.out.println("✅ Investimento realizado com sucesso!");
                System.out.printf("💰 Valor investido: R$ %.2f%n", valor);
                System.out.printf("📈 Tipo: %s%n", tipo.getNome());
                System.out.printf("📅 Rentabilidade esperada: %.2f%% ao mês%n", 
                    tipo.getRentabilidadeMensal() * 100);
                System.out.println("🎆 Parabéns! Você está investindo no seu futuro!");
            } else {
                System.out.println("❌ Erro ao realizar investimento!");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }

    private void resgatarInvestimento() {
        System.out.println("\n📤 RESGATAR INVESTIMENTO");
        System.out.println("─".repeat(30));
        
        try {
            System.out.print("🏷️ CPF do titular: ");
            String cpf = scanner.nextLine().trim();
            
            List<Investimento> investimentos = bancoService.listarInvestimentosPorCpf(cpf);
            
            if (investimentos.isEmpty()) {
                System.out.println("❌ Nenhum investimento encontrado para este CPF!");
                return;
            }
            
            // Lista investimentos ativos
            List<Investimento> ativos = investimentos.stream()
                .filter(Investimento::isAtivo)
                .toList();
            
            if (ativos.isEmpty()) {
                System.out.println("❌ Nenhum investimento ativo encontrado!");
                return;
            }
            
            System.out.println("\n📋 Investimentos ativos:");
            for (int i = 0; i < ativos.size(); i++) {
                Investimento inv = ativos.get(i);
                System.out.printf("%d. %s - R$ %.2f -> R$ %.2f (Rendimento: R$ %.2f)%n", 
                    (i + 1), inv.getTipo().getNome(), inv.getValor(), 
                    inv.calcularValorAtual(), inv.calcularRendimento());
                System.out.printf("   Dias para vencimento: %d %s%n", 
                    inv.getDiasParaVencimento(),
                    inv.podeResgatar() ? "(PODE RESGATAR)" : "(AINDA NÃO VENCEU)");
            }
            
            System.out.print("\n🔢 Escolha o investimento (1-" + ativos.size() + "): ");
            int investimentoEscolhido = Integer.parseInt(scanner.nextLine().trim()) - 1;
            
            if (investimentoEscolhido < 0 || investimentoEscolhido >= ativos.size()) {
                System.out.println("⚠️ Investimento inválido!");
                return;
            }
            
            Investimento investimento = ativos.get(investimentoEscolhido);
            
            if (!investimento.podeResgatar()) {
                System.out.println("⚠️ Este investimento ainda não pode ser resgatado!");
                System.out.printf("Dias restantes para vencimento: %d%n", 
                    investimento.getDiasParaVencimento());
                return;
            }
            
            String[] dadosConta = lerDadosConta();
            if (dadosConta == null) return;
            
            boolean sucesso = bancoService.resgatarInvestimento(
                investimento.getId(), dadosConta[0], dadosConta[1]);
            
            if (sucesso) {
                System.out.println("✅ Resgate realizado com sucesso!");
                System.out.printf("💰 Valor resgatado: R$ %.2f%n", investimento.calcularValorAtual());
                System.out.printf("🎆 Rendimento obtido: R$ %.2f (%.2f%%)%n", 
                    investimento.calcularRendimento(), investimento.calcularRentabilidadePercentual());
            } else {
                System.out.println("❌ Erro ao resgatar investimento!");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }

    private void consultarInvestimentos() {
        System.out.println("\n🔍 CONSULTAR INVESTIMENTOS");
        System.out.println("─".repeat(35));
        
        try {
            System.out.print("🏷️ CPF do titular: ");
            String cpf = scanner.nextLine().trim();
            
            List<Investimento> investimentos = bancoService.listarInvestimentosPorCpf(cpf);
            
            if (investimentos.isEmpty()) {
                System.out.println("❌ Nenhum investimento encontrado para este CPF!");
                return;
            }
            
            System.out.println("\n📊 PORTFÓLIO DE INVESTIMENTOS");
            System.out.println("=".repeat(60));
            
            BigDecimal totalInvestido = BigDecimal.ZERO;
            BigDecimal totalAtual = BigDecimal.ZERO;
            
            for (int i = 0; i < investimentos.size(); i++) {
                Investimento inv = investimentos.get(i);
                System.out.println((i + 1) + ". " + inv);
                System.out.println("-".repeat(60));
                
                if (inv.isAtivo()) {
                    totalInvestido = totalInvestido.add(inv.getValor());
                    totalAtual = totalAtual.add(inv.calcularValorAtual());
                }
            }
            
            System.out.println("\n📊 RESUMO DO PORTFÓLIO:");
            System.out.printf("💰 Total Investido: R$ %.2f%n", totalInvestido);
            System.out.printf("🏆 Valor Atual: R$ %.2f%n", totalAtual);
            System.out.printf("📈 Rendimento Total: R$ %.2f%n", totalAtual.subtract(totalInvestido));
            
            if (totalInvestido.compareTo(BigDecimal.ZERO) > 0) {
                double percentual = totalAtual.subtract(totalInvestido)
                    .divide(totalInvestido, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"))
                    .doubleValue();
                System.out.printf("📅 Rentabilidade: %.2f%%%n", percentual);
            }
            
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }

    private void listarTiposInvestimento() {
        System.out.println("\n📋 TIPOS DE INVESTIMENTO DISPONÍVEIS");
        System.out.println("=".repeat(50));
        
        TipoInvestimento[] tipos = TipoInvestimento.values();
        
        for (int i = 0; i < tipos.length; i++) {
            TipoInvestimento tipo = tipos[i];
            System.out.printf("%d. %s%n", (i + 1), tipo);
            System.out.printf("   Rentabilidade: %.2f%% ao mês%n", 
                tipo.getRentabilidadeMensal() * 100);
            System.out.println("-".repeat(50));
        }
        
        System.out.println("\n💡 DICA: Diversifique seus investimentos para reduzir riscos!");
    }

    private void simularInvestimento() {
        System.out.println("\n🎯 SIMULAR INVESTIMENTO");
        System.out.println("─".repeat(30));
        
        try {
            System.out.println("📋 Tipos de investimento:");
            TipoInvestimento[] tipos = TipoInvestimento.values();
            for (int i = 0; i < tipos.length; i++) {
                System.out.printf("%d. %s (%.2f%% a.m.)%n", 
                    (i + 1), tipos[i].getNome(), tipos[i].getRentabilidadeMensal() * 100);
            }
            
            System.out.print("\n🔢 Escolha o tipo (1-" + tipos.length + "): ");
            int tipoEscolhido = Integer.parseInt(scanner.nextLine().trim()) - 1;
            
            if (tipoEscolhido < 0 || tipoEscolhido >= tipos.length) {
                System.out.println("⚠️ Tipo inválido!");
                return;
            }
            
            TipoInvestimento tipo = tipos[tipoEscolhido];
            
            System.out.print("💵 Valor inicial: R$ ");
            BigDecimal valorInicial = new BigDecimal(scanner.nextLine().trim());
            
            System.out.print("📅 Número de meses: ");
            int meses = Integer.parseInt(scanner.nextLine().trim());
            
            // Simulação
            System.out.println("\n📈 SIMULAÇÃO DE INVESTIMENTO");
            System.out.println("=".repeat(50));
            System.out.printf("Investimento: %s%n", tipo.getNome());
            System.out.printf("Valor inicial: R$ %.2f%n", valorInicial);
            System.out.printf("Rentabilidade: %.2f%% ao mês%n", tipo.getRentabilidadeMensal() * 100);
            System.out.printf("Período: %d meses%n", meses);
            System.out.println("-".repeat(50));
            
            BigDecimal valorAtual = valorInicial;
            BigDecimal taxaMensal = BigDecimal.valueOf(tipo.getRentabilidadeMensal());
            
            for (int i = 1; i <= meses; i++) {
                valorAtual = valorAtual.multiply(BigDecimal.ONE.add(taxaMensal));
                
                if (i <= 12 || i % 6 == 0) { // Mostra os primeiros 12 meses e depois a cada 6 meses
                    System.out.printf("Mês %2d: R$ %.2f%n", i, valorAtual);
                }
            }
            
            BigDecimal rendimento = valorAtual.subtract(valorInicial);
            double percentualTotal = rendimento.divide(valorInicial, 4, java.math.RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100")).doubleValue();
            
            System.out.println("=".repeat(50));
            System.out.printf("🏆 Valor final: R$ %.2f%n", valorAtual);
            System.out.printf("📈 Rendimento: R$ %.2f (%.2f%%)%n", rendimento, percentualTotal);
            System.out.println("\n💡 Esta é apenas uma simulação. Rentabilidades passadas não garantem ganhos futuros.");
            
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