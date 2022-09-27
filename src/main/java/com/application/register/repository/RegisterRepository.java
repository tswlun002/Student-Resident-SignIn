package com.application.register.repository;
import com.application.register.data.Register;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RegisterRepository extends JpaRepository<Register, Integer> {

    @Query("select r from Register r join fetch r.hostStudent s where s.studentNumber=:studentId and r.signingDate=:date")
    Register getRegister(long studentId, LocalDate date);
    @Query("select r from Register r join fetch r.hostStudent s where s.studentNumber=:studentId and r.signingDate=:date and" +
            " r.numberVisitors>=:numberVisitors")
    Register getRegisterByRangeVisitors(long studentId, LocalDate date, int numberVisitors);

    @Query("select r from Register r join fetch r.hostStudent s ")
    List<Register> getRegister();
}
