package br.com.chenrique.learning.todo.meutodo.infra.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.chenrique.learning.todo.meutodo.infra.persistence.repository.UsuarioJpaRepository;

/**
 * CAMADA: Infrastructure — UserDetailsService
 *
 * O Spring Security usa esta interface para carregar os dados
 * do usuário durante a autenticação. É o contrato que o Spring
 * exige para saber como buscar um usuário pelo identificador
 * (no nosso caso, o email).
 *
 * O JwtFilter chama loadUserByUsername() para verificar se
 * o usuário do token ainda existe no banco.
 */
@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioJpaRepository repository;

    public UsuarioDetailsService(UsuarioJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var entity = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        return User.builder()
                .username(entity.getEmail())
                .password(entity.getSenha())
                .roles("USER")
                .build();
    }
}