package com.application.register.repository;

import com.application.register.data.VisitorsRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitorsRegisterRepository extends JpaRepository<VisitorsRegister, Integer> {

    @Query("select VR from VisitorsRegister VR join fetch VR.register r join fetch VR.visitor v")
    List<VisitorsRegister> getVisitorsRegister();
    @Query("select VR from VisitorsRegister VR join fetch VR.register r join fetch VR.visitor v where VR.signingStatus=:status")
    List<VisitorsRegister> getVisitorsRegister(String status);
}
