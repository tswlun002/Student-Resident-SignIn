package com.application.server.repository;
import com.application.server.data.ResidentStudent;
import com.application.student.data.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Repository
public interface ResidentStudentRepository extends JpaRepository<ResidentStudent,String> {
   /* @Query(value = "insert into ResidentStudent" +
            "select :residence, :blocks, :floor, :flat, :room where not exists" +
            "(select * from  ResidentStudent" +
            " where  blocks=:blocks, floor=:floor, flat=:flat, room=:room ",nativeQuery = true)
    void insert(String residence, String blocks, int floor, String flat, String room);*/
    @Query(value = "select * from ResidentStudent where studentId is null and studentFullname is null", nativeQuery = true)
    List<ResidentStudent> getAllAvailableRooms();
    @Transactional
    @Modifying
    @Query(value = "update ResidentStudent set studentId=:studentId, studentFullname=:fullname " +
            "where id=:Id and residence=:residence and blocks=:blocks and flat=:flat and room=:room",
    nativeQuery = true)
    void placeStudent(long studentId, String fullname, long Id,  String residence, String blocks, String flat, String room);

    @Query(value = "select * from ResidentStudent  where blocks =:blocks and floor=:floor and flat=:flat and room=:room",nativeQuery = true)
    ResidentStudent getBy(String blocks, int floor, String flat, String room);

    @Query("select r.student  from ResidentStudent r where  r.student.studentNumber=:studentNumber and  r.student.fullName=:fullName")
    Student checkStudentHasRoom(long studentNumber, String fullName);
}
