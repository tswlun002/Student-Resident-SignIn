package com.application.register.model;
import com.application.student.model.StudentService;
import com.application.server.OnHostNotSignedOut;
import com.application.server.Server;
import com.application.visitor.model.Relative;
import com.application.visitor.model.SchoolMate;
import com.application.visitor.model.Visitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class Register implements OnHostNotSignedOut, OnSigningOut{
    private ArrayList<Signing> signInItems;
    @Autowired
    private Server server;

    /**
     * Default construct
     */
    public Register(){
        signInItems = new ArrayList<>();
    }

    /**
     * Copy construct
     * @param register - register copy from
     */
    public Register(Register register){
        this.signInItems=register.signInItems;
    }

    /***
     * Store signed item
     * First check if studentService is authorized to sign in and check if the visitor is valid
     * If all checks are true, sign is authorized and sign item is stored
     * @param studentService - studentService object signed visitor
     * @param visitor - visitor object  being signed
     * @param newItem - signed item
     * @return true visitor successful not already signed else false
     * @throws Exception -  authorization fails
     */
    public  boolean  setSignInItem(StudentService studentService, Visitor visitor, Signing newItem) throws Exception {
         server.getSignedItems(signInItems);
        boolean authenticated = false;
        if(!alreadySignedVisitor(newItem)) {
             authenticated =  (visitor instanceof Relative) ?
                     server.authenticateAndAuthorizationRelative(studentService, (Relative) visitor) :
                     server.authenticateAndAuthorizationSchoolmate(studentService, (SchoolMate) visitor);
           if(authenticated)signInItems.add(newItem) ;
           else{
               try {
                   throw new Exception("Not allowed");
               }catch (Exception e){
                   return false;
               }
               finally {
                   System.out.println("Not Allowed to sign in");
               }

           }
        }
        return authenticated;
    }

    /**
     * Check if visitor is not already signed
     * @param newItem - signItem check if not already on register
     * @return true if already signed else false
     */
    public  boolean alreadySignedVisitor(Signing newItem){
        return signInItems.stream().anyMatch(item->
                 item.getVisitorId()==newItem.getVisitorId()
                 && item.getStatus().equalsIgnoreCase(newItem.getStatus()) );
    }

    public  void  clearItems(){
        signInItems.clear();
        signInItems = new ArrayList<>();
    }

    /**
     * @param signInItems - List of signInItems
     */
    public void setSignInItems(ArrayList<Signing> signInItems) {
        this.signInItems = signInItems;
    }

    /**
     * @return List of signInItems
     */
    public  ArrayList<Signing> getSignInItems(){return  this.signInItems;}




    /**
     * @param signInItems - list of student not yet signed out after 12:00 am
     */
    @Override
    public  void showNotSignedOutVisitors(ArrayList<Signing> signInItems) {
        AtomicInteger index  = new AtomicInteger();
        signInItems.forEach(item->{
            System.out.printf("%d. %s",index.get(),item);
            index.addAndGet(1);
        });
    }
    /**
     * Prints signInItems
     */
    public  void showAllVisitors() throws InterruptedException {
        Thread inflateSignedIn = new Thread(new InflateSignedIn(signInItems));
        inflateSignedIn.start();
    }


    /**
     * Allow signing out of visitor
     * Check if its signing time , verify student id and visitor id are register
     * @param hostId  - id of the student
     * @param visitorId - visitor id
     * @param date - date of signing in
     * @throws  - exception when signing out after signing time has passed
     * @return - true when signing out on time and ids appear on register else false
     */
    @Override
    public boolean signingOutVisitor(long hostId, long visitorId, Date date){
        return getSignInItems().stream().anyMatch(
               item->{
                       boolean okay  =    item.getDate().toString().equalsIgnoreCase(date.toString())&&
                               item.getHostId()==hostId && item.getVisitorId()==visitorId;
                       if(okay){

                           item.setSignOutTime(new Time(System.currentTimeMillis()));
                           item.setStatus("sign out");
                           return  true;
                       }else return  false;
               });
    }

     static class InflateSignedIn implements Runnable {

        List<Signing> signingList ;
             public  InflateSignedIn(List<Signing> signingList){
                 this.signingList = signingList;
             }
        /**
             * When an object implementing interface {@code Runnable} is used
             * to create a thread, starting the thread causes the object's
             * {@code run} method to be called in that separately executing
             * thread.
             * <p>
             * The general contract of the method {@code run} is that it may
             * take any action whatsoever.
             *
             * @see Thread#run()
             */
            @Override
            public void run() {
                while( ! LocalTime.of(7,10,0,0).
                        format(DateTimeFormatter.ofPattern("hh:mm a")).equalsIgnoreCase(
                                LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"))));
                System.out.println("/////////////////////////////////////////////////////\n" +
                        "                        Currently signed People          \n" +
                        "////////////////////////////////////////////////////////////////////");
                for ( Signing item :signingList){
                    if (item.getStatus().equalsIgnoreCase("sign in")) {
                        System.out.println(item);
                    }
                }
                // }
            }
        }
}
