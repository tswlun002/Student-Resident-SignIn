package com.application.student.model;
import com.application.server.data.Address;
import com.application.server.model.AddressService;
import com.application.student.data.Student;
import com.application.student.repostory.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import java.util.List;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private OnStudentChanges studentChanges;
    @Autowired
    private AddressService addressService;

    private  List<Student> studentList;

    /**
     * Fetch all students
     */
    @PostConstruct
    public   void  fetchAllStudents(){
        studentList = studentRepository.getAllStudent();
    }

    /**
     * Get student registered
     * @return list student if there are registered student else null
     */
    public List<Student> getStudentList() {
        return studentList;
    }


    /**
     * Save student to database
     * @param student - to save to database
     */
    public  Student saveStudent(Student student){
        if(student !=null) {
            Address address =saveStudentAddress(student.getAddress());
            student.setAddress(address);
            Student student1 =studentRepository.save(student);
            if(! student.getAccommodation().equalsIgnoreCase("no"))studentChanges.addedStudent(student1);
            student=student1;
            fetchAllStudents();
        }
        else throw new RuntimeException("Can not save null student");
        return student;
    }


    /**
     * Save Student address
     * @param address to be saved
     */
    public  Address saveStudentAddress(Address address){
        if(address !=null)return addressService.saveAddress(address);
        return null;
    }

    /**
     * Update Student  fullname or contact
     * @param studentNumber - to be updated
     * @return true if successfully updated else false
     */
    public    boolean updateStudent(long studentNumber, String fullname,
                                    String contact){
        boolean updated =false;
        Student student = getStudent(studentNumber);
        if(student !=null){
                student.setFullName(fullname);
                student.setContact(contact);
                saveStudent(student);
                updated =true;


        }
        return updated;
    }


    /**
     * Delete student from students entity
     * @param studentNumber  of student to be deleted from students entity
     * @return deleted student if it's present on students entity else null
     */
    public Student deleteStudent(long studentNumber){

        Student student1 = getStudent(studentNumber);
        if(student1 !=null) {
            studentChanges.deletedStudent(student1);
            studentRepository.delete(student1);
            fetchAllStudents();
            return  student1;
        }
        else return null;

    }

    /**
     * Update student department
     * @param student  to update its department
     */
    public  void updateStudentDepartment(Student student){
        studentRepository.save(student);
        fetchAllStudents();
    }


    /**
     * Get students with accommodation
     * @return - list of students with accommodation
     */
    public List<Student> getStudentsHaveResOffer() {
       return studentList.stream().filter(student ->
              ! student.getAccommodation().equalsIgnoreCase("no")).toList();
    }
    /**
     * Get students have no accommodation
     * @return - list of students with  no accommodation
     */
    public List<Student> getStudentsHaveNoResOffer() {
        return studentList.stream().filter(student ->
                student.getAccommodation().equalsIgnoreCase("no")).toList();
    }



    /**
     * Search for student given student number
     * @param studentNumber - student of the student
     * @return - Student if student is valid else return null
     */
    public Student getStudent(long studentNumber) {
        for (Student student1:studentList) {
            if(student1.getStudentNumber()==studentNumber){
                return student1;
            }
        }
       return  null;
    }

    /**
     * Changes student residence
     * @param studentId of the student to change residence
     * @param currentResName of the current residence student reside at
     * @param currentBlock of the current residence student reside at
     * @param newAccommodation new accommodation student to reside at
     * @return true if student changes residence else false
     */
    public boolean studentChangeResidence(long studentId, String currentResName, String currentBlock,
                                                     String newAccommodation){
        Student student = getStudent(studentId);
        if(student==null)return  false;
        return studentChanges.studentChangeResidence(student,currentResName,currentBlock,newAccommodation);
    }

}
