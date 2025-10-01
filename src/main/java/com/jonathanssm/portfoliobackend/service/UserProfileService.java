package com.jonathanssm.portfoliobackend.service;

import com.jonathanssm.portfoliobackend.constants.ValidationConstants;
import com.jonathanssm.portfoliobackend.messaging.UserProducer;
import com.jonathanssm.portfoliobackend.model.Profile;
import com.jonathanssm.portfoliobackend.model.User;
import com.jonathanssm.portfoliobackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service especializado em gerenciamento de perfis de usuário
 * Responsabilidade única: Operações relacionadas a perfis e roles de usuários
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService {

    private final UserRepository userRepository;
    private final ProfileService profileService;
    private final UserProducer userProducer;

    /**
     * Adiciona perfil padrão (USER_BASIC) a um usuário recém-criado
     *
     * @param user Usuário que receberá o perfil padrão
     */
    @Transactional
    public void addDefaultProfileToUser(User user) {
        Profile defaultProfile = profileService.findByName("USER_BASIC");
        user.addProfile(defaultProfile);
        userRepository.save(user);

        log.debug("✅ Default profile {} added to user: {}", "USER_BASIC", user.getUsername());
    }

    /**
     * Adiciona um perfil específico a um usuário
     *
     * @param username    Username do usuário
     * @param profileName Nome do perfil a ser adicionado
     */
    @Transactional
    public void addProfileToUser(String username, String profileName) {
        log.info("👤 Adding profile {} to user: {}", profileName, username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(ValidationConstants.ValidationMessages.USER_NOT_FOUND_BY_USERNAME + username));

        Profile profile = profileService.findByName(profileName);

        if (user.hasProfile(profileName)) {
            log.warn("⚠️ User {} already has profile: {}", username, profileName);
            return;
        }

        user.addProfile(profile);
        User updatedUser = userRepository.save(user);

        // Publica evento de adição de perfil
        userProducer.sendUserProfileAdded(updatedUser, profileName);

        log.info("✅ Profile {} added to user: {}", profileName, username);
    }

}
