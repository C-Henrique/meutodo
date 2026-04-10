package br.com.chenrique.learning.todo.meutodo.infra.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * CAMADA: Infrastructure — Filtro JWT
 *
 * Este filtro roda em TODA requisição recebida pela API (uma vez por request).
 * Ele é o guardião da porta: verifica se o token JWT é válido antes
 * de deixar a requisição chegar nos Controllers.
 *
 * Fluxo:
 *   1. Extrai o token do header "Authorization: Bearer <token>"
 *   2. Valida o token com o JwtService
 *   3. Se válido, autentica o usuário no contexto do Spring Security
 *   4. Passa a requisição adiante (com ou sem autenticação)
 *
 * Estende OncePerRequestFilter — garante que o filtro roda exatamente
 * uma vez por requisição, evitando duplicações.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Extrai o header Authorization
        String authHeader = request.getHeader("Authorization");

        // Se não tem header ou não começa com "Bearer ", passa sem autenticar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extrai o token removendo o prefixo "Bearer "
        String token = authHeader.substring(7);
        String email = jwtService.extrairEmail(token);

        // 3. Se tem email e o usuário ainda não está autenticado nesta requisição
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails = userDetailsService.loadUserByUsername(email);

            // 4. Valida o token
            if (jwtService.tokenValido(token)) {
                var authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 5. Registra o usuário como autenticado no contexto do Spring Security
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 6. Continua o fluxo da requisição
        filterChain.doFilter(request, response);
    }
}