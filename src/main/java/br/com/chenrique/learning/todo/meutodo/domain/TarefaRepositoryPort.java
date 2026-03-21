package br.com.chenrique.learning.todo.meutodo.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * CAMADA: Application — Porta de Saída (Port OUT)
 *
 * Esta é a "mágica" da Inversão de Dependência (DIP) na Clean Architecture.
 *
 * O Use Case DEFINE essa interface aqui, dentro da camada de aplicação.
 * A implementação real (com JPA) fica na camada de infraestrutura.
 *
 * Resultado: o Use Case nunca "enxerga" JPA. Ele só conhece esta interface.
 *
 * Quem define: Application (aqui)
 * Quem implementa: Infrastructure (TarefaRepositoryAdapter)
 * Quem injeta: Spring (IoC Container)
 */
public interface TarefaRepositoryPort {

    Tarefa salvar(Tarefa tarefa);

    Optional<Tarefa> buscarPorId(Long id);

    List<Tarefa> buscarTodas();

    List<Tarefa> buscarDestacadas();

    List<Tarefa> buscarComLembreteEntre(LocalDateTime inicio, LocalDateTime fim);

    void deletar(Long id);
}