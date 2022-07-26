package com.application.server.repository;
import com.application.server.data.ResidenceRules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResidentRulesRepository extends JpaRepository<ResidenceRules, Long> { }
