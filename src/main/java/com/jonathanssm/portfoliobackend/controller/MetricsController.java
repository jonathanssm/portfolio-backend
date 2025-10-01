package com.jonathanssm.portfoliobackend.controller;

import com.jonathanssm.portfoliobackend.service.MetricsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller para exposição de métricas da aplicação
 * Segue SRP - única responsabilidade: exposição de métricas
 */
@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
@Tag(name = "Métricas", description = "API para consulta de métricas da aplicação")
public class MetricsController {

    private final MetricsService metricsService;

    @Operation(
            summary = "Obter métricas da aplicação",
            description = "Retorna métricas coletadas pela aplicação (requer permissão de admin)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Métricas retornadas com sucesso"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado - requer permissão de admin"
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Map<String, Object>> getMetrics() {
        return ResponseEntity.ok(metricsService.getAllMetrics());
    }

    @Operation(
            summary = "Resetar métricas",
            description = "Reseta todas as métricas para zero (requer permissão de admin)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Métricas resetadas com sucesso"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado - requer permissão de admin"
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/reset")
    public ResponseEntity<Void> resetMetrics() {
        metricsService.resetMetrics();
        return ResponseEntity.ok().build();
    }
}

