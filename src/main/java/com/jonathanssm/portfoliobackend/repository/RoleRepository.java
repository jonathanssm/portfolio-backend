package com.jonathanssm.portfoliobackend.repository;

import com.jonathanssm.portfoliobackend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
