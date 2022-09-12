package com.application.visitor.repository;
import com.application.visitor.data.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuestRepository extends JpaRepository<Visitor, Long> {

    @Query("select v from Visitor v join fetch v.address where v.idNumber=:idNumber")
    Visitor findByStudentNumber(long idNumber);
    @Query("select v from Visitor v join fetch v.address")

    List<Visitor> getAllVisitors();
    @Query("select v from Visitor v join fetch v.address where v.visitorType=:visitorType")

    List<Visitor> getsStudentVisitors(String visitorType);
}
