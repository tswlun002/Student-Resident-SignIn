package com.application.server.repository;
import com.application.server.data.Residence;
import com.application.student.data.Student;
import com.application.server.data.ResidentStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Repository
public interface ResidentStudentRepository extends JpaRepository<ResidentStudent,String> {
    @Query(value = "select * from ResidentStudent where studentId is null and studentFullname is null", nativeQuery = true)
    public List<ResidentStudent> getAllAvailableRooms();
    @Transactional
    @Modifying
    @Query(value = "update ResidentStudent set studentId=:studentId, studentFullname=:fullname " +
            "where id=:Id and residence=:residence and blocks=:blocks and flat=:flat and room=:room",
    nativeQuery = true)
    public  void placeStudent(long studentId, String fullname, long Id,  String residence, String blocks, String flat, String room);
}
