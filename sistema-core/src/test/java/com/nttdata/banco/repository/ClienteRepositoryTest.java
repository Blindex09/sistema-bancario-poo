package com.nttdata.banco.repository;

import com.nttdata.banco.TestRunner;
import com.nttdata.banco.model.Cliente;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Testes automatizados para a classe ClienteRepository
 * Verifica operações CRUD e consultas
 */
public class ClienteRepositoryTest {

    public void testSalvarCliente() {
        // Arrange
        ClienteRepository repository = new ClienteRepository();
        Cliente cliente = new Cliente("João Silva", "12345678901", "joao@email.com", "11999999999", LocalDate.of(1990, 5, 15));

        // Act
        Cliente clienteSalvo = repository.salvar(cliente);

        // Assert
        TestRunner.assertEquals(cliente, clienteSalvo, "Cliente salvo deve ser igual ao original");
        TestRunner.assertEquals(1, repository.getTotalClientes(), "Deve ter 1 cliente");
        TestRunner.assertTrue(repository.existe("12345678901"), "Cliente deve existir no repositório");
    }

    public void testBuscarPorCpfExistente() {
        // Arrange
        ClienteRepository repository = new ClienteRepository();
        Cliente cliente = new Cliente("João Silva", "12345678901", "joao@email.com", "11999999999", LocalDate.of(1990, 5, 15));
        repository.salvar(cliente);

        // Act
        Optional<Cliente> resultado = repository.buscarPorCpf("12345678901");

        // Assert
        TestRunner.assertTrue(resultado.isPresent(), "Cliente deve ser encontrado");
        TestRunner.assertEquals(cliente, resultado.get(), "Cliente encontrado deve ser igual ao salvo");
    }

    public void testBuscarPorCpfInexistente() {
        // Arrange
        ClienteRepository repository = new ClienteRepository();

        // Act
        Optional<Cliente> resultado = repository.buscarPorCpf("99999999999");

        // Assert
        TestRunner.assertFalse(resultado.isPresent(), "Cliente inexistente não deve ser encontrado");
    }

    public void testBuscarPorNome() {
        // Arrange
        ClienteRepository repository = new ClienteRepository();
        Cliente cliente1 = new Cliente("João Silva", "12345678901", "joao@email.com", "11999999999", LocalDate.of(1990, 5, 15));
        Cliente cliente2 = new Cliente("João Santos", "98765432100", "joao.santos@email.com", "11888888888", LocalDate.of(1985, 3, 10));
        Cliente cliente3 = new Cliente("Maria Silva", "11122233344", "maria@email.com", "11777777777", LocalDate.of(1995, 7, 20));
        
        repository.salvar(cliente1);
        repository.salvar(cliente2);
        repository.salvar(cliente3);

        // Act
        List<Cliente> resultadoJoao = repository.buscarPorNome("João");
        List<Cliente> resultadoSilva = repository.buscarPorNome("Silva");

        // Assert
        TestRunner.assertEquals(2, resultadoJoao.size(), "Deve encontrar 2 clientes com 'João'");
        TestRunner.assertEquals(2, resultadoSilva.size(), "Deve encontrar 2 clientes com 'Silva'");
        TestRunner.assertTrue(resultadoJoao.contains(cliente1), "Lista deve conter João Silva");
        TestRunner.assertTrue(resultadoJoao.contains(cliente2), "Lista deve conter João Santos");
    }

    public void testBuscarPorNomeCaseInsensitive() {
        // Arrange
        ClienteRepository repository = new ClienteRepository();
        Cliente cliente = new Cliente("João Silva", "12345678901", "joao@email.com", "11999999999", LocalDate.of(1990, 5, 15));
        repository.salvar(cliente);

        // Act
        List<Cliente> resultado = repository.buscarPorNome("joão");

        // Assert
        TestRunner.assertEquals(1, resultado.size(), "Busca deve ser case-insensitive");
        TestRunner.assertTrue(resultado.contains(cliente), "Deve encontrar cliente independente da capitalização");
    }

    public void testBuscarPorEmailExistente() {
        // Arrange
        ClienteRepository repository = new ClienteRepository();
        Cliente cliente = new Cliente("João Silva", "12345678901", "joao@email.com", "11999999999", LocalDate.of(1990, 5, 15));
        repository.salvar(cliente);

        // Act
        Optional<Cliente> resultado = repository.buscarPorEmail("joao@email.com");

        // Assert
        TestRunner.assertTrue(resultado.isPresent(), "Cliente deve ser encontrado por email");
        TestRunner.assertEquals(cliente, resultado.get(), "Cliente encontrado deve ser igual ao salvo");
    }

    public void testBuscarPorEmailCaseInsensitive() {
        // Arrange
        ClienteRepository repository = new ClienteRepository();
        Cliente cliente = new Cliente("João Silva", "12345678901", "JOAO@EMAIL.COM", "11999999999", LocalDate.of(1990, 5, 15));
        repository.salvar(cliente);

        // Act
        Optional<Cliente> resultado = repository.buscarPorEmail("joao@email.com");

        // Assert
        TestRunner.assertTrue(resultado.isPresent(), "Busca por email deve ser case-insensitive");
        TestRunner.assertEquals(cliente, resultado.get(), "Deve encontrar cliente independente da capitalização");
    }

