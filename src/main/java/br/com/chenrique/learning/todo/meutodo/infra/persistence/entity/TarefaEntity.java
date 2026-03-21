package br.com.chenrique.learning.todo.meutodo.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * CAMADA: Infrastructure — Entidade JPA
 *
 * Esta classe existe SOMENTE para o JPA saber como persistir dados.
 * Ela é diferente de Tarefa (Domain) por um motivo crucial:
 *
 *   - Tarefa.java  → conhece regras de negócio, não conhece banco
 *   - TarefaEntity → conhece o banco, não tem regras de negócio
 *
 * O Adapter (TarefaRepositoryAdapter) faz a conversão entre as duas.
 * Isso se chama Anti-Corruption Layer.
 */
@Entity
@Table(name = "tarefas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TarefaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    private boolean concluida;

    @Column(nullable = false)
    private boolean destacada;

    @Column
    private LocalDateTime prazo;

    @Column
    private LocalDateTime lembrete;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadaEm;
}