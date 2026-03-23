package br.com.chenrique.learning.todo.meutodo.infra.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import br.com.chenrique.learning.todo.meutodo.domain.Tarefa;
import br.com.chenrique.learning.todo.meutodo.domain.TarefaRepositoryPort;

@DataJpaTest
@Import(TarefaRepositoryAdapter.class)
public class TarefaRepositoryAdapterIT {

    @Autowired
    private TarefaRepositoryPort repositoryPort;

    @Test
    @DisplayName("Deve salvar e recuperar tarefa com todos os campos")
    void deveSalvarERecuperarTarefa() {
        //Arrange - preparando ambiente
        var prazo = LocalDateTime.now().plusDays(3);
        var tarefa = new Tarefa("Teste", "Teste de integração", prazo,null);

        //ACT - realiza a ação e persistencia
        var salva = repositoryPort.salvar(tarefa);
        var recupera = repositoryPort.buscarPorId(salva.getId());

        //Assert - valida o resultado
        assertThat(recupera).isPresent();
        assertThat(recupera.get().getTitulo()).isEqualTo("Teste");
        assertThat(recupera.get().getDescricao()).isEqualTo("Teste de integração");
        assertThat(recupera.get().getPrazo()).isNotNull();
        assertThat(recupera.get().isConcluida()).isFalse();
        assertThat(recupera.get().isDestacada()).isFalse(); 
    }

    @Test
    @DisplayName("deve voltar somente tarefas destacadas")
    void deveVoltarSomenteTarefasDestacadas() {
        var normal = new Tarefa("Normal", null, null, null);
        var importante = new Tarefa("Importante", null, null, null);

        repositoryPort.salvar(normal);
        var destacadaSalva = repositoryPort.salvar(importante);

        destacadaSalva.destacar();
        repositoryPort.salvar(destacadaSalva);

        var resultado = repositoryPort.buscarDestacadas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTitulo()).isEqualTo("Importante");
    }

    @Test
    @DisplayName("deve deletar tarefa existente")
    void deveDeletarTarefa() {
        var tarefa = new Tarefa("Tarefa 1", "para deletar", null, null);
        var tarefaSalva = repositoryPort.salvar(tarefa);

        repositoryPort.deletar(tarefaSalva.getId());

        Optional<Tarefa> resultTarefa = repositoryPort.buscarPorId(tarefaSalva.getId());
        assertThat(resultTarefa).isEmpty();

    }
    
}