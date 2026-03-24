package br.com.chenrique.learning.todo.meutodo.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.chenrique.learning.todo.meutodo.application.port.in.GerenciarTarefaUseCase;
import br.com.chenrique.learning.todo.meutodo.application.usecase.exceptions.TarefaNaoEncontradaException;
import br.com.chenrique.learning.todo.meutodo.domain.Tarefa;
import br.com.chenrique.learning.todo.meutodo.domain.TarefaRepositoryPort;

/**
 * CAMADA: Application — Use Case (Caso de Uso)
 *
 * Esta classe orquestra as operações de negócio.
 * Ela NÃO conhece JPA, HTTP, nem bancos de dados.
 * Só conhece:
 *   - O domínio (Tarefa)
 *   - As portas de saída (TarefaRepositoryPort)
 *
 * Usa @Service do Spring (permitido pois é apenas um marcador de componente).
 * A lógica de negócio em si permanece pura e testável sem Spring.
 */
@Service
@Transactional
public class GerenciarTarefaService implements GerenciarTarefaUseCase{

    private final TarefaRepositoryPort repositoryPort;

    public GerenciarTarefaService(TarefaRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }




    @Override
    public Tarefa criarTarefa(String titulo, String descricao, LocalDateTime prazo, LocalDateTime lembrete) {
        Tarefa tarefa = new Tarefa(titulo, descricao, prazo, lembrete);
        return repositoryPort.salvar(tarefa);
    }

    @Override
    public Tarefa concluirTarefa(Long id) {
        Tarefa tarefa = repositoryPort.buscarPorId(id)
            .orElseThrow(() -> new TarefaNaoEncontradaException(id));

        tarefa.concluir();
        return repositoryPort.salvar(tarefa);
    }

    @Override
    public Tarefa destacarTarefa(Long id) {
        Tarefa tarefa = repositoryPort.buscarPorId(id)
            .orElseThrow(() -> new TarefaNaoEncontradaException(id));

        tarefa.destacar();
        return repositoryPort.salvar(tarefa);
    }

    @Override
    public Tarefa removerDestaqueTarefa(Long id) {
        Tarefa tarefa = repositoryPort.buscarPorId(id)
            .orElseThrow(() -> new TarefaNaoEncontradaException(id));

        tarefa.removerDestaque();
        return repositoryPort.salvar(tarefa);
    }

    @Override
    public Tarefa atualizarTarefa(Long id, String titulo, String descricao, LocalDateTime prazo,
            LocalDateTime lembrete) {
        Tarefa tarefa = repositoryPort.buscarPorId(id)
            .orElseThrow(() -> new TarefaNaoEncontradaException(id));

        tarefa.setTitulo(titulo);
        tarefa.setDescricao(descricao);
        tarefa.setPrazo(prazo);
        tarefa.setLembrete(lembrete);
        return repositoryPort.salvar(tarefa);
    }

    @Override
    public void excluirTarefa(Long id) {
        buscarTarefaOuFalhar(id);
        repositoryPort.deletar(id);
    }

    private Tarefa buscarTarefaOuFalhar(Long id) {
        return repositoryPort.buscarPorId(id)
            .orElseThrow(() -> new TarefaNaoEncontradaException(id));
    }
    
}
