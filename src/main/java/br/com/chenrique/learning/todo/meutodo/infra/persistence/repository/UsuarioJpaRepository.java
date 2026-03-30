package br.com.chenrique.learning.todo.meutodo.infra.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.chenrique.learning.todo.meutodo.infra.persistence.entity.UsuarioEntity;

/**
 * CAMADA: Infrastructure — Spring Data JPA Repository do Usuário
 */
public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}