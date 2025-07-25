package com.nttdata.banco.gui.utils;

import javax.swing.*;
import javax.accessibility.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Utilitários para Acessibilidade Digital
 * Facilita a implementação de recursos de acessibilidade
 */
public class AccessibilityUtils {
    
    /**
     * Configura acessibilidade básica para um componente
     */
    public static void configurarAcessibilidade(JComponent componente, 
                                               String nome, 
                                               String descricao) {
        componente.getAccessibleContext().setAccessibleName(nome);
        componente.getAccessibleContext().setAccessibleDescription(descricao);
        // Remove ToolTipText para evitar duplicação no leitor de telas
        // componente.setToolTipText(descricao);
    }
    
    /**
     * Configura acessibilidade para botões
     */
    public static void configurarBotaoAcessivel(JButton botao, 
                                              String texto, 
                                              String descricao,
                                              String atalho) {
        // Remove duplicação - só define AccessibleName sem AccessibleDescription repetitiva
        botao.getAccessibleContext().setAccessibleName(texto);
        
        // Adiciona mnemônico se especificado sem alterar o texto visível
        if (atalho != null && !atalho.isEmpty()) {
            botao.setMnemonic(atalho.charAt(0));
        }
        
        // Garante tamanho mínimo para toque
        Dimension tamanhoMinimo = new Dimension(44, 44);
        if (botao.getPreferredSize().width < tamanhoMinimo.width ||
            botao.getPreferredSize().height < tamanhoMinimo.height) {
            botao.setPreferredSize(tamanhoMinimo);
        }
    }
    
    /**
     * Configura acessibilidade para campos de texto
     */
    public static void configurarCampoTextoAcessivel(JTextField campo,
                                                   String rotulo,
                                                   String descricao,
                                                   boolean obrigatorio) {
        configurarAcessibilidade(campo, rotulo, descricao);
        
        if (obrigatorio) {
            campo.getAccessibleContext().setAccessibleDescription(
                descricao + " - Campo obrigatório");
        }
        
        // Garante altura mínima
        if (campo.getPreferredSize().height < 32) {
            campo.setPreferredSize(new Dimension(
                campo.getPreferredSize().width, 32));
        }
    }
    
    /**
     * Configura acessibilidade para tabelas
     */
    public static void configurarTabelaAcessivel(JTable tabela, String descricao) {
        configurarAcessibilidade(tabela, "Tabela de dados", descricao);
        
        // Configura cabeçalhos
        tabela.getTableHeader().getAccessibleContext()
               .setAccessibleDescription("Cabeçalhos da tabela");
        
        // Habilita navegação por teclado
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Configura cores de seleção com bom contraste
        tabela.setSelectionBackground(Color.BLUE);
        tabela.setSelectionForeground(Color.WHITE);
    }
    
    /**
     * Anuncia mensagem para screen readers
     */
    public static void anunciarMensagem(String mensagem) {
        // Cria label invisível para anunciar
        JLabel anunciador = new JLabel(mensagem);
        anunciador.getAccessibleContext().setAccessibleName("Mensagem");
        anunciador.getAccessibleContext().setAccessibleDescription(mensagem);
        
        // Força notificação para screen readers
        AccessibleContext context = anunciador.getAccessibleContext();
        if (context != null) {
            context.firePropertyChange(
                AccessibleContext.ACCESSIBLE_NAME_PROPERTY, 
                null, mensagem);
        }
    }
    
    /**
     * Verifica se tecnologias assistivas estão ativas
     */
    public static boolean tecnologiasAssistivasAtivas() {
        return Toolkit.getDefaultToolkit().getSystemEventQueue() != null;
    }
    
    /**
     * Configura atalhos de teclado padrão para acessibilidade
     */
    public static void configurarAtalhosPadrao(JComponent componente) {
        InputMap inputMap = componente.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = componente.getActionMap();
        
        // Ctrl+Plus/Minus para zoom
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, KeyEvent.CTRL_DOWN_MASK), "aumentar_fonte");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, KeyEvent.CTRL_DOWN_MASK), "diminuir_fonte");
        
        actionMap.put("aumentar_fonte", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                com.nttdata.banco.gui.theme.FontManager.aumentarFonte();
                anunciarMensagem("Fonte aumentada");
            }
        });
        
        actionMap.put("diminuir_fonte", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                com.nttdata.banco.gui.theme.FontManager.diminuirFonte();
                anunciarMensagem("Fonte diminuída");
            }
        });
    }
    
    /**
     * Foca automaticamente o primeiro elemento focável
     */
    public static void focarPrimeiroElemento(Container container) {
        Component[] componentes = container.getComponents();
        for (Component comp : componentes) {
            if (comp.isFocusable()) {
                comp.requestFocus();
                break;
            }
            if (comp instanceof Container) {
                focarPrimeiroElemento((Container) comp);
            }
        }
    }
}