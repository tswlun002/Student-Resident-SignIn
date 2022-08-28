package com.application.server.repository;
import com.application.server.data.ResidenceRegister;
import com.application.student.data.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Repository
public interface ResidenceRegisterRepository extends JpaRepository<ResidenceRegister,String> {

    @Query(value = "select * from ResidenceRegister where studentId is null and studentFullname is null", nativeQuery = true)
    List<ResidenceRegister> getAllAvailableRooms();

    @Query(value = "select * from ResidenceRegister  where blocks =:blocks and floor=:floor and flat=:flat and room=:room",nativeQuery = true)
    ResidenceRegister getBy(String blocks, int floor, String flat, String room);
}
