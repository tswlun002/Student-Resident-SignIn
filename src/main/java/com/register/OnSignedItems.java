package com.register;
import org.springframework.stereotype.Component;
import java.util.*;;

@Component
public interface OnSignedItems {
    public void getSignedItems(List<SignInItems> signInItems);
}
