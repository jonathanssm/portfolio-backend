package com.jonathanssm.portfoliobackend.controller;

import com.jonathanssm.portfoliobackend.constants.HttpConstants;
import com.jonathanssm.portfoliobackend.constants.SecurityConstants;
import com.jonathanssm.portfoliobackend.dto.AuthRequest;
import com.jonathanssm.portfoliobackend.dto.AuthResponse;
import com.jonathanssm.portfoliobackend.dto.UserRegistrationRequest;
import com.jonathanssm.portfoliobackend.dto.UserResponse;
import com.jonathanssm.portfoliobackend.service.AuthService;
import com.jonathanssm.portfoliobackend.service.UserManagementService;
import com.jonathanssm.portfoliobackend.util.RequestHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "API para autenticação e geração de tokens JWT")
public class AuthController {

    private final AuthService authService;
    private final UserManagementService userManagementService;

    @Operation(
            summary = "Autenticar usuário",
            description = "Autentica um usuário e retorna um token JWT válido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Autenticação realizada com sucesso",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = HttpConstants.Headers.CONTENT_TYPE_JSON,
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
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request,
                                            HttpServletRequest httpRequest,
                                            HttpServletResponse response) {
        // ✅ Usa RequestUtils para eliminar duplicação
        var context = RequestHelper.extractRequestContext(httpRequest);
        
        var loginResult = authService.login(request.username(), request.password(), context.ipAddress(), context.userAgent());
        authService.setSecureCookies(response, loginResult.accessToken(), loginResult.refreshToken());
        return ResponseEntity.ok(loginResult.authResponse());
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
    public ResponseEntity<Void> validateToken(@RequestHeader(SecurityConstants.Authentication.AUTHORIZATION_HEADER) String token) {
        if (authService.validateToken(token)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(
            summary = "Logout do usuário",
            description = "Realiza logout seguro invalidando o token JWT"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Logout realizado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token inválido ou não fornecido"
            )
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(value = SecurityConstants.Authentication.AUTHORIZATION_HEADER, required = false) String token,
                                     HttpServletRequest httpRequest,
                                     HttpServletResponse response) {
        // ✅ Usa RequestUtils para eliminar duplicação
        var context = RequestHelper.extractRequestContext(httpRequest);
        
        authService.logout(token, context.ipAddress(), context.userAgent());
        authService.clearAuthCookies(response);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Renovar token de acesso",
            description = "Usa refresh token para gerar novo access token"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Token renovado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Refresh token inválido ou expirado"
            )
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestHeader(value = SecurityConstants.Authentication.AUTHORIZATION_HEADER, required = false) String refreshToken,
                                                   HttpServletRequest httpRequest,
                                                   HttpServletResponse response) {
        // ✅ Usa RequestUtils para eliminar duplicação
        var context = RequestHelper.extractRequestContext(httpRequest);
        
        var refreshResult = authService.refreshToken(refreshToken, context.ipAddress(), context.userAgent());
        authService.setSecureCookies(response, refreshResult.newAccessToken(), refreshResult.newRefreshToken());
        return ResponseEntity.ok(refreshResult.authResponse());
    }

    @Operation(
            summary = "Registrar novo usuário",
            description = "Registra um novo usuário no sistema com perfil básico"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário registrado com sucesso",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = HttpConstants.Headers.CONTENT_TYPE_JSON,
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos fornecidos",
                    content = @io.swagger.v3.oas.annotations.media.Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Username ou email já existem",
                    content = @io.swagger.v3.oas.annotations.media.Content
            )
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRegistrationRequest request) {
        // ✅ Delega toda lógica de negócio para service especializado
        UserResponse userResponse = userManagementService.registerUser(request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

}
