package com.server;
import com.application.MainConfig;
import com.server.Dao.UCTDB;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes ={UCTDB.class})
class UCTDBTest {
     UCTDB uctdb ;
    @BeforeAll
    void setUp() {
        uctdb  = new UCTDB();
    }

    @Test
    @DisplayName("Test is UCTDB is not null, size greater than zer")
    void getHostList() {
        // check if not null
        assertNotNull(uctdb.getStudent());

        //Test size
        assertTrue(uctdb.getStudent().size()>0);

    }
    @Test
    @DisplayName(" Test all elements are not null")
    void elementsNotNull(){
        Assertions.assertTrue(uctdb.getStudent().stream().allMatch(
                Objects::nonNull
        ));
    }
    @Test
    @DisplayName("Fields of the not null")
    void fieldsElementNotNull(){
        Assertions.assertTrue(uctdb.getStudent().stream().allMatch(
                student->student.getStudentNumber() !=0 && student.getFullName() != null &&
                        student.getContact() != null && student.getResident() !=null
        ));
    }


}