package br.com.chenrique.learning.todo.meutodo.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.chenrique.learning.todo.meutodo.application.usecase.exceptions.TarefaNaoEncontradaException;
import br.com.chenrique.learning.todo.meutodo.domain.Tarefa;
import br.com.chenrique.learning.todo.meutodo.domain.TarefaRepositoryPort;

/**
 * TESTES UNITÁRIOS — GerenciarTarefaService
 *
 * O que torna este teste possível sem banco de dados?
 * A Inversão de Dependência! O Service recebe TarefaRepositoryPort
 * via construtor, então podemos injetar um Mock (fake) no lugar.
 *
 * Ferramentas:
 *   - JUnit 5    → estrutura e execução dos testes
 *   - Mockito    → criação de mocks (fakes controlados)
 *   - AssertJ    → assertions fluentes e legíveis
 *
 * Padrão usado em cada teste: AAA (Arrange → Act → Assert)
 */
@ExtendWith(MockitoExtension.class)
class GerenciarTarefaServiceTest {

    // ── Mockito cria um "fake" do repositório ────────────────────────────────
    // Nenhum banco de dados é iniciado. Zero Spring. Roda em milissegundos.
    @Mock
    private TarefaRepositoryPort repositoryPort;

    // ── Mockito cria o Service REAL, injetando o mock acima ─────────────────
    @InjectMocks
    private GerenciarTarefaService service;

