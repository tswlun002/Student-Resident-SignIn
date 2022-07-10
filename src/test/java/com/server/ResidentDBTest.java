package com.server;

import com.server.Dao.ResidentDB;
import com.server.Dao.UCTDB;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes ={ResidentDB.class})
class ResidentDBTest {
    ResidentDB residentDB;
    @BeforeAll
    public  void setUp(){
        residentDB = new ResidentDB();
    }

    @Test
    @DisplayName("Test is ResidentDB is not null, size greater than zer")
    void getHostList() {
        // check if not null
        assertNotNull(residentDB.getHostList());

        //Test size
        assertTrue(residentDB.getHostList().size()>0);

    }
    @Test
    @DisplayName(" Test all elements are not null")
    void elementsNotNull(){
        Assertions.assertTrue(residentDB.getHostList().stream().allMatch(
                Objects::nonNull
        ));
    }
    @Test
    @DisplayName("Fields of the not null")
    void fieldsElementNotNull(){
        Assertions.assertTrue(residentDB.getHostList().stream().allMatch(
                host->host.getHostNumber() !=0 && host.getFullName() != null &&
                host.getContact() != null && host.getRoomNumber() !=null
        ));
    }


}