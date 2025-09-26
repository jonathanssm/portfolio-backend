package com.jonathanssm.portfoliobackend.controller;

import com.jonathanssm.portfoliobackend.constants.ApplicationConstants;
import com.jonathanssm.portfoliobackend.dto.AuthRequest;
import com.jonathanssm.portfoliobackend.dto.AuthResponse;
import com.jonathanssm.portfoliobackend.model.User;
import com.jonathanssm.portfoliobackend.service.UserService;
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
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "API para autenticação e geração de tokens JWT")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

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
                ApplicationConstants.Authentication.TOKEN_TYPE,
                ApplicationConstants.Authentication.TOKEN_EXPIRATION_SECONDS,
                LocalDateTime.now().plusHours(ApplicationConstants.Authentication.TOKEN_EXPIRATION_HOURS),
                new AuthResponse.UserInfo(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getAllRoles().stream()
                                .map(role -> role.getName().name())
                                .collect(Collectors.toSet())
                )
        );

        log.info("✅ Login successful for user: {}", user.getUsername());
        return ResponseEntity.ok(response);
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
    public ResponseEntity<Void> validateToken(@RequestHeader(ApplicationConstants.Authentication.AUTHORIZATION_HEADER) String token) {
        if (token.startsWith(ApplicationConstants.Authentication.BEARER_PREFIX)) {
            token = token.substring(7);
        }

        if (Boolean.TRUE.equals(jwtUtil.validateToken(token))) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
