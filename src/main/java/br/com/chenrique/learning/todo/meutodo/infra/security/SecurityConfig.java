package br.com.chenrique.learning.todo.meutodo.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * CAMADA: Infrastructure — Configuração do Spring Security
 *
 * Define as regras de segurança da aplicação:
 * - Quais endpoints são públicos (login, registro, swagger)
 * - Quais endpoints exigem autenticação
 * - Que a API é stateless (sem sessão — usa JWT)
 * - Qual filtro processa o token JWT
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final UsuarioDetailsService userDetailsService;

    public SecurityConfig(JwtFilter jwtFilter, UsuarioDetailsService userDetailsService) {
        this.jwtFilter = jwtFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Desabilita CSRF — não necessário em APIs stateless com JWT
                .csrf(AbstractHttpConfigurer::disable)

                // Define as regras de acesso
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos — não precisam de token
                        .requestMatchers(
                                "/api/auth/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/h2-console/**")
                        .permitAll()
                        // Todos os outros endpoints exigem autenticação
                        .anyRequest().authenticated())

                // API stateless — o Spring não cria sessão HTTP
                // Cada requisição precisa trazer o token JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Registra o provedor de autenticação
                .authenticationProvider(authenticationProvider())

                // Adiciona o filtro JWT antes do filtro padrão do Spring Security
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt é o algoritmo recomendado para hash de senhas
        // Nunca armazenamos senha em texto puro
        return new BCryptPasswordEncoder();
    }
}