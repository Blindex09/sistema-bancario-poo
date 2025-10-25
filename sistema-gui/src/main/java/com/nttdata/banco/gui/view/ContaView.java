package com.nttdata.banco.gui.view;

import com.nttdata.banco.gui.components.AccessibleButton;
import com.nttdata.banco.gui.components.AccessibleTextField;
import com.nttdata.banco.gui.theme.ThemeManager;
import com.nttdata.banco.gui.utils.AccessibilityUtils;
import com.nttdata.banco.gui.utils.SoundUtils;
import com.nttdata.banco.model.Conta;
import com.nttdata.banco.model.ContaCorrente;
import com.nttdata.banco.model.ContaPoupanca;
import com.nttdata.banco.model.ContaInvestimento;
import com.nttdata.banco.enums.TipoConta;
import com.nttdata.banco.service.BancoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Tela de Gestão de Contas - Totalmente Acessível
 * Permite criar, buscar e listar contas
 */
public class ContaView extends JDialog {
    
    private BancoService bancoService;
    private JTabbedPane abas;
    
    // Aba Criar Conta
    private AccessibleTextField campoCpfTitular;
    private JComboBox<TipoConta> comboTipoConta;
    
    // Aba Buscar Conta
    private AccessibleTextField campoAgenciaBusca;
    private AccessibleTextField campoNumeroBusca;
    private JTextArea resultadoBuscaConta;
    
    // Aba Listar Contas
    private JTable tabelaContas;
    private DefaultTableModel modeloTabelaContas;
    
    public ContaView(Frame parent, BancoService bancoService) {
        super(parent, "Gestão de Contas", true);
        this.bancoService = bancoService;
        
        inicializarInterface();
        criarAbas();
        configurarAcessibilidade();
        
        setSize(900, 600);
        setLocationRelativeTo(parent);
    }
    
    private void inicializarInterface() {
        setLayout(new BorderLayout());
        
        // Título
        JLabel titulo = new JLabel("💳 Gestão de Contas", SwingConstants.CENTER);
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
        // Aba 1: Criar Conta
        JPanel abaCriar = criarAbaCriarConta();
        abas.addTab("Criar Conta", abaCriar);
        
        // Aba 2: Buscar Conta
        JPanel abaBuscar = criarAbaBuscarConta();
        abas.addTab("Buscar Conta", abaBuscar);
        
        // Aba 3: Listar Contas
        JPanel abaListar = criarAbaListarContas();
        abas.addTab("Listar Contas", abaListar);
    }
    
    private JPanel criarAbaCriarConta() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Título
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel titulo = new JLabel("➕ Criar Nova Conta");
        titulo.setFont(titulo.getFont().deriveFont(16f));
        painel.add(titulo, gbc);
        
        // CPF do Titular
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(new JLabel("CPF do Titular:"), gbc);
        gbc.gridx = 1;
        campoCpfTitular = new AccessibleTextField("CPF Titular", "CPF do cliente que será titular da conta", true);
        campoCpfTitular.setPreferredSize(new Dimension(300, 40));
        painel.add(campoCpfTitular, gbc);
        
        // Tipo de Conta
        gbc.gridx = 0; gbc.gridy = 2;
        painel.add(new JLabel("Tipo de Conta:"), gbc);
        gbc.gridx = 1;
        comboTipoConta = new JComboBox<>(TipoConta.values());
        comboTipoConta.setPreferredSize(new Dimension(300, 40));
        AccessibilityUtils.configurarAcessibilidade(comboTipoConta, 
            "Tipo de Conta", "Selecione o tipo de conta a ser criada");
        painel.add(comboTipoConta, gbc);
        
        // Informações sobre tipos de conta
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JTextArea infoTipos = new JTextArea(6, 40);
        infoTipos.setEditable(false);
        infoTipos.setText(
            "TIPOS DE CONTA DISPONÍVEIS:\n\n" +
            "• CORRENTE: Conta para uso diário com limite de cheque especial\n" +
            "• POUPANCA: Conta para poupança com rendimento automático\n" +
            "• INVESTIMENTO: Conta para investimentos em CDB, Tesouro Direto, etc."
        );
        infoTipos.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        infoTipos.setBackground(painel.getBackground());
        AccessibilityUtils.configurarAcessibilidade(infoTipos, 
            "Informações sobre tipos de conta", "Descrição dos tipos de conta disponíveis");
        
