package com.application.server.repository;
import com.application.server.data.Address;
import com.application.server.data.Residence;
import com.application.server.data.ResidenceRules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public interface ResidenceRepository  extends JpaRepository<Residence,Long> {
    @Query("select r from Residence r join fetch r.address where r.residenceName=:name")
    List<Residence> getResidenceForestHill(String name);
    @Query("select r from Residence r  join fetch r.address " +
            "where r.residenceName=:name and r.blocks =:block")
    Residence getResidence(String name, String block);
    @Transactional
    @Modifying
    @Query("delete from Residence b where  b.residenceName=:residenceName")
    void deleteResidence(String residenceName);
    /*@Query("select b.residenceRules  from Residence b  left join fetch b.residenceRules  where  b.residenceName=:residenceName and b.blocks=:blocks")
    ResidenceRules rulesOfResidence(String residenceName, String blocks);
    @Query("select r.address  from Residence r  left join fetch r.address  where r.residenceName=:residenceName and r.blocks=:blocks")
    Address addressOfResidence(String residenceName, String blocks)*/;
}
