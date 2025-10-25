package com.nttdata.banco.gui;

import com.nttdata.banco.gui.view.MainWindow;
import com.nttdata.banco.gui.theme.ThemeManager;
import com.nttdata.banco.gui.theme.FontManager;

import javax.swing.*;
import java.awt.*;

/**
 * Classe principal da Interface Gráfica
 * Ponto de entrada para o sistema bancário com interface acessível
 */
public class MainGUI {
    
    public static void main(String[] args) {
        // Configura Look and Feel do sistema
        configurarLookAndFeel();
        
        // Executa na thread de eventos do Swing
        SwingUtilities.invokeLater(() -> {
            try {
                // Inicializa e exibe a janela principal
                MainWindow janelaPrincipal = new MainWindow();
                janelaPrincipal.exibir();
                
            } catch (Exception e) {
                mostrarErroInicializacao(e);
            }
        });
    }
    
    /**
     * Configura o Look and Feel para melhor acessibilidade
     */
    private static void configurarLookAndFeel() {
        try {
            // Usa Look and Feel do sistema para melhor integração
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Configura propriedades globais de acessibilidade
            System.setProperty("javax.accessibility.assistive_technologies", 
                             "com.sun.java.accessibility.AccessBridge");
            
            // Habilita anti-aliasing para melhor legibilidade
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
            
            // Configura cores padrão do sistema
            configurarCoresPadrao();
            
        } catch (Exception e) {
            System.err.println("Aviso: Não foi possível configurar Look and Feel: " + e.getMessage());
            // Continua com o Look and Feel padrão
        }
    }
    
    /**
     * Configura cores padrão do sistema para acessibilidade
     */
    private static void configurarCoresPadrao() {
        // Configura cores com bom contraste
        UIManager.put("control", new Color(240, 240, 240));
        UIManager.put("info", new Color(255, 255, 225));
        UIManager.put("nimbusBase", new Color(51, 98, 140));
        UIManager.put("nimbusAlertYellow", new Color(255, 220, 35));
        UIManager.put("nimbusDisabledText", new Color(128, 128, 128));
        UIManager.put("nimbusFocus", new Color(115, 164, 209));
        UIManager.put("nimbusGreen", new Color(176, 179, 50));
        UIManager.put("nimbusInfoBlue", new Color(66, 139, 221));
        UIManager.put("nimbusLightBackground", new Color(255, 255, 255));
        UIManager.put("nimbusOrange", new Color(191, 98, 4));
        UIManager.put("nimbusRed", new Color(169, 46, 34));
        UIManager.put("nimbusSelectedText", new Color(255, 255, 255));
        UIManager.put("nimbusSelectionBackground", new Color(57, 105, 138));
        UIManager.put("text", new Color(0, 0, 0));
    }
    
    /**
     * Mostra mensagem de erro caso a inicialização falhe
     */
    private static void mostrarErroInicializacao(Exception e) {
        String mensagem = "Erro ao inicializar o sistema:\n" + 
                         e.getMessage() + "\n\n" +
                         "Por favor, verifique:\n" +
                         "• Se o Java está instalado corretamente\n" +
                         "• Se as classes do sistema estão no classpath\n" +
                         "• Se há permissões adequadas";
        
        JOptionPane.showMessageDialog(null, mensagem, 
            "Erro de Inicialização", JOptionPane.ERROR_MESSAGE);
        
        System.exit(1);
    }
}
