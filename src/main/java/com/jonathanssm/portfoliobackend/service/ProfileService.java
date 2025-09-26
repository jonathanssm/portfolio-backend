package com.jonathanssm.portfoliobackend.service;

import com.jonathanssm.portfoliobackend.constants.ApplicationConstants;
import com.jonathanssm.portfoliobackend.model.Profile;
import com.jonathanssm.portfoliobackend.model.Role;
import com.jonathanssm.portfoliobackend.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProfileService {


    private final ProfileRepository profileRepository;
    private final RoleService roleService;

    @Transactional(readOnly = true)
    public Profile findByName(String name) {
        return profileRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException(ApplicationConstants.ErrorMessages.PROFILE_NOT_FOUND + name));
    }

    private Profile findProfileByNameInternal(String name) {
        return profileRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException(ApplicationConstants.ErrorMessages.PROFILE_NOT_FOUND + name));
    }

    public Profile createProfile(String name, String description) {
        if (profileRepository.existsByName(name)) {
            throw new IllegalArgumentException(ApplicationConstants.ErrorMessages.AlreadyExists.PROFILE_ALREADY_EXISTS + name);
        }

        Profile profile = new Profile();
        profile.setName(name);
        profile.setDescription(description);
        
        Profile savedProfile = profileRepository.save(profile);
        log.info("✅ Profile created: {}", name);
        return savedProfile;
    }

    public Profile addRoleToProfile(String profileName, Role.RoleName roleName) {
        Profile profile = findProfileByNameInternal(profileName);
        Role role = roleService.findByName(roleName);
        profile.addRole(role);
        
        Profile savedProfile = profileRepository.save(profile);
        log.info("✅ Role {} added to profile {}", roleName, profileName);
        return savedProfile;
    }

    public Profile removeRoleFromProfile(String profileName, Role.RoleName roleName) {
        Profile profile = findProfileByNameInternal(profileName);
        Role role = roleService.findByName(roleName);
        profile.removeRole(role);
        
        Profile savedProfile = profileRepository.save(profile);
        log.info("✅ Role {} removed from profile {}", roleName, profileName);
        return savedProfile;
    }

}
