package com.application.register.model;

import com.application.ResidenceData;
import com.application.register.controller.RegisterController;
import com.application.server.data.Address;
import com.application.visitor.model.GuestType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Scanner;

@Component
public class SignVisitor {
    private     Address visitorAddress;

    @Autowired
    private RegisterController registerController;


    /***
     * Construct to Start sign process
     */
    @PostConstruct
    public void  signVisitor(){
        ResidenceData.residenceName="Forest Hill";
        Scanner keyboard = new Scanner(System.in);
        boolean quit =false;
        while (!quit) {
            System.out.println("""
                    ///////////////////////////////////////////////////////////////////////////
                    			             SIGN IN OR OUT VISITOR                          \s
                    //////////////////////////////////////////////////////////////////////////""");
            System.out.println("Enter your residence block");
            ResidenceData.residenceBlock= keyboard.nextLine();

            //prompt sign
            System.out.println("Enter IN to sign in or OUT  to sign out or Quit to quit");

            String signingType = keyboard.nextLine();
            if (signingType.equalsIgnoreCase("in")) {
                SigningStatus status = SigningStatus.SIGNEDIN;
                boolean siginedIn = signIn(keyboard);
                if (siginedIn) {
                    registerController.showAllVisitors();
                    System.out.println("Successful signed in");
                }
                else System.out.println("Can't sign in");
            } else if (signingType.equalsIgnoreCase("out")) {
                boolean signedOut = signOut(keyboard);
                if (signedOut) System.out.println("Successful signed out");
                else System.out.println("Can't find match , you can't sign out ");

            } else if (signingType.equalsIgnoreCase("quit")) {
                quit=true;
            }

            //System.out.println("**********************************************************");
            registerController.showAllVisitors();


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
            if (line.split(",").length == 5){
                 line.split(",");
                 break;
            }
            else System.out.println("Error !!!\n"+message);
        }
        return  line.split(",");
    }

    /***
     * Take details of the  StudentService and Visitor
     * @param keyboard - Scanner object for prompting user to enter details
     * @return true if StudentService successful signed in visitor else false
     * @throws Throwable  - when try to sign visitor which not Relative type or SchoolMate type
     */
    public boolean  signIn(Scanner keyboard)  {

        // StudentService details
        idValidation("Enter host student number (integer)", keyboard);
        long hostId = Long.parseLong(keyboard.nextLine().trim());
        System.out.println("Enter host fullName");
        String hostName = keyboard.nextLine();
        System.out.println("Enter UCT if visitor is a student else relative");
        String visitorType = keyboard.nextLine();

        long visitorId;
        String visitName;
        String visitorContact;
        GuestType guestType;
        if (visitorType.equalsIgnoreCase("UCT")) {
            // UCT Visitor details
            guestType = GuestType.STUDENT;
            idValidation("Enter  visitor student number ", keyboard);
            visitorId = Long.parseLong(keyboard.nextLine().trim());
            System.out.println("Enter Enter visitor fullName");
            visitName = keyboard.nextLine();
            System.out.println("Enter Enter visitor contact");
            visitorContact = keyboard.nextLine();

        } else {
            guestType =GuestType.RELATIVE;
            idValidation("Enter  visitor ID number (integer)", keyboard);
            visitorId = Long.parseLong(keyboard.nextLine().trim());
            validateID(keyboard, visitorId);
            System.out.println("Enter Enter visitor fullName");
            visitName = keyboard.nextLine();
            System.out.println("Enter Enter visitor contact");
            visitorContact = keyboard.nextLine();
            String[] addressDetails = addressValidation("Enter Enter address( streetNumber,streetName, suburb" +
                    ",postcode, city. separated by comma", keyboard);
           this.visitorAddress=
                   makeAddress(addressDetails[0], addressDetails[1],addressDetails[2], Integer.parseInt(addressDetails[3].trim()),
                    addressDetails[4]);
        }
        if(guestType ==GuestType.STUDENT) {
            return   registerController.signInStudent(
                    hostId,ResidenceData.residenceName,ResidenceData.residenceBlock,
                    visitorId, visitName, visitorContact
            );
        }
        else if (guestType ==GuestType.RELATIVE) {
             return registerController.signInRelativeGuests(
                     hostId,ResidenceData.residenceName,ResidenceData.residenceBlock,
                     visitName, visitorId, visitorContact,visitorAddress

             );
        }
        else{

            throw  new RuntimeException("When sign in, visitor must be either relative(friend, anyone) or schoolmate");
        }

    }
   com.application.server.data.Address makeAddress(String streetNumber,
                                                   String streetName, String suburbs, int postalCode, String city) {
      return com.application.server.data.Address.builder().streetNumber(streetNumber).streetName(streetName).suburbs(suburbs)
               .postcode(postalCode).city(city).build();
   }
    /***
     *  Sign out visitor
     *   @return
     */
    public   boolean signOut(Scanner keyboard) {
        idValidation("Enter  your student number (integer)",keyboard);
        long hostId  = Long.parseLong(keyboard.nextLine().trim());
        idValidation("Enter  visitor student/ID number (integer)",keyboard);
        long  visitorId = Long.parseLong(keyboard.nextLine().trim());
        return  registerController.signOut(hostId,ResidenceData.residenceName,ResidenceData.residenceBlock,visitorId);
    }


}
