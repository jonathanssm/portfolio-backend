package com.jonathanssm.portfoliobackend.repository;

import com.jonathanssm.portfoliobackend.model.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TechnologyRepository extends JpaRepository<Technology, Long> {
    Optional<Technology> findByName(String name);

    @Query("SELECT t FROM Technology t JOIN FETCH t.type WHERE t.name IN :names")
    List<Technology> findByNames(List<String> names);
}
