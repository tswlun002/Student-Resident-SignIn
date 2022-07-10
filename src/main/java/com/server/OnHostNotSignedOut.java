package com.server;

import com.register.SignInItems;


import java.util.ArrayList;

public  interface OnHostNotSignedOut {
    public   void showNotSignedOutVisitors(ArrayList<SignInItems> signInItems);
}
