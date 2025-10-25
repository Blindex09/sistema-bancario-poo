package com.nttdata.banco.gui.view;

import com.nttdata.banco.gui.components.AccessibleButton;
import com.nttdata.banco.gui.components.AccessibleTextField;
import com.nttdata.banco.gui.theme.ThemeManager;
import com.nttdata.banco.gui.utils.AccessibilityUtils;
import com.nttdata.banco.gui.utils.SoundUtils;
import com.nttdata.banco.model.Investimento;
import com.nttdata.banco.enums.TipoInvestimento;
import com.nttdata.banco.service.BancoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Tela de Investimentos - Totalmente Acessível
 * Permite investir, resgatar e consultar investimentos
 */
public class InvestimentoView extends JDialog {
    
    private BancoService bancoService;
    private JTabbedPane abas;
    
    // Aba Investir
    private AccessibleTextField campoAgenciaInvestir;
    private AccessibleTextField campoNumeroInvestir;
    private JComboBox<TipoInvestimento> comboTipoInvestimento;
    private AccessibleTextField campoValorInvestir;
    
    // Aba Resgatar
    private AccessibleTextField campoIdInvestimento;
    private AccessibleTextField campoAgenciaResgatar;
    private AccessibleTextField campoNumeroResgatar;
    
    // Aba Listar Investimentos
    private JTable tabelaInvestimentos;
    private DefaultTableModel modeloTabelaInvestimentos;
    
    public InvestimentoView(Frame parent, BancoService bancoService) {
        super(parent, "Investimentos", true);
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
        JLabel titulo = new JLabel("📈 Investimentos", SwingConstants.CENTER);
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
        // Aba 1: Investir
        JPanel abaInvestir = criarAbaInvestir();
        abas.addTab("📈 Investir", abaInvestir);
        
        // Aba 2: Resgatar
        JPanel abaResgatar = criarAbaResgatar();
        abas.addTab("💰 Resgatar", abaResgatar);
        
        // Aba 3: Listar Investimentos
        JPanel abaListar = criarAbaListarInvestimentos();
        abas.addTab("📋 Meus Investimentos", abaListar);
    }
    
    private JPanel criarAbaInvestir() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Título
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel titulo = new JLabel("📈 Realizar Investimento");
        titulo.setFont(titulo.getFont().deriveFont(16f));
        painel.add(titulo, gbc);
        
        // Campos do formulário
        gbc.gridwidth = 1;
        
        // Agência
        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(new JLabel("Agência:"), gbc);
        gbc.gridx = 1;
        campoAgenciaInvestir = new AccessibleTextField("Agência", "Agência da conta de investimento", true);
        campoAgenciaInvestir.setText("0001");
        campoAgenciaInvestir.setPreferredSize(new Dimension(200, 40));
        painel.add(campoAgenciaInvestir, gbc);
        
        // Número da Conta
        gbc.gridx = 0; gbc.gridy = 2;
        painel.add(new JLabel("Número da Conta:"), gbc);
        gbc.gridx = 1;
        campoNumeroInvestir = new AccessibleTextField("Número", "Número da conta de investimento", true);
        campoNumeroInvestir.setPreferredSize(new Dimension(200, 40));
        painel.add(campoNumeroInvestir, gbc);
        
        // Tipo de Investimento
        gbc.gridx = 0; gbc.gridy = 3;
        painel.add(new JLabel("Tipo de Investimento:"), gbc);
        gbc.gridx = 1;
        comboTipoInvestimento = new JComboBox<>(TipoInvestimento.values());
        comboTipoInvestimento.setPreferredSize(new Dimension(300, 40));
        AccessibilityUtils.configurarAcessibilidade(comboTipoInvestimento, 
            "Tipo de Investimento", "Selecione o tipo de investimento desejado");
        painel.add(comboTipoInvestimento, gbc);
        
        // Valor
        gbc.gridx = 0; gbc.gridy = 4;
        painel.add(new JLabel("Valor a Investir:"), gbc);
        gbc.gridx = 1;
        campoValorInvestir = new AccessibleTextField("Valor", "Valor a ser investido", true);
        campoValorInvestir.setPreferredSize(new Dimension(200, 40));
        painel.add(campoValorInvestir, gbc);
        
