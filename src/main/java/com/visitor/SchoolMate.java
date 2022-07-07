package com.visitor;

import com.main.OnShowStudentCard;
import org.springframework.stereotype.Component;

@Component
public class SchoolMate extends Visitor implements  OnShowIdentityCard, OnShowStudentCard {
    private long studentNumber;
    private  String resident;

    public SchoolMate(String fullName, String contact,long studentNumber, String resident) {
        super(fullName, contact);
        this.studentNumber = studentNumber;
        this.resident = resident;
    }

    public SchoolMate(SchoolMate schoolMate) {
        super(schoolMate);
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
