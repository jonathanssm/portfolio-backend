package com.jonathanssm.portfoliobackend.repository;

import com.jonathanssm.portfoliobackend.model.TechnologyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TechnologyTypeRepository extends JpaRepository<TechnologyType, Long> {
    Optional<TechnologyType> findByName(String name);
}
