package br.com.chenrique.learning.todo.meutodo.interfaces.dto;

import br.com.chenrique.learning.todo.meutodo.domain.Tarefa;

import java.time.LocalDateTime;

/**
 * CAMADA: Interfaces (API) — DTO de Saída
 *
 * Representa exatamente o que a API devolve ao cliente.
 * Inclui campos calculados como "atrasada" — que não existem
 * no banco, mas são derivados da lógica do Domain.
 *
 * O método estático fromDomain() centraliza a conversão
 * Domain → DTO neste próprio record.
 */
public record TarefaResponse(
        Long id,
        String titulo,
        String descricao,
        boolean concluida,
        boolean destacada,
        boolean atrasada,
        LocalDateTime prazo,
        LocalDateTime lembrete,
        LocalDateTime criadaEm
) {
    public static TarefaResponse fromDomain(Tarefa tarefa) {
        return new TarefaResponse(
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.isConcluida(),
                tarefa.isDestacada(),
                tarefa.isAtrasada(), 
                tarefa.getPrazo(),
                tarefa.getLembrete(),
                tarefa.getCriadaEm()
        );
    }
}