package com.jonathanssm.portfoliobackend.dto.mapper;

import com.jonathanssm.portfoliobackend.dto.ExperienceRequest;
import com.jonathanssm.portfoliobackend.dto.ExperienceResponse;
import com.jonathanssm.portfoliobackend.model.Experience;
import com.jonathanssm.portfoliobackend.model.Technology;
import com.jonathanssm.portfoliobackend.model.TechnologyType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ExperienceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "technologies", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Experience toEntity(ExperienceRequest request);

    @Mapping(target = "technologies", source = "technologies", qualifiedByName = "mapTechnologies")
    ExperienceResponse toResponse(Experience experience);

    @Named("mapTechnologies")
    default Set<ExperienceResponse.TechnologyResponse> mapTechnologies(Set<Technology> technologies) {
        return Optional.ofNullable(technologies)
                .orElse(Collections.emptySet())
                .stream()
                .map(tech -> new ExperienceResponse.TechnologyResponse(
                        tech.getId(),
                        tech.getName(),
                        tech.getDescription(),
                        Optional.ofNullable(tech.getType())
                                .map(TechnologyType::getName)
                                .orElse(null),
                        tech.getVersion()
                ))
                .collect(Collectors.toSet());
    }
}
