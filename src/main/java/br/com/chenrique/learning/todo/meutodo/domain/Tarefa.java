package br.com.chenrique.learning.todo.meutodo.domain;

import java.time.LocalDateTime;

public class Tarefa {
    private Long id;
    private String titulo;
    private String descricao;
    private boolean concluida;
    private boolean destacada;
    private LocalDateTime prazo;
    private LocalDateTime lembrete;
    private LocalDateTime criadaEm;

    // Construtor de criação — regra: toda tarefa nasce não concluída e não destacada
    public Tarefa(String titulo, String descricao, LocalDateTime prazo,
            LocalDateTime lembrete) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("O titulo da tarefa é obrigatório.");
        }
        this.titulo = titulo;
        this.descricao = descricao;
        this.prazo = prazo;
        this.lembrete = lembrete;
        this.criadaEm = LocalDateTime.now();
    }

    // Construtor completo usado pela infraestrutura ao reconstruir do banco
    public Tarefa(Long id, String titulo, String descricao, boolean concluida, boolean destacada, LocalDateTime prazo,
            LocalDateTime lembrete, LocalDateTime criadaEm) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.concluida = concluida;
        this.destacada = destacada;
        this.prazo = prazo;
        this.lembrete = lembrete;
        this.criadaEm = criadaEm;
    }

    // ─── Regras de negócio vivem aqui, não nos Use Cases ───────────────────────
    public void concluir() {
        if (this.concluida) {   
            throw new IllegalStateException("A tarefa já está concluída.");
        }
        this.concluida = true;
    }

    public void destacar() {
        this.destacada = true;
    }

    public void removerDestaque() {
        this.destacada = false;
    }

    public boolean isAtrasada() {
        return prazo != null && LocalDateTime.now().isAfter(prazo) && !concluida;
    }

    public boolean presisaLembrete() {
        return lembrete != null
                && !concluida
                && LocalDateTime.now().isAfter(lembrete)
                && LocalDateTime.now().isBefore(lembrete.plusMinutes(1));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }

    public boolean isDestacada() {
        return destacada;
    }

    public void setDestacada(boolean destacada) {
        this.destacada = destacada;
    }

    public LocalDateTime getPrazo() {
        return prazo;
    }

    public void setPrazo(LocalDateTime prazo) {
        this.prazo = prazo;
    }

    public LocalDateTime getLembrete() {
        return lembrete;
    }

    public void setLembrete(LocalDateTime lembrete) {
        this.lembrete = lembrete;
    }

    public LocalDateTime getCriadaEm() {
        return criadaEm;
    }

    public void setCriadaEm(LocalDateTime criadaEm) {
        this.criadaEm = criadaEm;
    }
    
}
