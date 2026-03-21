package br.com.chenrique.learning.todo.meutodo.application.port.in;

import java.time.LocalDateTime;

import br.com.chenrique.learning.todo.meutodo.domain.Tarefa;
 
/**
 * CAMADA: Application — Porta de Entrada (Port IN)
 *
 * Ports IN são as INTENÇÕES do mundo externo sobre o sistema.
 * Elas representam os casos de uso disponíveis para quem chama a aplicação.
 *
 * Quem usa: Controllers (camada de interfaces)
 * Quem implementa: Use Cases (camada de application)
 */
public interface GerenciarTarefaUseCase {

    Tarefa criarTarefa(String titulo, String descricao, LocalDateTime prazo, LocalDateTime lembrete);

    Tarefa concluirTarefa(Long id);

    Tarefa destacarTarefa(Long id);

    Tarefa removerDestaqueTarefa(Long id);

    Tarefa atualizarTarefa(Long id, String titulo, String descricao, LocalDateTime prazo, LocalDateTime lembrete);

    void excluirTarefa(Long id);
}