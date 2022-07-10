package com.host;

import com.application.OnShowStudentCard;
import com.visitor.Visitor;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
public class Host implements OnShowStudentCard, OnSign {

    private long studentNumber;
    private  String fullName;
    private String contact;
    private  String roomNumber;


    /**
     * Construct to initialise the fields
     * @param hostNumber  - host student number
     * @param fullName    - host full name
     * @param contact     - communication contacts of the host
     * @param roomNumber  - room number of the host
     */
    public Host(long hostNumber, String fullName, String contact, String roomNumber) {
        this.studentNumber = hostNumber;
        this.fullName = fullName;
        this.contact = contact;
        this.roomNumber = roomNumber;
    }

    /**
     * copy construct of host class
     * @param host  - is the host copy from
     */
    public Host(Host host) {
        studentNumber = host.studentNumber;
        this.fullName = host.fullName;
        this.contact = host.contact;
        this.roomNumber = host.roomNumber;
    }

    public Host() {

    }

    public long getHostNumber() {
        return studentNumber;
    }

    public void setHostNumber(long hostNumber) {
        this.studentNumber = hostNumber;
    }

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

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    /**
     *
     * @return to string for Host class
     */
    @Override
    public String toString() {
        return "Host{" +
                "hostNumber='" + studentNumber + '\'' +
                ", fullName='" + fullName + '\'' +
                ", contact='" + contact + '\'' +
                ", roomNumber=" + roomNumber +
                '}';
    }

    @Override
    public boolean equals(Object other_host) {
        if (this == other_host) return true;
        if (!(other_host instanceof Host host)) return false;
        return studentNumber == host.studentNumber &&   getFullName().equalsIgnoreCase(host.getFullName())
                && getRoomNumber().equalsIgnoreCase( host.getRoomNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentNumber, getFullName(), getContact(), getRoomNumber());
    }

    /**
     * @return  details of the student card  of host
     */
    @Override
    public String showStudentCard() {
        return toString();
    }

    /**
     *
     */
    @Override
    public void signIn(Host host, Visitor visitor) {

    }

    /**
     *
     */
    @Override
    public void signOut(Host host, Visitor visitor) {
    }
}
