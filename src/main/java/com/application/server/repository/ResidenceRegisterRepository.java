package com.application.server.repository;
import com.application.server.data.ResidenceRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResidenceRegisterRepository extends JpaRepository<ResidenceRegister,String> {

    @Query("select distinct Rr from ResidenceRegister Rr " +
            "join fetch Rr.residence r " +
            "left join fetch Rr.student s " +
            "where r.blocks=:blocks and  Rr.floor=:floor and  Rr.flat=:flat and Rr.room=:room")
    ResidenceRegister getBy(String blocks, int floor, String flat, String room);
    @Query("select distinct Rr from ResidenceRegister Rr " +
            "join fetch Rr.residence r " +
            "join fetch Rr.student s " )
    List<ResidenceRegister> getResidenceRegister();
}
