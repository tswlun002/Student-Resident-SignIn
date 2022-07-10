package com.visitor;

import com.application.OnShowStudentCard;
import org.springframework.stereotype.Service;


import java.util.Objects;

@Service

public class SchoolMate extends Visitor implements  OnShowIdentityCard, OnShowStudentCard {
    private long studentNumber;
    private  String resident;
    
    private  String fullName;
    private  String contact;

    public SchoolMate() {
        super();
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public void setFullName(String fullname) {
        this.fullName = fullname;
    }

    @Override
    public String getContact() {
        return contact;
    }

    @Override
    public void setContact(String contact) {
        this.contact = contact;
    }

    public SchoolMate(String fullName, String contact, long studentNumber, String resident) {
        super(fullName, contact);
        setFullName(fullName);
        setContact(contact);
        this.studentNumber = studentNumber;
        this.resident = resident;
    }

    public SchoolMate(SchoolMate schoolMate) {
        super(schoolMate);
        setFullName(super.getFullName());
        setContact(super.getContact());
        this.studentNumber = schoolMate.studentNumber;
        this.resident = schoolMate.resident;
    }

    public long getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(long studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getResident() {
        return resident;
    }

    public void setResident(String resident) {
        this.resident = resident;
    }

    @Override
    public boolean equals(Object student) {
        System.out.println(this.toString());

        if (this == student) return true;
        if (!(student instanceof SchoolMate student1)){
            return false;
        }
        else{
            System.out.println(student1.toString());
            return getStudentNumber() == student1.getStudentNumber() &&
                    getResident().equalsIgnoreCase( student1.getResident()) &&
                    getFullName().equalsIgnoreCase(student1.getFullName());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStudentNumber(), getResident());
    }

    @Override
    public String toString() {
        return "SchoolMate{" +
                "studentNumber='" + studentNumber + '\'' +
                ", fullName='" + super.getFullName() + '\'' +
                ", contact='" + super.getContact() + '\'' +
                ", resident='" + resident + '\'' +
                '}';
    }

    /**
     *@return  details of the identity card  of relative
     */
    @Override
    public String showIdentityCard() {
        return showStudentCard();
    }

    /**
     * @return details of the Student card of schoolmate
     */
    @Override
    public String showStudentCard() {
        return toString();
    }
}
