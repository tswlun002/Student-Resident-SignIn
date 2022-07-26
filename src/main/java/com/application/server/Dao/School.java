package com.application.server.Dao;
import com.application.server.repository.SchoolRepository;
import com.application.visitor.model.SchoolMate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class School {
    private final List<SchoolMate> uctStudents =
            List.of(new SchoolMate[]{
                    new SchoolMate("Zusakhe",  "0788148267",1034, "Roscommon"),
                    new SchoolMate( "Lulo", "0688148267",1035, "Lisbiek"),
                    new SchoolMate( "Kuhle", "0888148267",1045 ,"Ros-common"),
                    new SchoolMate("Nosipho", "0798148267",456 ,"Forest Hill"),
                    new SchoolMate("Sakhi", "0628148267",567, "Forest Hill"),
                    new SchoolMate( "Lunga", "0788148267", 123,"Forest Hill"),
                    new SchoolMate( "Mpumelelo", "0688148267", 234,"Forest Hill"),
                    new SchoolMate("Sizamkele", "0888148267",345,  "Forest Hill"),
            });
    //@Autowired
    //private SchoolRepository schoolRepository;
    @Autowired
    public School(){}
    public List<SchoolMate> getStudent() {
        return  uctStudents;
    }
    public  void saveAllStudents(List<SchoolMate> list){

    }
    public  void saveStudents(SchoolMate list){

    }
}
