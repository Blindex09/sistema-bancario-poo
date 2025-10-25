package com.nttdata.banco.gui.view;

import com.nttdata.banco.gui.components.AccessibleButton;
import com.nttdata.banco.gui.theme.ThemeManager;
import com.nttdata.banco.gui.theme.FontManager;
import com.nttdata.banco.gui.utils.AccessibilityUtils;
import com.nttdata.banco.gui.utils.SoundUtils;
import com.nttdata.banco.service.BancoService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * Janela Principal da Interface Gráfica
 * Ponto de entrada para todas as funcionalidades do sistema
 */
public class MainWindow extends JFrame {
    
    private BancoService bancoService;
    private JPanel painelPrincipal;
    private JPanel painelConteudo;
    private JMenuBar menuBar;
    
    public MainWindow() {
        this.bancoService = new BancoService();
        
        inicializarInterface();
        configurarAcessibilidade();
        criarMenu();
        criarPainelPrincipal();
        
        // Aplica tema padrão
        ThemeManager.aplicarTema(ThemeManager.TipoTema.CLARO);
        FontManager.aplicarTamanhoFonte(FontManager.TamanhoFonte.NORMAL);
    }
    
    private void inicializarInterface() {
        setTitle("Sistema Bancário NTT Data - Interface Acessível");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setLocationRelativeTo(null);
        
        // Ícone da aplicação (seria carregado de resources)
        setIconImage(criarIconePadrao());
    }
    
    private void configurarAcessibilidade() {
        // Configura propriedades de acessibilidade da janela
        getAccessibleContext().setAccessibleName("Sistema Bancário NTT Data");
        getAccessibleContext().setAccessibleDescription(
            "Sistema bancário com interface acessível para gestão de clientes, contas e transações"
        );
        
        // Configura atalhos globais
        AccessibilityUtils.configurarAtalhosPadrao(getRootPane());
    }
    
