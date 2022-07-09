package com.main;
import com.host.Host;
import com.register.Register;
import com.register.SignVisitor;
import com.server.Dao.OnUCTDB;
import com.server.Dao.ResidentDB;
import com.server.Dao.UCTDB;
import com.server.OnDayEnd;
import com.server.Resident;
import com.server.ResidentSignRules;
import com.server.Server;
import com.visitor.Address;
import com.visitor.Relative;
import com.visitor.SchoolMate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
@Configuration
@ComponentScan(basePackages = {"com.host","com.visitor", "com.register", "com.Server"})
class MainConfig {
    @Bean
    public UCTDB uctdb(){
        return  new UCTDB();
    }
    @Bean
    public ResidentDB residentDB (){
        return  new ResidentDB();
    }
    @Qualifier("defaultedResident")
    @Bean
    public Resident resident(){
        return  new Resident();
    }
    @Qualifier("defaultResRules")
    @Bean
    public ResidentSignRules residentSignRules(){
        return  new ResidentSignRules();
    }
     @Qualifier("server")
    @Bean
    public Server server(){
        return  new Server();
    }

    @Qualifier ("defaultedParamHost")
    @Bean
    public Host host(){
        return new Host();
    }
    @Qualifier ("defaultedParamRelative")
    @Bean
    public Relative relative(){
        return new Relative();
    }
    @Qualifier ("defaultedParamSchoolmate")
    @Bean
    public SchoolMate schoolMate(){

        return  new SchoolMate();
    }
    @Qualifier ("defaultRegister")
    @Bean
    public Register register(@Qualifier("server") Server serverObj) {
        return  new Register(serverObj);
    }

    @Bean
    public SignVisitor signVisitor(@Qualifier ("defaultedParamHost") Host host,
                                   @Qualifier ("defaultedParamRelative") Relative relative,
                                   @Qualifier ("defaultedParamSchoolmate") SchoolMate schoolMate,
                                   @Qualifier ("defaultRegister") Register register) throws Throwable {
        return  new SignVisitor(host,schoolMate,relative,register);
    }



}
