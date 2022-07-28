package com.application.server.repository;
import com.application.server.data.ResidenceRules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResidentRulesRepository extends JpaRepository<ResidenceRules, Long> {
    @Query(value = "SELECT * FROM ResidenceRules WHERE residence=:residenceName",nativeQuery = true)
    List<ResidenceRules> getRuleForResidence(@Param("residenceName") String residenceName);
    @Query(value = "SELECT * FROM ResidenceRules WHERE residence=:residenceName AND blocks=:blocks",nativeQuery = true)
    List<ResidenceRules> getRuleForResidence(String residence, String blocks);
}
