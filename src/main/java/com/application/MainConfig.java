package com.application;
import com.host.Host;
import com.register.Register;
import com.register.SignInItems;
import com.register.SignVisitor;
import com.server.*;
import com.server.Dao.ResidentDB;
import com.server.Dao.UCTDB;
import com.visitor.Relative;
import com.visitor.SchoolMate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import java.util.ArrayList;

@Configuration
@ComponentScan(basePackages = {"com.host","com.visitor", "com.register", "com.server"})
public class MainConfig {
    public UCTDB uctdb(){
        return  new UCTDB();
    }
    public ResidentDB residentDB (){
        return  new ResidentDB();
    }
    @Qualifier("defaultedResident")

    public Resident resident(){
        return  new Resident();
    }
    @Qualifier("defaultResRules")
    public ResidentSignRules residentSignRules(){
        return  new ResidentSignRules();
    }
     @Qualifier("serverObj")
    public Server server(@Qualifier("defaultedResident")  Resident resident,
                         @Qualifier("defaultResRules") ResidentSignRules residentSignRules,
                          ResidentDB residentDB ,UCTDB uctdb){
         System.out.println(" Hello **********************************************************");
        return  new Server(resident,residentSignRules,residentDB,uctdb);
    }

    @Qualifier ("defaultedParamHost")
    public Host host(){
        return new Host();
    }
    @Qualifier ("defaultedParamRelative")

    public Relative relative(){
        return new Relative();
    }
    @Qualifier ("defaultedParamSchoolmate")

    public SchoolMate schoolMate(){

        return  new SchoolMate();
    }
    @Qualifier ("defaultRegister")

    public Register register() {
        return  new Register();
    }


    public SignVisitor signVisitor(@Qualifier ("defaultedParamHost") Host host,
                                   @Qualifier ("defaultedParamRelative") Relative relative,
                                   @Qualifier ("defaultedParamSchoolmate") SchoolMate schoolMate,
                                   @Qualifier ("defaultRegister") Register register) throws Throwable {
        return  new SignVisitor(host,schoolMate,relative,register);
    }
   /*
    @Qualifier("onSigningPeriodInServer")
    @Bean
    public OnSigningPeriod onSigningPeriod(  @Qualifier("serverObj") Server server){
        return  server;
    }*/

    @Qualifier("onHostNotSignedOut")
    public OnHostNotSignedOut onHostNotSignedOut(  @Qualifier ("defaultRegister") Register theRegister){
        return  theRegister != null ? theRegister :
                new OnHostNotSignedOut() {
                    @Override
                    public void showNotSignedOutVisitors(ArrayList<SignInItems> signInItems) {

                    }
                };
    }



}
