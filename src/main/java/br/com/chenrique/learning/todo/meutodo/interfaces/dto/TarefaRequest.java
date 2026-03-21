package br.com.chenrique.learning.todo.meutodo.interfaces.dto;

import java.time.LocalDateTime;

/**
 * CAMADA: Interfaces (API) — DTO de Entrada
 *
 * Representa exatamente o que o cliente HTTP pode enviar.
 * Note o que NÃO está aqui: id, concluida, destacada, criadaEm.
 * Esses campos são controlados pelo sistema, não pelo cliente.
 *
 * Usar record (Java 16+) é ideal para DTOs: imutável por natureza.
 */
public record TarefaRequest(
        String titulo,
        String descricao,
        LocalDateTime prazo,
        LocalDateTime lembrete
) {}