package com.main;
import com.host.Host;
import com.register.Register;
import com.server.Server;
import com.visitor.Address;
import com.visitor.Relative;
import com.visitor.SchoolMate;
import com.visitor.Visitor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.sql.Date;
import java.sql.Time;

@Configuration
@ComponentScan(basePackages = {"com.host","com.visitor", "com.register", "com.Server"})
class MainConfig {
    @Bean
    public Server server(){
        return  new Server();
    }

    @Qualifier ("defaultedParamHost")
    @Bean
    public Host host(){
        return new Host(9802095886081L,"Lunga Tsewu","tswlun002@myuct.ac.za","C Bock 601C");
    }
    @Qualifier ("defaultedParamRelative")
    @Bean
    public Relative relative(){
        return new Relative("Wanga Tsewu","+27788148267",9705027L,
                new Address("6912 Dludaka street","Lower",7750,"Cape Town"));
    }
    @Qualifier ("defaultedParamSchoolmate")
    @Bean
    public SchoolMate schoolMate(){
        return  new SchoolMate("Sakhe Nombombo","0783538744",990806L, "Roscommon res");
    }
    @Qualifier ("Register")
    @Bean
    public Register register() {
        return  new Register();
    }
    /*
    new Date(System.currentTimeMillis()), new Time(System.currentTimeMillis()), host.getHostNumber(),
                    visitor.getStudentNumber(), host.getRoomNumber(), "Sign In"
    @Qualifier ("RegisterRelative")
    @Bean
    public Register registerRelative( @Qualifier ("defaultedParamHost")Host host,  @Qualifier ("defaultedParamRelative")Relative visitor) {

         return new Register(
                    new Date(System.currentTimeMillis()), new Time(System.currentTimeMillis()), host.getHostNumber(),
                    visitor.getIdNumber(),host.getRoomNumber(),"Sign In"
            );

    }*/

}
