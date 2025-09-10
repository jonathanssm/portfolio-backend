package com.jonathanssm.portfoliobackend.service;

import com.jonathanssm.portfoliobackend.dto.ExperienceRequest;
import com.jonathanssm.portfoliobackend.dto.ExperienceResponse;
import com.jonathanssm.portfoliobackend.dto.mapper.ExperienceMapper;
import com.jonathanssm.portfoliobackend.messaging.ExperienceProducer;
import com.jonathanssm.portfoliobackend.model.Experience;
import com.jonathanssm.portfoliobackend.model.Technology;
import com.jonathanssm.portfoliobackend.repository.ExperienceRepository;
import com.jonathanssm.portfoliobackend.repository.TechnologyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Transactional
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
        return resp;
    }

    @Transactional(readOnly = true)
    public ExperienceResponse getExperienceById(Long id) {
        log.info("🔎 Fetching experience with id: {}", id);

        Experience experience = experienceRepository.findByIdWithTechnologies(id).orElseThrow();

        experienceProducer.sendExperienceFetched(1);
        return experienceMapper.toResponse(experience);
    }

    @Transactional
    public ExperienceResponse updateExperience(Long id, ExperienceRequest request) {
        log.info("♻️ Updating experience with id: {}", id);

        Experience experience = experienceRepository.findById(id).orElseThrow();

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

        return experienceMapper.toResponse(experience);
    }

    @Transactional
    public void deleteExperience(Long id) {
        log.info("🗑️ Deleting experience with id: {}", id);

        experienceRepository.findById(id).orElseThrow();
        experienceRepository.deleteById(id);
        experienceProducer.sendExperienceDeleted(id);
    }
}
