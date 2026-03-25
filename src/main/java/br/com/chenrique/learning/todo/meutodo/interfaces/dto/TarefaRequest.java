package br.com.chenrique.learning.todo.meutodo.interfaces.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
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
        @Schema(description = "Título da tarefa", example = "Estudar Clean Architecture") @NotBlank(message = "O título não pode ser vazio.") @Size(max = 100) String titulo,
        @Schema(description = "Descrição detalhada", example = "Focar nos conceitos de portas e adaptadores") @Size(max = 500) String descricao,
        @Schema(description = "Prazo para conclusão", example = "2026-04-01T18:00:00") @Future LocalDateTime prazo,
        @Schema(description = "Data e hora do lembrete", example = "2026-03-30T09:00:00") @Future LocalDateTime lembrete) {
}