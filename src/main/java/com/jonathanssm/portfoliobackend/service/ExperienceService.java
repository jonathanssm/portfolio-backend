package com.jonathanssm.portfoliobackend.service;

import com.jonathanssm.portfoliobackend.constants.DefaultConstants;
import com.jonathanssm.portfoliobackend.dto.ExperienceRequest;
import com.jonathanssm.portfoliobackend.dto.ExperienceResponse;
import com.jonathanssm.portfoliobackend.dto.mapper.ExperienceMapper;
import com.jonathanssm.portfoliobackend.messaging.ExperienceProducer;
import com.jonathanssm.portfoliobackend.model.Experience;
import com.jonathanssm.portfoliobackend.model.Technology;
import com.jonathanssm.portfoliobackend.repository.ExperienceRepository;
import com.jonathanssm.portfoliobackend.repository.TechnologyRepository;
import com.jonathanssm.portfoliobackend.util.JpaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final TechnologyRepository technologyRepository;
    private final ExperienceMapper experienceMapper;
    private final ExperienceProducer experienceProducer;
    private final MetricsService metricsService;

    public ExperienceResponse createExperience(ExperienceRequest request) {
        log.info("📝 Creating new experience: {}", request.title());

        Experience experience = experienceMapper.toEntity(request);

        if (request.technologyIds() != null && !request.technologyIds().isEmpty()) {
            Set<Technology> technologies = new HashSet<>(
                    technologyRepository.findAllById(request.technologyIds())
            );
            experience.setTechnologies(technologies);
        }

        experienceRepository.save(experience);
        experienceProducer.sendExperienceCreated(experience);
        metricsService.recordExperienceCreation();
        metricsService.recordKafkaEventPublished();

        return experienceMapper.toResponse(experience);
    }

    @Transactional(readOnly = true)
    public List<ExperienceResponse> getAllExperiences() {
        log.info("🔎 Fetching all experiences");

        List<Experience> experiences = experienceRepository.findAllWithTechnologies();
        List<ExperienceResponse> resp = experiences.stream()
                .map(experienceMapper::toResponse)
                .toList();

        experienceProducer.sendExperienceFetched(experiences.size());
        metricsService.recordExperienceFetch(experiences.size());
        metricsService.recordKafkaEventPublished();
        return resp;
    }

    /**
     * Busca experiências com paginação para melhor performance
     */
    @Transactional(readOnly = true)
    public Page<ExperienceResponse> getAllExperiences(int page, int size) {
        log.info("🔎 Fetching experiences with pagination - page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("startDate").descending());
        Page<Experience> experiences = experienceRepository.findAll(pageable);
        
        Page<ExperienceResponse> response = experiences.map(experienceMapper::toResponse);
        
        experienceProducer.sendExperienceFetched(experiences.getNumberOfElements());
        metricsService.recordExperienceFetch(experiences.getNumberOfElements());
        metricsService.recordKafkaEventPublished();
        return response;
    }

    @Transactional(readOnly = true)
    public ExperienceResponse getExperienceById(Long id) {
        log.info("🔎 Fetching experience with id: {}", id);

        // ✅ Usa JpaUtils para eliminar duplicação
        Experience experience = JpaUtils.findEntityByIdOrThrow(
                experienceRepository,
                id,
                DefaultConstants.EntityNames.EXPERIENCE
        );

        experienceProducer.sendExperienceFetched(1);
        metricsService.recordExperienceFetch(1);
        metricsService.recordKafkaEventPublished();
        return experienceMapper.toResponse(experience);
    }

    public ExperienceResponse updateExperience(Long id, ExperienceRequest request) {
        log.info("♻️ Updating experience with id: {}", id);

        // ✅ Usa JpaUtils para eliminar duplicação
        Experience experience = JpaUtils.findEntityByIdOrThrow(
                experienceRepository,
                id,
                DefaultConstants.EntityNames.EXPERIENCE
        );

        experience.setTitle(request.title());
        experience.setCompanyName(request.companyName());
        experience.setProjectName(request.projectName());
        experience.setDescription(request.description());
        experience.setStartDate(request.startDate());
        experience.setEndDate(request.endDate());
        experience.setCurrent(request.current());
        experience.setResponsibilities(request.responsibilities());

        if (request.technologyIds() != null) {
            Set<Technology> technologies = new HashSet<>(
                    technologyRepository.findAllById(request.technologyIds())
            );
            experience.setTechnologies(technologies);
        }

        experienceRepository.saveAndFlush(experience);
        experienceProducer.sendExperienceUpdated(experience);
        metricsService.recordKafkaEventPublished();

        return experienceMapper.toResponse(experience);
    }

    public void deleteExperience(Long id) {
        log.info("🗑️ Deleting experience with id: {}", id);

        // ✅ Usa JpaUtils para eliminar duplicação
        JpaUtils.findEntityByIdOrThrow(
                experienceRepository,
                id,
                DefaultConstants.EntityNames.EXPERIENCE
        );

        experienceRepository.deleteById(id);
        experienceProducer.sendExperienceDeleted(id);
        metricsService.recordKafkaEventPublished();
    }
}
