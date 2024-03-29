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
     * @return  student visitor
     */
    public  Visitor  saveGuest(Student student) {

        if ((student=validateStudent(student))!=null){
                Student student1 = getStudentFromDepartment(student);
            if (student1 != null) {
                 return buildVisitor(student1,student1.getDepartment().getResidence().getAddress());
            }else {
                Student student2 = getStudent(student);
                if(student2.getAddress()!=null) return buildVisitor(student2,student2.getAddress());
                else  throw  new RuntimeException("Visitor must have address");
            }
       } else throw  new RuntimeException("Student is  not valid!");

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
    public List<Visitor> getStudentVisitors(GuestType visitorType){
        return  guestRepository.getsStudentVisitors(visitorType.name());
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
    private  Visitor buildVisitor(Student student, Address address){
        Visitor visitor = Visitor.builder().idNumber(student.getStudentNumber())
                .fullname(student.getFullName()).contact(student.getContact()).
                visitorType(GuestType.STUDENT.name()).address(address).build();
        saveGuestAddress(address);

        Visitor visitor1 =guestRepository.findByIdNumber(visitor.getIdNumber());

        if(visitor1==null) return  guestRepository.save(visitor);
        return visitor1;
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
        Visitor visitor= guestRepository.findByIdNumber(student.getStudentNumber());
        return deleteVisitor(visitor);
    }

    /**
     * Delete visitor
     * @param visitor to be deleted
     * @return deleted visitor
     */
    private  Visitor deleteVisitor(Visitor visitor){
        if(visitor != null){
            guestRepository.delete(visitor);
        }
        return visitor;
    }


    /**
     * Delete all  guests with given type
     * @param type is the guest type
     */
    public void deleteAll(GuestType type){
        List<Visitor> visitor = guestRepository.getsStudentVisitors(type.name());
        visitor.forEach(this::deleteVisitor);
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

