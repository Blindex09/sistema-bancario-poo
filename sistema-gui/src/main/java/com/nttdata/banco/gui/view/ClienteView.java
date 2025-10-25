package com.nttdata.banco.gui.view;

import com.nttdata.banco.gui.components.AccessibleButton;
import com.nttdata.banco.gui.components.AccessibleTextField;
import com.nttdata.banco.gui.theme.ThemeManager;
import com.nttdata.banco.gui.utils.AccessibilityUtils;
import com.nttdata.banco.gui.utils.SoundUtils;
import com.nttdata.banco.model.Cliente;
import com.nttdata.banco.service.BancoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

/**
 * Tela de Gestão de Clientes - Totalmente Acessível
 * Permite criar, buscar e listar clientes
 */
public class ClienteView extends JDialog {
    
    private BancoService bancoService;
    private JTabbedPane abas;
    
    // Aba Criar Cliente
    private AccessibleTextField campoNome;
    private AccessibleTextField campoCpf;
    private AccessibleTextField campoEmail;
    private AccessibleTextField campoTelefone;
    private AccessibleTextField campoDataNascimento;
    
    // Aba Buscar Cliente
    private AccessibleTextField campoBuscarCpf;
    private JTextArea resultadoBusca;
    
    // Aba Listar Clientes
    private JTable tabelaClientes;
    private DefaultTableModel modeloTabela;
    
    public ClienteView(Frame parent, BancoService bancoService) {
        super(parent, "Gestão de Clientes", true);
        this.bancoService = bancoService;
        
        inicializarInterface();
        criarAbas();
        configurarAcessibilidade();
        
        setSize(800, 600);
        setLocationRelativeTo(parent);
    }
    
    private void inicializarInterface() {
        setLayout(new BorderLayout());
        
        // Título
        JLabel titulo = new JLabel("👥 Gestão de Clientes", SwingConstants.CENTER);
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
        // Aba 1: Criar Cliente
        JPanel abaCriar = criarAbaCriarCliente();
        abas.addTab("Criar Cliente", abaCriar);
        
        // Aba 2: Buscar Cliente
        JPanel abaBuscar = criarAbaBuscarCliente();
        abas.addTab("Buscar Cliente", abaBuscar);
        
        // Aba 3: Listar Clientes
        JPanel abaListar = criarAbaListarClientes();
        abas.addTab("Listar Clientes", abaListar);
    }
    
    private JPanel criarAbaCriarCliente() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Título
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel titulo = new JLabel("➕ Criar Novo Cliente");
        titulo.setFont(titulo.getFont().deriveFont(16f));
        painel.add(titulo, gbc);
        
        // Campos do formulário
        gbc.gridwidth = 1;
        
        // Nome
        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(new JLabel("Nome Completo:"), gbc);
        gbc.gridx = 1;
        campoNome = new AccessibleTextField("Nome", "Nome completo do cliente", true);
        campoNome.setPreferredSize(new Dimension(300, 40));
        painel.add(campoNome, gbc);
        
        // CPF
        gbc.gridx = 0; gbc.gridy = 2;
        painel.add(new JLabel("CPF:"), gbc);
        gbc.gridx = 1;
        campoCpf = new AccessibleTextField("CPF", "CPF do cliente (apenas números)", true);
        campoCpf.setPreferredSize(new Dimension(300, 40));
        painel.add(campoCpf, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 3;
        painel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        campoEmail = new AccessibleTextField("Email", "Email do cliente", true);
        campoEmail.setPreferredSize(new Dimension(300, 40));
        painel.add(campoEmail, gbc);
        
        // Telefone
        gbc.gridx = 0; gbc.gridy = 4;
        painel.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1;
        campoTelefone = new AccessibleTextField("Telefone", "Telefone do cliente", true);
        campoTelefone.setPreferredSize(new Dimension(300, 40));
        painel.add(campoTelefone, gbc);
        
        // Data de Nascimento
        gbc.gridx = 0; gbc.gridy = 5;
        painel.add(new JLabel("Data Nascimento:"), gbc);
        gbc.gridx = 1;
        campoDataNascimento = new AccessibleTextField("Data Nascimento", "Data de nascimento (dd/MM/yyyy)", true);
        campoDataNascimento.setPreferredSize(new Dimension(300, 40));
        painel.add(campoDataNascimento, gbc);
        
        // Botão Criar
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        AccessibleButton btnCriar = new AccessibleButton("➕ Criar Cliente", "C");
        btnCriar.setPreferredSize(new Dimension(200, 50));
        btnCriar.addActionListener(e -> criarCliente());
        painel.add(btnCriar, gbc);
        
        return painel;
    }
    
