package com.nttdata.banco.gui.theme;

import javax.swing.*;
import java.awt.*;
import java.util.prefs.Preferences;

/**
 * Gerenciador de Fontes Acessíveis
 * Permite ajuste de tamanho e tipo de fonte para melhor legibilidade
 */
public class FontManager {
    
    public enum TamanhoFonte {
        PEQUENO(12, "Pequeno"),
        NORMAL(14, "Normal"),
        GRANDE(16, "Grande"),
        EXTRA_GRANDE(18, "Extra Grande"),
        GIGANTE(20, "Gigante");
        
        private final int tamanho;
        private final String descricao;
        
        TamanhoFonte(int tamanho, String descricao) {
            this.tamanho = tamanho;
            this.descricao = descricao;
        }
        
        public int getTamanho() { return tamanho; }
        public String getDescricao() { return descricao; }
    }
    
    private static TamanhoFonte tamanhoAtual = TamanhoFonte.NORMAL;
    private static final Preferences prefs = Preferences.userNodeForPackage(FontManager.class);
    private static final String FONTE_PREFERENCIA = "tamanho_fonte";
    
    static {
        carregarPreferencias();
    }
    
    /**
     * Aplica o tamanho de fonte especificado em todo o sistema
     */
    public static void aplicarTamanhoFonte(TamanhoFonte tamanho) {
        tamanhoAtual = tamanho;
        
        // Fonte principal para textos
        Font fonteTexto = new Font("Segoe UI", Font.PLAIN, tamanho.getTamanho());
        Font fonteTitulo = new Font("Segoe UI", Font.BOLD, tamanho.getTamanho() + 2);
        Font fonteBotao = new Font("Segoe UI", Font.PLAIN, tamanho.getTamanho());
        
        // Configura UIManager
        UIManager.put("Button.font", fonteBotao);
        UIManager.put("Label.font", fonteTexto);
        UIManager.put("TextField.font", fonteTexto);
        UIManager.put("TextArea.font", fonteTexto);
        UIManager.put("Table.font", fonteTexto);
        UIManager.put("Menu.font", fonteTexto);
        UIManager.put("MenuItem.font", fonteTexto);
        UIManager.put("TitledBorder.font", fonteTitulo);
        
        // Salva preferência
        salvarPreferencias();
        
        // Atualiza todas as janelas
        for (Window window : Window.getWindows()) {
            atualizarFontesComponente(window);
            SwingUtilities.updateComponentTreeUI(window);
        }
    }
    
    /**
     * Atualiza recursivamente as fontes de um componente e seus filhos
     */
    private static void atualizarFontesComponente(Component componente) {
        if (componente instanceof Container) {
            Container container = (Container) componente;
            for (Component filho : container.getComponents()) {
                atualizarFontesComponente(filho);
            }
        }
        
        if (componente instanceof JComponent) {
            JComponent jComponent = (JComponent) componente;
            Font fonteAtual = jComponent.getFont();
            if (fonteAtual != null) {
                Font novaFonte = new Font(fonteAtual.getName(), fonteAtual.getStyle(), 
                                        tamanhoAtual.getTamanho());
                jComponent.setFont(novaFonte);
            }
        }
    }
    
    /**
     * Carrega preferências salvas do usuário
     */
    private static void carregarPreferencias() {
        String tamanhoSalvo = prefs.get(FONTE_PREFERENCIA, TamanhoFonte.NORMAL.name());
        try {
            tamanhoAtual = TamanhoFonte.valueOf(tamanhoSalvo);
        } catch (IllegalArgumentException e) {
            tamanhoAtual = TamanhoFonte.NORMAL;
        }
    }
    
    /**
     * Salva preferências do usuário
     */
    private static void salvarPreferencias() {
        prefs.put(FONTE_PREFERENCIA, tamanhoAtual.name());
    }
    
    /**
     * Aumenta o tamanho da fonte (máximo: GIGANTE)
     */
    public static void aumentarFonte() {
        TamanhoFonte[] tamanhos = TamanhoFonte.values();
        int indiceAtual = tamanhoAtual.ordinal();
        if (indiceAtual < tamanhos.length - 1) {
            aplicarTamanhoFonte(tamanhos[indiceAtual + 1]);
        }
    }
    
    /**
     * Diminui o tamanho da fonte (mínimo: PEQUENO)
     */
    public static void diminuirFonte() {
        TamanhoFonte[] tamanhos = TamanhoFonte.values();
        int indiceAtual = tamanhoAtual.ordinal();
        if (indiceAtual > 0) {
            aplicarTamanhoFonte(tamanhos[indiceAtual - 1]);
        }
    }
    
    // Getters
    public static TamanhoFonte getTamanhoAtual() {
        return tamanhoAtual;
    }
    
    public static Font getFonteTexto() {
        return new Font("Segoe UI", Font.PLAIN, tamanhoAtual.getTamanho());
    }
    
    public static Font getFonteTitulo() {
        return new Font("Segoe UI", Font.BOLD, tamanhoAtual.getTamanho() + 2);
    }
    
    public static Font getFonteBotao() {
        return new Font("Segoe UI", Font.PLAIN, tamanhoAtual.getTamanho());
    }
}
