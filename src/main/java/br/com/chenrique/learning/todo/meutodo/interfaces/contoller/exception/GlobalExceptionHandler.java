package br.com.chenrique.learning.todo.meutodo.interfaces.contoller.exception;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.chenrique.learning.todo.meutodo.application.usecase.exceptions.TarefaNaoEncontradaException;

/**
 * CAMADA: Interfaces (API) — Tratamento Global de Erros
 *
 * Captura exceções do domínio e as traduz para respostas HTTP adequadas.
 * Usa ProblemDetail (RFC 9457) — padrão moderno do Spring Boot 3+.
 *
 * Isso mantém a separação de responsabilidades:
 * - O Domain lança exceções de negócio (sem conhecer HTTP)
 * - Esta classe traduz essas exceções para HTTP (sem conhecer negócio)
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TarefaNaoEncontradaException.class)
    public ProblemDetail handleTarefaNaoEncontrada(TarefaNaoEncontradaException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleArgumentoInvalido(IllegalArgumentException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleEstadoInvalido(IllegalStateException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidacao(MethodArgumentNotValidException ex) {
        Map<String, String> erros = ex.getBindingResult()
                .getFieldErrors().stream()
                .collect(Collectors.toMap(field -> field.getField(), field -> field.getDefaultMessage()));

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "Dados invalidos na requisição");
        problem.setProperty("erros", erros);
        return problem;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAutenticacao(AuthenticationException ex) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED, "Email ou senha inválidos.");
    }
}