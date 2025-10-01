package com.jonathanssm.portfoliobackend.controller;

import com.jonathanssm.portfoliobackend.service.AdminManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Endpoints administrativos temporários - REMOVER EM PRODUÇÃO!")
public class AdminController {

    private final AdminManagementService adminManagementService;

    @Operation(
            summary = "Criar usuário admin",
            description = "Cria o usuário administrador padrão para desenvolvimento/teste. REMOVER EM PRODUÇÃO!"
    )
    @PostMapping("/create-admin")
    public ResponseEntity<String> createAdminUser(HttpServletRequest request) {
        // ✅ Delega para service especializado - sem try-catch
        adminManagementService.createDefaultAdmin(request);
        
        return ResponseEntity.ok("Usuário admin criado com sucesso! Username: admin, Password: admin123");
    }
}
