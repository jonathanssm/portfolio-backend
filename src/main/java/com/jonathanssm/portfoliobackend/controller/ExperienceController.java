package com.jonathanssm.portfoliobackend.controller;

import com.jonathanssm.portfoliobackend.dto.ExperienceRequest;
import com.jonathanssm.portfoliobackend.dto.ExperienceResponse;
import com.jonathanssm.portfoliobackend.service.ExperienceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/experiences")
@RequiredArgsConstructor
@Tag(name = "Experiências", description = "API para gerenciamento de experiências profissionais")
public class ExperienceController {

    private final ExperienceService experienceService;

    @Operation(
            summary = "Criar nova experiência",
            description = "Cria uma nova experiência profissional no portfolio"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Experiência criada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExperienceResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos fornecidos",
                    content = @Content
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ExperienceResponse> createExperience(
            @Valid @RequestBody ExperienceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(experienceService.createExperience(request));
    }

    @Operation(
            summary = "Listar todas as experiências",
            description = "Retorna uma lista com todas as experiências profissionais cadastradas"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de experiências retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExperienceResponse.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<ExperienceResponse>> getAllExperiences() {
        return ResponseEntity.ok(experienceService.getAllExperiences());
    }

    @Operation(
            summary = "Buscar experiência por ID",
            description = "Retorna uma experiência específica baseada no ID fornecido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Experiência encontrada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExperienceResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Experiência não encontrada",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExperienceResponse> getExperienceById(
            @Parameter(description = "ID da experiência", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(experienceService.getExperienceById(id));
    }

    @Operation(
            summary = "Atualizar experiência",
            description = "Atualiza uma experiência existente com base no ID fornecido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Experiência atualizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExperienceResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos fornecidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Experiência não encontrada",
                    content = @Content
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ExperienceResponse> updateExperience(
            @Parameter(description = "ID da experiência", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ExperienceRequest request) {
        return ResponseEntity.ok(experienceService.updateExperience(id, request));
    }

    @Operation(
            summary = "Excluir experiência",
            description = "Remove uma experiência do portfolio baseada no ID fornecido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Experiência excluída com sucesso",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Experiência não encontrada",
                    content = @Content
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExperience(
            @Parameter(description = "ID da experiência", required = true)
            @PathVariable Long id) {
        experienceService.deleteExperience(id);
        return ResponseEntity.noContent().build();
    }
}