    private void criarMenu() {
        menuBar = new JMenuBar();
        
        // Menu Arquivo
        JMenu menuArquivo = new JMenu("Arquivo (A)");
        menuArquivo.setMnemonic('A');
        AccessibilityUtils.configurarAcessibilidade(menuArquivo, 
            "Menu Arquivo", "Opções de arquivo e configurações");
        
        JMenuItem itemSair = new JMenuItem("Sair", 'S');
        itemSair.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK));
        itemSair.addActionListener(e -> System.exit(0));
        menuArquivo.add(itemSair);
        
        // Menu Visualizar
        JMenu menuVisualizar = new JMenu("Visualizar (V)");
        menuVisualizar.setMnemonic('V');
        
        // Submenu Temas
        JMenu subMenuTemas = new JMenu("Temas");
        for (ThemeManager.TipoTema tema : ThemeManager.TipoTema.values()) {
            JMenuItem itemTema = new JMenuItem(tema.getDescricao());
            itemTema.addActionListener(e -> {
                ThemeManager.aplicarTema(tema);
                SoundUtils.tocarInfo();
                AccessibilityUtils.anunciarMensagem("Tema alterado para " + tema.getDescricao());
            });
            subMenuTemas.add(itemTema);
        }
        menuVisualizar.add(subMenuTemas);
        
        // Submenu Fontes
        JMenu subMenuFontes = new JMenu("Tamanho da Fonte");
        for (FontManager.TamanhoFonte tamanho : FontManager.TamanhoFonte.values()) {
            JMenuItem itemFonte = new JMenuItem(tamanho.getDescricao());
            itemFonte.addActionListener(e -> {
                FontManager.aplicarTamanhoFonte(tamanho);
                SoundUtils.tocarInfo();
                AccessibilityUtils.anunciarMensagem("Fonte alterada para " + tamanho.getDescricao());
            });
            subMenuFontes.add(itemFonte);
        }
        menuVisualizar.add(subMenuFontes);
        
        // Menu Acessibilidade
        JMenu menuAcessibilidade = new JMenu("Acessibilidade (C)");
        menuAcessibilidade.setMnemonic('C');
        
        JCheckBoxMenuItem itemSom = new JCheckBoxMenuItem("Feedback Sonoro", SoundUtils.isSomHabilitado());
        itemSom.addActionListener(e -> {
            SoundUtils.habilitarSom(itemSom.isSelected());
            AccessibilityUtils.anunciarMensagem(
                itemSom.isSelected() ? "Sons habilitados" : "Sons desabilitados");
        });
        menuAcessibilidade.add(itemSom);
        
        // Menu Ajuda
        JMenu menuAjuda = new JMenu("Ajuda (H)");
        menuAjuda.setMnemonic('H');
        
        JMenuItem itemSobre = new JMenuItem("Sobre", 'S');
        itemSobre.addActionListener(e -> mostrarSobre());
        menuAjuda.add(itemSobre);
        
        // Adiciona menus à barra
        menuBar.add(menuArquivo);
        menuBar.add(menuVisualizar);
        menuBar.add(menuAcessibilidade);
        menuBar.add(menuAjuda);
        
        setJMenuBar(menuBar);
    }
    
    private void criarPainelPrincipal() {
        painelPrincipal = new JPanel(new BorderLayout());
        
        // Painel de título
        JPanel painelTitulo = criarPainelTitulo();
        painelPrincipal.add(painelTitulo, BorderLayout.NORTH);
        
        // Painel de botões principais
        JPanel painelBotoes = criarPainelBotoes();
        painelPrincipal.add(painelBotoes, BorderLayout.CENTER);
        
        // Painel de status
        JPanel painelStatus = criarPainelStatus();
        painelPrincipal.add(painelStatus, BorderLayout.SOUTH);
        
        add(painelPrincipal);
    }
    
    private JPanel criarPainelTitulo() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        var config = ThemeManager.getConfig();
        painel.setBackground(config.getPrimary());
        
        JLabel titulo = new JLabel("🏦 Sistema Bancário NTT Data");
        titulo.setFont(FontManager.getFonteTitulo().deriveFont(24f));
        titulo.setForeground(Color.WHITE);
        AccessibilityUtils.configurarAcessibilidade(titulo, 
            "Título do Sistema", "Sistema Bancário NTT Data - Interface Acessível");
        
        painel.add(titulo);
        return painel;
    }
    
    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Primeira linha
        gbc.gridx = 0; gbc.gridy = 0;
        AccessibleButton btnClientes = new AccessibleButton("👥 Gerenciar Clientes", "C");
        btnClientes.setPreferredSize(new Dimension(200, 80));
        btnClientes.addActionListener(e -> abrirGestaoClientes());
        painel.add(btnClientes, gbc);
        
        gbc.gridx = 1;
        AccessibleButton btnContas = new AccessibleButton("💳 Gerenciar Contas", "O");
        btnContas.setPreferredSize(new Dimension(200, 80));
        btnContas.addActionListener(e -> abrirGestaoContas());
        painel.add(btnContas, gbc);
        
        // Segunda linha
        gbc.gridx = 0; gbc.gridy = 1;
        AccessibleButton btnTransacoes = new AccessibleButton("💰 Transações", "T");
        btnTransacoes.setPreferredSize(new Dimension(200, 80));
        btnTransacoes.addActionListener(e -> abrirTransacoes());
        painel.add(btnTransacoes, gbc);
        
        gbc.gridx = 1;
        AccessibleButton btnInvestimentos = new AccessibleButton("📈 Investimentos", "I");
        btnInvestimentos.setPreferredSize(new Dimension(200, 80));
        btnInvestimentos.addActionListener(e -> abrirInvestimentos());
        painel.add(btnInvestimentos, gbc);
        
        // Terceira linha
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        AccessibleButton btnRelatorios = new AccessibleButton("📊 Relatórios e Consultas", "R");
        btnRelatorios.setPreferredSize(new Dimension(410, 60));
        btnRelatorios.addActionListener(e -> abrirRelatorios());
        painel.add(btnRelatorios, gbc);
        
        return painel;
    }
    
    private JPanel criarPainelStatus() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        var config = ThemeManager.getConfig();
        painel.setBackground(config.getLight());
        
        JLabel status = new JLabel("Sistema carregado e pronto para uso");
        status.setForeground(config.getForeground());
        AccessibilityUtils.configurarAcessibilidade(status, 
            "Status do Sistema", "Sistema carregado e pronto para uso");
        
        painel.add(status);
        return painel;
    }
    
    private Image criarIconePadrao() {
        // Cria um ícone simples de 32x32 pixels
        Image icone = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) icone.getGraphics();
        g2.setColor(ThemeManager.getConfig().getPrimary());
        g2.fillRect(0, 0, 32, 32);
        g2.setColor(Color.WHITE);
        g2.drawString("NTT", 6, 20);
        g2.dispose();
        return icone;
    }
    
    // Métodos para abrir diferentes telas
    private void abrirGestaoClientes() {
        AccessibilityUtils.anunciarMensagem("Abrindo gestão de clientes");
        
        try {
            ClienteView clienteView = new ClienteView(this, bancoService);
            clienteView.setVisible(true);
        } catch (Exception e) {
            SoundUtils.tocarErro();
            AccessibilityUtils.anunciarMensagem("Erro ao abrir gestão de clientes");
            JOptionPane.showMessageDialog(this, 
                "Erro ao abrir gestão de clientes:\n" + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void abrirGestaoContas() {
        AccessibilityUtils.anunciarMensagem("Abrindo gestão de contas");
        
        try {
            ContaView contaView = new ContaView(this, bancoService);
            contaView.setVisible(true);
        } catch (Exception e) {
            SoundUtils.tocarErro();
            AccessibilityUtils.anunciarMensagem("Erro ao abrir gestão de contas");
            JOptionPane.showMessageDialog(this, 
                "Erro ao abrir gestão de contas:\n" + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void abrirTransacoes() {
        AccessibilityUtils.anunciarMensagem("Abrindo transações");
        
        try {
            TransacaoView transacaoView = new TransacaoView(this, bancoService);
            transacaoView.setVisible(true);
        } catch (Exception e) {
            SoundUtils.tocarErro();
            AccessibilityUtils.anunciarMensagem("Erro ao abrir transações");
            JOptionPane.showMessageDialog(this, 
                "Erro ao abrir transações:\n" + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void abrirInvestimentos() {
        AccessibilityUtils.anunciarMensagem("Abrindo investimentos");
        
        try {
            InvestimentoView investimentoView = new InvestimentoView(this, bancoService);
            investimentoView.setVisible(true);
        } catch (Exception e) {
            SoundUtils.tocarErro();
            AccessibilityUtils.anunciarMensagem("Erro ao abrir investimentos");
            JOptionPane.showMessageDialog(this, 
                "Erro ao abrir investimentos:\n" + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void abrirRelatorios() {
        AccessibilityUtils.anunciarMensagem("Abrindo relatórios");
        
        try {
            RelatorioView relatorioView = new RelatorioView(this, bancoService);
            relatorioView.setVisible(true);
        } catch (Exception e) {
            SoundUtils.tocarErro();
            AccessibilityUtils.anunciarMensagem("Erro ao abrir relatórios");
            JOptionPane.showMessageDialog(this, 
                "Erro ao abrir relatórios:\n" + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void mostrarSobre() {
        String mensagem = "Sistema Bancário NTT Data\n" +
                         "Versão 1.0 - Interface Acessível\n\n" +
                         "Desenvolvido com foco em acessibilidade digital\n" +
                         "Compatível com tecnologias assistivas\n\n" +
                         "Recursos de Acessibilidade:\n" +
                         "• Navegação por teclado\n" +
                         "• Suporte a screen readers\n" +
                         "• Temas para daltonismo\n" +
                         "• Fontes escaláveis\n" +
                         "• Feedback sonoro opcional";
        
        JOptionPane.showMessageDialog(this, mensagem, "Sobre o Sistema", 
                                    JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Torna a janela visível e foca o primeiro elemento
     */
    public void exibir() {
        setVisible(true);
        AccessibilityUtils.focarPrimeiroElemento(painelPrincipal);
        AccessibilityUtils.anunciarMensagem("Sistema Bancário NTT Data carregado com sucesso");
    }
}