        // Informações sobre investimentos
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        JTextArea infoInvestimentos = new JTextArea(8, 50);
        infoInvestimentos.setEditable(false);
        infoInvestimentos.setText(
            "TIPOS DE INVESTIMENTO DISPONÍVEIS:\n\n" +
            "• CDB (Certificado de Depósito Bancário)\n" +
            "  - Rentabilidade: 110% do CDI\n" +
            "  - Prazo: 6 meses\n" +
            "  - Valor mínimo: R$ 1.000,00\n\n" +
            "• POUPANCA_PLUS (Poupança com maior rendimento)\n" +
            "  - Rentabilidade: 8% ao ano\n" +
            "  - Prazo: 12 meses\n" +
            "  - Valor mínimo: R$ 500,00\n\n" +
            "• TESOURO_DIRETO (Títulos públicos)\n" +
            "  - Rentabilidade: 105% do CDI\n" +
            "  - Prazo: 24 meses\n" +
            "  - Valor mínimo: R$ 100,00"
        );
        infoInvestimentos.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        infoInvestimentos.setBackground(painel.getBackground());
        AccessibilityUtils.configurarAcessibilidade(infoInvestimentos, 
            "Informações sobre investimentos", "Descrição dos tipos de investimento disponíveis");
        
        JScrollPane scrollInfo = new JScrollPane(infoInvestimentos);
        scrollInfo.setBorder(BorderFactory.createTitledBorder("Informações dos Investimentos"));
        painel.add(scrollInfo, gbc);
        
        // Botão Investir
        gbc.gridy = 6; gbc.fill = GridBagConstraints.NONE;
        AccessibleButton btnInvestir = new AccessibleButton("📈 Investir", "I");
        btnInvestir.setPreferredSize(new Dimension(200, 50));
        btnInvestir.addActionListener(e -> realizarInvestimento());
        painel.add(btnInvestir, gbc);
        
