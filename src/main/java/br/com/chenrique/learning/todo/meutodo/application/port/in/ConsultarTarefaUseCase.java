package br.com.chenrique.learning.todo.meutodo.application.port.in;

import java.util.List;

import br.com.chenrique.learning.todo.meutodo.domain.Tarefa;

/**
 * CAMADA: Application — Porta de Entrada (Port IN)
 *
 * Separamos as intenções de escrita (GerenciarTarefaUseCase) das intenções de leitura.
 * Isso segue o princípio CQRS (Command Query Responsibility Segregation).
 */
public interface ConsultarTarefaUseCase {
    
    List<Tarefa> listarTodas();

    List<Tarefa> listarDestacadas();

    List<Tarefa> listarAtrasadas();

    Tarefa buscarPorId(Long id);
        
}
