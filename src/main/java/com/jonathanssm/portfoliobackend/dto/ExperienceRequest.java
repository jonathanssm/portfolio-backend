package com.jonathanssm.portfoliobackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Set;

public record ExperienceRequest(
        @NotBlank(message = "Title is mandatory")
        String title,

        @NotBlank(message = "Company is mandatory")
        String companyName,

        @NotBlank(message = "Project is mandatory")
        String projectName,

        String description,

        @NotNull(message = "Start date is mandatory")
        LocalDate startDate,

        LocalDate endDate,

        boolean current,

        Set<Long> technologyIds,

        Set<String> responsibilities
) {
}
