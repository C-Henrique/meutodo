package br.com.chenrique.learning.todo.meutodo.domain;

/**
 * CAMADA: Domain
 *
 * Entidade de usuário — Java puro, sem framework.
 * Representa o conceito de "usuário" no negócio.
 */
public class Usuario {

    private Long id;
    private String email;
    private String senha; // senha já criptografada (hash bcrypt)

    public Usuario(String email, String senha) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email não pode ser vazio.");
        }
        if (senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("Senha não pode ser vazia.");
        }
        this.email = email;
        this.senha = senha;
    }

    public Usuario(Long id, String email, String senha) {
        this(email, senha);
        this.id = id;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
}