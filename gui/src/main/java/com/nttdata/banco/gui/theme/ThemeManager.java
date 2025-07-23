package com.nttdata.banco.gui.theme;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Gerenciador de Temas Acessíveis
 * Implementa diferentes temas com foco em acessibilidade digital
 */
public class ThemeManager {
    
    public enum TipoTema {
        CLARO("Tema Claro"),
        ESCURO("Tema Escuro"), 
        ALTO_CONTRASTE("Alto Contraste"),
        PROTANOPIA("Protanopia/Deuteranopia"),
        TRITANOPIA("Tritanopia");
        
        private final String descricao;
        
        TipoTema(String descricao) {
            this.descricao = descricao;
        }
        
        public String getDescricao() {
            return descricao;
        }
    }
    
    private static TipoTema temaAtual = TipoTema.CLARO;
    private static final Map<TipoTema, TemaConfig> temas = new HashMap<>();
    
    static {
        inicializarTemas();
    }
    
    private static void inicializarTemas() {
        // Tema Claro - WCAG AA Compliant
        temas.put(TipoTema.CLARO, new TemaConfig(
            new Color(255, 255, 255), // background
            new Color(33, 37, 41),     // foreground (texto)
            new Color(0, 123, 255),    // primary (azul)
            new Color(40, 167, 69),    // success (verde)
            new Color(220, 53, 69),    // danger (vermelho)
            new Color(255, 193, 7),    // warning (amarelo)
            new Color(108, 117, 125),  // secondary (cinza)
            new Color(248, 249, 250),  // light (cinza claro)
            new Color(52, 58, 64)      // dark (cinza escuro)
        ));
        
        // Tema Escuro - WCAG AA Compliant
        temas.put(TipoTema.ESCURO, new TemaConfig(
            new Color(33, 37, 41),     // background
            new Color(248, 249, 250),  // foreground (texto)
            new Color(0, 123, 255),    // primary
            new Color(40, 167, 69),    // success
            new Color(220, 53, 69),    // danger
            new Color(255, 193, 7),    // warning
            new Color(108, 117, 125),  // secondary
            new Color(52, 58, 64),     // light
            new Color(248, 249, 250)   // dark
        ));
        
        // Alto Contraste - WCAG AAA Compliant
        temas.put(TipoTema.ALTO_CONTRASTE, new TemaConfig(
            new Color(0, 0, 0),        // background (preto)
            new Color(255, 255, 255),  // foreground (branco)
            new Color(255, 255, 0),    // primary (amarelo brilhante)
            new Color(0, 255, 0),      // success (verde brilhante)
            new Color(255, 0, 0),      // danger (vermelho brilhante)
            new Color(255, 165, 0),    // warning (laranja)
            new Color(128, 128, 128),  // secondary (cinza)
            new Color(64, 64, 64),     // light (cinza escuro)
            new Color(255, 255, 255)   // dark (branco)
        ));
        
        // Protanopia/Deuteranopia - Adaptado para daltonismo vermelho-verde
        temas.put(TipoTema.PROTANOPIA, new TemaConfig(
            new Color(255, 255, 255), // background
            new Color(33, 37, 41),     // foreground
            new Color(0, 119, 204),    // primary (azul mais forte)
            new Color(255, 187, 0),    // success (amarelo no lugar do verde)
            new Color(204, 85, 0),     // danger (laranja no lugar do vermelho)
            new Color(136, 34, 85),    // warning (roxo)
            new Color(108, 117, 125),  // secondary
            new Color(248, 249, 250),  // light
            new Color(52, 58, 64)      // dark
        ));
        
        // Tritanopia - Adaptado para daltonismo azul-amarelo
        temas.put(TipoTema.TRITANOPIA, new TemaConfig(
            new Color(255, 255, 255), // background
            new Color(33, 37, 41),     // foreground
            new Color(204, 51, 51),    // primary (vermelho no lugar do azul)
            new Color(40, 167, 69),    // success (verde mantido)
            new Color(220, 53, 69),    // danger (vermelho)
            new Color(255, 119, 51),   // warning (laranja)
            new Color(108, 117, 125),  // secondary
            new Color(248, 249, 250),  // light
            new Color(52, 58, 64)      // dark
        ));
    }
    
    /**
     * Aplica o tema especificado em todo o sistema
     */
    public static void aplicarTema(TipoTema tema) {
        temaAtual = tema;
        TemaConfig config = temas.get(tema);
        
        // Configura Look and Feel
        configurarLookAndFeel(config);
        
        // Atualiza todas as janelas abertas
        for (Window window : Window.getWindows()) {
            SwingUtilities.updateComponentTreeUI(window);
        }
    }
    
    /**
     * Configura o Look and Feel com as cores do tema
     */
    private static void configurarLookAndFeel(TemaConfig config) {
        UIManager.put("Panel.background", config.getBackground());
        UIManager.put("Panel.foreground", config.getForeground());
        UIManager.put("Button.background", config.getLight());
        UIManager.put("Button.foreground", config.getForeground());
        UIManager.put("TextField.background", config.getBackground());
        UIManager.put("TextField.foreground", config.getForeground());
        UIManager.put("Label.foreground", config.getForeground());
        UIManager.put("Table.background", config.getBackground());
        UIManager.put("Table.foreground", config.getForeground());
        UIManager.put("Table.selectionBackground", config.getPrimary());
        UIManager.put("Table.selectionForeground", config.getBackground());
        UIManager.put("MenuBar.background", config.getLight());
        UIManager.put("Menu.background", config.getLight());
        UIManager.put("MenuItem.background", config.getLight());
    }
    
    // Getters
    public static TipoTema getTemaAtual() {
        return temaAtual;
    }
    
    public static TemaConfig getConfig() {
        return temas.get(temaAtual);
    }
    
    public static TemaConfig getConfig(TipoTema tema) {
        return temas.get(tema);
    }
}
