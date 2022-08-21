package com.application.register.model;
import org.springframework.stereotype.Component;
import java.util.*;;

@Component
public interface OnSignings {
    public void getSignedItems(List<Signing> signInItems);
}
