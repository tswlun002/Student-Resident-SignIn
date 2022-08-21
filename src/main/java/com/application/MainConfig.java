package com.application;
import com.application.server.model.ResidentDepartmentService;
import com.application.server.model.ResidenceService;
import com.application.server.model.ResidentStudentService;
import com.application.student.model.StudentService;
import com.application.register.model.Register;
import com.application.register.model.Signing;
import com.application.register.model.SignVisitor;
import com.application.server.Dao.School;
import com.application.server.OnHostNotSignedOut;
import com.application.server.ResidentSignRules;
import com.application.server.Server;
import com.application.server.Dao.ResidentDB;
import com.application.visitor.model.Relative;
import com.application.visitor.model.SchoolMate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.ArrayList;
@Configuration
@EnableAutoConfiguration
/*@EnableJpaRepositories(basePackages="com.application")
@EntityScan(basePackages = {"com.application"})
@ComponentScan(basePackages = "com.application")*/
public class MainConfig {

/*    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder.setType(EmbeddedDatabaseType.HSQL).build();
    }
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        LocalContainerEntityManagerFactoryBean factory = new
                LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.application");
        factory.setDataSource(dataSource());
        return factory;
    }
    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory((EntityManagerFactory) entityManagerFactory());
        return txManager;
    }*/
    public School uctdb(){
        return  new School();
    }
    public ResidentDB residentDB (){
        return  new ResidentDB();
    }

    @Qualifier("Residence")
    public ResidenceService residenceService(){
        return  ResidenceService.builder().build();
    }
    @Qualifier("defaultedResident")
    public ResidentStudentService resident(){
        return  ResidentStudentService.builder().build();
    }
    @Qualifier("defaultResRules")
    public ResidentSignRules residentSignRules(){
        return  new ResidentSignRules();
    }
     @Qualifier("serverObj")
    public Server server(@Qualifier("defaultedResident") ResidentStudentService residentStudentService,
                         @Qualifier("defaultResRules") ResidentSignRules residentSignRules,
                         ResidentDB residentDB , School school){
         System.out.println(" Hello **********************************************************");
        return  new Server(residentStudentService,residentSignRules,residentDB, school);
    }

    @Qualifier ("defaultedParamHost")
    public StudentService host(){
        return StudentService.builder().build();
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


    public SignVisitor signVisitor(@Qualifier ("defaultedParamHost") StudentService studentService,
                                   @Qualifier ("defaultedParamRelative") Relative relative,
                                   @Qualifier ("defaultedParamSchoolmate") SchoolMate schoolMate,
                                   @Qualifier ("defaultRegister") Register register) throws Throwable {
        return  new SignVisitor(studentService,schoolMate,relative,register);
    }

    public ResidentDepartmentService schoolService(){
        return ResidentDepartmentService.builder().build();
    }
    @Qualifier("onHostNotSignedOut")
    public OnHostNotSignedOut onHostNotSignedOut(@Qualifier ("defaultRegister") Register theRegister){
        return  theRegister != null ? theRegister :
                new OnHostNotSignedOut() {
                    @Override
                    public void showNotSignedOutVisitors(ArrayList<Signing> signInItems) {

                    }
                };
    }



}
