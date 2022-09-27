package com.application.register.controller;

import com.application.register.model.RegisterService;
import com.application.server.data.Address;
import com.application.visitor.data.Visitor;
import com.application.visitor.model.GuestType;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
@Builder

public class RegisterController {
    @Autowired private RegisterService registerService;
/**
 * Sign in visitor to residence
 * Check the signing in is authorised and valid
 * by checking if signing has not elapsed, validate host and visitor
 * @param studentNumberHost  is the student number of the hosting student
 * @param visitorId of the visitor to be signed in to the residence
 * @param residenceName of the residence of the host
 * @param residenceBlock of the residence where host stays
 * @param address of the visitor
 * @return true if signed in successfully else false
 * */
    public boolean signInRelativeGuests(long studentNumberHost,String residenceName, String residenceBlock,
                                        String visitorName, long visitorId,String VisitorContact, Address address){
        Visitor visitor = Visitor.builder().fullname(visitorName).contact(VisitorContact).idNumber(visitorId).fullname(visitorName)
                .visitorType(GuestType.RELATIVE.name()).address(address).build();
        return registerService.singIn(studentNumberHost,visitor,residenceName,residenceBlock);
    }
/**
 * Sign in visitor to residence
 * Check the signing in is authorised and valid
 * by checking if signing has not elapsed, validate host and visitor
 * @param studentNumberHost  is the student number of the hosting student
 * @param visitorId of the visitor to be signed in to the residence
 * @param residenceName of the residence of the host
 * @param residenceBlock of the residence where host stays
 * @return true if signed in successfully else false
 * */
    public boolean signInStudent(long studentNumberHost,String residenceName, String residenceBlock,
                              long visitorId, String visitorName,String VisitorContact) {
        Visitor visitor = Visitor.builder().fullname(visitorName).
                contact(VisitorContact).idNumber(visitorId).fullname(visitorName)
                .visitorType(GuestType.STUDENT.name()).build();

        return  registerService.singIn(studentNumberHost,visitor,residenceName,residenceBlock);
    }
    /**
     * Sign out visitor from residence
     * @param studentNumberHost of the host student
     * @param visitorId of the visitor to be signed in to the residence
     * @param residenceName of residence where visitor was signed in
     * @param residenceBlock of residence where visitor was signed in
     * @return true if signed out else false
     * @throws  RuntimeException if signing out is not authorised
     */
    public boolean signOut(long studentNumberHost,String residenceName, String residenceBlock,long visitorId){
       return registerService.signOut(studentNumberHost,visitorId,residenceName,residenceBlock);
    }

    /**
     * Print all today  signing records
     */
    public void showAllVisitors() {
        System.out.println("------------------Signed GuestType-------------------------");
        registerService.getAllVisitorsByDate(LocalDate.now()).forEach(
                System.out::println
        );
    }
}
