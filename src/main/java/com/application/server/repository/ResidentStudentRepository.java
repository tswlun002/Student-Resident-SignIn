package com.application.server.repository;
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
    @Query(value = "select * from resident_student where student_id is null and student_name is null", nativeQuery = true)
    public List<ResidentStudent> getAllAvailableRooms();
    @Transactional
    @Modifying
    @Query("update ResidentStudent set student=:student where id=:Id and blocks=:blocks and flat=:flat and room=:room")
    public  void updateWhereStudent_student_numberIsNull(Student student, long Id,String blocks, String flat, String room);
}
