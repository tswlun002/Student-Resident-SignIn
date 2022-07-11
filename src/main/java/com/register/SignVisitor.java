package com.register;
import com.host.Host;
import com.visitor.Address;
import com.visitor.Relative;
import com.visitor.SchoolMate;
import com.visitor.Visitor;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Scanner;

@Service
public class SignVisitor {
    private  long hostId;
    private  long visitorId;
    private  String hostName;
    private  String visitName;
    private  String hostContact;
    private  String visitorContact;
    private  String roomNumber;
    private String residentVisitor;
    private     Address address ;
    private  Visitor visitor;

    public  Visitor getVisitor(){
        return  visitor;
    }
    public Address getAddress() {
        return address;
    }

    public long getHostId() {
        return hostId;
    }

    public long getVisitorId() {
        return visitorId;
    }

    public String getHostName() {
        return hostName;
    }

    public String getVisitName() {
        return visitName;
    }

    public String getHostContact() {
        return hostContact;
    }

    public String getVisitorContact() {
        return visitorContact;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getResidentVisitor() {
        return residentVisitor;
    }

    /***
     * Construct to Start sign process
     * @param host - Host object
     * @param schoolMate - SchoolMate object
     * @param relative - relative Host
     * @param register - Register Object
     * @throws Throwable - throw when create visitor that not Relative or SchoolMate type
     */
    public  SignVisitor(Host host, SchoolMate schoolMate, Relative relative,Register register) throws Throwable {
        Scanner keyboard = new Scanner(System.in);
        while (true) {
            System.out.println("""
                    ///////////////////////////////////////////////////////////////////////////
                    			             SIGN IN OR OUT VISITOR                          \s
                    //////////////////////////////////////////////////////////////////////////""");

            //prompt sign
            System.out.println("Enter IN to sign in or OUT  to sign out or Quit to quit");
            String signingType = keyboard.nextLine();
            if (signingType.equalsIgnoreCase("in")) {
                boolean siginedIn = signIn(keyboard, register, host, schoolMate,relative);
                if (siginedIn) {
                    register.showAllVisitors();
                    System.out.println("Successful signed in");
                }
                else System.out.println("Can't sign in");
            } else if (signingType.equalsIgnoreCase("out")) {
                boolean signedOut = signOut(keyboard, register);
                if (signedOut) System.out.println("Successful signed out");
                else System.out.println("Can't find match , you can't sign out ");

            } else if (signingType.equalsIgnoreCase("quit")) break;
            final int[] count = {0};
            System.out.println("------------------Signed Visitors-------------------------");
            register.getSignInItems().forEach(signInItems -> {
                System.out.format("Item %d :%s\n", count[0], signInItems.toString());
                count[0] += 1;
            });

        }

    }

    /**
     * Allow user enter integer type id or student number only
     * @param message  - prompt message
     * @param keyboard - scanner object
     */
    void idValidation(String message, Scanner keyboard){
            System.out.println(message);
            while (!keyboard.hasNextLong()){
                keyboard.nextLine();
                System.out.println("Error !!!\n"+message);
            }
    }
    void validateID(Scanner keyboard, long id){
        while (!validateId(id)){
            System.out.println("Enter correct visitor Id number");
            keyboard.nextLine();
        }
    }

    public  boolean validateId(long Id) {
        boolean isValid  = false;
        String ID  = String.valueOf(Id);
        if(ID.length() == 13){
            int century = (Integer.parseInt(ID.substring(0,1))==9)?19:20;
            long years = LocalDate.now().getYear()-(Integer.parseInt(century + ID.substring(0,2)));
            //check year
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
                    //isValid=true;

                }

            }
        }


