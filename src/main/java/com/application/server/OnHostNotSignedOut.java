package com.application.server;

import com.application.register.model.Signing;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
@Service
public  interface OnHostNotSignedOut {
    public   void showNotSignedOutVisitors(ArrayList<Signing> signInItems);
}
