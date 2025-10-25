package com.nttdata.banco.gui.utils;

import java.awt.Toolkit;
import java.util.prefs.Preferences;

/**
 * Utilitários para Feedback Sonoro
 * Fornece alertas sonoros opcionais para melhor acessibilidade
 */
public class SoundUtils {
    
    private static final Preferences prefs = Preferences.userNodeForPackage(SoundUtils.class);
    private static final String SOM_HABILITADO = "som_habilitado";
    private static boolean somHabilitado = true;
    
    static {
        carregarPreferencias();
    }
    
    /**
     * Emite som de sucesso
     */
    public static void tocarSucesso() {
        if (somHabilitado) {
            // Som padrão do sistema ou implementação customizada
            Toolkit.getDefaultToolkit().beep();
            
            // Aqui poderia carregar um arquivo de som personalizado
            // para diferentes tipos de feedback
        }
    }
    
    /**
     * Emite som de erro
     */
    public static void tocarErro() {
        if (somHabilitado) {
            // Som de erro mais distintivo
            Toolkit.getDefaultToolkit().beep();
            try {
                Thread.sleep(50);
                Toolkit.getDefaultToolkit().beep();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    /**
     * Emite som de aviso
     */
    public static void tocarAviso() {
        if (somHabilitado) {
            Toolkit.getDefaultToolkit().beep();
        }
    }
    
    /**
     * Emite som de informação
     */
    public static void tocarInfo() {
        if (somHabilitado) {
            // Som mais suave para informações
            Toolkit.getDefaultToolkit().beep();
        }
    }
    
    /**
     * Habilita ou desabilita sons
     */
    public static void habilitarSom(boolean habilitar) {
        somHabilitado = habilitar;
        salvarPreferencias();
    }
    
    /**
     * Verifica se os sons estão habilitados
     */
    public static boolean isSomHabilitado() {
        return somHabilitado;
    }
    
    /**
     * Carrega preferências salvas
     */
    private static void carregarPreferencias() {
        somHabilitado = prefs.getBoolean(SOM_HABILITADO, true);
    }
    
    /**
     * Salva preferências
     */
    private static void salvarPreferencias() {
        prefs.putBoolean(SOM_HABILITADO, somHabilitado);
    }
}
