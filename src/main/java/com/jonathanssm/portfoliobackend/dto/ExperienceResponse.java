package com.jonathanssm.portfoliobackend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public record ExperienceResponse(
        Long id,
        String title,
        String companyName,
        String projectName,
        String description,
        LocalDate startDate,
        LocalDate endDate,
        boolean current,
        Set<TechnologyResponse> technologies,
        Set<String> responsibilities,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public record TechnologyResponse(
            Long id,
            String name,
            String description,
            String type,
            Long version
    ) {
    }
}

