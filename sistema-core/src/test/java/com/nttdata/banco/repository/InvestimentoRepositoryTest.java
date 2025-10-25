package com.nttdata.banco.repository;

import com.nttdata.banco.TestRunner;
import com.nttdata.banco.model.Investimento;
import com.nttdata.banco.enums.TipoInvestimento;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Testes automatizados para a classe InvestimentoRepository
 * Verifica operações CRUD e consultas de investimentos
 */
public class InvestimentoRepositoryTest {

    public void testSalvarInvestimento() {
        // Arrange
        InvestimentoRepository repository = new InvestimentoRepository();
        Investimento investimento = new Investimento(TipoInvestimento.CDB, new BigDecimal("1000.00"), "João Silva");

        // Act
        Investimento investimentoSalvo = repository.salvar(investimento);

        // Assert
        TestRunner.assertEquals(investimento, investimentoSalvo, "Investimento salvo deve ser igual ao original");
        TestRunner.assertEquals(1, repository.getTotalInvestimentos(), "Deve ter 1 investimento");
        TestRunner.assertTrue(repository.existe(investimento.getId()), "Investimento deve existir no repositório");
    }

    public void testBuscarPorIdExistente() {
        // Arrange
        InvestimentoRepository repository = new InvestimentoRepository();
        Investimento investimento = new Investimento(TipoInvestimento.CDB, new BigDecimal("1000.00"), "João Silva");
        repository.salvar(investimento);

        // Act
        Optional<Investimento> resultado = repository.buscarPorId(investimento.getId());

        // Assert
        TestRunner.assertTrue(resultado.isPresent(), "Investimento deve ser encontrado");
        TestRunner.assertEquals(investimento, resultado.get(), "Investimento encontrado deve ser igual ao salvo");
    }

    public void testBuscarPorIdInexistente() {
        // Arrange
        InvestimentoRepository repository = new InvestimentoRepository();

        // Act
        Optional<Investimento> resultado = repository.buscarPorId("id-inexistente");

        // Assert
        TestRunner.assertFalse(resultado.isPresent(), "Investimento inexistente não deve ser encontrado");
    }

    public void testBuscarPorTitular() {
        // Arrange
        InvestimentoRepository repository = new InvestimentoRepository();
        Investimento inv1 = new Investimento(TipoInvestimento.CDB, new BigDecimal("1000.00"), "João Silva");
        Investimento inv2 = new Investimento(TipoInvestimento.LCI, new BigDecimal("2000.00"), "João Silva");
        Investimento inv3 = new Investimento(TipoInvestimento.POUPANCA, new BigDecimal("500.00"), "Maria Santos");
        
        repository.salvar(inv1);
        repository.salvar(inv2);
        repository.salvar(inv3);

        // Act
        List<Investimento> investimentosJoao = repository.buscarPorTitular("João Silva");
        List<Investimento> investimentosMaria = repository.buscarPorTitular("Maria Santos");

        // Assert
        TestRunner.assertEquals(2, investimentosJoao.size(), "João deve ter 2 investimentos");
        TestRunner.assertEquals(1, investimentosMaria.size(), "Maria deve ter 1 investimento");
        TestRunner.assertTrue(investimentosJoao.contains(inv1), "Lista deve conter inv1");
        TestRunner.assertTrue(investimentosJoao.contains(inv2), "Lista deve conter inv2");
        TestRunner.assertTrue(investimentosMaria.contains(inv3), "Lista deve conter inv3");
    }

