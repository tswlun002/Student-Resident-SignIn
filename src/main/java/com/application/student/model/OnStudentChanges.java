package com.application.student.model;
import com.application.student.data.Student;
public interface OnStudentChanges {
    /**
     * notify about student added or changes on students entity to residence department
     */
      void addedStudent(Student student);
    /**
     * notify about student deleted on students entity
     * @param student  was deleted on students entity
     */
      void deletedStudent(Student student);
}
