package com.jonathanssm.portfoliobackend.service;

import com.jonathanssm.portfoliobackend.constants.ApplicationConstants;
import com.jonathanssm.portfoliobackend.model.Role;
import com.jonathanssm.portfoliobackend.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Role findByName(Role.RoleName name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException(ApplicationConstants.ErrorMessages.ROLE_NOT_FOUND + name));
    }

    public Role createRole(Role.RoleName name, String description) {
        if (roleRepository.existsByName(name)) {
            throw new IllegalArgumentException(ApplicationConstants.ErrorMessages.AlreadyExists.ROLE_ALREADY_EXISTS + name);
        }

        Role role = new Role();
        role.setName(name);
        role.setDescription(description);
        
        Role savedRole = roleRepository.save(role);
        log.info("✅ Role created: {}", name);
        return savedRole;
    }

}
