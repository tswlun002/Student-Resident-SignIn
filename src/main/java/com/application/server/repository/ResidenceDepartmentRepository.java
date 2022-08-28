package com.application.server.repository;
import com.application.server.data.ResidenceDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public interface ResidenceDepartmentRepository extends JpaRepository<ResidenceDepartment,Long> {
   @Query("select distinct d from ResidenceDepartment d " +
           "join fetch d.residence r " +
           "left join fetch d.students s")
    List<ResidenceDepartment> getDepartments();

 @Query(value = "SELECT AccommodatedStudents.* from AccommodatedStudents ",nativeQuery = true)
 List<String> getDepartmentWithStudents();
 @Modifying
 @Transactional
 @Query(value = "delete  from AccommodatedStudents where" +
         " AccommodatedStudents.studentId=:id and AccommodatedStudents.studentName=:name",nativeQuery = true)
 void deleteStudent(Long id, String name);
    @Query("select distinct d from ResidenceDepartment d join fetch d.students s join fetch d.residence r " +
            " where  s.studentNumber=:studentId and r.id=:resId  and  d.accommodation=:accommodation")
    ResidenceDepartment getDepartments(long studentId, Long resId, String accommodation);
    @Query("select d from ResidenceDepartment d join fetch d.residence r where r.id=:id")
    ResidenceDepartment getDepartments(Long id);


}
