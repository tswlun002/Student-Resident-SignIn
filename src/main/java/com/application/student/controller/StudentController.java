package com.application.student.controller;

import com.application.server.data.Address;
import com.application.student.data.Student;
import com.application.student.model.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/admin")
public class StudentController {
     @Autowired private StudentService studentService;


    /**
     * Saved student using given student info
     * @param studentNumber of the student
     * @param fullname of the student
     * @param contact of the student
     * @param accommodation of the student
    // * @param address of the student
     * @return  true if student is saved else false
     */

    @PostMapping(value = "/student")
    public  boolean saveStudent(@RequestParam String fullname, @RequestParam long studentNumber,
                                @RequestParam String contact,
                                @RequestParam String accommodation, @RequestBody  Address address){
        Student student;

       if(accommodation.equalsIgnoreCase("no")){

            student = Student.builder().studentNumber(studentNumber).fullName(fullname).
                    contact(contact).accommodation(accommodation).address(address).build();
        }else{

            student = Student.builder().studentNumber(studentNumber).fullName(fullname).
                    contact(contact).accommodation(accommodation).build();
        }
        student = studentService.saveStudent(student);
        return  student !=null;


    }

    /**
     * Search registered student
     * @param studentNumber   of the student to search
     * @return true if the student is found else false
     */
    @GetMapping(value = "/students/search-student")
    public boolean searchStudent(@RequestParam long studentNumber) {
        return studentService.getStudent(studentNumber)!=null;
    }

    /**
     * Get student by  student number
     * @param studentNumber   of the student to search
     * @return student if the student is found else null
     */
    @GetMapping(value = "/students/student")
    public String getStudent(@RequestParam long studentNumber) {
        Student student= studentService.getStudent(studentNumber);
        if(student !=null){
            return  student.toString();
        }
        return "Student with student number:"+studentNumber+" is not found";
    }

    /**
     * Get student registered
     * @return list student if there are registered student else null
     */

    @GetMapping(value = "students")
    public List<String> getStudentList() {
        return studentService.getStudentList().stream().map(
                Student::toString
        ).toList();
    }

    /**
     * Get students that have accommodation
     * @return  List of student have residence else null
     */
    @GetMapping(value ="/students/accommodation" )
    public   List<String> studentsHasAccommodation(){
      return  studentService.getStudentsHaveResOffer().stream().map(
              Student::toString
      ).toList();

    }
    /**
     * Get students have no accommodation
     * @return - list of students with  no accommodation
     */
    @GetMapping(value ="/students/no-accommodation" )
    public List<String> getStudentsHaveNoResOffer() {
        return studentService.getStudentsHaveNoResOffer().stream().map(
                Student::toString
        ).toList();
    }
    /**
     * Update student  name or contact of student
     * @param studentNumber   of the student  to be updated
     * @param fullname updated name of the student
     * @param  contact updated contact of the student
     * @return true if student is updated else false
     */
    @PostMapping(value = "/update")
    public boolean  updateStudent(@RequestParam long studentNumber, @RequestParam String fullname,
                                  @RequestParam String contact) {
        return  studentService.updateStudent(
            studentNumber,fullname,contact
        );
    }

    /**
     * Delete student
     * @param studentNumber   of the student
     * @return true if student is deleted else false
     */
    @DeleteMapping(value = "/delete")
    public boolean deleteStudent(@RequestParam long studentNumber) {
        //.Logger.(getStudent(studentNumber));
       Student student = studentService.deleteStudent(studentNumber);
        return  student!=null;
    }



}
