package com.nttdata.banco.gui.components;

import com.nttdata.banco.gui.utils.AccessibilityUtils;
import com.nttdata.banco.gui.theme.ThemeManager;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Campo de texto com funcionalidades de acessibilidade aprimoradas
 * Inclui validação, feedback visual e suporte a screen readers
 */
public class AccessibleTextField extends JTextField implements FocusListener {
    
    private String rotulo;
    private String descricao;
    private boolean obrigatorio;
    private boolean valido = true;
    private String mensagemErro = "";
    
    public AccessibleTextField(String rotulo) {
        this(rotulo, "", false);
    }
    
    public AccessibleTextField(String rotulo, String descricao, boolean obrigatorio) {
        super();
        this.rotulo = rotulo;
        this.descricao = descricao;
        this.obrigatorio = obrigatorio;
        
        inicializarAcessibilidade();
        configurarAparencia();
        adicionarListeners();
    }
    
    private void inicializarAcessibilidade() {
        // Configura propriedades de acessibilidade
        AccessibilityUtils.configurarCampoTextoAcessivel(
            this, rotulo, descricao, obrigatorio
        );
        
        // Garante que é focável
        setFocusable(true);
    }
    
    private void configurarAparencia() {
        // Aplica cores do tema atual
        var config = ThemeManager.getConfig();
        setBackground(config.getBackground());
        setForeground(config.getForeground());
        
        // Bordas acessíveis
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(config.getSecondary(), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        // Altura mínima para toque
        setPreferredSize(new Dimension(getPreferredSize().width, 40));
    }
    
    private void adicionarListeners() {
        addFocusListener(this);
    }
    
    @Override
    public void focusGained(FocusEvent e) {
        // Destaca o campo quando recebe foco
        var config = ThemeManager.getConfig();
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(config.getPrimary(), 2),
            BorderFactory.createEmptyBorder(7, 11, 7, 11)
        ));
        
        // Anuncia o campo para screen readers
        AccessibilityUtils.anunciarMensagem("Campo " + rotulo + 
            (obrigatorio ? " obrigatório" : ""));
    }
    
    @Override
    public void focusLost(FocusEvent e) {
        // Remove destaque quando perde foco
        configurarAparencia();
        
        // Valida campo obrigatório
        if (obrigatorio && getText().trim().isEmpty()) {
            marcarComoInvalido("Campo obrigatório");
        } else {
            marcarComoValido();
        }
    }
    
    /**
     * Marca o campo como inválido com mensagem de erro
     */
    public void marcarComoInvalido(String mensagem) {
        this.valido = false;
        this.mensagemErro = mensagem;
        
        var config = ThemeManager.getConfig();
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(config.getDanger(), 2),
            BorderFactory.createEmptyBorder(7, 11, 7, 11)
        ));
        
        // Atualiza descrição para screen readers
        getAccessibleContext().setAccessibleDescription(
            descricao + " - ERRO: " + mensagem
        );
        
        // Anuncia erro
        AccessibilityUtils.anunciarMensagem("Erro: " + mensagem);
    }
    
    /**
     * Marca o campo como válido
     */
    public void marcarComoValido() {
        this.valido = true;
        this.mensagemErro = "";
        
        configurarAparencia();
        
        // Restaura descrição original
        getAccessibleContext().setAccessibleDescription(descricao);
    }
    
    // Getters e Setters
    public boolean isValido() {
        return valido;
    }
    
    public String getMensagemErro() {
        return mensagemErro;
    }
    
    public boolean isObrigatorio() {
        return obrigatorio;
    }
    
    public void setObrigatorio(boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
        inicializarAcessibilidade();
    }
    
    public String getRotulo() {
        return rotulo;
    }
}
