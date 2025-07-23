package com.nttdata.banco;

import com.nttdata.banco.model.*;
import com.nttdata.banco.service.*;
import com.nttdata.banco.repository.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe principal para executar todos os testes automatizados
 * Sistema de testes customizado sem dependências externas
 * 
 * @author Sistema Bancário NTT Data - Testes Automatizados
 * @version 1.0
 */
public class TestRunner {
    private static int testsPassed = 0;
    private static int testsFailed = 0;
    private static List<String> failedTests = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("\n" +
                "╔══════════════════════════════════════════════════════════╗\n" +
                "║            🧪 TESTES AUTOMATIZADOS - NTT DATA 🧪          ║\n" +
                "║                                                          ║\n" +
                "║           Sistema Bancário POO - Test Suite             ║\n" +
                "║                                                          ║\n" +
                "║                 Executando Testes...                    ║\n" +
                "╚══════════════════════════════════════════════════════════╝\n");

        // Executar todos os testes
        runAllTests();
        
        // Exibir resultados
        showResults();
    }

    private static void runAllTests() {
        System.out.println("🔍 Iniciando execução dos testes automatizados...\n");
        
        // Testes de Modelo
        runTestClass(new ClienteTest());
        runTestClass(new ContaCorrenteTest());
        runTestClass(new ContaPoupancaTest());
        runTestClass(new ContaInvestimentoTest());
        runTestClass(new TransacaoTest());
        runTestClass(new InvestimentoTest());
        
        // Testes de Repository
        runTestClass(new ClienteRepositoryTest());
        runTestClass(new ContaRepositoryTest());
        runTestClass(new InvestimentoRepositoryTest());
        
        // Testes de Service (Integração)
        runTestClass(new BancoServiceTest());
    }

    private static void runTestClass(Object testInstance) {
        String className = testInstance.getClass().getSimpleName();
        System.out.println("📋 Executando: " + className);
        
        Method[] methods = testInstance.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("test")) {
                runTest(testInstance, method, className);
            }
        }
        System.out.println();
    }

    private static void runTest(Object testInstance, Method testMethod, String className) {
        try {
            testMethod.setAccessible(true);
            testMethod.invoke(testInstance);
            testsPassed++;
            System.out.println("  ✅ " + testMethod.getName() + " - PASSOU");
        } catch (Exception e) {
            testsFailed++;
            String testName = className + "." + testMethod.getName();
            failedTests.add(testName + " - " + e.getCause().getMessage());
            System.out.println("  ❌ " + testMethod.getName() + " - FALHOU: " + e.getCause().getMessage());
        }
    }

    private static void showResults() {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                    📊 RESULTADOS FINAIS                  ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        System.out.printf("║  ✅ Testes Aprovados: %-32d ║\n", testsPassed);
        System.out.printf("║  ❌ Testes Reprovados: %-31d ║\n", testsFailed);
        System.out.printf("║  📈 Total de Testes: %-33d ║\n", testsPassed + testsFailed);
        
        if (testsFailed == 0) {
            System.out.println("║                                                          ║");
            System.out.println("║            🎉 TODOS OS TESTES PASSARAM! 🎉               ║");
            System.out.println("║               Sistema 100% Funcional                    ║");
        } else {
            System.out.println("╠══════════════════════════════════════════════════════════╣");
            System.out.println("║                   ⚠️  TESTES FALHARAM                    ║");
            for (String failedTest : failedTests) {
                System.out.println("║  " + failedTest);
            }
        }
        
        double successRate = (double) testsPassed / (testsPassed + testsFailed) * 100;
        System.out.printf("║  💯 Taxa de Sucesso: %.1f%%                             ║\n", successRate);
        System.out.println("╚══════════════════════════════════════════════════════════╝");
    }

    // Métodos auxiliares para asserções
    public static void assertEquals(Object expected, Object actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + " - Esperado: " + expected + ", Atual: " + actual);
        }
    }

    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    public static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionError(message);
        }
    }

    public static void assertNotNull(Object object, String message) {
        if (object == null) {
            throw new AssertionError(message);
        }
    }

    public static void assertNull(Object object, String message) {
        if (object != null) {
            throw new AssertionError(message);
        }
    }

    public static void assertEquals(double expected, double actual, double delta, String message) {
        if (Math.abs(expected - actual) > delta) {
            throw new AssertionError(message + " - Esperado: " + expected + ", Atual: " + actual);
        }
    }
}
