package br.com.chenrique.learning.todo.meutodo.interfaces.contoller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.chenrique.learning.todo.meutodo.infra.persistence.entity.UsuarioEntity;
import br.com.chenrique.learning.todo.meutodo.infra.persistence.repository.UsuarioJpaRepository;
import br.com.chenrique.learning.todo.meutodo.infra.security.JwtService;
import br.com.chenrique.learning.todo.meutodo.interfaces.dto.AuthDtos;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * CAMADA: Interfaces (API) — Controller de Autenticação
 *
 * Endpoints públicos — não exigem token JWT.
 * Definidos como públicos no SecurityConfig.
 */
@Tag(name = "Autenticação", description = "Registro e login de usuários")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioJpaRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UsuarioJpaRepository repository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService,
                          AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Operation(summary = "Registra novo usuário")
    @PostMapping("/registro")
    public ResponseEntity<AuthDtos.AuthResponse> registrar(
            @Valid @RequestBody AuthDtos.RegisterRequest request) {

        if (repository.existsByEmail(request.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        var entity = UsuarioEntity.builder()
                .email(request.email())
                .senha(passwordEncoder.encode(request.senha())) // hash bcrypt
                .build();

        repository.save(entity);

        var token = jwtService.gerarToken(request.email());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthDtos.AuthResponse(token, request.email()));
    }

    @Operation(summary = "Realiza login e retorna token JWT")
    @PostMapping("/login")
    public ResponseEntity<AuthDtos.AuthResponse> login(
            @Valid @RequestBody AuthDtos.LoginRequest request) {

        // O AuthenticationManager valida email + senha automaticamente
        // Lança AuthenticationException se as credenciais forem inválidas
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha())
        );

        var token = jwtService.gerarToken(request.email());
        return ResponseEntity.ok(new AuthDtos.AuthResponse(token, request.email()));
    }
}