package com.jonathanssm.portfoliobackend.repository;

import com.jonathanssm.portfoliobackend.model.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    @Query("SELECT e FROM Experience e LEFT JOIN FETCH e.technologies t LEFT JOIN FETCH t.type ORDER BY e.startDate DESC")
    List<Experience> findAllWithTechnologies();
}