    // ── Método auxiliar para criar uma tarefa de exemplo ────────────────────
    private Tarefa tarefaExemplo(Long id, String titulo) {
        return new Tarefa(id, titulo, "Descrição", false, false, null, null, LocalDateTime.now());
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CENÁRIOS: criarTarefa()
    // ═══════════════════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("criarTarefa()")
    class CriarTarefa {

        @Test
        @DisplayName("deve criar tarefa com título válido")
        void deveCriarTarefaComSucesso() {
            // ARRANGE — ensina o mock a retornar uma tarefa salva
            var tarefaSalva = tarefaExemplo(1L, "Estudar Clean Architecture");
            when(repositoryPort.salvar(any(Tarefa.class))).thenReturn(tarefaSalva);

            // ACT — executa o método que queremos testar
            var resultado = service.criarTarefa("Estudar Clean Architecture", null, null, null);

            // ASSERT — verifica os resultados esperados
            assertThat(resultado.getTitulo()).isEqualTo("Estudar Clean Architecture");
            assertThat(resultado.isConcluida()).isFalse();
            assertThat(resultado.isDestacada()).isFalse();

            // Verifica que o repositório foi chamado exatamente 1 vez
            verify(repositoryPort, times(1)).salvar(any(Tarefa.class));
        }

        @Test
        @DisplayName("deve lançar exceção quando título for vazio")
        void deveLancarExcecaoQuandoTituloForVazio() {
            // Regra de negócio definida no Domain (Tarefa.java)
            // O Service apenas delega a criação ao construtor do Domain
            assertThatThrownBy(() ->
                service.criarTarefa("", null, null, null)
            )
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("O titulo da tarefa é obrigatório.");

            // Garante que o repositório NUNCA foi chamado (falhou antes)
            verifyNoInteractions(repositoryPort);
        }

        @Test
        @DisplayName("deve lançar exceção quando título for nulo")
        void deveLancarExcecaoQuandoTituloForNulo() {
            assertThatThrownBy(() ->
                service.criarTarefa(null, null, null, null)
            ).isInstanceOf(IllegalArgumentException.class);

            verifyNoInteractions(repositoryPort);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CENÁRIOS: concluirTarefa()
    // ═══════════════════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("concluirTarefa()")
    class ConcluirTarefa {

        @Test
        @DisplayName("deve concluir tarefa existente")
        void deveConcluirTarefaComSucesso() {
            // ARRANGE
            var tarefa = tarefaExemplo(1L, "Tarefa para concluir");
            var tarefaConcluida = new Tarefa(1L, "Tarefa para concluir", "Descrição",
                    true, false, null, null, LocalDateTime.now());

            when(repositoryPort.buscarPorId(1L)).thenReturn(Optional.of(tarefa));
            when(repositoryPort.salvar(any(Tarefa.class))).thenReturn(tarefaConcluida);

            // ACT
            var resultado = service.concluirTarefa(1L);

            // ASSERT
            assertThat(resultado.isConcluida()).isTrue();
            verify(repositoryPort).buscarPorId(1L);
            verify(repositoryPort).salvar(any(Tarefa.class));
        }

        @Test
        @DisplayName("deve lançar exceção quando tarefa não existir")
        void deveLancarExcecaoQuandoTarefaNaoExistir() {
            // ARRANGE — repositório retorna vazio
            when(repositoryPort.buscarPorId(99L)).thenReturn(Optional.empty());

            // ASSERT + ACT — verifica que a exceção correta é lançada
            assertThatThrownBy(() -> service.concluirTarefa(99L))
                    .isInstanceOf(TarefaNaoEncontradaException.class)
                    .hasMessageContaining("99");
        }

        @Test
        @DisplayName("deve lançar exceção ao tentar concluir tarefa já concluída")
        void deveLancarExcecaoAoConcluirTarefaJaConcluida() {
            // ARRANGE — tarefa já está concluída
            var tarefaJaConcluida = new Tarefa(1L, "Já concluída", null,
                    true, false, null, null, LocalDateTime.now());
            when(repositoryPort.buscarPorId(1L)).thenReturn(Optional.of(tarefaJaConcluida));

            // ASSERT + ACT — a regra de negócio está no Domain
            assertThatThrownBy(() -> service.concluirTarefa(1L))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("A tarefa já está concluída.");
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CENÁRIOS: destacarTarefa() e removerDestaqueTarefa()
    // ═══════════════════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("destacarTarefa() e removerDestaqueTarefa()")
    class DestaqueTarefa {

        @Test
        @DisplayName("deve destacar tarefa existente")
        void deveDestacarTarefaComSucesso() {
            var tarefa = tarefaExemplo(1L, "Tarefa importante");
            var tarefaDestacada = new Tarefa(1L, "Tarefa importante", null,
                    false, true, null, null, LocalDateTime.now());

            when(repositoryPort.buscarPorId(1L)).thenReturn(Optional.of(tarefa));
            when(repositoryPort.salvar(any(Tarefa.class))).thenReturn(tarefaDestacada);

            var resultado = service.destacarTarefa(1L);

            assertThat(resultado.isDestacada()).isTrue();
        }

        @Test
        @DisplayName("deve remover destaque de tarefa existente")
        void deveRemoverDestaqueComSucesso() {
            var tarefaDestacada = new Tarefa(1L, "Tarefa", null,
                    false, true, null, null, LocalDateTime.now());
            var tarefaSemDestaque = new Tarefa(1L, "Tarefa", null,
                    false, false, null, null, LocalDateTime.now());

            when(repositoryPort.buscarPorId(1L)).thenReturn(Optional.of(tarefaDestacada));
            when(repositoryPort.salvar(any(Tarefa.class))).thenReturn(tarefaSemDestaque);

            var resultado = service.removerDestaqueTarefa(1L);

            assertThat(resultado.isDestacada()).isFalse();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CENÁRIOS: excluirTarefa()
    // ═══════════════════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("excluirTarefa()")
    class ExcluirTarefa {

        @Test
        @DisplayName("deve excluir tarefa existente")
        void deveExcluirTarefaComSucesso() {
            var tarefa = tarefaExemplo(1L, "Tarefa para excluir");
            when(repositoryPort.buscarPorId(1L)).thenReturn(Optional.of(tarefa));

            service.excluirTarefa(1L);

            // Verifica que deletar() foi chamado com o id correto
            verify(repositoryPort).deletar(1L);
        }

        @Test
        @DisplayName("deve lançar exceção ao excluir tarefa inexistente")
        void deveLancarExcecaoAoExcluirTarefaInexistente() {
            when(repositoryPort.buscarPorId(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.excluirTarefa(99L))
                    .isInstanceOf(TarefaNaoEncontradaException.class);

            // Garante que deletar() NUNCA foi chamado
            verify(repositoryPort, never()).deletar(any());
        }
    }
}