    public void testBuscarPorTipo() {
        // Arrange
        InvestimentoRepository repository = new InvestimentoRepository();
        Investimento inv1 = new Investimento(TipoInvestimento.CDB, new BigDecimal("1000.00"), "João Silva");
        Investimento inv2 = new Investimento(TipoInvestimento.CDB, new BigDecimal("2000.00"), "Maria Santos");
        Investimento inv3 = new Investimento(TipoInvestimento.LCI, new BigDecimal("500.00"), "João Silva");
        
        repository.salvar(inv1);
        repository.salvar(inv2);
        repository.salvar(inv3);

        // Act
        List<Investimento> investimentosCDB = repository.buscarPorTipo(TipoInvestimento.CDB);
        List<Investimento> investimentosLCI = repository.buscarPorTipo(TipoInvestimento.LCI);

        // Assert
        TestRunner.assertEquals(2, investimentosCDB.size(), "Deve ter 2 investimentos CDB");
        TestRunner.assertEquals(1, investimentosLCI.size(), "Deve ter 1 investimento LCI");
        TestRunner.assertTrue(investimentosCDB.contains(inv1), "Lista CDB deve conter inv1");
        TestRunner.assertTrue(investimentosCDB.contains(inv2), "Lista CDB deve conter inv2");
        TestRunner.assertTrue(investimentosLCI.contains(inv3), "Lista LCI deve conter inv3");
    }

    public void testBuscarAtivos() {
        // Arrange
        InvestimentoRepository repository = new InvestimentoRepository();
        Investimento inv1 = new Investimento(TipoInvestimento.CDB, new BigDecimal("1000.00"), "João Silva");
        Investimento inv2 = new Investimento(TipoInvestimento.LCI, new BigDecimal("2000.00"), "Maria Santos");
        inv2.setAtivo(false); // Desativar inv2
        
        repository.salvar(inv1);
        repository.salvar(inv2);

        // Act
        List<Investimento> ativos = repository.buscarAtivos();

        // Assert
        TestRunner.assertEquals(1, ativos.size(), "Deve retornar apenas investimentos ativos");
        TestRunner.assertTrue(ativos.contains(inv1), "Lista deve conter apenas inv1");
        TestRunner.assertFalse(ativos.contains(inv2), "Lista não deve conter inv2 (inativo)");
    }

    public void testBuscarVencendoEm() {
        // Arrange
        InvestimentoRepository repository = new InvestimentoRepository();
        LocalDate dataVencimentoProxima = LocalDate.now().plusDays(5);
        LocalDate dataVencimentoDistante = LocalDate.now().plusDays(30);
        
        Investimento inv1 = new Investimento(TipoInvestimento.CDB, new BigDecimal("1000.00"), "João", 
                                           dataVencimentoProxima, "Vence em 5 dias");
        Investimento inv2 = new Investimento(TipoInvestimento.LCI, new BigDecimal("2000.00"), "Maria", 
                                           dataVencimentoDistante, "Vence em 30 dias");
        
        repository.salvar(inv1);
        repository.salvar(inv2);

        // Act
        List<Investimento> vencendoEm10Dias = repository.buscarVencendoEm(10);
        List<Investimento> vencendoEm3Dias = repository.buscarVencendoEm(3);

        // Assert
        TestRunner.assertEquals(1, vencendoEm10Dias.size(), "Deve encontrar 1 investimento vencendo em até 10 dias");
        TestRunner.assertEquals(0, vencendoEm3Dias.size(), "Não deve encontrar investimentos vencendo em até 3 dias");
        TestRunner.assertTrue(vencendoEm10Dias.contains(inv1), "Lista deve conter inv1");
    }

    public void testBuscarVencidos() {
        // Arrange
        InvestimentoRepository repository = new InvestimentoRepository();
        LocalDate dataVencida = LocalDate.now().minusDays(5);
        LocalDate dataFutura = LocalDate.now().plusDays(30);
        
        Investimento inv1 = new Investimento(TipoInvestimento.CDB, new BigDecimal("1000.00"), "João", 
                                           dataVencida, "Já vencido");
        Investimento inv2 = new Investimento(TipoInvestimento.LCI, new BigDecimal("2000.00"), "Maria", 
                                           dataFutura, "Ainda não vencido");
        
        repository.salvar(inv1);
        repository.salvar(inv2);

        // Act
        List<Investimento> vencidos = repository.buscarVencidos();

        // Assert
        TestRunner.assertEquals(1, vencidos.size(), "Deve encontrar 1 investimento vencido");
        TestRunner.assertTrue(vencidos.contains(inv1), "Lista deve conter inv1 (vencido)");
        TestRunner.assertFalse(vencidos.contains(inv2), "Lista não deve conter inv2 (não vencido)");
    }

