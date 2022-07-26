package com.application.server.repository;
import com.application.server.data.Residence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResidenceRepository  extends JpaRepository<Residence,String> {

    @Query(value = "select * from residence where residence_name=:name",nativeQuery = true)
    public List<Residence> getResidenceForestHill(String name);
}
