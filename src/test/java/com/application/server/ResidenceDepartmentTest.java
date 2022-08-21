package com.application.server;
import com.application.server.Dao.School;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes ={School.class})
class ResidenceDepartmentTest {
     School school;
    @BeforeAll
    void setUp() {
        school = new School();
    }

    @Test
    @DisplayName("Test is ResidenceDepartment is not null, size greater than zer")
    void getHostList() {
        // check if not null
        assertNotNull(school.getStudent());

        //Test size
        assertTrue(school.getStudent().size()>0);

    }
    @Test
    @DisplayName(" Test all elements are not null")
    void elementsNotNull(){
        Assertions.assertTrue(school.getStudent().stream().allMatch(
                Objects::nonNull
        ));
    }
    @Test
    @DisplayName("Fields of the not null")
    void fieldsElementNotNull(){
        Assertions.assertTrue(school.getStudent().stream().allMatch(
                student->student.getStudentNumber() !=0 && student.getFullName() != null &&
                        student.getContact() != null && student.getResident() !=null
        ));
    }


}