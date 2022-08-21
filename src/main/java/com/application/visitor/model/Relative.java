package com.application.visitor.model;

import org.springframework.stereotype.Service;

@Service

public class Relative extends  Visitor {

    private long idNumber;
    private Address address;

    private String fullName;
    private String contact;

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String getContact() {
        return contact;
    }

    @Override
    public void setContact(String contact) {
        this.contact = contact;
    }

    public Relative(String fullName, String contact, long idNumber, Address address) {
        super(fullName, contact);
        setFullName(fullName);
        setContact(contact);
        this.idNumber = idNumber;
        this.address = address;
    }

    public Relative(Relative relative) {
        super(relative);
        this.idNumber = relative.idNumber;
        this.address = relative.address;
    }

    public Relative() {
        super();
    }

    public long getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(long idNumber) {
        this.idNumber = idNumber;
    }


    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Relative{" +
                "idNumber='" + idNumber + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", contact='" + getContact() + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}


