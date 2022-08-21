package com.application.server;
import com.application.server.model.ResidentStudentService;
import com.application.student.model.StudentService;
import com.application.register.model.Signing;
import com.application.server.Dao.ResidentDB;
import com.application.server.Dao.School;
import com.application.visitor.model.SchoolMate;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RunWith(SpringRunner.class)
@SpringBootTest( classes = {ResidentSignRules.class, ResidentStudentService.class, School.class,ResidentDB.class,
        OnSigningPeriod.class})
class ServerTest {

     private Server server;
    @Autowired
     private ResidentSignRules residentSignRules;
    @Autowired
    private ResidentDB residentDB;
    @Autowired
    private School uctDatabase;
    @Autowired
    private ResidentStudentService residentStudentService;

    private List<Signing> signingList;

    ServerTest() {
    }

    @BeforeAll
    void setUp() {
        server  = new Server(residentStudentService,residentSignRules,residentDB,uctDatabase);
        signingList = new ArrayList<>();
    }






    @ParameterizedTest
    @DisplayName("Test if able to validate  correct student")
   @CsvFileSource(resources = "/CorrectHosts.csv")
    void  validateHostWithValidHosts(long id, String fullName, String contact, String roomNumber)
            throws Exception {
        StudentService studentService1 = StudentService.builder().studentNumber(id).fullName(fullName).contact(contact).accommodation(roomNumber).build();
        Assertions.assertTrue(server.validateHost(studentService1));
    }

    @ParameterizedTest
    @DisplayName("Throw exception for invalid student")
    @CsvFileSource(resources = "/incorrectHosts.csv")
    void  validateHostWithInvalidHosts(long id, String fullName, String contact, String roomNumber) {
        StudentService studentService1 = StudentService.builder().studentNumber(id).fullName(fullName).contact(contact).accommodation(roomNumber).build();
        Assertions.assertEquals(Assertions.assertThrows(Exception.class, () ->
                server.validateHost(studentService1)).getMessage(),
                "StudentService " + studentService1.getFullName() + " is not found in ResidentStudentService database");
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
    void validateSchoolmateWithInValidSchoolMates(String fullName, String contact,long id, String resident) {
        SchoolMate schoolMate = new SchoolMate(fullName,contact,id,resident);
        Assertions.assertEquals(Assertions.assertThrows(Exception.class, ()->
                server.validateSchoolmate(schoolMate)).getMessage(),
                "Visitor " + schoolMate.getFullName() + " is not found in UCT database");
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

    @DisplayName("Test interface OnSignings")
    @ParameterizedTest
    @CsvFileSource(resources = "/signedVisitors.csv")
    public  void checkServerIsAbleToGetSignedVisitorDeatils(long hostId, long visitId, String room, String status){
        Date date = new Date(System.currentTimeMillis());
        Time signTime = new Time(System.currentTimeMillis());
        signingList.add( new Signing(date,signTime,hostId,visitId,room,status));
        server.getSignedItems(signingList);
        Assertions.assertTrue( !server.getSignInItems().isEmpty() && server.getSignInItems().size()<=5);
    }
    @DisplayName("Test student to sign when  visitor still less than 3")
    @ParameterizedTest
    @CsvSource(value = {"123:984736:c601c:sign in","456:1045:B605B: sign in","456:11245:B605B: sign in",
            "456:14645:B605B: sign in", "367:107565:C505C: sign in"},delimiter = ':')
    void countNumberSignInHostWithSigningLessThan3(long hostId, long visitId, String room, String status) throws Exception {
        Date date = new Date(System.currentTimeMillis());
        Assertions.assertTrue(server.countNumberSignIn(hostId, date));
    }
    @DisplayName("Test  student to sign   visitor still less than 3 visitors signed")
    @ParameterizedTest
    @CsvSource(value = {"123:984736:c601c:sign in","456:1045:B605B: sign in","456:11245:B605B: sign in",
            "456:14645:B605B: sign in", "367:107565:C505C: sign in"},delimiter = ':')
    void add(long hostId, long visitId, String room, String status) {
        Date date = new Date(System.currentTimeMillis());
        Time signTime = new Time(System.currentTimeMillis());
        signingList.add( new Signing(date,signTime,hostId,visitId,room,status));
        server.getSignedItems(signingList);
        Assertions.assertTrue(server.getSignInItems().size()<=10 && server.getSignInItems().size()>=5);
    }
    @DisplayName("Test student to sign when  visitor still more than 3")
    @ParameterizedTest
    @CsvSource(value = {"123:984736:c601c:sign in","456:1045:B605B: sign in","456:11245:B605B: sign in",
            "456:14645:B605B: sign in", "367:107565:C505C: sign in"},delimiter = ':')
    void countNumberSignInHostWithSigningMoreThan3(long hostId, long visitId, String room, String status) {

        Date date = new Date(System.currentTimeMillis());
        Time signTime = new Time(System.currentTimeMillis());
        signingList.add( new Signing(date,signTime,hostId,visitId,room,status));
        server.getSignedItems(signingList);
        Assertions.assertEquals(
                Assertions.assertThrows(Exception.class,()->
                        server.countNumberSignIn(hostId, date)).getMessage(),
                "StudentService  can't sign more than  3 visitor.");
    }
    @DisplayName("StudentService signs within the period of signing ")
    @Test
    void HostWithInSigningTime() throws Exception {
        LocalTime now  = LocalTime.parse("12:00:00",DateTimeFormatter.ISO_LOCAL_TIME);
        Assertions.assertTrue(server.withInSigningTime(now));
    }

    @DisplayName("StudentService can't sign because after allowed time to sign in ")
    @Test
    void HostAfterSigningTimeElapsed() {

        LocalTime now  = LocalTime.parse("01:00:00",DateTimeFormatter.ISO_LOCAL_TIME);
        Assertions.assertEquals(Assertions.assertThrows(
                Exception.class,()->server.withInSigningTime(now)
        ).getMessage(),"You can not  sign visitor between "+residentSignRules.getSignOutTime().format(DateTimeFormatter.ofPattern("hh:mm a"))
        +" - "+ residentSignRules.getSigInTime().format(DateTimeFormatter.ofPattern("hh:mm a")) );
    }
   /*
    @DisplayName("Test capture existing arrayList")
    @Test
    public  void checkServerAlertWhenSigningPeriodElapses(){
        server.endSigningPeriodAlert(new Register());
        //onHostNotSignedOut.showNotSignedOutVisitors((ArrayList<Signing>) signingList);
        Mockito.verify(onHostNotSignedOut).showNotSignedOutVisitors((ArrayList<Signing>) argCaptor.capture());
        Assertions.assertEquals(signingList,argCaptor.getValue());
    }*/







}

   /*
    @Test
    void authenticateAndAuthorizationSchoolmate() {
    }
     residentSignRules.getSigInTime().format(DateTimeFormatter.ofPattern("hh:mm a"))
    @Test
    void authenticateAndAuthorizationRelative() {
    }
    */





