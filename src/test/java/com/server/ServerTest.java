package com.server;
import com.host.Host;
import com.register.SignInItems;
import com.server.Dao.ResidentDB;
import com.server.Dao.UCTDB;
import com.visitor.SchoolMate;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RunWith(SpringRunner.class)
@SpringBootTest( classes = {ResidentSignRules.class,Resident.class,UCTDB.class,ResidentDB.class})
class ServerTest {

     private Server server;
    @Autowired
     private ResidentSignRules residentSignRules;
    @Autowired
    private ResidentDB residentDB;
    @Autowired
    private UCTDB uctDatabase;
    @Autowired
    private Resident resident;

    @Autowired
    ApplicationContext applicationContext;

    private List<SignInItems> signInItemsList ;

    ServerTest() {
    }

    @BeforeAll
    void setUp() {
        server  = new Server(resident,residentSignRules,residentDB,uctDatabase);
        signInItemsList = new ArrayList<>();

    }

    @ParameterizedTest
    @DisplayName("Test if able to validate  correct host")
   @CsvFileSource(resources = "/CorrectHosts.csv")
    void  validateHostWithValidHosts(long id, String fullName, String contact, String roomNumber)
            throws Exception {
        Host host1 = new Host(id, fullName,contact, roomNumber);
        Assertions.assertTrue(server.validateHost(host1));
    }

    @ParameterizedTest
    @DisplayName("Throw exception for invalid host")
    @CsvFileSource(resources = "/incorrectHosts.csv")
    void  validateHostWithInvalidHosts(long id, String fullName, String contact, String roomNumber)
            throws Exception {
        Host host1 = new Host(id, fullName,contact, roomNumber);
        Assertions.assertEquals(Assertions.assertThrows(Exception.class, () -> {
            server.validateHost(host1);
        }).getMessage(), "Host " + host1.getFullName() + " is not found in Resident database");
    }
    @ParameterizedTest
    @DisplayName("Tes for valid schoolmate visitors")
    @CsvFileSource(resources = "/registeredStudents.csv")
    void validateSchoolmateWithValidSchoolMates(String fullName, String contact,long id, String resident) throws Exception {
        SchoolMate schoolMate = new SchoolMate(fullName,contact,id,resident);
        Assertions.assertTrue(server.validateSchoolmate(schoolMate));
    }

    @ParameterizedTest
    @DisplayName("Test for invalid schoolmate visitors")
    @CsvFileSource(resources = "/unregisteredStudents.csv")
    void validateSchoolmateWithInValidSchoolMates(String fullName, String contact,long id, String resident) throws Exception {
        SchoolMate schoolMate = new SchoolMate(fullName,contact,id,resident);
        Assertions.assertEquals(Assertions.assertThrows(Exception.class, ()->{
            server.validateSchoolmate(schoolMate);}).getMessage(), "Visitor " + schoolMate.getFullName() + " is not found in UCT database");
    }


    @ParameterizedTest
    @DisplayName("Test validate correct RSA ID")
    @ValueSource(longs =9802095886081L)
    void validateIdWithValid(Long id){
        Assertions.assertTrue(server.validateId(id));
    }

    @ParameterizedTest
    @DisplayName("Test invalidate  RSA ID")
    @ValueSource(longs ={3702095886081L, 2309025886081L,350298588618L} )
    void validateIdWithInvalid(long id) {
        Assertions.assertFalse(server.validateId(id));
    }

