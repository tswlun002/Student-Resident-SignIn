package com.register;
import com.host.Host;
import com.server.OnHostNotSignedOut;
import com.server.Server;
import com.visitor.Relative;
import com.visitor.SchoolMate;
import com.visitor.Visitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class Register implements OnHostNotSignedOut {

    private ArrayList<SignInItems> signInItems = new ArrayList<>();



    @Autowired
    private ApplicationContext context;
    public Register(){}

    public Register(Register register){
        this.signInItems=register.signInItems;
    }
    public  boolean  setSignInItem(Host host, Visitor visitor, SignInItems newItem) throws Exception {
         Server server = context.getBean(Server.class);
         server.getSignDetail(signInItems);
        boolean authenticated = (visitor instanceof Relative) ?
                server.authenticateAndAuthorizationRelative(host, (Relative) visitor) :
                server.authenticateAndAuthorizationSchoolmate(host, (SchoolMate) visitor);
        if(authenticated) {
            boolean is_newItemInRegister=false;
            for (SignInItems item : signInItems) {
                if(item.getVisitorId() == newItem.getVisitorId()) break;
            }
            if (!is_newItemInRegister) {
                signInItems.add(newItem);
                return  true;
            }
            else throw  new Exception(host.getFullName() +" Already Signed in " + visitor.getFullName());
        }
        return false;
    }
    public void setSignInItems(ArrayList<SignInItems> signInItems) {
        this.signInItems = signInItems;
    }
    public  ArrayList<SignInItems> getSignInItems(){return  this.signInItems;}

    public  void showItemsRegister(){
        signInItems.forEach(System.out::println);
    }

    /**
     * @param signInItems - list of host not yet signed out after 12:00 am
     */
    @Override
    public  void showNotSignedOutVisitors(ArrayList<SignInItems> signInItems) {
        AtomicInteger index  = new AtomicInteger();
        signInItems.forEach(item->{
            System.out.printf("%d. %s",index.get(),item);
            index.addAndGet(1);
        });
    }

    public  void showAllVisitors(){
        AtomicInteger index  = new AtomicInteger();
        signInItems.forEach(item->{
            System.out.printf("%d. %s",index.get(),item);
            index.addAndGet(1);
        });
    }




}
