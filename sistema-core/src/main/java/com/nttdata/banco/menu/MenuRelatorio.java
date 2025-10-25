package com.nttdata.banco.menu;

import com.nttdata.banco.model.Conta;
import com.nttdata.banco.model.Investimento;
import com.nttdata.banco.service.BancoService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

/**
 * Menu para relatórios e consultas
 */
public class MenuRelatorio {
    private final BancoService bancoService;
    private final Scanner scanner;

    public MenuRelatorio(BancoService bancoService, Scanner scanner) {
        this.bancoService = bancoService;
        this.scanner = scanner;
    }

    public void exibir() {
        boolean voltar = false;
        
        while (!voltar) {
            mostrarOpcoes();
            int opcao = lerOpcao();
            
            switch (opcao) {
                case 1 -> exibirExtrato();
                case 2 -> relatorioGeral();
                case 3 -> relatorioInvestimentos();
                case 4 -> posicaoConsolidada();
                case 5 -> rankingClientes();
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
        System.out.println("📋 RELATÓRIOS E CONSULTAS");
        System.out.println("─".repeat(50));
        System.out.println("📄 1. Extrato de Conta");
        System.out.println("📈 2. Relatório Geral do Banco");
        System.out.println("💰 3. Relatório de Investimentos");
        System.out.println("🏆 4. Posição Consolidada (CPF)");
        System.out.println("📅 5. Ranking de Clientes");
        System.out.println("⬅️ 0. Voltar");
        System.out.println("─".repeat(50));
        System.out.print("🔴 Escolha uma opção: ");
    }

    private void exibirExtrato() {
        System.out.println("\n📄 EXTRATO DE CONTA");
        System.out.println("─".repeat(25));
        
        try {
            String[] dadosConta = lerDadosConta();
            if (dadosConta == null) return;
            
            bancoService.exibirExtrato(dadosConta[0], dadosConta[1]);
            
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }

    private void relatorioGeral() {
        System.out.println("\n📈 RELATÓRIO GERAL DO BANCO");
        System.out.println("=".repeat(50));
        
        try {
            // Estatísticas de clientes
            int totalClientes = bancoService.getClienteRepository().getTotalClientes();
            
            // Estatísticas de contas
            int totalContas = bancoService.getContaRepository().getTotalContas();
            int contasAtivas = bancoService.getContaRepository().getTotalContasAtivas();
            
            // Estatísticas de investimentos
            int totalInvestimentos = bancoService.getInvestimentoRepository().getTotalInvestimentos();
            int investimentosAtivos = bancoService.getInvestimentoRepository().getTotalInvestimentosAtivos();
            BigDecimal valorTotalInvestido = bancoService.getInvestimentoRepository().getValorTotalInvestido();
            BigDecimal valorTotalAtual = bancoService.getInvestimentoRepository().getValorTotalAtual();
            
            // Saldo total em contas
            List<Conta> todasContas = bancoService.listarContas();
            BigDecimal saldoTotal = todasContas.stream()
                .filter(Conta::isAtiva)
                .map(Conta::getSaldo)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            System.out.println("👥 CLIENTES:");
            System.out.printf("   Total de clientes: %d%n", totalClientes);
            
            System.out.println("\n💳 CONTAS:");
            System.out.printf("   Total de contas: %d%n", totalContas);
            System.out.printf("   Contas ativas: %d%n", contasAtivas);
            System.out.printf("   Saldo total: R$ %.2f%n", saldoTotal);
            
            System.out.println("\n💰 INVESTIMENTOS:");
            System.out.printf("   Total de investimentos: %d%n", totalInvestimentos);
            System.out.printf("   Investimentos ativos: %d%n", investimentosAtivos);
            System.out.printf("   Valor total investido: R$ %.2f%n", valorTotalInvestido);
            System.out.printf("   Valor atual: R$ %.2f%n", valorTotalAtual);
            
            if (valorTotalInvestido.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal rendimento = valorTotalAtual.subtract(valorTotalInvestido);
                System.out.printf("   Rendimento total: R$ %.2f%n", rendimento);
            }
            
            System.out.println("\n🏆 PATRIMÔNIO TOTAL DO BANCO:");
            BigDecimal patrimonioTotal = saldoTotal.add(valorTotalAtual);
            System.out.printf("   R$ %.2f%n", patrimonioTotal);
            
            System.out.println("=".repeat(50));
            
        } catch (Exception e) {
            System.out.println("❌ Erro ao gerar relatório: " + e.getMessage());
        }
    }

    private void relatorioInvestimentos() {
        System.out.println("\n💰 RELATÓRIO DE INVESTIMENTOS");
        System.out.println("=".repeat(40));
        
        try {
            List<Investimento> todosInvestimentos = bancoService.listarTodosInvestimentos();
            
            if (todosInvestimentos.isEmpty()) {
                System.out.println("😔 Nenhum investimento encontrado.");
                return;
            }
            
            // Agrupa por tipo
            System.out.println("📈 INVESTIMENTOS POR TIPO:");
            System.out.println("-".repeat(40));
            
            for (var tipo : com.nttdata.banco.enums.TipoInvestimento.values()) {
                List<Investimento> investimentosTipo = todosInvestimentos.stream()
                    .filter(inv -> inv.getTipo() == tipo && inv.isAtivo())
                    .toList();
                
                if (!investimentosTipo.isEmpty()) {
                    BigDecimal valorTipo = investimentosTipo.stream()
                        .map(Investimento::getValor)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                    BigDecimal valorAtualTipo = investimentosTipo.stream()
                        .map(Investimento::calcularValorAtual)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                    System.out.printf("%s:%n", tipo.getNome());
                    System.out.printf("   Quantidade: %d investimentos%n", investimentosTipo.size());
                    System.out.printf("   Valor investido: R$ %.2f%n", valorTipo);
                    System.out.printf("   Valor atual: R$ %.2f%n", valorAtualTipo);
                    System.out.printf("   Rendimento: R$ %.2f%n", valorAtualTipo.subtract(valorTipo));
                    System.out.println();
                }
            }
            
            // Investimentos vencendo
            List<Investimento> vencendoEm30Dias = bancoService.getInvestimentoRepository()
                .buscarVencendoEm(30);
            
            if (!vencendoEm30Dias.isEmpty()) {
                System.out.println("⚠️ INVESTIMENTOS VENCENDO EM 30 DIAS:");
                System.out.println("-".repeat(40));
                
                for (Investimento inv : vencendoEm30Dias) {
                    System.out.printf("%s - R$ %.2f (%d dias)%n", 
                        inv.getTipo().getNome(), inv.calcularValorAtual(), inv.getDiasParaVencimento());
                }
                System.out.println();
            }
            
        } catch (Exception e) {
            System.out.println("❌ Erro ao gerar relatório: " + e.getMessage());
        }
    }

    private void posicaoConsolidada() {
        System.out.println("\n🏆 POSIÇÃO CONSOLIDADA");
        System.out.println("─".repeat(30));
        
        try {
            System.out.print("🏷️ CPF: ");
            String cpf = scanner.nextLine().trim();
            
            var clienteOpt = bancoService.buscarClientePorCpf(cpf);
            if (clienteOpt.isEmpty()) {
                System.out.println("❌ Cliente não encontrado!");
                return;
            }
            
            var cliente = clienteOpt.get();
            List<Conta> contas = bancoService.buscarContasPorCpf(cpf);
            List<Investimento> investimentos = bancoService.listarInvestimentosPorCpf(cpf);
            
            System.out.println("\n" + "=".repeat(60));
            System.out.println("             POSIÇÃO CONSOLIDADA - " + cliente.getNome().toUpperCase());
            System.out.println("=".repeat(60));
            
            // Contas
            BigDecimal saldoTotalContas = BigDecimal.ZERO;
            System.out.println("💳 CONTAS:");
            
            if (contas.isEmpty()) {
                System.out.println("   Nenhuma conta encontrada.");
            } else {
                for (Conta conta : contas) {
                    System.out.printf("   %s - Ag: %s, Conta: %s - R$ %.2f%n", 
                        conta.getTipoConta().getDescricao(), conta.getAgencia(), 
                        conta.getNumero(), conta.getSaldo());
                    saldoTotalContas = saldoTotalContas.add(conta.getSaldo());
                }
            }
            System.out.printf("   TOTAL EM CONTAS: R$ %.2f%n", saldoTotalContas);
            
            // Investimentos
            BigDecimal valorTotalInvestimentos = BigDecimal.ZERO;
            BigDecimal valorAtualInvestimentos = BigDecimal.ZERO;
            
            System.out.println("\n💰 INVESTIMENTOS:");
            
            if (investimentos.isEmpty()) {
                System.out.println("   Nenhum investimento encontrado.");
            } else {
                List<Investimento> ativos = investimentos.stream()
                    .filter(Investimento::isAtivo)
                    .toList();
                
                for (Investimento inv : ativos) {
                    System.out.printf("   %s - R$ %.2f -> R$ %.2f (%.2f%%)%n", 
                        inv.getTipo().getNome(), inv.getValor(), 
                        inv.calcularValorAtual(), inv.calcularRentabilidadePercentual());
                    
                    valorTotalInvestimentos = valorTotalInvestimentos.add(inv.getValor());
                    valorAtualInvestimentos = valorAtualInvestimentos.add(inv.calcularValorAtual());
                }
            }
            
            System.out.printf("   TOTAL INVESTIDO: R$ %.2f%n", valorTotalInvestimentos);
            System.out.printf("   VALOR ATUAL: R$ %.2f%n", valorAtualInvestimentos);
            System.out.printf("   RENDIMENTO: R$ %.2f%n", valorAtualInvestimentos.subtract(valorTotalInvestimentos));
            
            // Patrimônio total
            BigDecimal patrimonioTotal = saldoTotalContas.add(valorAtualInvestimentos);
            
            System.out.println("\n" + "=".repeat(60));
            System.out.printf("🏆 PATRIMÔNIO TOTAL: R$ %.2f%n", patrimonioTotal);
            System.out.println("=".repeat(60));
            
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }

    private void rankingClientes() {
        System.out.println("\n📅 RANKING DE CLIENTES");
        System.out.println("=".repeat(30));
        
        try {
            var clientes = bancoService.listarClientes();
            
            if (clientes.isEmpty()) {
                System.out.println("😔 Nenhum cliente encontrado.");
                return;
            }
            
            // Calcula patrimônio de cada cliente
            System.out.println("🏆 TOP CLIENTES POR PATRIMÔNIO:");
            System.out.println("-".repeat(50));
            
            clientes.stream()
                .map(cliente -> {
                    List<Conta> contas = bancoService.buscarContasPorCpf(cliente.getCpf());
                    List<Investimento> investimentos = bancoService.listarInvestimentosPorCpf(cliente.getCpf());
                    
                    BigDecimal saldoContas = contas.stream()
                        .map(Conta::getSaldo)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                    BigDecimal valorInvestimentos = investimentos.stream()
                        .filter(Investimento::isAtivo)
                        .map(Investimento::calcularValorAtual)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                    BigDecimal patrimonioTotal = saldoContas.add(valorInvestimentos);
                    
                    return new Object[]{
                        cliente.getNome(),
                        cliente.getCpf(),
                        contas.size(),
                        investimentos.size(),
                        patrimonioTotal
                    };
                })
                .sorted((a, b) -> ((BigDecimal) b[4]).compareTo((BigDecimal) a[4]))
                .limit(10)
                .forEach(dados -> {
                    System.out.printf("%s (CPF: %s)%n", dados[0], dados[1]);
                    System.out.printf("   Contas: %d | Investimentos: %d | Patrimônio: R$ %.2f%n", 
                        dados[2], dados[3], dados[4]);
                    System.out.println("-".repeat(50));
                });
            
        } catch (Exception e) {
            System.out.println("❌ Erro ao gerar ranking: " + e.getMessage());
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