        return painel;
    }
    
    private JPanel criarAbaResgatar() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Título
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel titulo = new JLabel("💰 Resgatar Investimento");
        titulo.setFont(titulo.getFont().deriveFont(16f));
        painel.add(titulo, gbc);
        
        // Campos
        gbc.gridwidth = 1;
        
        // ID do Investimento
        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(new JLabel("ID do Investimento:"), gbc);
        gbc.gridx = 1;
        campoIdInvestimento = new AccessibleTextField("ID Investimento", "ID do investimento a ser resgatado", true);
        campoIdInvestimento.setPreferredSize(new Dimension(350, 40));
        painel.add(campoIdInvestimento, gbc);
        
        // Agência da Conta de Destino
        gbc.gridx = 0; gbc.gridy = 2;
        painel.add(new JLabel("Agência (Destino):"), gbc);
        gbc.gridx = 1;
        campoAgenciaResgatar = new AccessibleTextField("Agência Destino", "Agência da conta que receberá o resgate", true);
        campoAgenciaResgatar.setText("0001");
        campoAgenciaResgatar.setPreferredSize(new Dimension(200, 40));
        painel.add(campoAgenciaResgatar, gbc);
        
        // Número da Conta de Destino
        gbc.gridx = 0; gbc.gridy = 3;
        painel.add(new JLabel("Número da Conta (Destino):"), gbc);
        gbc.gridx = 1;
        campoNumeroResgatar = new AccessibleTextField("Número Destino", "Número da conta que receberá o resgate", true);
        campoNumeroResgatar.setPreferredSize(new Dimension(200, 40));
        painel.add(campoNumeroResgatar, gbc);
        
        // Informações sobre resgate
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        JTextArea infoResgate = new JTextArea(6, 50);
        infoResgate.setEditable(false);
        infoResgate.setText(
            "INFORMAÇÕES SOBRE RESGATE:\n\n" +
            "• O resgate só é permitido após o vencimento do investimento\n" +
            "• O valor resgatado incluirá o principal + rendimentos\n" +
            "• Para obter o ID do investimento, consulte a lista na aba 'Meus Investimentos'\n" +
            "• O valor será creditado na conta de destino informada\n" +
            "• Investimentos resgatados serão removidos da lista"
        );
        infoResgate.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        infoResgate.setBackground(painel.getBackground());
        AccessibilityUtils.configurarAcessibilidade(infoResgate, 
            "Informações sobre resgate", "Instruções para resgatar investimentos");
        
        JScrollPane scrollInfoResgate = new JScrollPane(infoResgate);
        scrollInfoResgate.setBorder(BorderFactory.createTitledBorder("Como Resgatar"));
        painel.add(scrollInfoResgate, gbc);
        
        // Botão Resgatar
        gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE;
        AccessibleButton btnResgatar = new AccessibleButton("💰 Resgatar", "R");
        btnResgatar.setPreferredSize(new Dimension(200, 50));
        btnResgatar.addActionListener(e -> resgatarInvestimento());
        painel.add(btnResgatar, gbc);
        
        return painel;
    }
    
    private JPanel criarAbaListarInvestimentos() {
        JPanel painel = new JPanel(new BorderLayout());
        
        // Título e botões
        JPanel painelTopo = new JPanel(new FlowLayout());
        JLabel titulo = new JLabel("📋 Meus Investimentos");
        titulo.setFont(titulo.getFont().deriveFont(16f));
        painelTopo.add(titulo);
        
        AccessibleButton btnAtualizarInvestimentos = new AccessibleButton("🔄 Atualizar", "A");
        btnAtualizarInvestimentos.addActionListener(e -> atualizarListaInvestimentos());
        painelTopo.add(btnAtualizarInvestimentos);
        
        AccessibleButton btnListarTodos = new AccessibleButton("🌐 Todos os Investimentos", "T");
        btnListarTodos.addActionListener(e -> listarTodosInvestimentos());
        painelTopo.add(btnListarTodos);
        
        painel.add(painelTopo, BorderLayout.NORTH);
        
        // Tabela de investimentos
        String[] colunas = {"ID", "Tipo", "Valor Inicial", "Valor Atual", "Rendimento", "Data Vencimento", "Status", "Titular"};
        modeloTabelaInvestimentos = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Somente leitura
            }
        };
        
        tabelaInvestimentos = new JTable(modeloTabelaInvestimentos);
        AccessibilityUtils.configurarTabelaAcessivel(tabelaInvestimentos, 
            "Lista de investimentos no sistema");
        
        // Configura largura das colunas
        tabelaInvestimentos.getColumnModel().getColumn(0).setPreferredWidth(100); // ID
        tabelaInvestimentos.getColumnModel().getColumn(1).setPreferredWidth(150); // Tipo
        tabelaInvestimentos.getColumnModel().getColumn(2).setPreferredWidth(120); // Valor Inicial
        tabelaInvestimentos.getColumnModel().getColumn(3).setPreferredWidth(120); // Valor Atual
        tabelaInvestimentos.getColumnModel().getColumn(4).setPreferredWidth(120); // Rendimento
        tabelaInvestimentos.getColumnModel().getColumn(5).setPreferredWidth(120); // Data Vencimento
        tabelaInvestimentos.getColumnModel().getColumn(6).setPreferredWidth(80);  // Status
        tabelaInvestimentos.getColumnModel().getColumn(7).setPreferredWidth(150); // Titular
        
        JScrollPane scrollTabelaInvestimentos = new JScrollPane(tabelaInvestimentos);
        painel.add(scrollTabelaInvestimentos, BorderLayout.CENTER);
        
        // Painel de informações
        JTextArea infoGeral = new JTextArea(4, 60);
        infoGeral.setEditable(false);
        infoGeral.setText(
            "DICAS:\n" +
            "• Clique em 'Atualizar' para ver apenas seus investimentos\n" +
            "• Clique em 'Todos os Investimentos' para ver investimentos de todos os clientes\n" +
            "• Copie o ID do investimento para usar na aba 'Resgatar'"
        );
        infoGeral.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        AccessibilityUtils.configurarAcessibilidade(infoGeral, 
            "Dicas de uso", "Instruções para usar a lista de investimentos");
        
        JScrollPane scrollInfo = new JScrollPane(infoGeral);
        scrollInfo.setBorder(BorderFactory.createTitledBorder("Dicas"));
        scrollInfo.setPreferredSize(new Dimension(0, 100));
        painel.add(scrollInfo, BorderLayout.SOUTH);
        
        // Carrega dados iniciais
        listarTodosInvestimentos();
        
        return painel;
    }
    
    // ================== FUNCIONALIDADES ==================
    
    private void realizarInvestimento() {
        try {
            // Validações
            String agencia = campoAgenciaInvestir.getText().trim();
            String numero = campoNumeroInvestir.getText().trim();
            String valorStr = campoValorInvestir.getText().trim();
            
            if (agencia.isEmpty()) {
                campoAgenciaInvestir.marcarComoInvalido("Agência é obrigatória");
                SoundUtils.tocarErro();
                return;
            }
            
            if (numero.isEmpty()) {
                campoNumeroInvestir.marcarComoInvalido("Número da conta é obrigatório");
                SoundUtils.tocarErro();
                return;
            }
            
            if (valorStr.isEmpty()) {
                campoValorInvestir.marcarComoInvalido("Valor é obrigatório");
                SoundUtils.tocarErro();
                return;
            }
            
            // Marcar campos como válidos
            campoAgenciaInvestir.marcarComoValido();
            campoNumeroInvestir.marcarComoValido();
            campoValorInvestir.marcarComoValido();
            
            // Converter valor
            BigDecimal valor = new BigDecimal(valorStr.replace(",", "."));
            
            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                campoValorInvestir.marcarComoInvalido("Valor deve ser positivo");
                SoundUtils.tocarErro();
                return;
            }
            
            // Obter tipo de investimento
            TipoInvestimento tipo = (TipoInvestimento) comboTipoInvestimento.getSelectedItem();
            
            // Realizar investimento
            boolean sucesso = bancoService.investir(agencia, numero, tipo, valor);
            
            if (sucesso) {
                // Limpar campos
                campoNumeroInvestir.setText("");
                campoValorInvestir.setText("");
                comboTipoInvestimento.setSelectedIndex(0);
                
                // Feedback de sucesso
                SoundUtils.tocarSucesso();
                String mensagem = "Investimento realizado com sucesso!\n\n" +
                                "Tipo: " + tipo.getDescricao() + "\n" +
                                "Valor: R$ " + String.format("%.2f", valor) + "\n" +
                                "Conta: " + agencia + "-" + numero + "\n" +
                                "Rentabilidade: " + (tipo.getRentabilidadeMensal() * 100) + "% a.m.\n" +
                                "Prazo: Conforme vencimento";
                
                AccessibilityUtils.anunciarMensagem("Investimento realizado com sucesso");
                JOptionPane.showMessageDialog(this, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                // Atualizar lista
                listarTodosInvestimentos();
                
            } else {
                SoundUtils.tocarErro();
                AccessibilityUtils.anunciarMensagem("Falha ao realizar investimento");
                JOptionPane.showMessageDialog(this,
                    "Falha ao realizar investimento.\n" +
                    "Verifique se:\n" +
                    "• A conta existe e está ativa\n" +
                    "• Há saldo suficiente\n" +
                    "• O valor atende ao mínimo do investimento",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            campoValorInvestir.marcarComoInvalido("Valor inválido");
            SoundUtils.tocarErro();
            AccessibilityUtils.anunciarMensagem("Valor inválido");
        } catch (Exception e) {
            SoundUtils.tocarErro();
            AccessibilityUtils.anunciarMensagem("Erro inesperado");
            JOptionPane.showMessageDialog(this,
                "Erro inesperado: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void resgatarInvestimento() {
        try {
            // Validações
            String idInvestimento = campoIdInvestimento.getText().trim();
            String agencia = campoAgenciaResgatar.getText().trim();
            String numero = campoNumeroResgatar.getText().trim();
            
            if (idInvestimento.isEmpty()) {
                campoIdInvestimento.marcarComoInvalido("ID do investimento é obrigatório");
                SoundUtils.tocarErro();
                return;
            }
            
            if (agencia.isEmpty()) {
                campoAgenciaResgatar.marcarComoInvalido("Agência é obrigatória");
                SoundUtils.tocarErro();
                return;
            }
            
            if (numero.isEmpty()) {
                campoNumeroResgatar.marcarComoInvalido("Número da conta é obrigatório");
                SoundUtils.tocarErro();
                return;
            }
            
            // Marcar campos como válidos
            campoIdInvestimento.marcarComoValido();
            campoAgenciaResgatar.marcarComoValido();
            campoNumeroResgatar.marcarComoValido();
            
            // Realizar resgate
            boolean sucesso = bancoService.resgatarInvestimento(idInvestimento, agencia, numero);
            
            if (sucesso) {
                // Limpar campos
                campoIdInvestimento.setText("");
                campoNumeroResgatar.setText("");
                
                // Feedback de sucesso
                SoundUtils.tocarSucesso();
                AccessibilityUtils.anunciarMensagem("Investimento resgatado com sucesso");
                JOptionPane.showMessageDialog(this,
                    "Investimento resgatado com sucesso!\n\n" +
                    "O valor foi creditado na conta " + agencia + "-" + numero + "\n" +
                    "Incluindo principal + rendimentos.",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                // Atualizar lista
                listarTodosInvestimentos();
                
            } else {
                SoundUtils.tocarErro();
                AccessibilityUtils.anunciarMensagem("Falha ao resgatar investimento");
                JOptionPane.showMessageDialog(this,
                    "Falha ao resgatar investimento.\n" +
                    "Verifique se:\n" +
                    "• O ID do investimento está correto\n" +
                    "• O investimento ainda está ativo\n" +
                    "• O investimento já venceu\n" +
                    "• A conta de destino existe",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            SoundUtils.tocarErro();
            AccessibilityUtils.anunciarMensagem("Erro inesperado");
            JOptionPane.showMessageDialog(this,
                "Erro inesperado: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void atualizarListaInvestimentos() {
        // Para esta implementação, vamos listar todos os investimentos
        // Em uma versão mais avançada, poderíamos filtrar por CPF do usuário logado
        listarTodosInvestimentos();
    }
    
    private void listarTodosInvestimentos() {
        try {
            // Limpar tabela
            modeloTabelaInvestimentos.setRowCount(0);
            
            // Buscar todos os investimentos
            List<Investimento> investimentos = bancoService.listarTodosInvestimentos();
            
            // Adicionar investimentos à tabela
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            for (Investimento inv : investimentos) {
                Object[] linha = {
                    inv.getId().substring(0, 8) + "...", // ID abreviado para caber na coluna
                    inv.getTipo().getDescricao(),
                    "R$ " + String.format("%.2f", inv.getValor()),
                    "R$ " + String.format("%.2f", inv.calcularValorAtual()),
                    "R$ " + String.format("%.2f", inv.calcularRendimento()),
                    inv.getDataVencimento().format(formatter),
                    inv.isAtivo() ? 
                        (inv.podeResgatar() ? "Vencido" : "Ativo") : "Resgatado",
                    inv.getTitular() != null ? inv.getTitular() : "N/A"
                };
                modeloTabelaInvestimentos.addRow(linha);
            }
            
            // Feedback
            String mensagem = "Lista atualizada: " + investimentos.size() + " investimento(s) encontrado(s)";
            AccessibilityUtils.anunciarMensagem(mensagem);
            
            if (investimentos.isEmpty()) {
                SoundUtils.tocarAviso();
            } else {
                SoundUtils.tocarInfo();
            }
            
        } catch (Exception e) {
            SoundUtils.tocarErro();
            AccessibilityUtils.anunciarMensagem("Erro ao carregar lista de investimentos");
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar lista de investimentos:\n" + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void configurarAcessibilidade() {
        // Configura acessibilidade da janela
        getAccessibleContext().setAccessibleName("Investimentos");
        getAccessibleContext().setAccessibleDescription(
            "Janela para realizar, resgatar e consultar investimentos");
        
        // Configura acessibilidade das abas
        abas.getAccessibleContext().setAccessibleName("Abas de Investimentos");
        abas.getAccessibleContext().setAccessibleDescription(
            "Abas para diferentes operações com investimentos");
        
        // Foca primeiro elemento ao abrir
        SwingUtilities.invokeLater(() -> {
            AccessibilityUtils.focarPrimeiroElemento(this);
        });
    }
}
