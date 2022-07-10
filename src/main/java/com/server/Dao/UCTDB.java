package com.server.Dao;
import com.visitor.SchoolMate;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UCTDB {

    private final List<SchoolMate> uctStudents =
            List.of(new SchoolMate[]{
                    new SchoolMate("Zusakhe",  "0788148267",1034, "Roscommon"),
                    new SchoolMate( "Lula", "0688148267",1035, "Lisbiek"),
                    new SchoolMate( "Kuhle", "0888148267",1045 ,"Ros-common"),
                    new SchoolMate("Nosipho", "0798148267",456 ,"Forest Hill"),
                    new SchoolMate("Sakhi", "0628148267",567, "Forest Hill"),
                    new SchoolMate( "Lunga", "0788148267", 123,"Forest Hill"),
                    new SchoolMate( "Mpumelelo", "0688148267", 234,"Forest Hill"),
                    new SchoolMate("Sizamkele", "0888148267",345,  "Forest Hill"),
            });

    public  UCTDB(){}
    public List<SchoolMate> getStudent() {
        return  uctStudents;
    }
}
