package com.server;
import com.host.Host;
import com.visitor.SchoolMate;
import org.springframework.stereotype.Component;

@Component
public class Server {
    public Server(){}
    public boolean authenticateAndAuthorizationSchoolmate(Host host, SchoolMate visitor){
        return validateHost(host.getHostNumber(), host.getFullName()) && validateSchoolmate(visitor.getStudentNumber(),
                visitor.getFullName());
    }
    public boolean authenticateAndAuthorizationRelative(Host host, SchoolMate visitor){
        return validateHost(host.getHostNumber(), host.getFullName()) && validateId(visitor.getStudentNumber());
    }

    public  boolean validateHost(long studentNumber, String fullName){
        return  true;
    }
    public  boolean validateSchoolmate(long studentNumber, String fullName){
        return  true;
    }
    public  boolean validateId(long Id){
         return (Id + "").length() == 13;
    }

}
