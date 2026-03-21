package br.com.chenrique.learning.todo.meutodo.infra.persistence.repository;

import br.com.chenrique.learning.todo.meutodo.domain.Tarefa;
import br.com.chenrique.learning.todo.meutodo.domain.TarefaRepositoryPort;
import br.com.chenrique.learning.todo.meutodo.infra.persistence.entity.TarefaEntity;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * CAMADA: Infrastructure — Adapter (Adaptador)
 *
 * Este é o ponto MAIS IMPORTANTE da arquitetura limpa neste projeto.
 *
 * Ele implementa TarefaRepositoryPort (definida no Application),
 * mas usa TarefaJpaRepository (Spring Data JPA) internamente.
 *
 * Responsabilidades:
 *  1. Converter Tarefa (Domain) → TarefaEntity (JPA) antes de salvar
 *  2. Converter TarefaEntity (JPA) → Tarefa (Domain) após buscar
 *  3. Delegação para o repositório Spring Data
 *
 * Esse padrão de conversão se chama Anti-Corruption Layer (ACL).
 * Ele protege o Domain de qualquer mudança no banco ou ORM.
 */
@Component
public class TarefaRepositoryAdapter implements TarefaRepositoryPort {

    private final TarefaJpaRepository jpaRepository;

    public TarefaRepositoryAdapter(TarefaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Tarefa salvar(Tarefa tarefa) {
        TarefaEntity entity = toEntity(tarefa);
        TarefaEntity salva = jpaRepository.save(entity);
        return toDomain(salva);
    }

    @Override
    public Optional<Tarefa> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Tarefa> buscarTodas() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Tarefa> buscarDestacadas() {
        return jpaRepository.findByDestacadaTrue().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Tarefa> buscarComLembreteEntre(LocalDateTime inicio, LocalDateTime fim) {
        return jpaRepository.findLembretesPendentes(inicio, fim).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void deletar(Long id) {
        jpaRepository.deleteById(id);
    }

    // ─── Anti-Corruption Layer: conversões Domain ↔ Entity ─────────────────────

    /**
     * Domain → Entity
     * Transforma o objeto puro de negócio na entidade que o JPA entende.
     */
    private TarefaEntity toEntity(Tarefa tarefa) {
        return TarefaEntity.builder()
                .id(tarefa.getId())
                .titulo(tarefa.getTitulo())
                .descricao(tarefa.getDescricao())
                .concluida(tarefa.isConcluida())
                .destacada(tarefa.isDestacada())
                .prazo(tarefa.getPrazo())
                .lembrete(tarefa.getLembrete())
                .criadaEm(tarefa.getCriadaEm() != null ? tarefa.getCriadaEm() : LocalDateTime.now())
                .build();
    }

    /**
     * Entity → Domain
     * Reconstrói o objeto de domínio a partir dos dados do banco.
     */
    private Tarefa toDomain(TarefaEntity entity) {
        return new Tarefa(
                entity.getId(),
                entity.getTitulo(),
                entity.getDescricao(),
                entity.isConcluida(),
                entity.isDestacada(),
                entity.getPrazo(),
                entity.getLembrete(),
                entity.getCriadaEm()
        );
    }
}