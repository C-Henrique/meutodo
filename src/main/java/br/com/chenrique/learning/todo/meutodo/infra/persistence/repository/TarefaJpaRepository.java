package br.com.chenrique.learning.todo.meutodo.infra.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.chenrique.learning.todo.meutodo.infra.persistence.entity.TarefaEntity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * CAMADA: Infrastructure — Spring Data JPA Repository
 *
 * Interface do Spring Data: geração automática de queries via convenção de nomes.
 * Esta interface trabalha com TarefaEntity, nunca com o Domain (Tarefa).
 *
 * Quem usa: TarefaRepositoryAdapter (o Adapter injeta este repositório)
 */
public interface TarefaJpaRepository extends JpaRepository<TarefaEntity, Long> {

    List<TarefaEntity> findByDestacadaTrue();

    @Query("SELECT t FROM TarefaEntity t WHERE t.lembrete BETWEEN :inicio AND :fim AND t.concluida = false")
    List<TarefaEntity> findLembretesPendentes(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );
}