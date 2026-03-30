package br.com.chenrique.learning.todo.meutodo.infra.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * CAMADA: Infrastructure — Serviço JWT
 *
 * Responsável por:
 *   1. Gerar tokens JWT após login bem-sucedido
 *   2. Validar tokens recebidos nas requisições
 *   3. Extrair o email (subject) do token
 *
 * Usa a biblioteca JJWT para manipulação dos tokens.
 * O secret vem do application.yaml — nunca hardcodado no código.
 */
@Service
public class JwtService {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expiration}")
    private long expiration;

    /**
     * Gera um token JWT para o email informado.
     * O token contém:
     *   - subject: email do usuário
     *   - iat: data de criação
     *   - exp: data de expiração
     */
    public String gerarToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSecretKey())
                .compact();
    }

    /**
     * Extrai o email (subject) de um token válido.
     */
    public String extrairEmail(String token) {
        return extrairClaims(token).getSubject();
    }

    /**
     * Verifica se o token é válido e pertence ao email informado.
     */
    public boolean tokenValido(String token, String email) {
        try {
            String emailDoToken = extrairEmail(token);
            return emailDoToken.equals(email) && !tokenExpirado(token);
        } catch (Exception e) {
            return false;
        }
    }

    // ─── Métodos privados de apoio ───────────────────────────────────────────

    private Claims extrairClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean tokenExpirado(String token) {
        return extrairClaims(token).getExpiration().before(new Date());
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}