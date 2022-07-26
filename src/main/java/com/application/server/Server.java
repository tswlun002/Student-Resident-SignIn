package com.application.server;
import com.application.server.model.ResidentStudentService;
import com.application.student.model.StudentService;
import com.application.register.model.OnSignings;
import com.application.register.model.Signing;
import com.application.server.Dao.ResidentDB;
import com.application.server.Dao.School;
import com.application.visitor.model.Relative;
import com.application.visitor.model.SchoolMate;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class Server implements OnSignings {

    private List<Signing> signInItems;
    private  final ResidentStudentService residentStudentService;

    private final ResidentDB residentDB;

    private final School school;
    private  ResidentSignRules residentSignRules;



    /**
     *Default construct
     */
    public Server(ResidentStudentService residentStudentService, ResidentSignRules residentSignRules, ResidentDB residentDB, School school){
        this.residentStudentService = residentStudentService;
        this.residentSignRules=residentSignRules;
        this.residentDB=residentDB;
        this.school = school;
    }

    /**
     * Authenticate studentService, validate visitor , check max signings of studentService and check signing time
     * @param studentService - studentService object
     * @param visitor - visitor object
     * @return - true if  signing meet rules else false
     */
    public boolean authenticateAndAuthorizationSchoolmate(StudentService studentService, SchoolMate visitor) throws Exception {
        return    withInSigningTime(LocalTime.now()) & validateHost(studentService) & validateSchoolmate(visitor) &
                countNumberSignIn(studentService.getHostNumber(), new Date(System.currentTimeMillis()));
    }

    /**
     * Authenticate studentService, validate visitor , check max signings of studentService and check signing time
     * @param studentService - studentService object
     * @param visitor - visitor object
     * @return - true if  signing meet rules else false
     */
    public boolean authenticateAndAuthorizationRelative(StudentService studentService, Relative visitor) throws Exception {
        return withInSigningTime(LocalTime.now()) & validateHost(studentService) & validateId(visitor.getIdNumber()) &
                countNumberSignIn(studentService.getHostNumber(), new Date(System.currentTimeMillis())) ;
    }

    /***
     *  Check sign in is with sign in period
     * @param currentTime - time student want to sign
     * @return - true if current time is within the period of signing in the residentStudentService
     */
    public   boolean withInSigningTime(LocalTime currentTime) throws Exception {

        if(  currentTime.compareTo(residentSignRules.getSigInTime())>=0 &&
                currentTime.compareTo(residentSignRules.getSignOutTime())<=0){
            return  true;
        }
        else {

            throw new Exception("You can not  sign visitor between "+ residentSignRules.getSignOutTime().format(DateTimeFormatter.ofPattern(
                    "hh:mm a")) +
                    " - "+ residentSignRules.getSigInTime().format(DateTimeFormatter.ofPattern("hh:mm a")));
        }
    }

    /**
     *  Check if the student have not reached sign max
     * @param date - date of the sign
     * @return - true if the student haven't reach max else false
     */
    public   boolean countNumberSignIn(long hostId, Date date) throws Exception {
       int []count  ={ 0};
       if(signInItems != null) {
           signInItems.forEach(signInItems1 -> count[0] += (signInItems1.getHostId() == hostId &&
                   signInItems1.getDate().toString().equalsIgnoreCase(date.toString()) &&
                   signInItems1.getStatus().equalsIgnoreCase("sign in")) ? 1 : 0);
       }
       if( count[0]< residentSignRules.getNumberVisitors()){
           return  true;
       } else {
           throw new Exception("StudentService  can't sign more than  3 visitor.");
       }
    }

    /**
     * Validate is the said studentService  belong to the residentStudentService
     * @param studentService - object of the studentService
     * @return - true if said studentService belong to res else false
     */
    public    boolean validateHost(StudentService studentService) throws Exception {
        if( residentDB.getHostList().stream().anyMatch(studentService::equals)){
            return  true;
        }
        else {
            throw new Exception("StudentService " + studentService.getFullName() + " is not found in ResidentStudentService database");
        }
    }

    /**
     * Check if said  student visitor is registered
     * @param student - object  Schoolmate
     * @return - true if the student is register else false
     */
    public    boolean validateSchoolmate(SchoolMate student) throws Exception {
        if(  school.getStudent().stream().anyMatch(student::equals))
            return  true;
        else {
            throw new Exception("Visitor " + student.getFullName() + " is not found in UCT database");
        }
    }

    /**
     * Apply Luhn formula for check-digits
     * @param Id - RSA id number
     * @return - true if correct ID number else false
     */
    public  boolean validateId(long Id) {
         boolean isValid  = false;
         String ID  = String.valueOf(Id);
          if(ID.length() == 13) {
              int century = (Integer.parseInt(ID.substring(0, 1)) == 9) ? 19 : 20;
              long years = LocalDate.now().getYear() - (Integer.parseInt(century + ID.substring(0, 2)));
              //check year
              if (years <= 90 && years >= 0) {
                  int gender = Integer.parseInt(ID.substring(6, 10));
                  int citizenship = Integer.parseInt(ID.substring(10, 11));
                  // check valid citizenship or gender
                  if (((gender <= 4999 & gender >= 0) || (gender <= 8999 & gender >= 5000)) &&
                          (citizenship == 0 || citizenship == 1)) {
                      // Apply Luhn algo  to check if ID validated by RSA home affairs
                      long tempTotal;
                      long checkSum = 0;
                      int multiplier = 1;
                      for (int i = 1; i < 13; ++i) {
                          tempTotal = Long.parseLong(ID.charAt(i) + "") * multiplier;
                          if (tempTotal > 9) {
                              tempTotal = Long.parseLong(String.valueOf(tempTotal).charAt(0) + "") +
                                      Long.parseLong(String.valueOf(tempTotal).charAt(1) + "");
                          }
                          checkSum = checkSum + tempTotal;
                          multiplier = (multiplier % 2 == 0) ? 1 : 2;
                          if (checkSum % 10 == 0) {
                              isValid = true;
                          }

                      }
                  }
              }
          }
         return  isValid;
    }

    private ResidentSignRules getResidentSignRules() {
        return residentSignRules;
    }

    private void setResidentSignRules(ResidentSignRules residentSignRules) {
        this.residentSignRules = residentSignRules;
    }

    public List<Signing> getSignInItems() {
        return signInItems;
    }

    /**
     * @return true at the end signing period else false
     */
    public  boolean endSigningPeriod(){
        LocalTime now  = LocalTime.now();
        return  residentSignRules.getSignOutTime()==now;
    }

    /**
     * @return true at start of signing period else false
     */
    public  boolean startSigningPeriod(){
        LocalTime now  = LocalTime.now();
        return  residentSignRules.getSigInTime()==now;
    }





    /**
     * Get List of signed in or out SigningItems
     * @param signInItems - is list of sign in and out
     */
    @Override
    public void getSignedItems(List<Signing> signInItems) {
        this.signInItems = signInItems;

    }
}