    public void testListarTodos() {
        // Arrange
        InvestimentoRepository repository = new InvestimentoRepository();
        Investimento inv1 = new Investimento(TipoInvestimento.CDB, new BigDecimal("1000.00"), "João Silva");
        Investimento inv2 = new Investimento(TipoInvestimento.LCI, new BigDecimal("2000.00"), "Maria Santos");
        
        repository.salvar(inv1);
        repository.salvar(inv2);

        // Act
        List<Investimento> todos = repository.listarTodos();

        // Assert
        TestRunner.assertEquals(2, todos.size(), "Deve retornar todos os investimentos");
        TestRunner.assertTrue(todos.contains(inv1), "Lista deve conter inv1");
        TestRunner.assertTrue(todos.contains(inv2), "Lista deve conter inv2");
    }

    public void testExcluirInvestimentoExistente() {
        // Arrange
        InvestimentoRepository repository = new InvestimentoRepository();
        Investimento investimento = new Investimento(TipoInvestimento.CDB, new BigDecimal("1000.00"), "João Silva");
        repository.salvar(investimento);

        // Act
        boolean resultado = repository.excluir(investimento.getId());

        // Assert
        TestRunner.assertTrue(resultado, "Exclusão deve ser bem-sucedida");
        TestRunner.assertEquals(0, repository.getTotalInvestimentos(), "Deve ter 0 investimentos após exclusão");
        TestRunner.assertFalse(repository.existe(investimento.getId()), "Investimento não deve mais existir");
    }

    public void testExcluirInvestimentoInexistente() {
        // Arrange
        InvestimentoRepository repository = new InvestimentoRepository();

        // Act
        boolean resultado = repository.excluir("id-inexistente");

        // Assert
        TestRunner.assertFalse(resultado, "Exclusão de investimento inexistente deve falhar");
    }

    public void testExiste() {
        // Arrange
        InvestimentoRepository repository = new InvestimentoRepository();
        Investimento investimento = new Investimento(TipoInvestimento.CDB, new BigDecimal("1000.00"), "João Silva");
        repository.salvar(investimento);

        // Act & Assert
        TestRunner.assertTrue(repository.existe(investimento.getId()), "Investimento deve existir");
        TestRunner.assertFalse(repository.existe("id-inexistente"), "Investimento inexistente não deve existir");
    }

    public void testGetTotalInvestimentos() {
        // Arrange
        InvestimentoRepository repository = new InvestimentoRepository();

        // Assert inicial
        TestRunner.assertEquals(0, repository.getTotalInvestimentos(), "Total inicial deve ser 0");

        // Act
        Investimento inv1 = new Investimento(TipoInvestimento.CDB, new BigDecimal("1000.00"), "João Silva");
        Investimento inv2 = new Investimento(TipoInvestimento.LCI, new BigDecimal("2000.00"), "Maria Santos");
        repository.salvar(inv1);
        repository.salvar(inv2);

        // Assert
        TestRunner.assertEquals(2, repository.getTotalInvestimentos(), "Total deve ser 2 após adicionar 2 investimentos");
    }

    public void testGetTotalInvestimentosAtivos() {
        // Arrange
        InvestimentoRepository repository = new InvestimentoRepository();
        Investimento inv1 = new Investimento(TipoInvestimento.CDB, new BigDecimal("1000.00"), "João Silva");
        Investimento inv2 = new Investimento(TipoInvestimento.LCI, new BigDecimal("2000.00"), "Maria Santos");
        inv2.setAtivo(false);
        
        repository.salvar(inv1);
        repository.salvar(inv2);

        // Act
        int totalAtivos = repository.getTotalInvestimentosAtivos();

        // Assert
        TestRunner.assertEquals(1, totalAtivos, "Deve ter 1 investimento ativo");
    }

