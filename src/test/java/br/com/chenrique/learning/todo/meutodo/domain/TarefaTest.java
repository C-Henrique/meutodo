package br.com.chenrique.learning.todo.meutodo.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * TESTES UNITÁRIOS — Tarefa (Domain)
 *
 * O Domain é a camada mais fácil de testar porque é Java puro.
 * Sem Spring, sem Mockito, sem banco. Apenas lógica e assertions.
 *
 * Se esses testes quebram, significa que uma regra de negócio foi violada.
 */
@DisplayName("Tarefa (Domain)")
class TarefaTest {

    // ═══════════════════════════════════════════════════════════════════════════
    // CENÁRIOS: Construção
    // ═══════════════════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("Construção")
    class Construcao {

        @Test
        @DisplayName("deve criar tarefa com valores padrão corretos")
        void deveCriarTarefaComValoresPadrao() {
            var tarefa = new Tarefa("Minha tarefa", "Descrição", null, null);

            assertThat(tarefa.getTitulo()).isEqualTo("Minha tarefa");
            assertThat(tarefa.isConcluida()).isFalse();
            assertThat(tarefa.isDestacada()).isFalse();
            assertThat(tarefa.getCriadaEm()).isNotNull();
        }

        @Test
        @DisplayName("deve lançar exceção com título vazio")
        void deveLancarExcecaoComTituloVazio() {
            assertThatThrownBy(() -> new Tarefa("", null, null, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("deve lançar exceção com título nulo")
        void deveLancarExcecaoComTituloNulo() {
            assertThatThrownBy(() -> new Tarefa(null, null, null, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CENÁRIOS: concluir()
    // ═══════════════════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("concluir()")
    class Concluir {

        @Test
        @DisplayName("deve concluir tarefa pendente")
        void deveConcluirTarefaPendente() {
            var tarefa = new Tarefa("Tarefa", null, null, null);

            tarefa.concluir();

            assertThat(tarefa.isConcluida()).isTrue();
        }

        @Test
        @DisplayName("deve lançar exceção ao concluir tarefa já concluída")
        void deveLancarExcecaoAoConcluirDuasVezes() {
            var tarefa = new Tarefa("Tarefa", null, null, null);
            tarefa.concluir();

            assertThatThrownBy(tarefa::concluir)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("já foi concluída");
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CENÁRIOS: destacar() e removerDestaque()
    // ═══════════════════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("destacar() e removerDestaque()")
    class Destaque {

        @Test
        @DisplayName("deve destacar e remover destaque corretamente")
        void deveAlternarDestaque() {
            var tarefa = new Tarefa("Tarefa", null, null, null);

            assertThat(tarefa.isDestacada()).isFalse();

            tarefa.destacar();
            assertThat(tarefa.isDestacada()).isTrue();

            tarefa.removerDestaque();
            assertThat(tarefa.isDestacada()).isFalse();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CENÁRIOS: estaAtrasada()
    // ═══════════════════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("estaAtrasada()")
    class Atrasada {

        @Test
        @DisplayName("deve retornar true quando prazo já passou e não está concluída")
        void deveRetornarTrueQuandoAtrasada() {
            var prazoPassado = LocalDateTime.now().minusDays(1);
            var tarefa = new Tarefa("Tarefa atrasada", null, prazoPassado, null);

            assertThat(tarefa.isAtrasada()).isTrue();
        }

        @Test
        @DisplayName("deve retornar false quando já está concluída mesmo com prazo passado")
        void deveRetornarFalseQuandoConcluidaMesmAtrasada() {
            var prazoPassado = LocalDateTime.now().minusDays(1);
            var tarefa = new Tarefa("Tarefa", null, prazoPassado, null);
            tarefa.concluir();

            assertThat(tarefa.isAtrasada()).isFalse();
        }

        @Test
        @DisplayName("deve retornar false quando prazo é futuro")
        void deveRetornarFalseQuandoPrazoFuturo() {
            var prazoFuturo = LocalDateTime.now().plusDays(1);
            var tarefa = new Tarefa("Tarefa", null, prazoFuturo, null);

            assertThat(tarefa.isAtrasada()).isFalse();
        }

        @Test
        @DisplayName("deve retornar false quando não tem prazo definido")
        void deveRetornarFalseQuandoSemPrazo() {
            var tarefa = new Tarefa("Tarefa sem prazo", null, null, null);

            assertThat(tarefa.isAtrasada()).isFalse();
        }
    }
}