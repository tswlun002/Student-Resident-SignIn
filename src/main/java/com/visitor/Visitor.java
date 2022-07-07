package com.visitor;

import com.main.MemberProof;
import org.springframework.stereotype.Component;


public class Visitor{
    private  String fullName;
    private String contact;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Visitor(String fullName, String contact) {
        this.fullName = fullName;
        this.contact = contact;
    }

    public Visitor(Visitor visitor) {
        this.fullName = visitor.fullName;
        this.contact = visitor.contact;
    }

    @Override
    public String toString() {
        return "Visitor{" +
                "fullName='" + fullName + '\'' +
                ", contact='" + contact + '\'' +
                '}';
    }

}
