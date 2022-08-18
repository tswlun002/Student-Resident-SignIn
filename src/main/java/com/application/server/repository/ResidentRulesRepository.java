package com.application.server.repository;
import com.application.server.data.ResidenceRules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface ResidentRulesRepository extends JpaRepository<ResidenceRules, Long> {
    @Query(value = "SELECT * FROM ResidenceRules WHERE residence=:residenceName",nativeQuery = true)
    List<ResidenceRules> getRuleForResidence(@Param("residenceName") String residenceName);
    @Query(value = "SELECT * FROM ResidenceRules WHERE residence=:residenceName AND blocks=:blocks",nativeQuery = true)
    List<ResidenceRules> getRuleForResidence(String residence, String blocks);

    @Query("select r from ResidenceRules r where r.id=:id and r.startSigningTime=:start and r.endSigningTime=:end and r.numberVisitor=:numberVisitor")
    ResidenceRules getRule(Long id, LocalTime start, LocalTime end, int numberVisitor);

    @Query("select r from ResidenceRules r  where r.id=:id ")
    ResidenceRules rulesOfResidence(Long id);
    @Transactional
    @Modifying
    @Query("delete from ResidenceRules b where b.id=:id")
    void deleteRules(Long id);
}
