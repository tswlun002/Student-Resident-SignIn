package com.register;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
@Component
public class Register{
    ArrayList<SignInItems> signInItems = new ArrayList<>();
    public Register(){}
    public Register(Register register){this.signInItems=register.signInItems;}
    public  void  setSignInItem(SignInItems item){signInItems.add(item);}
    public void setSignInItems(ArrayList<SignInItems> signInItems) {
        this.signInItems = signInItems;
    }
    public  ArrayList<SignInItems> getSignInItems(){return  this.signInItems;}

    public  void showItemsRegister(){
        signInItems.forEach(System.out::println);
    }

}
