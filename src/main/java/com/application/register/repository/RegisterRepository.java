package com.application.register.repository;
import com.application.register.data.Register;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface RegisterRepository extends JpaRepository<Register, Integer> {

    @Query("select r from Register r join fetch r.hostStudent s where s.studentNumber=:studentId and r.signingDate=:date")
    Register getRegister(long studentId, LocalDate date);

}
