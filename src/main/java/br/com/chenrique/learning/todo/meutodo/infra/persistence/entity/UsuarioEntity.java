package br.com.chenrique.learning.todo.meutodo.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CAMADA: Infrastructure — Entidade JPA do Usuário
 *
 * Separada do Domain (Usuario.java) pelo mesmo motivo
 * que TarefaEntity é separada de Tarefa:
 * o Domain não pode conhecer anotações JPA.
 *
 * Implementa UserDetails do Spring Security para que
 * o Spring saiba como autenticar este usuário.
 */
@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;
}