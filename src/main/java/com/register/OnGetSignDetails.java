package com.register;
import org.springframework.stereotype.Component;
import java.util.*;;

@Component
public interface OnGetSignDetails {
    public void getSignDetail(List<SignInItems> signInItems);
}