    public void testBuscarPorEmailInexistente() {
        // Arrange
        ClienteRepository repository = new ClienteRepository();

        // Act
        Optional<Cliente> resultado = repository.buscarPorEmail("inexistente@email.com");

        // Assert
        TestRunner.assertFalse(resultado.isPresent(), "Email inexistente não deve ser encontrado");
    }

    public void testListarTodos() {
        // Arrange
        ClienteRepository repository = new ClienteRepository();
        Cliente cliente1 = new Cliente("João Silva", "12345678901", "joao@email.com", "11999999999", LocalDate.of(1990, 5, 15));
        Cliente cliente2 = new Cliente("Maria Santos", "98765432100", "maria@email.com", "11888888888", LocalDate.of(1985, 3, 10));
        
        repository.salvar(cliente1);
        repository.salvar(cliente2);

        // Act
        List<Cliente> todos = repository.listarTodos();

        // Assert
        TestRunner.assertEquals(2, todos.size(), "Deve retornar todos os clientes");
        TestRunner.assertTrue(todos.contains(cliente1), "Lista deve conter cliente1");
        TestRunner.assertTrue(todos.contains(cliente2), "Lista deve conter cliente2");
    }

    public void testListarTodosVazio() {
        // Arrange
        ClienteRepository repository = new ClienteRepository();

        // Act
        List<Cliente> todos = repository.listarTodos();

        // Assert
        TestRunner.assertEquals(0, todos.size(), "Lista deve estar vazia");
    }

    public void testExcluirClienteExistente() {
        // Arrange
        ClienteRepository repository = new ClienteRepository();
        Cliente cliente = new Cliente("João Silva", "12345678901", "joao@email.com", "11999999999", LocalDate.of(1990, 5, 15));
        repository.salvar(cliente);

        // Act
        boolean resultado = repository.excluir("12345678901");

        // Assert
        TestRunner.assertTrue(resultado, "Exclusão deve ser bem-sucedida");
        TestRunner.assertEquals(0, repository.getTotalClientes(), "Deve ter 0 clientes após exclusão");
        TestRunner.assertFalse(repository.existe("12345678901"), "Cliente não deve mais existir");
    }

    public void testExcluirClienteInexistente() {
        // Arrange
        ClienteRepository repository = new ClienteRepository();

        // Act
        boolean resultado = repository.excluir("99999999999");

        // Assert
        TestRunner.assertFalse(resultado, "Exclusão de cliente inexistente deve falhar");
    }

    public void testExiste() {
        // Arrange
        ClienteRepository repository = new ClienteRepository();
        Cliente cliente = new Cliente("João Silva", "12345678901", "joao@email.com", "11999999999", LocalDate.of(1990, 5, 15));
        repository.salvar(cliente);

        // Act & Assert
        TestRunner.assertTrue(repository.existe("12345678901"), "Cliente deve existir");
        TestRunner.assertFalse(repository.existe("99999999999"), "Cliente inexistente não deve existir");
    }

    public void testGetTotalClientes() {
        // Arrange
        ClienteRepository repository = new ClienteRepository();

        // Assert inicial
        TestRunner.assertEquals(0, repository.getTotalClientes(), "Total inicial deve ser 0");

        // Act
        Cliente cliente1 = new Cliente("João Silva", "12345678901", "joao@email.com", "11999999999", LocalDate.of(1990, 5, 15));
        Cliente cliente2 = new Cliente("Maria Santos", "98765432100", "maria@email.com", "11888888888", LocalDate.of(1985, 3, 10));
        repository.salvar(cliente1);
        repository.salvar(cliente2);

        // Assert
        TestRunner.assertEquals(2, repository.getTotalClientes(), "Total deve ser 2 após adicionar 2 clientes");
    }

    public void testLimpar() {
        // Arrange
        ClienteRepository repository = new ClienteRepository();
        Cliente cliente1 = new Cliente("João Silva", "12345678901", "joao@email.com", "11999999999", LocalDate.of(1990, 5, 15));
        Cliente cliente2 = new Cliente("Maria Santos", "98765432100", "maria@email.com", "11888888888", LocalDate.of(1985, 3, 10));
        repository.salvar(cliente1);
        repository.salvar(cliente2);

        // Act
        repository.limpar();

        // Assert
        TestRunner.assertEquals(0, repository.getTotalClientes(), "Total deve ser 0 após limpar");
        TestRunner.assertFalse(repository.existe("12345678901"), "Nenhum cliente deve existir após limpar");
        TestRunner.assertFalse(repository.existe("98765432100"), "Nenhum cliente deve existir após limpar");
    }

    public void testAtualizarCliente() {
        // Arrange
        ClienteRepository repository = new ClienteRepository();
        Cliente cliente = new Cliente("João Silva", "12345678901", "joao@email.com", "11999999999", LocalDate.of(1990, 5, 15));
        repository.salvar(cliente);

        // Act - alterar nome e salvar novamente
        cliente.setNome("João Silva Santos");
        Cliente clienteAtualizado = repository.salvar(cliente);

        // Assert
        TestRunner.assertEquals("João Silva Santos", clienteAtualizado.getNome(), "Nome deve estar atualizado");
        TestRunner.assertEquals(1, repository.getTotalClientes(), "Deve continuar tendo 1 cliente");
        
        Optional<Cliente> clienteBuscado = repository.buscarPorCpf("12345678901");
        TestRunner.assertTrue(clienteBuscado.isPresent(), "Cliente deve existir");
        TestRunner.assertEquals("João Silva Santos", clienteBuscado.get().getNome(), "Nome no repositório deve estar atualizado");
    }
}
