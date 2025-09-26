package com.jonathanssm.portfoliobackend.controller;

import com.jonathanssm.portfoliobackend.dto.ExperienceRequest;
import com.jonathanssm.portfoliobackend.dto.ExperienceResponse;
import com.jonathanssm.portfoliobackend.service.ExperienceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/experiences")
@RequiredArgsConstructor
public class ExperienceController {

    private final ExperienceService experienceService;

    @PostMapping
    public ResponseEntity<ExperienceResponse> createExperience(
            @Valid @RequestBody ExperienceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(experienceService.createExperience(request));
    }

    @GetMapping
    public ResponseEntity<List<ExperienceResponse>> getAllExperiences() {
        return ResponseEntity.ok(experienceService.getAllExperiences());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExperienceResponse> getExperienceById(@PathVariable Long id) {
        return ResponseEntity.ok(experienceService.getExperienceById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExperienceResponse> updateExperience(
            @PathVariable Long id,
            @Valid @RequestBody ExperienceRequest request) {
        return ResponseEntity.ok(experienceService.updateExperience(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExperience(@PathVariable Long id) {
        experienceService.deleteExperience(id);
        return ResponseEntity.noContent().build();
    }
}
