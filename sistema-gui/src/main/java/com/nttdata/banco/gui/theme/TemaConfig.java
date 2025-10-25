package com.nttdata.banco.gui.theme;

import java.awt.Color;

/**
 * Configuração de cores para temas acessíveis
 * Todas as cores atendem critérios WCAG para contraste
 */
public class TemaConfig {
    private final Color background;
    private final Color foreground;
    private final Color primary;
    private final Color success;
    private final Color danger;
    private final Color warning;
    private final Color secondary;
    private final Color light;
    private final Color dark;
    
    public TemaConfig(Color background, Color foreground, Color primary, 
                     Color success, Color danger, Color warning,
                     Color secondary, Color light, Color dark) {
        this.background = background;
        this.foreground = foreground;
        this.primary = primary;
        this.success = success;
        this.danger = danger;
        this.warning = warning;
        this.secondary = secondary;
        this.light = light;
        this.dark = dark;
    }
    
    // Getters
    public Color getBackground() { return background; }
    public Color getForeground() { return foreground; }
    public Color getPrimary() { return primary; }
    public Color getSuccess() { return success; }
    public Color getDanger() { return danger; }
    public Color getWarning() { return warning; }
    public Color getSecondary() { return secondary; }
    public Color getLight() { return light; }
    public Color getDark() { return dark; }
    
    /**
     * Calcula o contraste entre duas cores
     * @return valor de contraste (mínimo 4.5 para WCAG AA)
     */
    public static double calcularContraste(Color cor1, Color cor2) {
        double lum1 = calcularLuminancia(cor1);
        double lum2 = calcularLuminancia(cor2);
        double maior = Math.max(lum1, lum2);
        double menor = Math.min(lum1, lum2);
        return (maior + 0.05) / (menor + 0.05);
    }
    
    private static double calcularLuminancia(Color cor) {
        double r = cor.getRed() / 255.0;
        double g = cor.getGreen() / 255.0;
        double b = cor.getBlue() / 255.0;
        
        r = (r <= 0.03928) ? r / 12.92 : Math.pow((r + 0.055) / 1.055, 2.4);
        g = (g <= 0.03928) ? g / 12.92 : Math.pow((g + 0.055) / 1.055, 2.4);
        b = (b <= 0.03928) ? b / 12.92 : Math.pow((b + 0.055) / 1.055, 2.4);
        
        return 0.2126 * r + 0.7152 * g + 0.0722 * b;
    }
}
