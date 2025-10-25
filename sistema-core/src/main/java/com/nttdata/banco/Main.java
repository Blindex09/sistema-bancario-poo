package com.nttdata.banco;

import com.nttdata.banco.menu.MenuPrincipal;
import com.nttdata.banco.service.BancoService;

/**
 * Classe principal do Sistema Bancário
 * Bootcamp NTT Data - Java para Iniciantes
 * 
 * @author Blindex09
 * @version 1.0
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("\n" +
                "╔══════════════════════════════════════════════════════════╗\n" +
                "║              🏦 SISTEMA BANCÁRIO NTT DATA 🏦              ║\n" +
                "║                                                          ║\n" +
                "║        Controle de Transações Financeiras com POO       ║\n" +
                "║                                                          ║\n" +
                "║                   Bootcamp Java                         ║\n" +
                "╚══════════════════════════════════════════════════════════╝\n");
        
        BancoService bancoService = new BancoService();
        MenuPrincipal menu = new MenuPrincipal(bancoService);
        menu.exibirMenu();
    }
}
