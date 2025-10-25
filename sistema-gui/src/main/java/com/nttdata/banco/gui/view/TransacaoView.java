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
import com.nttdata.banco.service.BancoService;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * Tela de Transações Bancárias - Totalmente Acessível
 * Permite depósito, saque, transferência, PIX e consulta de saldo
 */
public class TransacaoView extends JDialog {
    
    private BancoService bancoService;
    private JTabbedPane abas;
    
    // Campos comuns
    private AccessibleTextField campoAgencia;
    private AccessibleTextField campoNumero;
    private AccessibleTextField campoValor;
    
    // Campos específicos para transferência/PIX
    private AccessibleTextField campoAgenciaDestino;
    private AccessibleTextField campoNumeroDestino;
    
    // Área de resultado
    private JTextArea areaResultado;
    
    public TransacaoView(Frame parent, BancoService bancoService) {
        super(parent, "Transações Bancárias", true);
        this.bancoService = bancoService;
        
        inicializarInterface();
        criarAbas();
        configurarAcessibilidade();
        
        setSize(900, 700);
        setLocationRelativeTo(parent);
    }
    
    private void inicializarInterface() {
        setLayout(new BorderLayout());
        
        // Título
        JLabel titulo = new JLabel("💰 Transações Bancárias", SwingConstants.CENTER);
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
        // Aba 1: Depósito
        JPanel abaDeposito = criarAbaDeposito();
        abas.addTab("📥 Depósito", abaDeposito);
        
        // Aba 2: Saque
        JPanel abaSaque = criarAbaSaque();
        abas.addTab("📤 Saque", abaSaque);
        
        // Aba 3: Transferência
        JPanel abaTransferencia = criarAbaTransferencia();
        abas.addTab("🔄 Transferência", abaTransferencia);
        
        // Aba 4: PIX
        JPanel abaPix = criarAbaPix();
        abas.addTab("⚡ PIX", abaPix);
        
        // Aba 5: Consultar Saldo
        JPanel abaSaldo = criarAbaSaldo();
        abas.addTab("💳 Consultar Saldo", abaSaldo);
    }
    
    private JPanel criarAbaDeposito() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Título
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel titulo = new JLabel("📥 Realizar Depósito");
        titulo.setFont(titulo.getFont().deriveFont(16f));
        painel.add(titulo, gbc);
        
        // Campos
        gbc.gridwidth = 1;
        
        // Agência
        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(new JLabel("Agência:"), gbc);
        gbc.gridx = 1;
        AccessibleTextField campoAgenciaDeposito = new AccessibleTextField("Agência", "Número da agência", true);
        campoAgenciaDeposito.setText("0001");
        campoAgenciaDeposito.setPreferredSize(new Dimension(200, 40));
        painel.add(campoAgenciaDeposito, gbc);
        
        // Número da Conta
        gbc.gridx = 0; gbc.gridy = 2;
        painel.add(new JLabel("Número da Conta:"), gbc);
        gbc.gridx = 1;
        AccessibleTextField campoNumeroDeposito = new AccessibleTextField("Número", "Número da conta", true);
        campoNumeroDeposito.setPreferredSize(new Dimension(200, 40));
        painel.add(campoNumeroDeposito, gbc);
        
        // Valor
        gbc.gridx = 0; gbc.gridy = 3;
        painel.add(new JLabel("Valor:"), gbc);
        gbc.gridx = 1;
        AccessibleTextField campoValorDeposito = new AccessibleTextField("Valor", "Valor do depósito", true);
        campoValorDeposito.setPreferredSize(new Dimension(200, 40));
        painel.add(campoValorDeposito, gbc);
        
        // Botão
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        AccessibleButton btnDepositar = new AccessibleButton("📥 Depositar", "D");
        btnDepositar.setPreferredSize(new Dimension(180, 50));
        btnDepositar.addActionListener(e -> realizarDeposito(
            campoAgenciaDeposito.getText().trim(),
            campoNumeroDeposito.getText().trim(),
            campoValorDeposito.getText().trim()
        ));
        painel.add(btnDepositar, gbc);
        
