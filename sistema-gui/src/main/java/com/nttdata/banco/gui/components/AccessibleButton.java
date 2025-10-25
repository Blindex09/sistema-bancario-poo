package com.nttdata.banco.gui.components;

import com.nttdata.banco.gui.utils.AccessibilityUtils;
import com.nttdata.banco.gui.utils.SoundUtils;
import com.nttdata.banco.gui.theme.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Botão com funcionalidades de acessibilidade aprimoradas
 * Inclui feedback sonoro, navegação por teclado e cores acessíveis
 */
public class AccessibleButton extends JButton implements KeyListener {
    
    private String atalho;
    private boolean temFeedbackSonoro;
    
    public AccessibleButton(String texto) {
        this(texto, null, true);
    }
    
    public AccessibleButton(String texto, String atalho) {
        this(texto, atalho, true);
    }
    
    public AccessibleButton(String texto, String atalho, boolean feedbackSonoro) {
        super(texto);
        this.atalho = atalho;
        this.temFeedbackSonoro = feedbackSonoro;
        
        inicializarAcessibilidade();
        configurarAparencia();
        adicionarListeners();
    }
    
    private void inicializarAcessibilidade() {
        // Configura propriedades de acessibilidade
        AccessibilityUtils.configurarBotaoAcessivel(
            this, 
            getText(), 
            "Botão " + getText(), 
            atalho
        );
        
        // Garante que é focável
        setFocusable(true);
        
        // Adiciona suporte a teclas
        addKeyListener(this);
    }
    
    private void configurarAparencia() {
        // Aplica cores do tema atual
        var config = ThemeManager.getConfig();
        setBackground(config.getLight());
        setForeground(config.getForeground());
        
        // Bordas acessíveis
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(config.getSecondary(), 1),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        
        // Cursor em modo de ponteiro
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    
    private void adicionarListeners() {
        // Adiciona feedback sonoro ao click
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (temFeedbackSonoro) {
                    SoundUtils.tocarSucesso();
                }
            }
        });
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        // Enter ou Espaço ativam o botão
        if (e.getKeyCode() == KeyEvent.VK_ENTER || 
            e.getKeyCode() == KeyEvent.VK_SPACE) {
            doClick();
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        // Implementação vazia - necessária para interface
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        // Implementação vazia - necessária para interface
    }
    
    /**
     * Define se o botão emite feedback sonoro
     */
    public void setFeedbackSonoro(boolean feedback) {
        this.temFeedbackSonoro = feedback;
    }
    
    /**
     * Obtém o atalho do botão
     */
    public String getAtalho() {
        return atalho;
    }
    
    /**
     * Define novo atalho para o botão
     */
    public void setAtalho(String novoAtalho) {
        this.atalho = novoAtalho;
        if (novoAtalho != null && !novoAtalho.isEmpty()) {
            setMnemonic(novoAtalho.charAt(0));
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Adiciona indicador visual de foco para usuários que não usam screen reader
        if (hasFocus()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(ThemeManager.getConfig().getPrimary());
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(2, 2, getWidth() - 4, getHeight() - 4);
            g2.dispose();
        }
    }
}
