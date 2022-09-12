package com.application.visitor.model;

import com.application.server.data.Address;
import  com.application.visitor.data.Visitor;
import com.application.server.model.AddressService;
import com.application.server.model.ResidentDepartmentService;
import com.application.student.data.Student;
import com.application.student.model.StudentService;
import com.application.visitor.repository.GuestRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Service
public class StudentGuestService {
    @Autowired
    private GuestRepository guestRepository;
    @Autowired
    private StudentService studentService;
    @Autowired private AddressService addressService;

    @Autowired private ResidentDepartmentService residentDepartmentService;

    /**
     * Save Guest
     * throw runtimeException if the student is not found at any residence
     * @param student - guest being saved
     */
    public  Student  saveGuest(Student student) {

        if ((student=validateStudent(student))!=null){
                Student student1 = getStudentFromDepartment(student);
            if (student1 != null) {
                 buildVisitor(student1,student1.getDepartment().getResidence().getAddress());
            }else {
                Student student2 = getStudent(student);
                if(student2.getAddress()!=null) buildVisitor(student2,student2.getAddress());
                else  throw  new RuntimeException("Guest/Visitor must have address");
            }
       } else throw  new RuntimeException("Student is  not valid!");

        return student;
    }

    /**
     * Fetch all visitors
     * @return list visitors
     */
    public List<Visitor> getAllVisitors(){
        return  guestRepository.getAllVisitors();
    }
    /**
     * Fetch all student visitors
     * @return list student visitors
     */
    public List<Visitor> getStudentVisitors(String visitorType){
        visitorType=visitorType.length()==0?"student":visitorType;
        return  guestRepository.getsStudentVisitors(visitorType);
    }

    /**
     *  Get student  from students
     * @param student to  be fetched
     * @return student if found else null
     */
    private Student getStudent(Student student) {
        if(student==null)return null;
        return  studentService.getStudent(student.getStudentNumber());
    }

    /**
     * helper method to get residence of the student
     * @param student - student used to find residence
     * @return residence of the student
     */

    private Student getStudentFromDepartment(Student student) {
        if(student==null)return null;

        student= studentService.getStudentWithRes(student.getStudentNumber());
        return student;
    }

    /**
     * Make the visitor based on Student details
     * @param student  is the guest to save
     * @param address of the student guest
     */
    private  void buildVisitor(Student student, Address address){
        Visitor visitor = Visitor.builder().idNumber(student.getStudentNumber())
                .fullname(student.getFullName()).contact(student.getContact()).
                visitorType(Student.class.getTypeName()).address(address).build();
        saveGuestAddress(address);

        if(isStored(visitor)) guestRepository.save(visitor);

    }
    private boolean isStored(Visitor visitor) {
         return guestRepository.findByStudentNumber(visitor.getIdNumber())==null;
    }

    /**
     * Saves guest address
     * @param address of the guest
     */
    private void saveGuestAddress(Address address) {
        if(address !=null)addressService.saveAddress(address);
    }

    /**
     * Check if the student is valid
     * @param student to be checked
     * @return true if student is valid  else false
     */
    public Student validateStudent(Student student){
        if(student==null)return null;
        return studentService.getStudent(student.getStudentNumber());
    }

    /**
     * delete the student guest
     * @param student  guest to be deleted
     * @return  deleted student guest
     */
    public Visitor deleteVisitor(Student student){
         if(student ==null)return  null;
         Visitor visitor= guestRepository.findByStudentNumber(student.getStudentNumber());
         if(visitor != null){
             guestRepository.delete(visitor);
         }
         return visitor;
    }


    /**+
     * search for student in students
     * @param student - object of student to search
     * @return true if found else false
     */
    public  boolean searchStudentGuest(Student student){
        return  studentService.search(student);
    }

}

