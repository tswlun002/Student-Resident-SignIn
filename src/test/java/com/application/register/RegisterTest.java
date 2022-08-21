package com.application.register;
import com.application.server.model.ResidentStudentService;
import com.application.student.model.StudentService;
import com.application.register.model.Register;
import com.application.register.model.Signing;
import com.application.server.Dao.ResidentDB;
import com.application.server.Dao.School;
import com.application.server.OnSigningPeriod;
import com.application.server.ResidentSignRules;
import com.application.server.Server;
import com.application.visitor.model.Address;
import com.application.visitor.model.Relative;
import com.application.visitor.model.SchoolMate;
import com.application.visitor.model.Visitor;

import java.time.LocalTime;
import java.util.ArrayList;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;;
import java.sql.Date;
import java.sql.Time;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Register.class, Server.class, StudentService.class, Relative.class, SchoolMate.class,
        Visitor.class, ResidentSignRules.class, ResidentStudentService.class, School.class, ResidentDB.class,
        OnSigningPeriod.class})
class RegisterTest {
       @Autowired
      private  Register register;
     @MockBean
     private Server server ;
     @BeforeAll
    void setUp() {
         doNothing().when(server).getSignedItems(List.of(new Signing[]{}));

    }
    @BeforeEach
    void clearStoreItems() throws Exception {

         register.clearItems();
    }


    @DisplayName("Not Signed  yet")

    @Test
    void NotAlreadySigned(){
        Signing my = new Signing(new Date(System.currentTimeMillis()),
                new Time(System.currentTimeMillis()), 0,0,
                "c608c","sign in");
          assertFalse(register.alreadySignedVisitor(my));
    }
    @DisplayName("Signed  already signed")
    @Test
    void alreadySigned(){
        Signing my = new Signing(new Date(System.currentTimeMillis()),
                new Time(System.currentTimeMillis()), 1,7,
                "c608c","sign in");
         ArrayList<Signing> signInItems = new ArrayList<>();
         signInItems.add(my);
        register.setSignInItems(signInItems);
        assertTrue(register.alreadySignedVisitor(my));
    }

    @DisplayName("Authorized signing and   Not signed yet schoolmte")
    @Test
    void setSignInItemWithValidSchoolMateSignInItem() throws Exception {

        StudentService host = StudentService.builder().studentNumber(23).fullName("Lunga").contact("09877").accommodation("c601c").build();
        SchoolMate schoolMate = new SchoolMate("zusakhe", "0927272",1034,"roscommon");
        Mockito.when(server.authenticateAndAuthorizationSchoolmate(host,schoolMate)).thenReturn(true);
        Signing my = new Signing(new Date(System.currentTimeMillis()),
                new Time(System.currentTimeMillis()), host.getHostNumber(),schoolMate.getStudentNumber(),
                host.getAccommodation(),"sign in");
        Assertions.assertTrue(register.setSignInItem(host, schoolMate,my));
        Assertions.assertEquals(1, register.getSignInItems().size());
    }


    @DisplayName("Test when register store incorrect or unauthorised signing schoolmate   items")
    @Test
    void setSignInItemWithInValidSchoolMateSignInItem() throws Exception {

        StudentService host = StudentService.builder().studentNumber(234).fullName("Lunga").contact("0997").accommodation("18701c").build();
        SchoolMate schoolMate = new SchoolMate("zusakhe", "0927272",1034,"roscommon");
        Mockito.when(server.authenticateAndAuthorizationSchoolmate(host,schoolMate)).thenReturn(false);
        Signing my = new Signing(new Date(System.currentTimeMillis()),
                new Time(System.currentTimeMillis()), host.getHostNumber(),schoolMate.getStudentNumber(),
                host.getAccommodation(),"sign in");
        Assertions.assertFalse(register.setSignInItem(host, schoolMate,my));
        Assertions.assertEquals(0, register.getSignInItems().size());

    }

    @DisplayName("Test when register store correct relative signing in items")
    @Test
    void setSignInItemWithValidRelativeSignInItem() throws Exception {

        StudentService host = StudentService.builder().studentNumber(234).fullName("Lunga").contact("097678").accommodation("c609c").build();
        Relative relative  = new Relative("Hehle","09272",9902095886081L,
                new Address("1234","Lower", 7760,"Cape Town"));
        Mockito.when(server.authenticateAndAuthorizationRelative(host,relative)).thenReturn(true);
        Signing my = new Signing(new Date(System.currentTimeMillis()),
                new Time(System.currentTimeMillis()), host.getHostNumber(),relative.getIdNumber(),
                host.getAccommodation(),"sign in");
        Assertions.assertTrue(register.setSignInItem(host, relative,my));
        Assertions.assertEquals(1, register.getSignInItems().size());
    }

