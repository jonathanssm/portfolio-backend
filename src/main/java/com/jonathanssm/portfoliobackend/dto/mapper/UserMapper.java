package com.jonathanssm.portfoliobackend.dto.mapper;

import com.jonathanssm.portfoliobackend.dto.UserRegistrationRequest;
import com.jonathanssm.portfoliobackend.dto.UserResponse;
import com.jonathanssm.portfoliobackend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para conversões entre User entity e DTOs
 * Usa MapStruct para geração automática de código de mapeamento
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Converte UserRegistrationRequest para User entity
     * 
     * @param request DTO de registro de usuário
     * @return User entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profiles", ignore = true)
    @Mapping(target = "isEnabled", constant = "true")
    @Mapping(target = "isAccountNonExpired", constant = "true")
    @Mapping(target = "isAccountNonLocked", constant = "true")
    @Mapping(target = "isCredentialsNonExpired", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(UserRegistrationRequest request);

    /**
     * Converte User entity para UserResponse
     * 
     * @param user User entity
     * @return UserResponse DTO
     */
    @Mapping(target = "profiles", source = "profiles", qualifiedByName = "mapProfiles")
    @Mapping(target = "roles", source = "user", qualifiedByName = "mapRoles")
    UserResponse toResponse(User user);

    /**
     * Mapeia profiles para Set<String>
     */
    @org.mapstruct.Named("mapProfiles")
    default java.util.Set<String> mapProfiles(java.util.Set<com.jonathanssm.portfoliobackend.model.Profile> profiles) {
        return profiles.stream()
                .map(com.jonathanssm.portfoliobackend.model.Profile::getName)
                .collect(java.util.stream.Collectors.toSet());
    }

    /**
     * Mapeia roles do usuário para Set<String>
     */
    @org.mapstruct.Named("mapRoles")
    default java.util.Set<String> mapRoles(User user) {
        return user.getAllRoles().stream()
                .map(role -> role.getName().name())
                .collect(java.util.stream.Collectors.toSet());
    }
}
