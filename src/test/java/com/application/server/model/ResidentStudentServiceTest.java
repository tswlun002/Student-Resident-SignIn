package com.application.server.model;

import com.application.MainConfig;
import com.application.server.data.Address;
import com.application.server.model.AddressService;
import com.application.server.model.ResidenceService;
import com.application.server.model.ResidentDepartmentService;
import com.application.server.model.ResidentStudentService;
import com.application.server.repository.ResidenceRepository;
import com.application.student.model.StudentService;
import com.application.student.repostory.StudentRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {MainConfig.class, ResidenceRepository.class, ResidenceService.class, ResidentDepartmentService.class,
        ResidentStudentService.class, StudentService.class, StudentRepository.class, Address.class, AddressService.class})
class ResidentStudentServiceTest {
    @Autowired
    private ResidentStudentService residentStudentService;

    @Test
    @Disabled
    @DisplayName("Test is ResidentDB is not null, size greater than zer")
    void getHostList() {
        // check if not null
       // assertNotNull(residentDB.getHostList());

        //Test size
        //assertTrue(residentDB.getStudentList().size()>0);

    }
    @Test
    @Disabled
    @DisplayName(" Test all elements are not null")
    void elementsNotNull(){
       /* Assertions.assertTrue(residentDB.getHostList().stream().allMatch(
                Objects::nonNull
        ));*/
    }
    @Test
    @Disabled
    @DisplayName("Fields of the not null")
    void fieldsElementNotNull(){
        /*Assertions.assertTrue(residentDB.getHostList().stream().allMatch(
                host->host.getStudentNumber() !=0 && host.getFullName() != null &&
                host.getContact() != null
        ));*/
    }

    @Test
    @Disabled
    public  void testSave(){
         residentStudentService.fillResidentFeature("Forest Hill");
    }
    @Test
    public  void testSaveStudents(){
       Assertions.assertTrue(residentStudentService.placeStudentRoom("Forest Hill"));
    }


}