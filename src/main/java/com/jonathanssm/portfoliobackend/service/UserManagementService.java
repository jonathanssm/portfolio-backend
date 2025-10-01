package com.jonathanssm.portfoliobackend.service;

import com.jonathanssm.portfoliobackend.dto.UserRegistrationRequest;
import com.jonathanssm.portfoliobackend.dto.UserResponse;
import com.jonathanssm.portfoliobackend.dto.mapper.UserMapper;
import com.jonathanssm.portfoliobackend.messaging.UserProducer;
import com.jonathanssm.portfoliobackend.model.User;
import com.jonathanssm.portfoliobackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service especializado em gerenciamento de usuários (CRUD)
 * Responsabilidade única: Operações de criação, atualização e exclusão de usuários
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserManagementService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserProfileService userProfileService;
    private final UserProducer userProducer;
    private final UserMapper userMapper;

    /**
     * Cria um novo usuário
     *
     * @param user Usuário a ser criado
     * @return Usuário criado
     * @throws IllegalArgumentException se username ou email já existirem
     */
    @Transactional
    public User createUser(User user) {
        log.info("👤 Creating new user: {}", user.getUsername());

        validateUserCreation(user);

        // Codifica a senha
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Salva o usuário
        User savedUser = userRepository.save(user);

        // Adiciona perfil básico por padrão
        userProfileService.addDefaultProfileToUser(savedUser);

        // Publica evento de criação de usuário
        userProducer.sendUserCreated(savedUser);

        log.info("✅ User created successfully: {}", savedUser.getUsername());
        return savedUser;
    }


    /**
     * Registra um novo usuário no sistema (endpoint público)
     *
     * @param request Dados do usuário para registro
     * @return UserResponse com dados do usuário criado
     */
    @Transactional
    public UserResponse registerUser(UserRegistrationRequest request) {
        log.info("📝 Registering new user: {}", request.username());

        // Converte DTO para Entity usando mapper
        User user = userMapper.toEntity(request);

        // Valida dados de criação
        validateUserCreation(user);

        // Codifica a senha
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Salva o usuário
        User savedUser = userRepository.save(user);

        // Adiciona perfil básico por padrão
        userProfileService.addDefaultProfileToUser(savedUser);

        // Publica evento de criação de usuário
        userProducer.sendUserCreated(savedUser);

        // Publica evento de registro de usuário (diferente de criação)
        userProducer.sendUserRegistered(savedUser);

        log.info("✅ User registered successfully: {}", savedUser.getUsername());

        // Converte Entity para Response usando mapper
        return userMapper.toResponse(savedUser);
    }

    /**
     * Valida dados para criação de usuário
     *
     * @param user Usuário a ser validado
     * @throws IllegalArgumentException se validação falhar
     */
    private void validateUserCreation(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }
    }
}
