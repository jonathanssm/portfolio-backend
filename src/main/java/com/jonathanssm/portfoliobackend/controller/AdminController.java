package com.jonathanssm.portfoliobackend.controller;

import com.jonathanssm.portfoliobackend.constants.ApplicationConstants;
import com.jonathanssm.portfoliobackend.model.Profile;
import com.jonathanssm.portfoliobackend.model.Role;
import com.jonathanssm.portfoliobackend.model.User;
import com.jonathanssm.portfoliobackend.service.ProfileService;
import com.jonathanssm.portfoliobackend.service.RoleService;
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
@Tag(name = "Admin", description = "Endpoints administrativos temporários - REMOVER EM PRODUÇÃO!")
public class AdminController {

    private final UserService userService;
    private final ProfileService profileService;
    private final RoleService roleService;

    @Operation(
            summary = "Criar usuário admin",
            description = "Cria o usuário administrador padrão para desenvolvimento/teste. REMOVER EM PRODUÇÃO!"
    )
    @PostMapping("/create-admin")
    public ResponseEntity<String> createAdminUser() {
        try {
            User adminUser = getAdminUser();
            userService.createUser(adminUser);
            
            // Adiciona o perfil ADMIN após criar o usuário (evita LazyInitializationException)
            userService.addProfileToUser(adminUser.getUsername(), ApplicationConstants.Profiles.ADMIN);
            
            log.info("✅ Usuário admin criado com sucesso!");
            return ResponseEntity.ok("Usuário admin criado com sucesso! Username: admin, Password: admin123");
        } catch (Exception e) {
            log.error("❌ Erro ao criar usuário admin: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Erro ao criar usuário admin: " + e.getMessage());
        }
    }

    private User getAdminUser() {
        User adminUser = new User();
        adminUser.setUsername(ApplicationConstants.DefaultAdmin.USERNAME);
        adminUser.setEmail(ApplicationConstants.DefaultAdmin.EMAIL);
        adminUser.setPassword(ApplicationConstants.DefaultAdmin.PASSWORD); // Será codificado automaticamente
        adminUser.setFirstName(ApplicationConstants.DefaultAdmin.FIRST_NAME);
        adminUser.setLastName(ApplicationConstants.DefaultAdmin.LAST_NAME);
        adminUser.setIsEnabled(true);
        adminUser.setIsAccountNonExpired(true);
        adminUser.setIsAccountNonLocked(true);
        adminUser.setIsCredentialsNonExpired(true);
        
        // O perfil será adicionado automaticamente pelo UserService.createUser()
        // que já tem a lógica para adicionar o perfil USER_BASIC por padrão
        // e depois podemos adicionar o perfil ADMIN via UserService.addProfileToUser()
        
        return adminUser;
    }
}
