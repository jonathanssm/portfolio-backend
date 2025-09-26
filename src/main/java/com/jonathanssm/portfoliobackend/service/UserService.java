package com.jonathanssm.portfoliobackend.service;

import com.jonathanssm.portfoliobackend.constants.ApplicationConstants;
import com.jonathanssm.portfoliobackend.model.Profile;
import com.jonathanssm.portfoliobackend.model.Role;
import com.jonathanssm.portfoliobackend.model.User;
import com.jonathanssm.portfoliobackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileService profileService;
    private final RoleService roleService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("🔐 Loading user by username: {}", username);
        
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ApplicationConstants.ErrorMessages.USER_NOT_FOUND + username));
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ApplicationConstants.ErrorMessages.USER_NOT_FOUND + username));
    }

    private User findUserByUsernameInternal(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ApplicationConstants.ErrorMessages.USER_NOT_FOUND + username));
    }

    public void createUser(User user) {
        log.info("👤 Creating new user: {}", user.getUsername());
        
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException(ApplicationConstants.ErrorMessages.AlreadyExists.USERNAME_ALREADY_EXISTS + user.getUsername());
        }
        
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException(ApplicationConstants.ErrorMessages.AlreadyExists.EMAIL_ALREADY_EXISTS + user.getEmail());
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Se o usuário não tem perfis, adiciona o perfil USER_BASIC por padrão
        if (user.getProfiles().isEmpty()) {
            Profile userBasicProfile = profileService.findByName(ApplicationConstants.Profiles.USER_BASIC);
            user.addProfile(userBasicProfile);
        }
        
        userRepository.save(user);
        log.info("✅ User created successfully: {}", user.getUsername());
    }

    public void addProfileToUser(String username, String profileName) {
        User user = findUserByUsernameInternal(username);
        Profile profile = profileService.findByName(profileName);
        user.addProfile(profile);
        userRepository.save(user);
        log.info("✅ Profile {} added to user {}", profileName, username);
    }

    public void removeProfileFromUser(String username, String profileName) {
        User user = findUserByUsernameInternal(username);
        Profile profile = profileService.findByName(profileName);
        user.removeProfile(profile);
        userRepository.save(user);
        log.info("✅ Profile {} removed from user {}", profileName, username);
    }

    public void addRoleToUser(String username, Role.RoleName roleName) {
        User user = findUserByUsernameInternal(username);
        Role role = roleService.findByName(roleName);
        user.addRole(role);
        userRepository.save(user);
        log.info("✅ Role {} added to user {}", roleName, username);
    }

    public void removeRoleFromUser(String username, Role.RoleName roleName) {
        User user = findUserByUsernameInternal(username);
        Role role = roleService.findByName(roleName);
        user.removeRole(role);
        userRepository.save(user);
        log.info("✅ Role {} removed from user {}", roleName, username);
    }
}
