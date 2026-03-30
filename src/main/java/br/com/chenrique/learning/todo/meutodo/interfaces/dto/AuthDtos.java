package br.com.chenrique.learning.todo.meutodo.interfaces.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * CAMADA: Interfaces (API) — DTOs de Autenticação
 */
public class AuthDtos {

    @Schema(description = "Dados para registro de novo usuário")
    public record RegisterRequest(
            @Schema(description = "Email do usuário", example = "usuario@email.com")
            @NotBlank @Email
            String email,

            @Schema(description = "Senha do usuário", example = "senha123")
            @NotBlank @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
            String senha
    ) {}

    @Schema(description = "Dados para login")
    public record LoginRequest(
            @NotBlank @Email
            String email,

            @NotBlank
            String senha
    ) {}

    @Schema(description = "Token JWT gerado após autenticação")
    public record AuthResponse(
            String token,
            String email
    ) {}
}