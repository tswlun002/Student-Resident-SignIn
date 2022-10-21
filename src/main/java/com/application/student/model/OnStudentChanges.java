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

    /**
     * Changes student residence
     * @param student  to change residence
     * @param currentResName of the current residence student reside at
     * @param currentBlock of the current residence student reside at
     * @param newAccommodation new accommodation student to reside at
     * @return true if student changes residence else false
     */
      boolean studentChangeResidence(Student student, String currentResName, String currentBlock,
                                     String newAccommodation);
}
