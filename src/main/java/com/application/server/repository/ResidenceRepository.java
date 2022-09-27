package com.application.server.repository;
import com.application.server.data.Residence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ResidenceRepository  extends JpaRepository<Residence,Long> {
    @Query("select r from Residence r join fetch r.address where r.residenceName=:name")
    List<Residence> getResidenceForestHill(String name);
    @Query("select r from Residence r  join fetch r.address  join fetch r.residenceRules rr " +
            "where r.residenceName=:name and r.blocks =:block")
    Residence getResidence(String name, String block);
}
