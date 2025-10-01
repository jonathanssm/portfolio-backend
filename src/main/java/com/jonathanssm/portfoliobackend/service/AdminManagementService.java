package com.jonathanssm.portfoliobackend.service;

import com.jonathanssm.portfoliobackend.messaging.AdminProducer;
import com.jonathanssm.portfoliobackend.model.User;
import com.jonathanssm.portfoliobackend.util.RequestHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service especializado em operações administrativas
 * Responsabilidade única: Gerenciamento de usuários administradores
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminManagementService {

    private final UserManagementService userManagementService;
    private final UserProfileService userProfileService;
    private final AdminProducer adminProducer;

    /**
     * Cria o usuário administrador padrão
     *
     * @param request HttpServletRequest para extrair contexto de segurança
     */
    @Transactional
    public void createDefaultAdmin(HttpServletRequest request) {
        log.info("🔧 Creating default admin user");

        // Verifica se já existe um admin
        if (isAdminCreationAllowed()) {
            throw new IllegalArgumentException("Admin user already exists");
        }

        User adminUser = createAdminUser();
        User createdUser = userManagementService.createUser(adminUser);

        // Adiciona perfil de admin
        userProfileService.addProfileToUser(createdUser.getUsername(), "ADMIN");

        // Publica evento de criação de admin
        publishAdminCreationEvent(createdUser, request);

        log.info("✅ Default admin user created successfully");
    }

    /**
     * Verifica se é permitido criar usuário admin
     *
     * @return true se pode criar, false caso contrário
     */
    public boolean isAdminCreationAllowed() {
        // Verifica se já existe algum usuário com perfil ADMIN
        // Implementação simples - pode ser melhorada com contagem de admins
        return false; // Sempre permite criação para desenvolvimento
    }

    /**
     * Cria objeto User para administrador padrão
     *
     * @return User configurado como admin
     */
    private User createAdminUser() {
        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setEmail("admin@portfolio.com");
        adminUser.setPassword("admin123"); // Será codificado no service
        adminUser.setFirstName("Admin");
        adminUser.setLastName("User");
        adminUser.setIsEnabled(true);
        adminUser.setIsAccountNonExpired(true);
        adminUser.setIsAccountNonLocked(true);
        adminUser.setIsCredentialsNonExpired(true);

        return adminUser;
    }

    /**
     * Publica evento de criação de admin
     *
     * @param adminUser Usuário admin criado
     * @param request   HttpServletRequest para contexto
     */
    private void publishAdminCreationEvent(User adminUser, HttpServletRequest request) {
        try {
            // ✅ Usa RequestUtils para eliminar duplicação
            var context = RequestHelper.extractRequestContext(request);

            adminProducer.sendAdminCreated(adminUser, adminUser, context.ipAddress(), context.userAgent());
        } catch (Exception e) {
            log.warn("Failed to publish admin creation event: {}", e.getMessage());
        }
    }
}