    private JPanel criarAbaBuscarCliente() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Título
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel titulo = new JLabel("🔍 Buscar Cliente por CPF");
        titulo.setFont(titulo.getFont().deriveFont(16f));
        painel.add(titulo, gbc);
        
        // Campo CPF para busca
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(new JLabel("CPF:"), gbc);
        gbc.gridx = 1;
        campoBuscarCpf = new AccessibleTextField("CPF Busca", "Digite o CPF para buscar", true);
        campoBuscarCpf.setPreferredSize(new Dimension(300, 40));
        painel.add(campoBuscarCpf, gbc);
        
        // Botão Buscar
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        AccessibleButton btnBuscar = new AccessibleButton("🔍 Buscar", "B");
        btnBuscar.setPreferredSize(new Dimension(150, 40));
        btnBuscar.addActionListener(e -> buscarCliente());
        painel.add(btnBuscar, gbc);
        
        // Área de resultado
        gbc.gridy = 3; gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        resultadoBusca = new JTextArea(15, 50);
        resultadoBusca.setEditable(false);
        resultadoBusca.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        AccessibilityUtils.configurarAcessibilidade(resultadoBusca, 
            "Resultado da busca", "Mostra os dados do cliente encontrado");
        
        JScrollPane scrollResultado = new JScrollPane(resultadoBusca);
        painel.add(scrollResultado, gbc);
        
