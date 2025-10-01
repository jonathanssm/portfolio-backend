package com.jonathanssm.portfoliobackend.config;

import com.jonathanssm.portfoliobackend.service.UserAuthenticationService;
import com.jonathanssm.portfoliobackend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Configuração de segurança do Spring Security
 * <p>
 * Seguindo as melhores práticas do Spring Security 6.x:
 * - Usa apenas SecurityFilterChain (não WebSecurityCustomizer deprecated)
 * - Configuração declarativa com permitAll() em vez de ignoring()
 * - Endpoints organizados por categoria de acesso
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserAuthenticationService userAuthenticationService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil, userAuthenticationService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
                        .contentTypeOptions(withDefaults())
                        .httpStrictTransportSecurity(hstsConfig -> hstsConfig
                                .maxAgeInSeconds(31536000)
                                .includeSubDomains(true)
                        )
                )
                .authorizeHttpRequests(authz -> authz
                        // ===== ENDPOINTS PÚBLICOS =====

                        // Autenticação pública
                        .requestMatchers("/auth/login", "/auth/register").permitAll()

                        // Swagger/OpenAPI documentation (público para desenvolvimento)
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/api-docs/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-resources/**").permitAll()
                        .requestMatchers("/webjars/**").permitAll()

                        // Health checks (público para monitoramento)
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()

                        // Métricas (apenas para admin)
                        .requestMatchers("/metrics/**").hasRole("ADMIN")

                        // Endpoint temporário de criação de admin (REMOVER EM PRODUÇÃO)
                        .requestMatchers("/admin/create-admin").permitAll()

                        // ===== ENDPOINTS PROTEGIDOS =====

                        // Experiências - visualização pública, modificação apenas para admin
                        .requestMatchers("/experiences").permitAll() // GET público
                        .requestMatchers("/experiences/{id}").permitAll() // GET por ID público

                        // Todos os outros endpoints requerem autenticação
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
