package com.nttdata.banco.gui.view;

import com.nttdata.banco.gui.components.AccessibleButton;
import com.nttdata.banco.gui.components.AccessibleTextField;
import com.nttdata.banco.gui.theme.ThemeManager;
import com.nttdata.banco.gui.utils.AccessibilityUtils;
import com.nttdata.banco.gui.utils.SoundUtils;
import com.nttdata.banco.service.BancoService;

import javax.swing.*;
import java.awt.*;

/**
 * Tela de Relatórios e Consultas - Totalmente Acessível
 * Permite gerar relatórios e consultas diversas do sistema
 */
public class RelatorioView extends JDialog {
    
    private BancoService bancoService;
    private JTabbedPane abas;
    
    // Campos para extrato
    private AccessibleTextField campoAgenciaExtrato;
    private AccessibleTextField campoNumeroExtrato;
    
    // Campos para posição consolidada
    private AccessibleTextField campoCpfPosicao;
    
    // Área de resultado
    private JTextArea areaResultado;
    
    public RelatorioView(Frame parent, BancoService bancoService) {
        super(parent, "Relatórios e Consultas", true);
        this.bancoService = bancoService;
        
        inicializarInterface();
        criarAbas();
        configurarAcessibilidade();
        
        setSize(1000, 700);
        setLocationRelativeTo(parent);
    }
    
    private void inicializarInterface() {
        setLayout(new BorderLayout());
        
        // Título
        JLabel titulo = new JLabel("📊 Relatórios e Consultas", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(20f));
        var config = ThemeManager.getConfig();
        titulo.setForeground(config.getPrimary());
        add(titulo, BorderLayout.NORTH);
        
        // Painel principal com abas
        abas = new JTabbedPane();
        add(abas, BorderLayout.CENTER);
        
        // Botão fechar
        JPanel painelBotoes = new JPanel(new FlowLayout());
        AccessibleButton btnFechar = new AccessibleButton("Fechar", "F");
        btnFechar.addActionListener(e -> dispose());
        painelBotoes.add(btnFechar);
        add(painelBotoes, BorderLayout.SOUTH);
    }
    
    private void criarAbas() {
        // Aba 1: Extrato de Conta
        JPanel abaExtrato = criarAbaExtrato();
        abas.addTab("📋 Extrato", abaExtrato);
        
        // Aba 2: Relatório Geral
        JPanel abaRelatorio = criarAbaRelatorioGeral();
        abas.addTab("📊 Relatório Geral", abaRelatorio);
        
        // Aba 3: Posição Consolidada
        JPanel abaPosicao = criarAbaPosicaoConsolidada();
        abas.addTab("💼 Posição Consolidada", abaPosicao);
        
        // Aba 4: Ranking de Clientes
        JPanel abaRanking = criarAbaRanking();
        abas.addTab("🏆 Ranking", abaRanking);
        
        // Aba 5: Resultado
        JPanel abaResultado = criarAbaResultado();
        abas.addTab("📄 Resultado", abaResultado);
    }
    
    private JPanel criarAbaExtrato() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Título
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel titulo = new JLabel("📋 Extrato de Conta");
        titulo.setFont(titulo.getFont().deriveFont(16f));
        painel.add(titulo, gbc);
        
        // Campos
        gbc.gridwidth = 1;
        
        // Agência
        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(new JLabel("Agência:"), gbc);
        gbc.gridx = 1;
        campoAgenciaExtrato = new AccessibleTextField("Agência", "Agência da conta", true);
        campoAgenciaExtrato.setText("0001");
        campoAgenciaExtrato.setPreferredSize(new Dimension(200, 40));
        painel.add(campoAgenciaExtrato, gbc);
        
        // Número da Conta
        gbc.gridx = 0; gbc.gridy = 2;
        painel.add(new JLabel("Número da Conta:"), gbc);
        gbc.gridx = 1;
        campoNumeroExtrato = new AccessibleTextField("Número", "Número da conta", true);
        campoNumeroExtrato.setPreferredSize(new Dimension(200, 40));
        painel.add(campoNumeroExtrato, gbc);
        
