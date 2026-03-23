package br.com.chenrique.learning.todo.meutodo.interfaces.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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
        @NotBlank(message = "O título é obrigatório")
        @Size(max = 100, message = "O título deve ter no máximo 100 caracteres")
        String titulo,

        @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
        String descricao,

        @Future(message = "O prazo deve ser uma data futura")
        LocalDateTime prazo,

        @Future(message = "O lembrete deve ser uma data futura")
        LocalDateTime lembrete
) {}