    public void testGetValorTotalInvestido() {
        // Arrange
        InvestimentoRepository repository = new InvestimentoRepository();
        Investimento inv1 = new Investimento(TipoInvestimento.CDB, new BigDecimal("1000.00"), "João Silva");
        Investimento inv2 = new Investimento(TipoInvestimento.LCI, new BigDecimal("2000.00"), "Maria Santos");
        Investimento inv3 = new Investimento(TipoInvestimento.POUPANCA, new BigDecimal("500.00"), "Pedro Santos");
        inv3.setAtivo(false); // Não deve contar
        
        repository.salvar(inv1);
        repository.salvar(inv2);
        repository.salvar(inv3);

        // Act
        BigDecimal valorTotal = repository.getValorTotalInvestido();

        // Assert
        TestRunner.assertEquals(new BigDecimal("3000.00"), valorTotal, "Valor total deve ser R$ 3000 (apenas ativos)");
    }

    public void testGetValorTotalAtual() {
        // Arrange
        InvestimentoRepository repository = new InvestimentoRepository();
        Investimento inv1 = new Investimento(TipoInvestimento.CDB, new BigDecimal("1000.00"), "João Silva");
        Investimento inv2 = new Investimento(TipoInvestimento.LCI, new BigDecimal("2000.00"), "Maria Santos");
        
        repository.salvar(inv1);
        repository.salvar(inv2);

        // Act
        BigDecimal valorAtualTotal = repository.getValorTotalAtual();

        // Assert
        TestRunner.assertTrue(valorAtualTotal.compareTo(new BigDecimal("3000.00")) >= 0, 
                             "Valor atual total deve ser pelo menos R$ 3000 (valor inicial + rendimentos)");
        TestRunner.assertNotNull(valorAtualTotal, "Valor atual total não deve ser null");
    }

    public void testLimpar() {
        // Arrange
        InvestimentoRepository repository = new InvestimentoRepository();
        Investimento inv1 = new Investimento(TipoInvestimento.CDB, new BigDecimal("1000.00"), "João Silva");
        Investimento inv2 = new Investimento(TipoInvestimento.LCI, new BigDecimal("2000.00"), "Maria Santos");
        repository.salvar(inv1);
        repository.salvar(inv2);

        // Act
        repository.limpar();

        // Assert
        TestRunner.assertEquals(0, repository.getTotalInvestimentos(), "Total deve ser 0 após limpar");
        TestRunner.assertFalse(repository.existe(inv1.getId()), "Nenhum investimento deve existir após limpar");
        TestRunner.assertFalse(repository.existe(inv2.getId()), "Nenhum investimento deve existir após limpar");
        TestRunner.assertEquals(BigDecimal.ZERO, repository.getValorTotalInvestido(), "Valor total deve ser zero após limpar");
    }

    public void testAtualizarInvestimento() {
        // Arrange
        InvestimentoRepository repository = new InvestimentoRepository();
        Investimento investimento = new Investimento(TipoInvestimento.CDB, new BigDecimal("1000.00"), "João Silva");
        repository.salvar(investimento);

        // Act - alterar observações e salvar novamente
        investimento.setObservacoes("Observações atualizadas");
        Investimento investimentoAtualizado = repository.salvar(investimento);

        // Assert
        TestRunner.assertEquals("Observações atualizadas", investimentoAtualizado.getObservacoes(), "Observações devem estar atualizadas");
        TestRunner.assertEquals(1, repository.getTotalInvestimentos(), "Deve continuar tendo 1 investimento");
        
        Optional<Investimento> investimentoBuscado = repository.buscarPorId(investimento.getId());
        TestRunner.assertTrue(investimentoBuscado.isPresent(), "Investimento deve existir");
        TestRunner.assertEquals("Observações atualizadas", investimentoBuscado.get().getObservacoes(), "Observações no repositório devem estar atualizadas");
    }
}
