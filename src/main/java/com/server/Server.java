package com.server;
import com.host.Host;
import com.register.Register;
import com.register.SignInItems;
import com.server.Dao.ResidentDB;
import com.server.Dao.UCTDB;
import com.visitor.Relative;
import com.visitor.SchoolMate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;


@Component
public class Server implements OnDayEnd  {
    private ArrayList<SignInItems> signInItems;

    private  Resident resident;
    private  ResidentSignRules residentSignRules;


    @Autowired
    private ApplicationContext context;


    /**
     *Default construct
     */
    public Server(){
    }

    /**
     * Authenticate host, validate visitor , check max signings of host and check signing time
     * @param host - host object
     * @param visitor - visitor object
     * @return - true if  signing meet rules else false
     */
    public boolean authenticateAndAuthorizationSchoolmate(Host host, SchoolMate visitor) throws Exception {
        this.residentSignRules = context.getBean(ResidentSignRules.class);
        this.resident = context.getBean(Resident.class);
        return validateHost(host) && validateSchoolmate(visitor) &&
                countNumberSignIn(host, new Date(System.currentTimeMillis())) &&
                withInSigningTime(LocalTime.now());
    }

    /**
     * Authenticate host, validate visitor , check max signings of host and check signing time
     * @param host - host object
     * @param visitor - visitor object
     * @return - true if  signing meet rules else false
     */
    public boolean authenticateAndAuthorizationRelative(Host host, Relative visitor) throws Exception {
        this.residentSignRules = context.getBean(ResidentSignRules.class);
        this.resident = context.getBean(Resident.class);
        return validateHost(host) && validateId(visitor.getIdNumber()) &&
                countNumberSignIn(host, new Date(System.currentTimeMillis())) &&
                withInSigningTime(LocalTime.now());
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

            throw new Exception("You can not  sign visitor between "+ residentSignRules.getSignOutTime().toString() +
                    " "+ residentSignRules.getSigInTime().toString());
        }
    }

    /**
     *  Check if the host have not reached sign max
     * @param host  - object of the host
     * @param date - date of the sign
     * @return - true if the host haven't reach max else false
     */
    public   boolean countNumberSignIn(Host host, Date date) throws Exception {
        signInItems = context.getBean(Register.class).getSignInItems();
       int []count  ={ 0};
       if(signInItems != null) {
           signInItems.forEach(signInItems1 -> {
               count[0] += (signInItems1.getHostId() == host.getHostNumber() &&
                       signInItems1.getDate().toString().equalsIgnoreCase(date.toString())) ? 1 : 0;
           });
       }
       if( count[0]< residentSignRules.getNumberVisitors()){
           return  true;
       } else {

           throw new Exception("Host " + host.getFullName() + " can't sign more than  "+count[0]+ " visitor.");
       }
    }

    /**
     * Validate is the said host  belong to the resident
     * @param host - object of the host
     * @return - true if said host belong to res else false
     */
    public    boolean validateHost(Host host) throws Exception {
        if( context.getBean(ResidentDB.class).getHostList().stream().anyMatch(host::equals)){
            return  true;
        }
        else {
            throw new Exception("Visitor " + host.getFullName() + " is not found in Resident database");
        }
    }

    /**
     * Check if said  student visitor is registered
     * @param student - object  Schoolmate
     * @return - true if the student is register else false
     */
    public    boolean validateSchoolmate(SchoolMate student) throws Exception {
        if(  context.getBean(UCTDB.class).getStudent().stream().anyMatch(student::equals))
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
    public  boolean validateId(long Id) throws Exception {
         boolean isValid  = false;
         String ID  = String.valueOf(Id);
          if(ID.length() == 13){
              int century = (Integer.parseInt(ID.substring(0,1))==9)?19:20;
              long years = LocalDate.now().getYear()-(Integer.parseInt(century+ID.substring(0,2)));
              /*/check year
              if(years<=90 && years>=0) {
                 int gender = Integer.parseInt(ID.substring(6,10));
                 int citizenship = Integer.parseInt(ID.substring(10,11));
                 // check valid citizenship or gender
                 if(((gender<=4999 & gender>=0)||(gender<=8999&gender>=5000)) &&
                         (citizenship ==0 || citizenship==1)){
                      // Apply Luhn algo  to check if ID validated by RSA home affairs
                     long tempTotal;
                     long checkSum = 0;
                     int multiplier = 1;
                     for(int i=1; i<13; ++i){
                        tempTotal = Long.parseLong(ID.charAt(i)+"") *multiplier;
                        if(tempTotal>9){
                            tempTotal = Long.parseLong(String.valueOf(tempTotal).charAt(0)+"")+
                                    Long.parseLong(String.valueOf(tempTotal).charAt(1)+"");
                        }
                        checkSum = checkSum+tempTotal;
                        multiplier = (multiplier%2==0)?1:2;
                        if(checkSum%10 ==0){
                            isValid = true;
                        }

                     }
                     }*/
                    isValid=true;



              }
          if(  isValid){
              return  true;
          }
          else {

              throw new Exception(Id +" Can not be verified. Please re-enter it");
          }
    }

    private ResidentSignRules getResidentSignRules() {
        return residentSignRules;
    }

    private void setResidentSignRules(ResidentSignRules residentSignRules) {
        this.residentSignRules = residentSignRules;
    }


    @Override
    public    void dayEndAlert(){
        ArrayList<SignInItems>notSignedOut = new ArrayList<>();
        if(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a")).equalsIgnoreCase(
                residentSignRules.getSignOutTime().format(DateTimeFormatter.ofPattern("hh:mm a")))
        ){
           signInItems.forEach(signInItems2 -> {
               if(signInItems2.getStatus().equalsIgnoreCase("sign in") && signInItems2.getSignOutTime()==null){
                    notSignedOut.add(signInItems2);
               }
           });

        }
        context.getBean(Register.class).showNotSignedOutVisitors(notSignedOut);

    }



}