    @DisplayName("Test when register store incorrect or unauthorised  signing student and relative  items")
    @Test
    void setSignInItemWithInValidRelativeSignInItem() throws Exception {
        StudentService host = StudentService.builder().studentNumber(23345).fullName("Zola").contact("09877").accommodation("c609c").build();
        Relative relative  = new Relative("Hehle","09272",9902095886081L,
                new Address("1234","Lower", 7760,"Cape Town"));
        Mockito.when(server.authenticateAndAuthorizationRelative(host,relative)).thenReturn(false);
        Signing my = new Signing(new Date(System.currentTimeMillis()),
                new Time(System.currentTimeMillis()), host.getHostNumber(),relative.getIdNumber(),
                host.getAccommodation(),"sign in");
        Assertions.assertFalse(register.setSignInItem(host, relative,my));
        Assertions.assertEquals(0, register.getSignInItems().size());

    }

    @DisplayName("Test show sign ins by checking size array of signed items")
    @Test
    void showAllVisitors() throws Exception {

         //When we add two , we will have size of
        StudentService host = StudentService.builder().studentNumber(2538044).fullName("Anda").contact("09877").accommodation("c609c").build();
        Relative relative  = new Relative("Hle","09272",9863,
                new Address("1234","Lower", 7760,"Cape Town"));


        StudentService host1 = StudentService.builder().studentNumber(4732520).fullName("Anda").contact("09877").accommodation("c609c").build();
        Relative relative1  = new Relative("Hle","09272",63937483,
                new Address("1234","Lower", 7760,"Cape Town"));
        Mockito.when(server.authenticateAndAuthorizationRelative(host,relative)).thenReturn(true);
        Signing my = new Signing(new Date(System.currentTimeMillis()),
                new Time(System.currentTimeMillis()), host.getHostNumber(),relative.getIdNumber(),
                host.getAccommodation(),"sign in");
        Signing my1 = new Signing(new Date(System.currentTimeMillis()),
                new Time(System.currentTimeMillis()), host1.getHostNumber(),relative1.getIdNumber(),
                host1.getAccommodation(),"sign in");
        register.setSignInItem(host, relative,my);
        register.setSignInItem(host, relative,my1);
        Assertions.assertEquals(2,register.getSignInItems().size());
        Assertions.assertTrue(

                register.getSignInItems().stream().allMatch(
                item->item.getDate() !=null & item.getSignInTime() !=null & item.getSignOutTime() == null&
                        item.getHostId() !=0 & item.getVisitorId() !=0 & item.getStatus() != null
        ));
    }

    @DisplayName("Sign Out signed visitor with correct details.")
    @Test
    public   void trySignOutSigned() throws Exception {
        //1

        StudentService host = StudentService.builder().studentNumber(2538044).fullName("Anda").contact("09877").accommodation("c609c").build();

        Relative relative  = new Relative("Hle","09272",9863,
                new Address("1234","Lower", 7760,"Cape Town"));
        Signing my = new Signing(new Date(System.currentTimeMillis()),
                new Time(System.currentTimeMillis()), host.getHostNumber(),relative.getIdNumber(),
                host.getAccommodation(),"sign in");
        Mockito.when(server.authenticateAndAuthorizationRelative(host,relative)).thenReturn(true);

        //2

        StudentService host1 = StudentService.builder().studentNumber(4732520).fullName("Anda").contact("09877").accommodation("c609c").build();
        Relative relative1  = new Relative("Hle","09272",63937483,
                new Address("1234","Lower", 7760,"Cape Town"));
        Signing my1 = new Signing(new Date(System.currentTimeMillis()),
                new Time(System.currentTimeMillis()), host1.getHostNumber(),relative1.getIdNumber(),
                host1.getAccommodation(),"sign in");

        Mockito.when(server.authenticateAndAuthorizationRelative(host1,relative1)).thenReturn(true);
        register.setSignInItem(host, relative,my);
        register.setSignInItem(host, relative,my1);

        Mockito.when(server.withInSigningTime( LocalTime.now())).thenReturn(true);
        Assertions.assertTrue(
                register.signingOutVisitor(my.getHostId(), my.getVisitorId(),new Date(System.currentTimeMillis())));
    }

    @DisplayName("Sign Out signed visitor with incorrect details.")
    @Test
    public   void trySignOutWithIncorrectSignedVisitors() throws Exception {
        StudentService host = StudentService.builder().studentNumber(2538044).fullName("Anda").contact("09877").accommodation("c609c").build();
        StudentService host1 = StudentService.builder().studentNumber(4732520).fullName("Anda").contact("09877").accommodation("c609c").build();
        Relative relative  = new Relative("Hle","09272",9863,
                new Address("1234","Lower", 7760,"Cape Town"));
        Mockito.when(server.authenticateAndAuthorizationRelative(host,relative)).thenReturn(true);

        Signing my = new Signing(new Date(System.currentTimeMillis()),
                new Time(System.currentTimeMillis()), host.getHostNumber(),relative.getIdNumber(),
                host.getAccommodation(),"sign in");



        register.setSignInItem(host, relative,my);

        Relative relative1  = new Relative("Hle","09272",63937483,
                new Address("1234","Lower", 7760,"Cape Town"));

        Mockito.when(server.withInSigningTime(LocalTime.now())).thenReturn(true);
       Assertions.assertFalse(
               register.signingOutVisitor(host1.getHostNumber(),relative1.getIdNumber(),new Date(System.currentTimeMillis())));
    }

