package br.com.chenrique.learning.todo.meutodo.application.usecase;

import br.com.chenrique.learning.todo.meutodo.application.port.in.ConsultarTarefaUseCase;
import br.com.chenrique.learning.todo.meutodo.application.usecase.exceptions.TarefaNaoEncontradaException;
import br.com.chenrique.learning.todo.meutodo.domain.Tarefa;
import br.com.chenrique.learning.todo.meutodo.domain.TarefaRepositoryPort;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CAMADA: Application — Use Case de Consulta
 *
 * Separamos leitura de escrita (CQRS simples).
 * Transações de leitura são marcadas com readOnly=true — melhor performance.
 */
@Service
@Transactional(readOnly = true)
public class ConsultarTarefaService implements ConsultarTarefaUseCase {

    private final TarefaRepositoryPort repositoryPort;

    public ConsultarTarefaService(TarefaRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public List<Tarefa> listarTodas() {
        return repositoryPort.buscarTodas();
    }

    @Override
    public List<Tarefa> listarDestacadas() {
        return repositoryPort.buscarDestacadas();
    }

    @Override
    public List<Tarefa> listarAtrasadas() {
        return repositoryPort.buscarTodas()
                .stream()
                .filter(Tarefa::isAtrasada)
                .toList();
    }

    @Override
    public Tarefa buscarPorId(Long id) {
        return repositoryPort.buscarPorId(id)
                .orElseThrow(() -> new TarefaNaoEncontradaException(id));
    }
}