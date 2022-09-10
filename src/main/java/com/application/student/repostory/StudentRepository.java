package com.application.student.repostory;
import com.application.student.data.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/***
 * StudentService Repository .
 * Help to get and implement different ways of accessing
 * and manipulating student object
 */
@Repository
public interface StudentRepository extends JpaRepository <Student,Long>{
    @Override
    <S extends Student> S save(S entity);
    @Override
    Student getReferenceById(Long aLong);
   @Query("select s from Student s where s.accommodation<>'no' and s.department is  null")
    List<Student> getStudentsWithResOffer();
}
