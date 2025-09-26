package com.jonathanssm.portfoliobackend.controller;

import com.jonathanssm.portfoliobackend.model.User;
import com.jonathanssm.portfoliobackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Admin", description = "Endpoints administrativos temporários")
public class AdminController {

    private final UserService userService;

    @Operation(
            summary = "Criar usuário admin",
            description = "Cria o usuário administrador padrão (TEMPORÁRIO - remover em produção)"
    )
    @PostMapping("/create-admin")
    public ResponseEntity<String> createAdminUser() {
        User adminUser = getAdminUser();
        userService.createUser(adminUser);
        
        log.info("✅ Usuário admin criado com sucesso!");
        return ResponseEntity.ok("Usuário admin criado com sucesso! Username: admin, Password: admin123");
    }

    private static User getAdminUser() {
        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setEmail("admin@portfolio.com");
        adminUser.setPassword("admin123"); // Será codificado automaticamente
        adminUser.setFirstName("Admin");
        adminUser.setLastName("User");
        adminUser.setRole(User.Role.ADMIN);
        adminUser.setIsEnabled(true);
        adminUser.setIsAccountNonExpired(true);
        adminUser.setIsAccountNonLocked(true);
        adminUser.setIsCredentialsNonExpired(true);
        return adminUser;
    }
}