    @DisplayName("Test interface OnGetSignDetails")
    @ParameterizedTest
    @CsvFileSource(resources = "/signedVisitors.csv")
    public  void checkServerIsAbleToGetSignedVisitorDeatils(long hostId, long visitId, String room, String status){
        Date date = new Date(System.currentTimeMillis());
        Time signTime = new Time(System.currentTimeMillis());
        signInItemsList.add( new SignInItems(date,signTime,hostId,visitId,room,status));
        server.getSignDetail(signInItemsList);
        Assertions.assertTrue( !server.getSignInItems().isEmpty() && server.getSignInItems().size()<=5);
    }
    @DisplayName("Test host to sign when  visitor still less than 3")
    @ParameterizedTest
    @CsvSource(value = {"123:984736:c601c:sign in","456:1045:B605B: sign in","456:11245:B605B: sign in",
            "456:14645:B605B: sign in", "367:107565:C505C: sign in"},delimiter = ':')
    void countNumberSignInHostWithSigningLessThan3(long hostId, long visitId, String room, String status) throws Exception {
        Date date = new Date(System.currentTimeMillis());
        Assertions.assertTrue(server.countNumberSignIn(hostId, date));
    }
    @DisplayName("Test  host to sign   visitor still less than 3 visitors signed")
    @ParameterizedTest
    @CsvSource(value = {"123:984736:c601c:sign in","456:1045:B605B: sign in","456:11245:B605B: sign in",
            "456:14645:B605B: sign in", "367:107565:C505C: sign in"},delimiter = ':')
    void add(long hostId, long visitId, String room, String status) throws Exception {
        Date date = new Date(System.currentTimeMillis());
        Time signTime = new Time(System.currentTimeMillis());
        signInItemsList.add( new SignInItems(date,signTime,hostId,visitId,room,status));
        server.getSignDetail(signInItemsList);
        Assertions.assertTrue(server.getSignInItems().size()<=10 && server.getSignInItems().size()>=5);
    }
    @DisplayName("Test host to sign when  visitor still more than 3")
    @ParameterizedTest
    @CsvSource(value = {"123:984736:c601c:sign in","456:1045:B605B: sign in","456:11245:B605B: sign in",
            "456:14645:B605B: sign in", "367:107565:C505C: sign in"},delimiter = ':')
    void countNumberSignInHostWithSigningMoreThan3(long hostId, long visitId, String room, String status)
            throws Exception {

        Date date = new Date(System.currentTimeMillis());
        Time signTime = new Time(System.currentTimeMillis());
        signInItemsList.add( new SignInItems(date,signTime,hostId,visitId,room,status));
        server.getSignDetail(signInItemsList);
        Assertions.assertEquals(
                Assertions.assertThrows(Exception.class,()->{
            server.countNumberSignIn(hostId, date);}).getMessage(),"Host  can't sign more than  3 visitor.");
    }
    @DisplayName("Host signs within the period of signing ")
    @Test
    void HostWithInSigningTime() throws Exception {
        residentSignRules.setSigInTime(LocalTime.now());
        LocalTime now  = LocalTime.now();
        residentSignRules.setSignOutTime(LocalTime.now().plusHours(System.currentTimeMillis()));
        Assertions.assertTrue(server.withInSigningTime(now));
    }

    @DisplayName("Host can't sign because after allowed time to sign in ")
    @Test
    void HostAfterSigningTimeElapsed() throws Exception {
        residentSignRules.setSigInTime(LocalTime.of(6,0,0,0));
        residentSignRules.setSignOutTime(LocalTime.MAX);
        LocalTime now  = LocalTime.now();
        Assertions.assertEquals(Assertions.assertThrows(
                Exception.class,()->server.withInSigningTime(now)
        ).getMessage(),"You can not  sign visitor between "+residentSignRules.getSignOutTime().format(DateTimeFormatter.ofPattern("hh:mm a"))
        +" - "+ residentSignRules.getSigInTime().format(DateTimeFormatter.ofPattern("hh:mm a")) );
    }





}

   /*
    @Test
    void authenticateAndAuthorizationSchoolmate() {
    }
     residentSignRules.getSigInTime().format(DateTimeFormatter.ofPattern("hh:mm a"))
    @Test
    void authenticateAndAuthorizationRelative() {
    }

    @Test
    void withInSigningTime() {
    }*/