        if(  isValid){
            return  true;
        }
        else {

            return  false;
        }
    }

    /***
     *  Checks if  address entered has home number (street number), suburb, postcode and city
     * @param message -  is the prompt message (instruction)
     * @param keyboard - Scanner object
     * @return  List details of the address
     */
    String[] addressValidation(String message, Scanner keyboard){
        System.out.println(message);
        String line ="";
        while (true){
              line  = keyboard.nextLine();
            if (line.split(",").length == 4){
                 line.split(",");
                 break;
            }
            else System.out.println("Error !!!\n"+message);
        }
        return  line.split(",");
    }

    /***
     * Take details of the  Host and Visitor
     * @param keyboard - Scanner object for prompting user to enter details
     * @param register - Register  object
     * @param host - Host object
     * @param schoolMate - Schoolmate object
     * @param relative  - Relative object
     * @return true if Host successful signed in visitor else false
     * @throws Throwable  - when try to sign visitor which not Relative type or SchoolMate type
     */
    public boolean  signIn(Scanner keyboard, Register register, Host host,SchoolMate schoolMate,Relative relative) throws Throwable {

        // Host details
        idValidation("Enter  your student number (integer)", keyboard);
        this.hostId = Long.parseLong(keyboard.nextLine().trim());
        System.out.println("Enter Enter your fullName");
        this.hostName = keyboard.nextLine();
        System.out.println("Enter Enter your contact");
        this.hostContact = keyboard.nextLine();
        System.out.println("Enter Enter your roomNumber (block flat room)");
        this.roomNumber = keyboard.nextLine();
        System.out.println("Enter UCT if visitor is a student else relative");
        String visitorType = keyboard.nextLine();

        if (visitorType.equalsIgnoreCase("UCT")) {
            // UCT Visitor details
            idValidation("Enter  visitor student number (integer)", keyboard);
            this.visitorId = Long.parseLong(keyboard.nextLine().trim());
            System.out.println("Enter Enter visitor fullName");
            this.visitName = keyboard.nextLine();
            System.out.println("Enter Enter visitor contact");
            this.visitorContact = keyboard.nextLine();
            System.out.println("Enter Enter visitor resident");
            this.residentVisitor = keyboard.nextLine();
            this.visitor = schoolMate;

        } else {
            idValidation("Enter  visitor ID number (integer)", keyboard);
            this.visitorId = Long.parseLong(keyboard.nextLine().trim());
            validateID(keyboard,this.visitorId);
            System.out.println("Enter Enter visitor fullName");
            this.visitName = keyboard.nextLine();
            System.out.println("Enter Enter visitor contact");
            this.visitorContact = keyboard.nextLine();
            String[] addressDetails = addressValidation("Enter Enter address( street, suburb,postcode, city. separated by comma", keyboard);
            this.address = new Address(addressDetails[0], addressDetails[1], Integer.parseInt(addressDetails[2].trim()),
                    addressDetails[3]);
            this.visitor =relative;
        }
        if( visitor instanceof  SchoolMate) {
            setHostDetails(host);
            setDetailsVisitor(schoolMate);
            return register.setSignInItem(host, schoolMate,
                    new Signing(new Date(System.currentTimeMillis()), new Time(System.currentTimeMillis()), host.getHostNumber(),
                            ((SchoolMate) visitor).getStudentNumber(), host.getRoomNumber(), "Sign In")
            );
        }
        else if (visitor != null) {
            setHostDetails(host);
            setDetailsVisitor(relative);
            return register.setSignInItem(host, relative,
                    new Signing(new Date(System.currentTimeMillis()), new Time(System.currentTimeMillis()), host.getHostNumber(),
                            ((Relative) visitor).getIdNumber(), host.getRoomNumber(), "Sign In")
            );
        }
        else{

            throw  new Exception("When sign in, visitor must be either relative(friend, anyone) or schoolmate").fillInStackTrace();
        }

    }

    /***
     *  Sign out visitor
     * @param register  -  object of register we sign out on
     * @return
     */
    public   boolean signOut(Scanner keyboard, Register register) throws Exception {
        idValidation("Enter  your student number (integer)",keyboard);
        long hostId  = Long.parseLong(keyboard.nextLine().trim());
        idValidation("Enter  visitor student/ID number (integer)",keyboard);
        long  visitorId = Long.parseLong(keyboard.nextLine().trim());
        Date date  = new Date(System.currentTimeMillis());
        return register.signingOutVisitor(hostId,visitorId,date);
    }

    /**
     * Set the details of the Host to host  object
     * @param host - is the Host object
     */
    private   void setHostDetails(Host host){
        host.setHostNumber(getHostId());
        host.setFullName(getHostName());
        host.setContact(getHostContact());
        host.setRoomNumber(getRoomNumber());
    }

    /**
     * Set details to Schoolmate/Relative    object
     * @param myVisitor - is the Schoolmate/Relative visitor object
     */
    private  void setDetailsVisitor(Visitor myVisitor){
        if(myVisitor instanceof SchoolMate schoolMate){
            schoolMate.setStudentNumber(getVisitorId());
            schoolMate.setFullName(getVisitName());
            schoolMate.setContact(getVisitorContact());
            schoolMate.setResident(getResidentVisitor());
        }
        else if( myVisitor instanceof  Relative relative){
            relative.setIdNumber(getVisitorId());
            relative.setFullName(getVisitName());
            relative.setContact(getVisitorContact());
            relative.setAddress(address);
        }
    }


}
