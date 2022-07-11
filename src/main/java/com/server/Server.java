package com.server;
import com.host.Host;
import com.register.OnSignings;
import com.register.Signing;
import com.server.Dao.ResidentDB;
import com.server.Dao.UCTDB;
import com.visitor.Relative;
import com.visitor.SchoolMate;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class Server implements OnSignings {

    private List<Signing> signInItems;
    private  final Resident resident;

    private final ResidentDB residentDB;

    private final UCTDB uctdb;
    private  ResidentSignRules residentSignRules;



    /**
     *Default construct
     */
    public Server(Resident resident, ResidentSignRules residentSignRules, ResidentDB residentDB, UCTDB uctdb){
        this.resident =resident;
        this.residentSignRules=residentSignRules;
        this.residentDB=residentDB;
        this.uctdb =uctdb;
    }

    /**
     * Authenticate host, validate visitor , check max signings of host and check signing time
     * @param host - host object
     * @param visitor - visitor object
     * @return - true if  signing meet rules else false
     */
    public boolean authenticateAndAuthorizationSchoolmate(Host host, SchoolMate visitor) throws Exception {
        return    withInSigningTime(LocalTime.now()) & validateHost(host) & validateSchoolmate(visitor) &
                countNumberSignIn(host.getHostNumber(), new Date(System.currentTimeMillis()));
    }

    /**
     * Authenticate host, validate visitor , check max signings of host and check signing time
     * @param host - host object
     * @param visitor - visitor object
     * @return - true if  signing meet rules else false
     */
    public boolean authenticateAndAuthorizationRelative(Host host, Relative visitor) throws Exception {
        return withInSigningTime(LocalTime.now()) & validateHost(host) & validateId(visitor.getIdNumber()) &
                countNumberSignIn(host.getHostNumber(), new Date(System.currentTimeMillis())) ;
    }

    /***
     *  Check sign in is with sign in period
     * @param currentTime - time host want to sign
     * @return - true if current time is within the period of signing in the resident
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
     *  Check if the host have not reached sign max
     * @param date - date of the sign
     * @return - true if the host haven't reach max else false
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
           throw new Exception("Host  can't sign more than  3 visitor.");
       }
    }

    /**
     * Validate is the said host  belong to the resident
     * @param host - object of the host
     * @return - true if said host belong to res else false
     */
    public    boolean validateHost(Host host) throws Exception {
        if( residentDB.getHostList().stream().anyMatch(host::equals)){
            return  true;
        }
        else {
            throw new Exception("Host " + host.getFullName() + " is not found in Resident database");
        }
    }

    /**
     * Check if said  student visitor is registered
     * @param student - object  Schoolmate
     * @return - true if the student is register else false
     */
    public    boolean validateSchoolmate(SchoolMate student) throws Exception {
        if(  uctdb.getStudent().stream().anyMatch(student::equals))
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
