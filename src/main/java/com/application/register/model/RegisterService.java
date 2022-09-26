package com.application.register.model;

import com.application.register.data.Register;
import com.application.register.data.VisitorsRegister;
import com.application.register.repository.RegisterRepository;
import com.application.server.data.Residence;
import com.application.server.data.ResidenceDepartment;
import com.application.server.data.ResidenceRules;
import com.application.server.model.ResidenceRegisterService;
import com.application.server.model.ResidenceService;
import com.application.student.data.Student;
import com.application.student.model.StudentService;
import com.application.visitor.data.Visitor;
import com.application.visitor.model.GuestType;
import com.application.visitor.model.RelativeGuestService;
import com.application.visitor.model.StudentGuestService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Service
public class RegisterService {

    @Autowired private RegisterRepository registerRepository;
    @Autowired private StudentGuestService studentGuestService;
    @Autowired private RelativeGuestService relativeGuestService;
    @Autowired private StudentService studentService;
    @Autowired private ResidenceService residenceService;

    @Autowired private ResidenceRegisterService residenceRegisterService;

    @Autowired private VisitorsRegisterService visitorsRegisterService;
    private Student host;


    public boolean singIn(long studentHostId, Visitor visitor, String residenceName, String residenceBlock){

        //Student host  = residenceRegisterService.getStudent()

        if(visitor==null) return false;

        LocalTime time = LocalTime.now();
        boolean authorised = authoriseSigning(studentHostId,visitor,residenceName,residenceBlock,time);
        if(authorised) {
            Residence residence = residenceService.getResidence(residenceName,residenceBlock);
            Register register = Register.builder().hostStudent(host).numberVisitors(1).signingDate(LocalDate.now()).residence(residence).build();
            Register register1 = saveRegister(register,visitor);

            Visitor visitor1 = GuestType.valueOf(visitor.getVisitorType())==GuestType.STUDENT?
                    studentGuestService.saveGuest(studentService.getStudent(visitor.getIdNumber())) :
                    relativeGuestService.saveGuest(visitor);

            System.out.println(visitor1);
            VisitorsRegister visitorsRegister = VisitorsRegister.builder().register(register1).visitor(visitor1)
                    .signInTime(time).signingStatus(SigningStatus.SIGNEDIN.name()).build();

            return visitorsRegisterService.addVisitor(visitorsRegister);
        }else throw  new RuntimeException(studentHostId +" is not authorised to sign in");
    }

    private  void setHostStudent(Student host){
        this.host=host;
    }

    /**
     * Saves the register 
     * @param register to be saved
     * @param visitor  stored in the register
     * @return register  if saved else null
     */
    private Register saveRegister(Register register,Visitor visitor){
        if (register==null || visitor==null) return null ;

        Register register1 = registerRepository.getRegister(register.getHostStudent().getStudentNumber(),LocalDate.now());
        if(register1==null){
            return  registerRepository.save(register);

        }else {
            register1.setNumberVisitors(register1.getNumberVisitors() + 1);
            return registerRepository.save(register1);
        }
    }



    private  boolean authoriseSigning(long hostId, Visitor visitor, String name, String block,LocalTime time){
        return withInSigningTime(name,block,time) &&
                authenticateAndAuthorise(name,block,hostId)
        && authoriseVisitor(visitor);
    }
    /**
     * Validate host by checking if
     * Valid student by validating host student
     * and authorise by checking visitors signed by
     * host student are less than maximum visitors allowed to be signed by host
     * @param residenceName  of the residence
     * @param block of the residence
     * @param studentNumber of host student
     * @return  true if the valid student and number visitor is less or equal maxim number visitors per host student
     */
    public boolean authenticateAndAuthorise(String residenceName, String block, long studentNumber){
        return  validateHost(residenceName,block,studentNumber) &&
                validNumberVisitorsSigned(studentNumber,residenceName,block);
    }

    /**
     * Check if number visitor is less or equal maxim number visitors per host student
     * @param studentId of the host student
     * @param residenceName of the residence of the host student
     * @param block of the residence of the host student
     * @return if visitor's of the host less than or equal maximum visitors allowed
     */
    private    boolean validNumberVisitorsSigned(long studentId,String residenceName, String block){
        ResidenceRules residenceRules = residenceService.getRulesResidence(residenceName,block);
        if(residenceRules==null) return true;
        LocalDate date  = LocalDate.now();
        Register register = registerRepository.getRegister(studentId, date);
        if(register==null) return  true;

        return  register.getNumberVisitors()<=residenceRules.getNumberVisitor();
    }