        return painel;
    }
    
    private JPanel criarAbaSaque() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Título
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel titulo = new JLabel("📤 Realizar Saque");
        titulo.setFont(titulo.getFont().deriveFont(16f));
        painel.add(titulo, gbc);
        
        // Campos
        gbc.gridwidth = 1;
        
        // Agência
        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(new JLabel("Agência:"), gbc);
        gbc.gridx = 1;
        AccessibleTextField campoAgenciaSaque = new AccessibleTextField("Agência", "Número da agência", true);
        campoAgenciaSaque.setText("0001");
        campoAgenciaSaque.setPreferredSize(new Dimension(200, 40));
        painel.add(campoAgenciaSaque, gbc);
        
        // Número da Conta
        gbc.gridx = 0; gbc.gridy = 2;
        painel.add(new JLabel("Número da Conta:"), gbc);
        gbc.gridx = 1;
        AccessibleTextField campoNumeroSaque = new AccessibleTextField("Número", "Número da conta", true);
        campoNumeroSaque.setPreferredSize(new Dimension(200, 40));
        painel.add(campoNumeroSaque, gbc);
        
        // Valor
        gbc.gridx = 0; gbc.gridy = 3;
        painel.add(new JLabel("Valor:"), gbc);
        gbc.gridx = 1;
        AccessibleTextField campoValorSaque = new AccessibleTextField("Valor", "Valor do saque", true);
        campoValorSaque.setPreferredSize(new Dimension(200, 40));
        painel.add(campoValorSaque, gbc);
        
        // Botão
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        AccessibleButton btnSacar = new AccessibleButton("📤 Sacar", "S");
        btnSacar.setPreferredSize(new Dimension(180, 50));
        btnSacar.addActionListener(e -> realizarSaque(
            campoAgenciaSaque.getText().trim(),
            campoNumeroSaque.getText().trim(),
            campoValorSaque.getText().trim()
        ));
        painel.add(btnSacar, gbc);
        
        return painel;
    }
    
    private JPanel criarAbaTransferencia() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Título
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        JLabel titulo = new JLabel("🔄 Realizar Transferência");
        titulo.setFont(titulo.getFont().deriveFont(16f));
        painel.add(titulo, gbc);
        
        // Conta Origem
        gbc.gridwidth = 4;
        gbc.gridy = 1;
        JLabel labelOrigemTransf = new JLabel("CONTA ORIGEM:");
        labelOrigemTransf.setFont(labelOrigemTransf.getFont().deriveFont(Font.BOLD));
        painel.add(labelOrigemTransf, gbc);
        
        // Agência Origem
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 2;
        painel.add(new JLabel("Agência:"), gbc);
        gbc.gridx = 1;
        AccessibleTextField campoAgenciaOrigemTransf = new AccessibleTextField("Agência Origem", "Agência da conta origem", true);
        campoAgenciaOrigemTransf.setText("0001");
        campoAgenciaOrigemTransf.setPreferredSize(new Dimension(100, 40));
        painel.add(campoAgenciaOrigemTransf, gbc);
        
        // Número Origem
        gbc.gridx = 2; gbc.gridy = 2;
        painel.add(new JLabel("Número:"), gbc);
        gbc.gridx = 3;
        AccessibleTextField campoNumeroOrigemTransf = new AccessibleTextField("Número Origem", "Número da conta origem", true);
        campoNumeroOrigemTransf.setPreferredSize(new Dimension(150, 40));
        painel.add(campoNumeroOrigemTransf, gbc);
        
        // Conta Destino
        gbc.gridwidth = 4;
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel labelDestinoTransf = new JLabel("CONTA DESTINO:");
        labelDestinoTransf.setFont(labelDestinoTransf.getFont().deriveFont(Font.BOLD));
        painel.add(labelDestinoTransf, gbc);
        
        // Agência Destino
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 4;
        painel.add(new JLabel("Agência:"), gbc);
        gbc.gridx = 1;
        AccessibleTextField campoAgenciaDestinoTransf = new AccessibleTextField("Agência Destino", "Agência da conta destino", true);
        campoAgenciaDestinoTransf.setText("0001");
        campoAgenciaDestinoTransf.setPreferredSize(new Dimension(100, 40));
        painel.add(campoAgenciaDestinoTransf, gbc);
        
        // Número Destino
        gbc.gridx = 2; gbc.gridy = 4;
        painel.add(new JLabel("Número:"), gbc);
        gbc.gridx = 3;
        AccessibleTextField campoNumeroDestinoTransf = new AccessibleTextField("Número Destino", "Número da conta destino", true);
        campoNumeroDestinoTransf.setPreferredSize(new Dimension(150, 40));
        painel.add(campoNumeroDestinoTransf, gbc);
        
        // Valor
        gbc.gridx = 0; gbc.gridy = 5;
        painel.add(new JLabel("Valor:"), gbc);
        gbc.gridx = 1;
        AccessibleTextField campoValorTransf = new AccessibleTextField("Valor", "Valor da transferência", true);
        campoValorTransf.setPreferredSize(new Dimension(200, 40));
        painel.add(campoValorTransf, gbc);
        
        // Botão
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 4;
        AccessibleButton btnTransferir = new AccessibleButton("🔄 Transferir", "T");
        btnTransferir.setPreferredSize(new Dimension(200, 50));
        btnTransferir.addActionListener(e -> realizarTransferencia(
            campoAgenciaOrigemTransf.getText().trim(),
            campoNumeroOrigemTransf.getText().trim(),
            campoAgenciaDestinoTransf.getText().trim(),
            campoNumeroDestinoTransf.getText().trim(),
            campoValorTransf.getText().trim()
        ));
        painel.add(btnTransferir, gbc);
        
        return painel;
    }
    
    private JPanel criarAbaPix() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Título
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        JLabel titulo = new JLabel("⚡ Realizar PIX");
        titulo.setFont(titulo.getFont().deriveFont(16f));
        painel.add(titulo, gbc);
        
        // Info PIX
        gbc.gridy = 1;
        JLabel infoPix = new JLabel("PIX utiliza o mesmo sistema de transferência");
        infoPix.setFont(infoPix.getFont().deriveFont(Font.ITALIC));
        painel.add(infoPix, gbc);
        
        // Conta Origem
        gbc.gridwidth = 4;
        gbc.gridy = 2;
        JLabel labelOrigemPix = new JLabel("CONTA ORIGEM:");
        labelOrigemPix.setFont(labelOrigemPix.getFont().deriveFont(Font.BOLD));
        painel.add(labelOrigemPix, gbc);
        
        // Agência Origem
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 3;
        painel.add(new JLabel("Agência:"), gbc);
        gbc.gridx = 1;
        AccessibleTextField campoAgenciaOrigemPix = new AccessibleTextField("Agência Origem", "Agência da conta origem", true);
        campoAgenciaOrigemPix.setText("0001");
        campoAgenciaOrigemPix.setPreferredSize(new Dimension(100, 40));
        painel.add(campoAgenciaOrigemPix, gbc);
        
        // Número Origem
        gbc.gridx = 2; gbc.gridy = 3;
        painel.add(new JLabel("Número:"), gbc);
        gbc.gridx = 3;
        AccessibleTextField campoNumeroOrigemPix = new AccessibleTextField("Número Origem", "Número da conta origem", true);
        campoNumeroOrigemPix.setPreferredSize(new Dimension(150, 40));
        painel.add(campoNumeroOrigemPix, gbc);
        
        // Conta Destino
        gbc.gridwidth = 4;
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel labelDestinoPix = new JLabel("CONTA DESTINO:");
        labelDestinoPix.setFont(labelDestinoPix.getFont().deriveFont(Font.BOLD));
        painel.add(labelDestinoPix, gbc);
        
        // Agência Destino
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 5;
        painel.add(new JLabel("Agência:"), gbc);
        gbc.gridx = 1;
        AccessibleTextField campoAgenciaDestinoPix = new AccessibleTextField("Agência Destino", "Agência da conta destino", true);
        campoAgenciaDestinoPix.setText("0001");
        campoAgenciaDestinoPix.setPreferredSize(new Dimension(100, 40));
        painel.add(campoAgenciaDestinoPix, gbc);
        
        // Número Destino
        gbc.gridx = 2; gbc.gridy = 5;
        painel.add(new JLabel("Número:"), gbc);
        gbc.gridx = 3;
        AccessibleTextField campoNumeroDestinoPix = new AccessibleTextField("Número Destino", "Número da conta destino", true);
        campoNumeroDestinoPix.setPreferredSize(new Dimension(150, 40));
        painel.add(campoNumeroDestinoPix, gbc);
        
        // Valor
        gbc.gridx = 0; gbc.gridy = 6;
        painel.add(new JLabel("Valor:"), gbc);
        gbc.gridx = 1;
        AccessibleTextField campoValorPix = new AccessibleTextField("Valor", "Valor do PIX", true);
        campoValorPix.setPreferredSize(new Dimension(200, 40));
        painel.add(campoValorPix, gbc);
        
        // Botão
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 4;
        AccessibleButton btnPix = new AccessibleButton("⚡ Enviar PIX", "P");
        btnPix.setPreferredSize(new Dimension(200, 50));
        btnPix.addActionListener(e -> realizarPix(
            campoAgenciaOrigemPix.getText().trim(),
            campoNumeroOrigemPix.getText().trim(),
            campoAgenciaDestinoPix.getText().trim(),
            campoNumeroDestinoPix.getText().trim(),
            campoValorPix.getText().trim()
        ));
        painel.add(btnPix, gbc);
        
        return painel;
    }
    
    private JPanel criarAbaSaldo() {
        JPanel painel = new JPanel(new BorderLayout());
        
        // Painel superior com formulário
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Título
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel titulo = new JLabel("💳 Consultar Saldo");
        titulo.setFont(titulo.getFont().deriveFont(16f));
        painelFormulario.add(titulo, gbc);
        
        // Agência
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        painelFormulario.add(new JLabel("Agência:"), gbc);
        gbc.gridx = 1;
        AccessibleTextField campoAgenciaSaldo = new AccessibleTextField("Agência", "Número da agência", true);
        campoAgenciaSaldo.setText("0001");
        campoAgenciaSaldo.setPreferredSize(new Dimension(200, 40));
        painelFormulario.add(campoAgenciaSaldo, gbc);
        
        // Número da Conta
        gbc.gridx = 0; gbc.gridy = 2;
        painelFormulario.add(new JLabel("Número da Conta:"), gbc);
        gbc.gridx = 1;
        AccessibleTextField campoNumeroSaldo = new AccessibleTextField("Número", "Número da conta", true);
        campoNumeroSaldo.setPreferredSize(new Dimension(200, 40));
        painelFormulario.add(campoNumeroSaldo, gbc);
        
        // Botão
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        AccessibleButton btnConsultarSaldo = new AccessibleButton("💳 Consultar Saldo", "C");
        btnConsultarSaldo.setPreferredSize(new Dimension(200, 50));
        btnConsultarSaldo.addActionListener(e -> consultarSaldo(
            campoAgenciaSaldo.getText().trim(),
            campoNumeroSaldo.getText().trim()
        ));
        painelFormulario.add(btnConsultarSaldo, gbc);
        
        painel.add(painelFormulario, BorderLayout.NORTH);
        
        // Área de resultado
        areaResultado = new JTextArea(20, 60);
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        AccessibilityUtils.configurarAcessibilidade(areaResultado, 
            "Resultado das operações", "Mostra o resultado das transações realizadas");
        
        JScrollPane scrollResultado = new JScrollPane(areaResultado);
        scrollResultado.setBorder(BorderFactory.createTitledBorder("Resultado"));
        painel.add(scrollResultado, BorderLayout.CENTER);
        
        return painel;
    }
    
    // ================== FUNCIONALIDADES ==================
    
    private void realizarDeposito(String agencia, String numero, String valorStr) {
        try {
            // Validações
            if (agencia.isEmpty() || numero.isEmpty() || valorStr.isEmpty()) {
                mostrarErro("Todos os campos são obrigatórios");
                return;
            }
            
            BigDecimal valor = new BigDecimal(valorStr.replace(",", "."));
            
            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                mostrarErro("Valor deve ser positivo");
                return;
            }
            
            // Realizar depósito
            boolean sucesso = bancoService.depositar(agencia, numero, valor);
            
            if (sucesso) {
                String resultado = "DEPÓSITO REALIZADO COM SUCESSO!\n" +
                                 "================================\n" +
                                 "Agência: " + agencia + "\n" +
                                 "Conta: " + numero + "\n" +
                                 "Valor: R$ " + String.format("%.2f", valor) + "\n" +
                                 "Data/Hora: " + java.time.LocalDateTime.now().format(
                                     java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n";
                
                mostrarSucesso(resultado);
                AccessibilityUtils.anunciarMensagem("Depósito realizado com sucesso");
            } else {
                mostrarErro("Falha ao realizar depósito. Verifique os dados da conta.");
            }
            
        } catch (NumberFormatException e) {
            mostrarErro("Valor inválido. Use apenas números.");
        } catch (Exception e) {
            mostrarErro("Erro ao realizar depósito: " + e.getMessage());
        }
    }
    
    private void realizarSaque(String agencia, String numero, String valorStr) {
        try {
            // Validações
            if (agencia.isEmpty() || numero.isEmpty() || valorStr.isEmpty()) {
                mostrarErro("Todos os campos são obrigatórios");
                return;
            }
            
            BigDecimal valor = new BigDecimal(valorStr.replace(",", "."));
            
            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                mostrarErro("Valor deve ser positivo");
                return;
            }
            
            // Realizar saque
            boolean sucesso = bancoService.sacar(agencia, numero, valor);
            
            if (sucesso) {
                String resultado = "SAQUE REALIZADO COM SUCESSO!\n" +
                                 "============================\n" +
                                 "Agência: " + agencia + "\n" +
                                 "Conta: " + numero + "\n" +
                                 "Valor: R$ " + String.format("%.2f", valor) + "\n" +
                                 "Data/Hora: " + java.time.LocalDateTime.now().format(
                                     java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n";
                
                mostrarSucesso(resultado);
                AccessibilityUtils.anunciarMensagem("Saque realizado com sucesso");
            } else {
                mostrarErro("Falha ao realizar saque. Verifique saldo e dados da conta.");
            }
            
        } catch (NumberFormatException e) {
            mostrarErro("Valor inválido. Use apenas números.");
        } catch (Exception e) {
            mostrarErro("Erro ao realizar saque: " + e.getMessage());
        }
    }
    
    private void realizarTransferencia(String agenciaOrigem, String numeroOrigem, 
                                     String agenciaDestino, String numeroDestino, String valorStr) {
        try {
            // Validações
            if (agenciaOrigem.isEmpty() || numeroOrigem.isEmpty() || 
                agenciaDestino.isEmpty() || numeroDestino.isEmpty() || valorStr.isEmpty()) {
                mostrarErro("Todos os campos são obrigatórios");
                return;
            }
            
            BigDecimal valor = new BigDecimal(valorStr.replace(",", "."));
            
            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                mostrarErro("Valor deve ser positivo");
                return;
            }
            
            // Realizar transferência
            boolean sucesso = bancoService.transferir(agenciaOrigem, numeroOrigem, 
                                                    agenciaDestino, numeroDestino, valor);
            
            if (sucesso) {
                String resultado = "TRANSFERÊNCIA REALIZADA COM SUCESSO!\n" +
                                 "===================================\n" +
                                 "Conta Origem: " + agenciaOrigem + "-" + numeroOrigem + "\n" +
                                 "Conta Destino: " + agenciaDestino + "-" + numeroDestino + "\n" +
                                 "Valor: R$ " + String.format("%.2f", valor) + "\n" +
                                 "Data/Hora: " + java.time.LocalDateTime.now().format(
                                     java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n";
                
                mostrarSucesso(resultado);
                AccessibilityUtils.anunciarMensagem("Transferência realizada com sucesso");
            } else {
                mostrarErro("Falha ao realizar transferência. Verifique saldo e dados das contas.");
            }
            
        } catch (NumberFormatException e) {
            mostrarErro("Valor inválido. Use apenas números.");
        } catch (Exception e) {
            mostrarErro("Erro ao realizar transferência: " + e.getMessage());
        }
    }
    
    private void realizarPix(String agenciaOrigem, String numeroOrigem, 
                           String agenciaDestino, String numeroDestino, String valorStr) {
        try {
            // Validações
            if (agenciaOrigem.isEmpty() || numeroOrigem.isEmpty() || 
                agenciaDestino.isEmpty() || numeroDestino.isEmpty() || valorStr.isEmpty()) {
                mostrarErro("Todos os campos são obrigatórios");
                return;
            }
            
            BigDecimal valor = new BigDecimal(valorStr.replace(",", "."));
            
            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                mostrarErro("Valor deve ser positivo");
                return;
            }
            
            // Realizar PIX (precisa buscar CPF da conta destino para usar como chave PIX)
            Optional<Conta> contaDestinoOpt = bancoService.buscarConta(agenciaDestino, numeroDestino);
            if (contaDestinoOpt.isEmpty()) {
                mostrarErro("Conta destino não encontrada");
                return;
            }
            
            String cpfDestino = contaDestinoOpt.get().getTitular().getCpf();
            boolean sucesso = bancoService.pix(agenciaOrigem, numeroOrigem, 
                                             cpfDestino, valor);
            
            if (sucesso) {
                String resultado = "PIX REALIZADO COM SUCESSO!\n" +
                                 "=========================\n" +
                                 "Conta Origem: " + agenciaOrigem + "-" + numeroOrigem + "\n" +
                                 "Conta Destino: " + agenciaDestino + "-" + numeroDestino + "\n" +
                                 "Valor: R$ " + String.format("%.2f", valor) + "\n" +
                                 "Data/Hora: " + java.time.LocalDateTime.now().format(
                                     java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n" +
                                 "Transação instantânea processada!\n";
                
                mostrarSucesso(resultado);
                AccessibilityUtils.anunciarMensagem("PIX realizado com sucesso");
            } else {
                mostrarErro("Falha ao realizar PIX. Verifique saldo e dados das contas.");
            }
            
        } catch (NumberFormatException e) {
            mostrarErro("Valor inválido. Use apenas números.");
        } catch (Exception e) {
            mostrarErro("Erro ao realizar PIX: " + e.getMessage());
        }
    }
    
    private void consultarSaldo(String agencia, String numero) {
        try {
            // Validações
            if (agencia.isEmpty() || numero.isEmpty()) {
                mostrarErro("Agência e número da conta são obrigatórios");
                return;
            }
            
            // Buscar conta
            var contaOpt = bancoService.buscarConta(agencia, numero);
            
            if (contaOpt.isPresent()) {
                var conta = contaOpt.get();
                
                StringBuilder resultado = new StringBuilder();
                resultado.append("CONSULTA DE SALDO\n");
                resultado.append("=================\n\n");
                resultado.append("Agência: ").append(conta.getAgencia()).append("\n");
                resultado.append("Número: ").append(conta.getNumero()).append("\n");
                resultado.append("Tipo: ").append(conta.getTipoConta().getDescricao()).append("\n");
                resultado.append("Titular: ").append(conta.getTitular().getNome()).append("\n");
                resultado.append("CPF: ").append(conta.getTitular().getCpf()).append("\n\n");
                
                resultado.append("SALDO ATUAL: R$ ").append(String.format("%.2f", conta.getSaldo())).append("\n\n");
                
                // Informações específicas por tipo
                switch (conta.getTipoConta()) {
                    case CORRENTE:
                    if (conta instanceof ContaCorrente) {
                        ContaCorrente cc = (ContaCorrente) conta;
                        resultado.append("Limite de Crédito: R$ ").append(String.format("%.2f", cc.getLimite())).append("\n");
                        resultado.append("Saldo + Limite: R$ ").append(String.format("%.2f", 
                            conta.getSaldo().add(cc.getLimite()))).append("\n");
                    }
                        break;
                    case POUPANCA:
                    if (conta instanceof ContaPoupanca) {
                        resultado.append("Taxa de Rendimento: ").append(ContaPoupanca.getTaxaRendimento().multiply(new BigDecimal("100")).setScale(2)).append("% a.m.\n");
                    }
                        break;
                    case INVESTIMENTO:
                    if (conta instanceof ContaInvestimento) {
                        ContaInvestimento ci = (ContaInvestimento) conta;
                        resultado.append("Valor Mínimo Saque: R$ ").append(String.format("%.2f", ContaInvestimento.getValorMinimoSaque())).append("\n");
                        resultado.append("Total de Investimentos: ").append(ci.getInvestimentos().size()).append("\n");
                    }
                        break;
                }
                
                resultado.append("\nTotal de Transações: ").append(conta.getHistorico().size()).append("\n");
                resultado.append("Status: ").append(conta.isAtiva() ? "Ativa" : "Inativa").append("\n");
                resultado.append("Consulta realizada em: ").append(
                    java.time.LocalDateTime.now().format(
                        java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))).append("\n");
                
                areaResultado.setText(resultado.toString());
                areaResultado.setCaretPosition(0); // Rola para o topo
                
                SoundUtils.tocarSucesso();
                AccessibilityUtils.anunciarMensagem("Consulta de saldo realizada. Saldo: R$ " + 
                    String.format("%.2f", conta.getSaldo()));
                
            } else {
                mostrarErro("Conta não encontrada com agência " + agencia + " e número " + numero);
            }
            
        } catch (Exception e) {
            mostrarErro("Erro ao consultar saldo: " + e.getMessage());
        }
    }
    
    private void mostrarSucesso(String mensagem) {
        areaResultado.setText(mensagem);
        areaResultado.setCaretPosition(0);
        SoundUtils.tocarSucesso();
        
        // Ir para aba de saldo para mostrar resultado
        abas.setSelectedIndex(4); // Índice da aba consultar saldo
    }
    
    private void mostrarErro(String mensagem) {
        String textoErro = "ERRO\n" +
                          "====\n\n" +
                          mensagem + "\n\n" +
                          "Verifique os dados e tente novamente.\n" +
                          "Erro reportado em: " + 
                          java.time.LocalDateTime.now().format(
                              java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        
        areaResultado.setText(textoErro);
        areaResultado.setCaretPosition(0);
        SoundUtils.tocarErro();
        AccessibilityUtils.anunciarMensagem("Erro: " + mensagem);
        
        // Ir para aba de saldo para mostrar resultado
        abas.setSelectedIndex(4); // Índice da aba consultar saldo
        
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }
    
    private void configurarAcessibilidade() {
        // Configura acessibilidade da janela
        getAccessibleContext().setAccessibleName("Transações Bancárias");
        getAccessibleContext().setAccessibleDescription(
            "Janela para realizar transações bancárias: depósito, saque, transferência, PIX e consulta de saldo");
        
        // Configura acessibilidade das abas
        abas.getAccessibleContext().setAccessibleName("Abas de Transações");
        abas.getAccessibleContext().setAccessibleDescription(
            "Abas para diferentes tipos de transações bancárias");
        
        // Foca primeiro elemento ao abrir
        SwingUtilities.invokeLater(() -> {
            AccessibilityUtils.focarPrimeiroElemento(this);
        });
    }
}
