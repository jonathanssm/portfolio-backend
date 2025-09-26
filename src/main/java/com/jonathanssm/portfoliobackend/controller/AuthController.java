package com.jonathanssm.portfoliobackend.controller;

import com.jonathanssm.portfoliobackend.dto.AuthRequest;
import com.jonathanssm.portfoliobackend.dto.AuthResponse;
import com.jonathanssm.portfoliobackend.model.User;
import com.jonathanssm.portfoliobackend.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "API para autenticação e geração de tokens JWT")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Operation(
            summary = "Autenticar usuário",
            description = "Autentica um usuário e retorna um token JWT válido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Autenticação realizada com sucesso",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = AuthResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciais inválidas",
                    content = @io.swagger.v3.oas.annotations.media.Content
            )
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        log.info("🔐 Attempting login for user: {}", request.username());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(),
                            request.password()
                    )
            );

            User user = (User) authentication.getPrincipal();
            String token = jwtUtil.generateToken(user);

            AuthResponse response = new AuthResponse(
                    token,
                    "Bearer",
                    86400L, // 24 horas
                    LocalDateTime.now().plusHours(24),
                    new AuthResponse.UserInfo(
                            user.getId(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getRole().name()
                    )
            );

            log.info("✅ Login successful for user: {}", user.getUsername());
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            log.warn("❌ Login failed for user: {} - Invalid credentials", request.username());
            throw new IllegalArgumentException("Credenciais inválidas");
        }
    }

    @Operation(
            summary = "Validar token",
            description = "Valida se um token JWT é válido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Token válido"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token inválido ou expirado"
            )
    })
    @PostMapping("/validate")
    public ResponseEntity<Void> validateToken(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (Boolean.TRUE.equals(jwtUtil.validateToken(token))) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