    @DisplayName("Sign Out signed visitor with no previous sign ins.")
    @Test
    public   void trySignOutNotSignedVisitors() throws Exception {

        StudentService host = StudentService.builder().studentNumber(2538044).fullName("Anda").contact("09877").accommodation("c609c").build();
        StudentService host1 = StudentService.builder().studentNumber(4732520).fullName("Anda").contact("09877").accommodation("c609c").build();
        Relative relative1  = new Relative("Hle","09272",63937483,
                new Address("1234","Lower", 7760,"Cape Town"));

        Mockito.when(server.withInSigningTime(LocalTime.now())).thenReturn(true);
        Assertions.assertFalse(
                register.signingOutVisitor(host1.getHostNumber(),relative1.getIdNumber(),new Date(System.currentTimeMillis())));
    }

    @DisplayName("Sign Out signed visitor with  sign ins and check status.")
    @Test
    public   void trySignOutSignedVisitorsAndCehckStatus() throws Exception {

        //1
        StudentService host = StudentService.builder().studentNumber(2538044).fullName("Anda").contact("09877").accommodation("c609c").build();
        StudentService host1 = StudentService.builder().studentNumber(4732520).fullName("Anda").contact("09877").accommodation("c609c").build();
        Relative relative  = new Relative("Hle","09272",9863,
                new Address("1234","Lower", 7760,"Cape Town"));
        Signing my = new Signing(new Date(System.currentTimeMillis()),
                new Time(System.currentTimeMillis()), host.getHostNumber(),relative.getIdNumber(),
                host.getAccommodation(),"sign in");
        Mockito.when(server.authenticateAndAuthorizationRelative(host,relative)).thenReturn(true);

        //2

        Relative relative1  = new Relative("Hle","09272",63937483,
                new Address("1234","Lower", 7760,"Cape Town"));
        Signing my1 = new Signing(new Date(System.currentTimeMillis()),
                new Time(System.currentTimeMillis()), host1.getHostNumber(),relative1.getIdNumber(),
                host1.getAccommodation(),"sign in");

        Mockito.when(server.authenticateAndAuthorizationRelative(host1,relative1)).thenReturn(true);
        register.setSignInItem(host, relative,my);
        register.setSignInItem(host, relative,my1);

        Mockito.when(server.withInSigningTime( LocalTime.now())).thenReturn(true);
        Assertions.assertTrue(
                register.signingOutVisitor(my.getHostId(), my.getVisitorId(),new Date(System.currentTimeMillis())));
        Assertions.assertTrue(
                register.signingOutVisitor(my1.getHostId(), my1.getVisitorId(),new Date(System.currentTimeMillis())));
        Assertions.assertEquals(2, register.getSignInItems().size());
        Assertions.assertTrue(register.getSignInItems().stream().allMatch(
                item->item.getStatus().equalsIgnoreCase("sign out")));
        Assertions.assertTrue(register.getSignInItems().stream().allMatch(
                item->item.getSignOutTime() !=null));
  }

  @Test
    public  void  notSignedOut() throws Exception {
      //1
      StudentService host = StudentService.builder().studentNumber(2538044).fullName("Anda").contact("09877").accommodation("c609c").build();
      StudentService host1 = StudentService.builder().studentNumber(4732520).fullName("Anda").contact("09877").accommodation("c609c").build();

      StudentService host2 = StudentService.builder().studentNumber(4732520).fullName("Anda").contact("09877").accommodation("c709c").build();
      Relative relative  = new Relative("Hle","09272",9863,
              new Address("1234","Lower", 7760,"Cape Town"));
      Signing my = new Signing(new Date(System.currentTimeMillis()),
              new Time(System.currentTimeMillis()), host.getHostNumber(),relative.getIdNumber(),
              host.getAccommodation(),"sign in");
      Mockito.when(server.authenticateAndAuthorizationRelative(host,relative)).thenReturn(true);

      //2

      Relative relative1  = new Relative("Hle","09272",63937483,
              new Address("1234","Lower", 7760,"Cape Town"));
      Signing my1 = new Signing(new Date(System.currentTimeMillis()),
              new Time(System.currentTimeMillis()), host1.getHostNumber(),relative1.getIdNumber(),
              host1.getAccommodation(),"sign in");
      //3

      Relative relative2  = new Relative("Hle","09272",63583637483L,
              new Address("1234","Lower", 7760,"Cape Town"));
      Signing my2 = new Signing(new Date(System.currentTimeMillis()),
              new Time(System.currentTimeMillis()), host2.getHostNumber(),relative2.getIdNumber(),
              host2.getAccommodation(),"sign in");

      Mockito.when(server.authenticateAndAuthorizationRelative(host1,relative1)).thenReturn(true);
      register.setSignInItem(host, relative,my1);
      register.setSignInItem(host, relative,my);
      register.setSignInItem(host, relative,my2);


      Mockito.when(server.withInSigningTime( LocalTime.now())).thenReturn(true);
         register.showAllVisitors();
  }

}