        return painel;
    }
    
    private JPanel criarAbaListarClientes() {
        JPanel painel = new JPanel(new BorderLayout());
        
        // Título e botão atualizar
        JPanel painelTopo = new JPanel(new FlowLayout());
        JLabel titulo = new JLabel("📋 Lista de Todos os Clientes");
        titulo.setFont(titulo.getFont().deriveFont(16f));
        painelTopo.add(titulo);
        
        AccessibleButton btnAtualizar = new AccessibleButton("🔄 Atualizar", "A");
        btnAtualizar.addActionListener(e -> atualizarListaClientes());
        painelTopo.add(btnAtualizar);
        
        painel.add(painelTopo, BorderLayout.NORTH);
        
        // Tabela de clientes
        String[] colunas = {"Nome", "CPF", "Email", "Telefone", "Data Nascimento"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Somente leitura
            }
        };
        
        tabelaClientes = new JTable(modeloTabela);
        AccessibilityUtils.configurarTabelaAcessivel(tabelaClientes, 
            "Lista de clientes cadastrados no sistema");
        
        // Configura largura das colunas
        tabelaClientes.getColumnModel().getColumn(0).setPreferredWidth(200); // Nome
        tabelaClientes.getColumnModel().getColumn(1).setPreferredWidth(120); // CPF
        tabelaClientes.getColumnModel().getColumn(2).setPreferredWidth(200); // Email
        tabelaClientes.getColumnModel().getColumn(3).setPreferredWidth(120); // Telefone
        tabelaClientes.getColumnModel().getColumn(4).setPreferredWidth(100); // Data
        
        JScrollPane scrollTabela = new JScrollPane(tabelaClientes);
        painel.add(scrollTabela, BorderLayout.CENTER);
        
        // Carrega dados iniciais
        atualizarListaClientes();
        
        return painel;
    }
    
    // ================== FUNCIONALIDADES ==================
    
    private void criarCliente() {
        try {
            // Validar campos
            if (!validarCamposCliente()) {
                return;
            }
            
            // Obter dados dos campos
            String nome = campoNome.getText().trim();
            String cpf = campoCpf.getText().trim();
            String email = campoEmail.getText().trim();
            String telefone = campoTelefone.getText().trim();
            
            // Converter data
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataNascimento;
            try {
                dataNascimento = LocalDate.parse(campoDataNascimento.getText().trim(), formatter);
            } catch (DateTimeParseException e) {
                campoDataNascimento.marcarComoInvalido("Formato inválido. Use dd/MM/yyyy");
                SoundUtils.tocarErro();
                AccessibilityUtils.anunciarMensagem("Erro: Data em formato inválido");
                return;
            }
            
            // Criar cliente
            Cliente cliente = bancoService.criarCliente(nome, cpf, email, telefone, dataNascimento);
            
            // Limpar campos
            limparCamposCliente();
            
            // Feedback de sucesso
            SoundUtils.tocarSucesso();
            AccessibilityUtils.anunciarMensagem("Cliente criado com sucesso: " + nome);
            JOptionPane.showMessageDialog(this,
                "Cliente criado com sucesso!\n" +
                "Nome: " + cliente.getNome() + "\n" +
                "CPF: " + cliente.getCpf(),
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            // Atualizar lista se estiver na aba correspondente
            atualizarListaClientes();
            
        } catch (IllegalArgumentException e) {
            SoundUtils.tocarErro();
            AccessibilityUtils.anunciarMensagem("Erro: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                "Erro ao criar cliente:\n" + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            SoundUtils.tocarErro();
            AccessibilityUtils.anunciarMensagem("Erro inesperado");
            JOptionPane.showMessageDialog(this,
                "Erro inesperado: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validarCamposCliente() {
        boolean valido = true;
        
        // Validar nome
        if (campoNome.getText().trim().isEmpty()) {
            campoNome.marcarComoInvalido("Nome é obrigatório");
            valido = false;
        } else {
            campoNome.marcarComoValido();
        }
        
        // Validar CPF
        String cpf = campoCpf.getText().trim();
        if (cpf.isEmpty()) {
            campoCpf.marcarComoInvalido("CPF é obrigatório");
            valido = false;
        } else if (!cpf.matches("\\d{11}")) {
            campoCpf.marcarComoInvalido("CPF deve ter 11 dígitos");
            valido = false;
        } else {
            campoCpf.marcarComoValido();
        }
        
        // Validar email
        String email = campoEmail.getText().trim();
        if (email.isEmpty()) {
            campoEmail.marcarComoInvalido("Email é obrigatório");
            valido = false;
        } else if (!email.contains("@")) {
            campoEmail.marcarComoInvalido("Email deve conter @");
            valido = false;
        } else {
            campoEmail.marcarComoValido();
        }
        
        // Validar telefone
        if (campoTelefone.getText().trim().isEmpty()) {
            campoTelefone.marcarComoInvalido("Telefone é obrigatório");
            valido = false;
        } else {
            campoTelefone.marcarComoValido();
        }
        
        // Validar data
        if (campoDataNascimento.getText().trim().isEmpty()) {
            campoDataNascimento.marcarComoInvalido("Data é obrigatória");
            valido = false;
        } else {
            campoDataNascimento.marcarComoValido();
        }
        
        return valido;
    }
    
    private void limparCamposCliente() {
        campoNome.setText("");
        campoCpf.setText("");
        campoEmail.setText("");
        campoTelefone.setText("");
        campoDataNascimento.setText("");
        
        // Marcar todos como válidos
        campoNome.marcarComoValido();
        campoCpf.marcarComoValido();
        campoEmail.marcarComoValido();
        campoTelefone.marcarComoValido();
        campoDataNascimento.marcarComoValido();
    }
    
    private void buscarCliente() {
        try {
            String cpf = campoBuscarCpf.getText().trim();
            
            if (cpf.isEmpty()) {
                campoBuscarCpf.marcarComoInvalido("CPF é obrigatório");
                SoundUtils.tocarErro();
                return;
            }
            
            campoBuscarCpf.marcarComoValido();
            
            Optional<Cliente> clienteOpt = bancoService.buscarClientePorCpf(cpf);
            
            if (clienteOpt.isPresent()) {
                Cliente cliente = clienteOpt.get();
                
                StringBuilder resultado = new StringBuilder();
                resultado.append("CLIENTE ENCONTRADO\n");
                resultado.append("==================\n\n");
                resultado.append("Nome: ").append(cliente.getNome()).append("\n");
                resultado.append("CPF: ").append(cliente.getCpf()).append("\n");
                resultado.append("Email: ").append(cliente.getEmail()).append("\n");
                resultado.append("Telefone: ").append(cliente.getTelefone()).append("\n");
                resultado.append("Data Nascimento: ").append(
                    cliente.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                ).append("\n");
                
                if (cliente.getEndereco() != null) {
                    resultado.append("Endereço: ").append(cliente.getEndereco()).append("\n");
                }
                
                resultado.append("\nCliente cadastrado em: ").append(
                    (cliente.getDataNascimento() != null ? 
                        cliente.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : 
                        "Não informado")
                ).append("\n");
                
                resultadoBusca.setText(resultado.toString());
                
                SoundUtils.tocarSucesso();
                AccessibilityUtils.anunciarMensagem("Cliente encontrado: " + cliente.getNome());
                
            } else {
                resultadoBusca.setText("CLIENTE NÃO ENCONTRADO\n" +
                                     "====================\n\n" +
                                     "Nenhum cliente encontrado com o CPF: " + cpf + "\n\n" +
                                     "Verifique se o CPF está correto ou\n" +
                                     "cadastre um novo cliente.");
                
                SoundUtils.tocarAviso();
                AccessibilityUtils.anunciarMensagem("Cliente não encontrado");
            }
            
        } catch (Exception e) {
            SoundUtils.tocarErro();
            AccessibilityUtils.anunciarMensagem("Erro na busca");
            resultadoBusca.setText("ERRO NA BUSCA\n" +
                                 "=============\n\n" +
                                 "Erro: " + e.getMessage());
        }
    }
    
    private void atualizarListaClientes() {
        try {
            // Limpar tabela
            modeloTabela.setRowCount(0);
            
            // Buscar todos os clientes
            List<Cliente> clientes = bancoService.listarClientes();
            
            // Adicionar clientes à tabela
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            for (Cliente cliente : clientes) {
                Object[] linha = {
                    cliente.getNome(),
                    cliente.getCpf(),
                    cliente.getEmail(),
                    cliente.getTelefone(),
                    cliente.getDataNascimento().format(formatter)
                };
                modeloTabela.addRow(linha);
            }
            
            // Feedback
            String mensagem = "Lista atualizada: " + clientes.size() + " cliente(s) encontrado(s)";
            AccessibilityUtils.anunciarMensagem(mensagem);
            
            if (clientes.isEmpty()) {
                SoundUtils.tocarAviso();
            } else {
                SoundUtils.tocarInfo();
            }
            
        } catch (Exception e) {
            SoundUtils.tocarErro();
            AccessibilityUtils.anunciarMensagem("Erro ao carregar lista de clientes");
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar lista de clientes:\n" + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void configurarAcessibilidade() {
        // Configura acessibilidade da janela
        getAccessibleContext().setAccessibleName("Gestão de Clientes");
        getAccessibleContext().setAccessibleDescription(
            "Janela para criar, buscar e listar clientes do sistema bancário");
        
        // Configura acessibilidade das abas
        abas.getAccessibleContext().setAccessibleName("Abas de Gestão de Clientes");
        abas.getAccessibleContext().setAccessibleDescription(
            "Abas para diferentes operações com clientes");
        
        // Foca primeiro elemento ao abrir
        SwingUtilities.invokeLater(() -> {
            AccessibilityUtils.focarPrimeiroElemento(this);
        });
    }
}