    /**
     * Validate host by checking if the student is registered at the residence
     * @param residenceName of the residence of the host student
     * @param studentNumber of the residence of the host student
     * @return true  if the student is valid
     * @throws  RuntimeException if the student is not registered at the residence
     */
    private    boolean validateHost(String residenceName,String block, long studentNumber) {
        ResidenceDepartment department = residenceRegisterService.getDepartment(residenceName,block);
        Student student = residenceRegisterService.getStudent(department,studentNumber);
        if(student !=null){
            setHostStudent(student);
            return  true;
        }else  throw  new RuntimeException("Student with student number "+studentNumber+" does not exists at" +
                "residence "+residenceName +block);

    }

    /**
     * Authorize visitor by checking if the details are correct and valid
     * @param visitor to be checked
     * @return true if details are valid  else false
     * @throws  RuntimeException exception if visitor is not relative or student type
     */
    private  boolean authoriseVisitor( Visitor visitor){

        if(visitor==null)return false;
        return GuestType.valueOf(visitor.getVisitorType())==GuestType.STUDENT?
                validateStudentGuest(visitor) : GuestType.valueOf(visitor.getVisitorType())==GuestType.RELATIVE
                ?true & !checkVisitorAlreadySigned(visitor):noneTypeGuest();
    }

    /**
     * check if visitor is signed
     * @param visitor to be checked
     * @return true visitor signed else false
     */
    private boolean checkVisitorAlreadySigned(Visitor visitor){
        return visitorsRegisterService.checkVisitorIsSigned(visitor);
    }

    /**
     * @throws  RuntimeException of none visitor or guest type
     */
    private boolean noneTypeGuest(){
        throw new RuntimeException("Visitor must be either Student or Relative type");
    }

    /**
     * Check if its signing time  at the residence
     * @param name  of the residence
     * @param block of the block
     * @param time  it is a current time be checked
     * @return true if within singing period else false
     */
    private  boolean withInSigningTime(String name, String block, LocalTime time){
            return SigningTime.builder().build().checkSigningTime(residenceService,name,block,time);
    }

    /**
     * check if visitor is the registered student
     * @param visitor of  student type
     * @return  true if the student is registered else false
     */
    private boolean   validateStudentGuest(Visitor visitor){

        if(visitor==null)return  false;
        return studentService.getStudent(visitor.getIdNumber())!=null;
    }
    /**
     * Apply Luhn formula for check-digits
     * @param Id - RSA id number
     * @return - true if correct ID number else false
     */
     private  boolean validateRelativeGuestByRSAId(long Id) {
        boolean isValid  = false;
        String ID  = String.valueOf(Id);
        if(ID.length() == 13) {
            int century = (Integer.parseInt(ID.substring(0, 1)) == 9) ? 19 : 20;
            long years = LocalDate.now().getYear() - (Integer.parseInt(century + ID.substring(0, 2)));
            //check year
            if (years <= 90 && years >= 0) {
                int gender = Integer.parseInt(ID.substring(6, 10));
                int citizenship = Integer.parseInt(ID.substring(10, 11));
                // check valid citizenship or gender
                if (((gender <= 4999 & gender >= 0) || (gender <= 8999 & gender >= 5000)) &&
                        (citizenship == 0 || citizenship == 1)) {
                    // Apply Luhn algo  to check if ID validated by RSA home affairs
                    long tempTotal;
                    long checkSum = 0;
                    int multiplier = 1;
                    for (int i = 1; i < 13; ++i) {
                        tempTotal = Long.parseLong(ID.charAt(i) + "") * multiplier;
                        if (tempTotal > 9) {
                            tempTotal = Long.parseLong(String.valueOf(tempTotal).charAt(0) + "") +
                                    Long.parseLong(String.valueOf(tempTotal).charAt(1) + "");
                        }
                        checkSum = checkSum + tempTotal;
                        multiplier = (multiplier % 2 == 0) ? 1 : 2;
                        if (checkSum % 10 == 0) {
                            isValid = true;
                        }

                    }
                }
            }
        }
        return  isValid;
    }
}
