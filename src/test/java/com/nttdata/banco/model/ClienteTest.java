package com.nttdata.banco.model;

import com.nttdata.banco.TestRunner;
import java.time.LocalDate;

/**
 * Testes automatizados para a classe Cliente
 * Verifica funcionalidades de criação, validação e métodos
 */
public class ClienteTest {

    public void testCriacaoClienteCompleto() {
        // Arrange
        String nome = "João Silva";
        String cpf = "12345678901";
        String email = "joao@email.com";
        String telefone = "11999999999";
        LocalDate dataNascimento = LocalDate.of(1990, 5, 15);

        // Act
        Cliente cliente = new Cliente(nome, cpf, email, telefone, dataNascimento);

        // Assert
        TestRunner.assertEquals(nome, cliente.getNome(), "Nome deve ser igual");
        TestRunner.assertEquals(cpf, cliente.getCpf(), "CPF deve ser igual");
        TestRunner.assertEquals(email, cliente.getEmail(), "Email deve ser igual");
        TestRunner.assertEquals(telefone, cliente.getTelefone(), "Telefone deve ser igual");
        TestRunner.assertEquals(dataNascimento, cliente.getDataNascimento(), "Data nascimento deve ser igual");
    }

    public void testCriacaoClienteVazio() {
        // Act
        Cliente cliente = new Cliente();

        // Assert
        TestRunner.assertNull(cliente.getNome(), "Nome deve ser null");
        TestRunner.assertNull(cliente.getCpf(), "CPF deve ser null");
        TestRunner.assertNull(cliente.getEmail(), "Email deve ser null");
        TestRunner.assertNull(cliente.getTelefone(), "Telefone deve ser null");
        TestRunner.assertNull(cliente.getDataNascimento(), "Data nascimento deve ser null");
    }

    public void testSettersCliente() {
        // Arrange
        Cliente cliente = new Cliente();
        String nome = "Maria Santos";
        String cpf = "98765432100";
        String email = "maria@email.com";

        // Act
        cliente.setNome(nome);
        cliente.setCpf(cpf);
        cliente.setEmail(email);

        // Assert
        TestRunner.assertEquals(nome, cliente.getNome(), "Nome setado deve ser igual");
        TestRunner.assertEquals(cpf, cliente.getCpf(), "CPF setado deve ser igual");
        TestRunner.assertEquals(email, cliente.getEmail(), "Email setado deve ser igual");
    }

    public void testEqualsCliente() {
        // Arrange
        Cliente cliente1 = new Cliente("João", "12345678901", "joao@email.com", "11999999999", LocalDate.of(1990, 5, 15));
        Cliente cliente2 = new Cliente("Maria", "12345678901", "maria@email.com", "11888888888", LocalDate.of(1985, 3, 10));
        Cliente cliente3 = new Cliente("Pedro", "98765432100", "pedro@email.com", "11777777777", LocalDate.of(1995, 7, 20));

        // Assert
        TestRunner.assertTrue(cliente1.equals(cliente2), "Clientes com mesmo CPF devem ser iguais");
        TestRunner.assertFalse(cliente1.equals(cliente3), "Clientes com CPF diferente devem ser diferentes");
        TestRunner.assertTrue(cliente1.equals(cliente1), "Cliente deve ser igual a si mesmo");
        TestRunner.assertFalse(cliente1.equals(null), "Cliente não deve ser igual a null");
    }

    public void testHashCodeCliente() {
        // Arrange
        Cliente cliente1 = new Cliente("João", "12345678901", "joao@email.com", "11999999999", LocalDate.of(1990, 5, 15));
        Cliente cliente2 = new Cliente("Maria", "12345678901", "maria@email.com", "11888888888", LocalDate.of(1985, 3, 10));

        // Assert
        TestRunner.assertEquals(cliente1.hashCode(), cliente2.hashCode(), "HashCode deve ser igual para CPFs iguais");
    }

    public void testToStringCliente() {
        // Arrange
        Cliente cliente = new Cliente("João Silva", "12345678901", "joao@email.com", "11999999999", LocalDate.of(1990, 5, 15));

        // Act
        String toString = cliente.toString();

        // Assert
        TestRunner.assertTrue(toString.contains("João Silva"), "ToString deve conter o nome");
        TestRunner.assertTrue(toString.contains("12345678901"), "ToString deve conter o CPF");
        TestRunner.assertTrue(toString.contains("joao@email.com"), "ToString deve conter o email");
    }

    public void testEndereco() {
        // Arrange
        Cliente cliente = new Cliente();
        Endereco endereco = new Endereco("Rua A", "123", "Centro", "São Paulo", "SP", "01000-000");

        // Act
        cliente.setEndereco(endereco);

        // Assert
        TestRunner.assertEquals(endereco, cliente.getEndereco(), "Endereço deve ser igual ao setado");
        TestRunner.assertNotNull(cliente.getEndereco(), "Endereço não deve ser null");
    }
}