        JScrollPane scrollInfo = new JScrollPane(infoTipos);
        scrollInfo.setBorder(BorderFactory.createTitledBorder("Informações"));
        painel.add(scrollInfo, gbc);
        
        // Botão Criar
        gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        AccessibleButton btnCriarConta = new AccessibleButton("➕ Criar Conta", "C");
        btnCriarConta.setPreferredSize(new Dimension(200, 50));
        btnCriarConta.addActionListener(e -> criarConta());
        painel.add(btnCriarConta, gbc);
        
        return painel;
    }
    
    private JPanel criarAbaBuscarConta() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Título
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel titulo = new JLabel("🔍 Buscar Conta por Agência e Número");
        titulo.setFont(titulo.getFont().deriveFont(16f));
        painel.add(titulo, gbc);
        
        // Agência
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(new JLabel("Agência:"), gbc);
        gbc.gridx = 1;
        campoAgenciaBusca = new AccessibleTextField("Agência", "Número da agência", true);
        campoAgenciaBusca.setPreferredSize(new Dimension(150, 40));
        campoAgenciaBusca.setText("0001"); // Agência padrão
        painel.add(campoAgenciaBusca, gbc);
        
        // Número da Conta
        gbc.gridx = 0; gbc.gridy = 2;
        painel.add(new JLabel("Número da Conta:"), gbc);
        gbc.gridx = 1;
        campoNumeroBusca = new AccessibleTextField("Número da Conta", "Número da conta", true);
        campoNumeroBusca.setPreferredSize(new Dimension(200, 40));
        painel.add(campoNumeroBusca, gbc);
        
        // Botão Buscar
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        AccessibleButton btnBuscarConta = new AccessibleButton("🔍 Buscar Conta", "B");
        btnBuscarConta.setPreferredSize(new Dimension(180, 40));
        btnBuscarConta.addActionListener(e -> buscarConta());
        painel.add(btnBuscarConta, gbc);
        
        // Área de resultado
        gbc.gridy = 4; gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        resultadoBuscaConta = new JTextArea(15, 60);
        resultadoBuscaConta.setEditable(false);
        resultadoBuscaConta.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        AccessibilityUtils.configurarAcessibilidade(resultadoBuscaConta, 
            "Resultado da busca de conta", "Mostra os dados da conta encontrada");
        
        JScrollPane scrollResultadoConta = new JScrollPane(resultadoBuscaConta);
        painel.add(scrollResultadoConta, gbc);
        
        return painel;
    }
    
    private JPanel criarAbaListarContas() {
        JPanel painel = new JPanel(new BorderLayout());
        
        // Título e botão atualizar
        JPanel painelTopo = new JPanel(new FlowLayout());
        JLabel titulo = new JLabel("📋 Lista de Todas as Contas");
        titulo.setFont(titulo.getFont().deriveFont(16f));
        painelTopo.add(titulo);
        
        AccessibleButton btnAtualizarContas = new AccessibleButton("🔄 Atualizar", "A");
        btnAtualizarContas.addActionListener(e -> atualizarListaContas());
        painelTopo.add(btnAtualizarContas);
        
        painel.add(painelTopo, BorderLayout.NORTH);
        
        // Tabela de contas
        String[] colunas = {"Agência", "Número", "Tipo", "Titular", "Saldo", "Status"};
        modeloTabelaContas = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Somente leitura
            }
        };
        
        tabelaContas = new JTable(modeloTabelaContas);
        AccessibilityUtils.configurarTabelaAcessivel(tabelaContas, 
            "Lista de contas cadastradas no sistema");
        
        // Configura largura das colunas
        tabelaContas.getColumnModel().getColumn(0).setPreferredWidth(80);  // Agência
        tabelaContas.getColumnModel().getColumn(1).setPreferredWidth(120); // Número
        tabelaContas.getColumnModel().getColumn(2).setPreferredWidth(120); // Tipo
        tabelaContas.getColumnModel().getColumn(3).setPreferredWidth(200); // Titular
        tabelaContas.getColumnModel().getColumn(4).setPreferredWidth(150); // Saldo
        tabelaContas.getColumnModel().getColumn(5).setPreferredWidth(80);  // Status
        
        JScrollPane scrollTabelaContas = new JScrollPane(tabelaContas);
        painel.add(scrollTabelaContas, BorderLayout.CENTER);
        
        // Carrega dados iniciais
        atualizarListaContas();
        
        return painel;
    }
    
    // ================== FUNCIONALIDADES ==================
    
    private void criarConta() {
        try {
            // Validar CPF
            String cpf = campoCpfTitular.getText().trim();
            
            if (cpf.isEmpty()) {
                campoCpfTitular.marcarComoInvalido("CPF é obrigatório");
                SoundUtils.tocarErro();
                return;
            }
            
            if (!cpf.matches("\\d{11}")) {
                campoCpfTitular.marcarComoInvalido("CPF deve ter 11 dígitos");
                SoundUtils.tocarErro();
                return;
            }
            
            campoCpfTitular.marcarComoValido();
            
            // Obter tipo de conta selecionado
            TipoConta tipoConta = (TipoConta) comboTipoConta.getSelectedItem();
            
            // Criar conta
            Conta conta = bancoService.criarConta(cpf, tipoConta);
            
            // Limpar campos
            campoCpfTitular.setText("");
            comboTipoConta.setSelectedIndex(0);
            
            // Feedback de sucesso
            SoundUtils.tocarSucesso();
            String mensagem = "Conta criada com sucesso!\n" +
                            "Agência: " + conta.getAgencia() + "\n" +
                            "Número: " + conta.getNumero() + "\n" +
                            "Tipo: " + conta.getTipoConta().getDescricao() + "\n" +
                            "Titular: " + conta.getTitular().getNome();
            
            AccessibilityUtils.anunciarMensagem("Conta criada com sucesso");
            JOptionPane.showMessageDialog(this, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            // Atualizar lista se estiver na aba correspondente
            atualizarListaContas();
            
        } catch (IllegalArgumentException e) {
            SoundUtils.tocarErro();
            AccessibilityUtils.anunciarMensagem("Erro: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                "Erro ao criar conta:\n" + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            SoundUtils.tocarErro();
            AccessibilityUtils.anunciarMensagem("Erro inesperado");
            JOptionPane.showMessageDialog(this,
                "Erro inesperado: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void buscarConta() {
        try {
            String agencia = campoAgenciaBusca.getText().trim();
            String numero = campoNumeroBusca.getText().trim();
            
            if (agencia.isEmpty()) {
                campoAgenciaBusca.marcarComoInvalido("Agência é obrigatória");
                SoundUtils.tocarErro();
                return;
            }
            
            if (numero.isEmpty()) {
                campoNumeroBusca.marcarComoInvalido("Número da conta é obrigatório");
                SoundUtils.tocarErro();
                return;
            }
            
            campoAgenciaBusca.marcarComoValido();
            campoNumeroBusca.marcarComoValido();
            
            Optional<Conta> contaOpt = bancoService.buscarConta(agencia, numero);
            
            if (contaOpt.isPresent()) {
                Conta conta = contaOpt.get();
                
                StringBuilder resultado = new StringBuilder();
                resultado.append("CONTA ENCONTRADA\n");
                resultado.append("================\n\n");
                resultado.append("Agência: ").append(conta.getAgencia()).append("\n");
                resultado.append("Número: ").append(conta.getNumero()).append("\n");
                resultado.append("Tipo: ").append(conta.getTipoConta().getDescricao()).append("\n");
                resultado.append("Titular: ").append(conta.getTitular().getNome()).append("\n");
                resultado.append("CPF Titular: ").append(conta.getTitular().getCpf()).append("\n");
                resultado.append("Saldo: R$ ").append(String.format("%.2f", conta.getSaldo())).append("\n");
                resultado.append("Status: ").append(conta.isAtiva() ? "Ativa" : "Inativa").append("\n\n");
                
                // Informações específicas por tipo de conta
                switch (conta.getTipoConta()) {
                    case CORRENTE:
                        resultado.append("INFORMAÇÕES CONTA CORRENTE:\n");
                        if (conta instanceof ContaCorrente) {
                            ContaCorrente cc = (ContaCorrente) conta;
                            resultado.append("Limite: R$ ").append(String.format("%.2f", cc.getLimite())).append("\n");
                        }
                        break;
                    case POUPANCA:
                        resultado.append("INFORMAÇÕES CONTA POUPANÇA:\n");
                        if (conta instanceof ContaPoupanca) {
                            resultado.append("Taxa de Rendimento: ").append(ContaPoupanca.getTaxaRendimento().multiply(new BigDecimal("100")).setScale(2)).append("%\n");
                        }
                        break;
                    case INVESTIMENTO:
                        resultado.append("INFORMAÇÕES CONTA INVESTIMENTO:\n");
                        if (conta instanceof ContaInvestimento) {
                            resultado.append("Valor Mínimo Saque: R$ ").append(String.format("%.2f", ContaInvestimento.getValorMinimoSaque())).append("\n");
                        }
                        break;
                }
                
                resultado.append("\nTotal de Transações: ").append(conta.getHistorico().size()).append("\n");
                
                resultadoBuscaConta.setText(resultado.toString());
                
                SoundUtils.tocarSucesso();
                AccessibilityUtils.anunciarMensagem("Conta encontrada");
                
            } else {
                resultadoBuscaConta.setText("CONTA NÃO ENCONTRADA\n" +
                                          "===================\n\n" +
                                          "Nenhuma conta encontrada com:\n" +
                                          "Agência: " + agencia + "\n" +
                                          "Número: " + numero + "\n\n" +
                                          "Verifique se os dados estão corretos.");
                
                SoundUtils.tocarAviso();
                AccessibilityUtils.anunciarMensagem("Conta não encontrada");
            }
            
        } catch (Exception e) {
            SoundUtils.tocarErro();
            AccessibilityUtils.anunciarMensagem("Erro na busca");
            resultadoBuscaConta.setText("ERRO NA BUSCA\n" +
                                      "=============\n\n" +
                                      "Erro: " + e.getMessage());
        }
    }
    
    private void atualizarListaContas() {
        try {
            // Limpar tabela
            modeloTabelaContas.setRowCount(0);
            
            // Buscar todas as contas
            List<Conta> contas = bancoService.listarContas();
            
            // Adicionar contas à tabela
            for (Conta conta : contas) {
                Object[] linha = {
                    conta.getAgencia(),
                    conta.getNumero(),
                    conta.getTipoConta().getDescricao(),
                    conta.getTitular().getNome(),
                    "R$ " + String.format("%.2f", conta.getSaldo()),
                    conta.isAtiva() ? "Ativa" : "Inativa"
                };
                modeloTabelaContas.addRow(linha);
            }
            
            // Feedback
            String mensagem = "Lista atualizada: " + contas.size() + " conta(s) encontrada(s)";
            AccessibilityUtils.anunciarMensagem(mensagem);
            
            if (contas.isEmpty()) {
                SoundUtils.tocarAviso();
            } else {
                SoundUtils.tocarInfo();
            }
            
        } catch (Exception e) {
            SoundUtils.tocarErro();
            AccessibilityUtils.anunciarMensagem("Erro ao carregar lista de contas");
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar lista de contas:\n" + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void configurarAcessibilidade() {
        // Configura acessibilidade da janela
        getAccessibleContext().setAccessibleName("Gestão de Contas");
        getAccessibleContext().setAccessibleDescription(
            "Janela para criar, buscar e listar contas bancárias do sistema");
        
        // Configura acessibilidade das abas
        abas.getAccessibleContext().setAccessibleName("Abas de Gestão de Contas");
        abas.getAccessibleContext().setAccessibleDescription(
            "Abas para diferentes operações com contas bancárias");
        
        // Foca primeiro elemento ao abrir
        SwingUtilities.invokeLater(() -> {
            AccessibilityUtils.focarPrimeiroElemento(this);
        });
    }
}