        // Informações
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JTextArea infoExtrato = new JTextArea(4, 50);
        infoExtrato.setEditable(false);
        infoExtrato.setText(
            "O extrato mostra:\n" +
            "• Informações da conta e titular\n" +
            "• Histórico completo de transações\n" +
            "• Saldo atual e movimentações"
        );
        infoExtrato.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        infoExtrato.setBackground(painel.getBackground());
        AccessibilityUtils.configurarAcessibilidade(infoExtrato, 
            "Informações sobre extrato", "Descrição do que é mostrado no extrato");
        
        JScrollPane scrollInfoExtrato = new JScrollPane(infoExtrato);
        scrollInfoExtrato.setBorder(BorderFactory.createTitledBorder("Informações"));
        painel.add(scrollInfoExtrato, gbc);
        
        // Botão
        gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        AccessibleButton btnExtrato = new AccessibleButton("📋 Gerar Extrato", "E");
        btnExtrato.setPreferredSize(new Dimension(200, 50));
        btnExtrato.addActionListener(e -> gerarExtrato());
        painel.add(btnExtrato, gbc);
        
        return painel;
    }
    
    private JPanel criarAbaRelatorioGeral() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Título
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel titulo = new JLabel("📊 Relatório Geral do Banco");
        titulo.setFont(titulo.getFont().deriveFont(16f));
        painel.add(titulo, gbc);
        
        // Informações
        gbc.gridy = 1;
        JTextArea infoRelatorio = new JTextArea(6, 50);
        infoRelatorio.setEditable(false);
        infoRelatorio.setText(
            "O Relatório Geral mostra:\n\n" +
            "• Total de clientes cadastrados\n" +
            "• Número de contas ativas\n" +
            "• Saldo total do banco\n" +
            "• Estatísticas de investimentos\n" +
            "• Patrimônio total gerenciado"
        );
        infoRelatorio.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        infoRelatorio.setBackground(painel.getBackground());
        AccessibilityUtils.configurarAcessibilidade(infoRelatorio, 
            "Informações sobre relatório geral", "Descrição do relatório geral do banco");
        
        JScrollPane scrollInfoRelatorio = new JScrollPane(infoRelatorio);
        scrollInfoRelatorio.setBorder(BorderFactory.createTitledBorder("Informações"));
        painel.add(scrollInfoRelatorio, gbc);
        
        // Botão
        gbc.gridy = 2;
        AccessibleButton btnRelatorio = new AccessibleButton("📊 Gerar Relatório Geral", "G");
        btnRelatorio.setPreferredSize(new Dimension(250, 50));
        btnRelatorio.addActionListener(e -> gerarRelatorioGeral());
        painel.add(btnRelatorio, gbc);
        
        return painel;
    }
    
    private JPanel criarAbaPosicaoConsolidada() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Título
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel titulo = new JLabel("💼 Posição Consolidada por CPF");
        titulo.setFont(titulo.getFont().deriveFont(16f));
        painel.add(titulo, gbc);
        
        // Campo CPF
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(new JLabel("CPF do Cliente:"), gbc);
        gbc.gridx = 1;
        campoCpfPosicao = new AccessibleTextField("CPF", "CPF do cliente para consulta", true);
        campoCpfPosicao.setPreferredSize(new Dimension(200, 40));
        painel.add(campoCpfPosicao, gbc);
        
        // Informações
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JTextArea infoPosicao = new JTextArea(5, 50);
        infoPosicao.setEditable(false);
        infoPosicao.setText(
            "A Posição Consolidada mostra:\n" +
            "• Todas as contas do cliente\n" +
            "• Saldos individuais e total\n" +
            "• Investimentos ativos\n" +
            "• Patrimônio total consolidado"
        );
        infoPosicao.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        infoPosicao.setBackground(painel.getBackground());
        AccessibilityUtils.configurarAcessibilidade(infoPosicao, 
            "Informações sobre posição consolidada", "Descrição da posição consolidada por cliente");
        
        JScrollPane scrollInfoPosicao = new JScrollPane(infoPosicao);
        scrollInfoPosicao.setBorder(BorderFactory.createTitledBorder("Informações"));
        painel.add(scrollInfoPosicao, gbc);
        
        // Botão
        gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        AccessibleButton btnPosicao = new AccessibleButton("💼 Consultar Posição", "P");
        btnPosicao.setPreferredSize(new Dimension(200, 50));
        btnPosicao.addActionListener(e -> consultarPosicaoConsolidada());
        painel.add(btnPosicao, gbc);
        
        return painel;
    }
    
    private JPanel criarAbaRanking() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Título
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel titulo = new JLabel("🏆 Ranking de Clientes");
        titulo.setFont(titulo.getFont().deriveFont(16f));
        painel.add(titulo, gbc);
        
        // Informações
        gbc.gridy = 1;
        JTextArea infoRanking = new JTextArea(5, 50);
        infoRanking.setEditable(false);
        infoRanking.setText(
            "O Ranking de Clientes mostra:\n\n" +
            "• Clientes ordenados por patrimônio total\n" +
            "• Saldo em contas + valor dos investimentos\n" +
            "• Identificação dos maiores investidores"
        );
        infoRanking.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        infoRanking.setBackground(painel.getBackground());
        AccessibilityUtils.configurarAcessibilidade(infoRanking, 
            "Informações sobre ranking", "Descrição do ranking de clientes");
        
        JScrollPane scrollInfoRanking = new JScrollPane(infoRanking);
        scrollInfoRanking.setBorder(BorderFactory.createTitledBorder("Informações"));
        painel.add(scrollInfoRanking, gbc);
        
        // Botão
        gbc.gridy = 2;
        AccessibleButton btnRanking = new AccessibleButton("🏆 Gerar Ranking", "R");
        btnRanking.setPreferredSize(new Dimension(200, 50));
        btnRanking.addActionListener(e -> gerarRanking());
        painel.add(btnRanking, gbc);
        
        return painel;
    }
    
    private JPanel criarAbaResultado() {
        JPanel painel = new JPanel(new BorderLayout());
        
        // Título
        JPanel painelTitulo = new JPanel(new FlowLayout());
        JLabel titulo = new JLabel("📄 Resultado dos Relatórios");
        titulo.setFont(titulo.getFont().deriveFont(16f));
        painelTitulo.add(titulo);
        painel.add(painelTitulo, BorderLayout.NORTH);
        
        // Área de resultado
        areaResultado = new JTextArea(30, 80);
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        areaResultado.setText("Selecione um relatório nas abas anteriores para visualizar os resultados aqui.");
        AccessibilityUtils.configurarAcessibilidade(areaResultado, 
            "Resultado dos relatórios", "Área onde são exibidos os resultados dos relatórios gerados");
        
        JScrollPane scrollResultado = new JScrollPane(areaResultado);
        scrollResultado.setBorder(BorderFactory.createTitledBorder("Resultado"));
        painel.add(scrollResultado, BorderLayout.CENTER);
        
        return painel;
    }
    
    // ================== FUNCIONALIDADES ==================
    
    private void gerarExtrato() {
        try {
            String agencia = campoAgenciaExtrato.getText().trim();
            String numero = campoNumeroExtrato.getText().trim();
            
            if (agencia.isEmpty()) {
                campoAgenciaExtrato.marcarComoInvalido("Agência é obrigatória");
                SoundUtils.tocarErro();
                return;
            }
            
            if (numero.isEmpty()) {
                campoNumeroExtrato.marcarComoInvalido("Número da conta é obrigatório");
                SoundUtils.tocarErro();
                return;
            }
            
            campoAgenciaExtrato.marcarComoValido();
            campoNumeroExtrato.marcarComoValido();
            
            // Capturar saída do extrato (simular)
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            java.io.PrintStream ps = new java.io.PrintStream(baos);
            java.io.PrintStream originalOut = System.out;
            
            try {
                System.setOut(ps);
                bancoService.exibirExtrato(agencia, numero);
                System.setOut(originalOut);
                
                String extrato = baos.toString();
                
                if (extrato.trim().isEmpty()) {
                    mostrarErro("Conta não encontrada: " + agencia + "-" + numero);
                } else {
                    mostrarResultado("EXTRATO DA CONTA\n" +
                                   "================\n\n" + extrato);
                    AccessibilityUtils.anunciarMensagem("Extrato gerado com sucesso");
                }
                
            } finally {
                System.setOut(originalOut);
                ps.close();
            }
            
        } catch (Exception e) {
            mostrarErro("Erro ao gerar extrato: " + e.getMessage());
        }
    }
    
    private void gerarRelatorioGeral() {
        try {
            StringBuilder relatorio = new StringBuilder();
            relatorio.append("RELATÓRIO GERAL DO BANCO\n");
            relatorio.append("========================\n\n");
            relatorio.append("Data/Hora: ").append(
                java.time.LocalDateTime.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))).append("\n\n");
            
            // Estatísticas de clientes
            var totalClientes = bancoService.getClienteRepository().getTotalClientes();
            relatorio.append("👥 CLIENTES:\n");
            relatorio.append("   Total de clientes: ").append(totalClientes).append("\n\n");
            
            // Estatísticas de contas
            var totalContas = bancoService.getContaRepository().getTotalContas();
            var contasAtivas = bancoService.getContaRepository().getTotalContasAtivas();
            var todasContas = bancoService.listarContas();
            
            // Calcular saldo total
            java.math.BigDecimal saldoTotal = todasContas.stream()
                .map(conta -> conta.getSaldo())
                .reduce(java.math.BigDecimal.ZERO, (a, b) -> a.add(b));
            
            relatorio.append("💳 CONTAS:\n");
            relatorio.append("   Total de contas: ").append(totalContas).append("\n");
            relatorio.append("   Contas ativas: ").append(contasAtivas).append("\n");
            relatorio.append("   Saldo total: R$ ").append(String.format("%.2f", saldoTotal)).append("\n\n");
            
            // Estatísticas de investimentos
            var todosInvestimentos = bancoService.listarTodosInvestimentos();
            var investimentosAtivos = (int) todosInvestimentos.stream()
                .filter(inv -> inv.isAtivo())
                .count();
            
            java.math.BigDecimal valorTotalInvestido = todosInvestimentos.stream()
                .filter(inv -> inv.isAtivo())
                .map(inv -> inv.getValor())
                .reduce(java.math.BigDecimal.ZERO, (a, b) -> a.add(b));
            
            java.math.BigDecimal valorAtualInvestimentos = todosInvestimentos.stream()
                .filter(inv -> inv.isAtivo())
                .map(inv -> inv.calcularValorAtual())
                .reduce(java.math.BigDecimal.ZERO, (a, b) -> a.add(b));
            
            relatorio.append("📈 INVESTIMENTOS:\n");
            relatorio.append("   Total de investimentos: ").append(todosInvestimentos.size()).append("\n");
            relatorio.append("   Investimentos ativos: ").append(investimentosAtivos).append("\n");
            relatorio.append("   Valor total investido: R$ ").append(String.format("%.2f", valorTotalInvestido)).append("\n");
            relatorio.append("   Valor atual: R$ ").append(String.format("%.2f", valorAtualInvestimentos)).append("\n\n");
            
            // Patrimônio total
            java.math.BigDecimal patrimonioTotal = saldoTotal.add(valorAtualInvestimentos);
            relatorio.append("💰 PATRIMÔNIO TOTAL DO BANCO:\n");
            relatorio.append("   R$ ").append(String.format("%.2f", patrimonioTotal)).append("\n");
            
            mostrarResultado(relatorio.toString());
            AccessibilityUtils.anunciarMensagem("Relatório geral gerado com sucesso");
            
        } catch (Exception e) {
            mostrarErro("Erro ao gerar relatório geral: " + e.getMessage());
        }
    }
    
    private void consultarPosicaoConsolidada() {
        try {
            String cpf = campoCpfPosicao.getText().trim();
            
            if (cpf.isEmpty()) {
                campoCpfPosicao.marcarComoInvalido("CPF é obrigatório");
                SoundUtils.tocarErro();
                return;
            }
            
            campoCpfPosicao.marcarComoValido();
            
            // Buscar cliente
            var clienteOpt = bancoService.buscarClientePorCpf(cpf);
            
            if (!clienteOpt.isPresent()) {
                mostrarErro("Cliente não encontrado com CPF: " + cpf);
                return;
            }
            
            var cliente = clienteOpt.get();
            
            StringBuilder posicao = new StringBuilder();
            posicao.append("POSIÇÃO CONSOLIDADA\n");
            posicao.append("===================\n\n");
            posicao.append("Cliente: ").append(cliente.getNome()).append("\n");
            posicao.append("CPF: ").append(cliente.getCpf()).append("\n");
            posicao.append("Email: ").append(cliente.getEmail()).append("\n\n");
            
            // Buscar contas do cliente
            var contas = bancoService.buscarContasPorCpf(cpf);
            
            posicao.append("💳 CONTAS:\n");
            java.math.BigDecimal saldoTotalContas = java.math.BigDecimal.ZERO;
            
            for (var conta : contas) {
                posicao.append("   ").append(conta.getAgencia()).append("-").append(conta.getNumero());
                posicao.append(" (").append(conta.getTipoConta().getDescricao()).append(")");
                posicao.append(" - R$ ").append(String.format("%.2f", conta.getSaldo())).append("\n");
                saldoTotalContas = saldoTotalContas.add(conta.getSaldo());
            }
            
            posicao.append("   TOTAL EM CONTAS: R$ ").append(String.format("%.2f", saldoTotalContas)).append("\n\n");
            
            // Buscar investimentos do cliente
            var investimentos = bancoService.listarInvestimentosPorCpf(cpf);
            
            posicao.append("📈 INVESTIMENTOS:\n");
            java.math.BigDecimal valorTotalInvestimentos = java.math.BigDecimal.ZERO;
            
            for (var inv : investimentos) {
                if (inv.isAtivo()) {
                    posicao.append("   ").append(inv.getTipo().getDescricao());
                    posicao.append(" - R$ ").append(String.format("%.2f", inv.calcularValorAtual()));
                    posicao.append(" (Inicial: R$ ").append(String.format("%.2f", inv.getValor())).append(")");
                    posicao.append("\n");
                    valorTotalInvestimentos = valorTotalInvestimentos.add(inv.calcularValorAtual());
                }
            }
            
            posicao.append("   TOTAL EM INVESTIMENTOS: R$ ").append(String.format("%.2f", valorTotalInvestimentos)).append("\n\n");
            
            // Patrimônio total
            java.math.BigDecimal patrimonioTotal = saldoTotalContas.add(valorTotalInvestimentos);
            posicao.append("💰 PATRIMÔNIO TOTAL: R$ ").append(String.format("%.2f", patrimonioTotal)).append("\n");
            
            mostrarResultado(posicao.toString());
            AccessibilityUtils.anunciarMensagem("Posição consolidada gerada com sucesso");
            
        } catch (Exception e) {
            mostrarErro("Erro ao consultar posição consolidada: " + e.getMessage());
        }
    }
    
    private void gerarRanking() {
        try {
            StringBuilder ranking = new StringBuilder();
            ranking.append("RANKING DE CLIENTES POR PATRIMÔNIO\n");
            ranking.append("==================================\n\n");
            
            var todosClientes = bancoService.listarClientes();
            
            // Calcular patrimônio de cada cliente
            java.util.List<ClientePatrimonio> clientesComPatrimonio = new java.util.ArrayList<>();
            
            for (var cliente : todosClientes) {
                java.math.BigDecimal patrimonioTotal = java.math.BigDecimal.ZERO;
                
                // Somar saldos das contas
                var contas = bancoService.buscarContasPorCpf(cliente.getCpf());
                for (var conta : contas) {
                    patrimonioTotal = patrimonioTotal.add(conta.getSaldo());
                }
                
                // Somar investimentos
                var investimentos = bancoService.listarInvestimentosPorCpf(cliente.getCpf());
                for (var inv : investimentos) {
                    if (inv.isAtivo()) {
                        patrimonioTotal = patrimonioTotal.add(inv.calcularValorAtual());
                    }
                }
                
                clientesComPatrimonio.add(new ClientePatrimonio(cliente, patrimonioTotal));
            }
            
            // Ordenar por patrimônio (maior para menor)
            clientesComPatrimonio.sort((a, b) -> b.patrimonio.compareTo(a.patrimonio));
            
            // Gerar ranking
            for (int i = 0; i < clientesComPatrimonio.size(); i++) {
                var cp = clientesComPatrimonio.get(i);
                ranking.append(String.format("%2d. %-25s - R$ %15s\n", 
                    (i + 1), 
                    cp.cliente.getNome(), 
                    String.format("%.2f", cp.patrimonio)));
            }
            
            mostrarResultado(ranking.toString());
            AccessibilityUtils.anunciarMensagem("Ranking gerado com sucesso");
            
        } catch (Exception e) {
            mostrarErro("Erro ao gerar ranking: " + e.getMessage());
        }
    }
    
    // Classe auxiliar para ranking
    private static class ClientePatrimonio {
        final com.nttdata.banco.model.Cliente cliente;
        final java.math.BigDecimal patrimonio;
        
        ClientePatrimonio(com.nttdata.banco.model.Cliente cliente, java.math.BigDecimal patrimonio) {
            this.cliente = cliente;
            this.patrimonio = patrimonio;
        }
    }
    
    private void mostrarResultado(String resultado) {
        areaResultado.setText(resultado);
        areaResultado.setCaretPosition(0);
        SoundUtils.tocarSucesso();
        
        // Ir para aba de resultado
        abas.setSelectedIndex(4);
    }
    
    private void mostrarErro(String mensagem) {
        String textoErro = "ERRO\n" +
                          "====\n\n" +
                          mensagem + "\n\n" +
                          "Erro reportado em: " + 
                          java.time.LocalDateTime.now().format(
                              java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        
        areaResultado.setText(textoErro);
        areaResultado.setCaretPosition(0);
        SoundUtils.tocarErro();
        AccessibilityUtils.anunciarMensagem("Erro: " + mensagem);
        
        // Ir para aba de resultado
        abas.setSelectedIndex(4);
        
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }
    
    private void configurarAcessibilidade() {
        // Configura acessibilidade da janela
        getAccessibleContext().setAccessibleName("Relatórios e Consultas");
        getAccessibleContext().setAccessibleDescription(
            "Janela para gerar relatórios e consultas do sistema bancário");
        
        // Configura acessibilidade das abas
        abas.getAccessibleContext().setAccessibleName("Abas de Relatórios");
        abas.getAccessibleContext().setAccessibleDescription(
            "Abas para diferentes tipos de relatórios e consultas");
        
        // Foca primeiro elemento ao abrir
        SwingUtilities.invokeLater(() -> {
            AccessibilityUtils.focarPrimeiroElemento(this);
        });
    }
}
