package com.jonathanssm.portfoliobackend.repository;

import com.jonathanssm.portfoliobackend.model.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnologyRepository extends JpaRepository<Technology, Long> {
}
