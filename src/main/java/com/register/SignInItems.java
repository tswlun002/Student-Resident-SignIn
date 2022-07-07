package com.register;
import org.springframework.stereotype.Component;
import java.sql.Time;
import java.util.Date;
public class SignInItems implements OnGetSignDetails{

    private Date date;
    private Time signInTime;
    private  Time signOutTime;
    private  long hostId;
    private  long visitorId;
    private   String roomNumber;
    private  String status;

    /***
     * Construct initialise fields
     * @param date - date  host sign in visitor
     * @param signInTime - time host sign visitor
     * @param hostId - host id ( can be student number)
     * @param visitorId - Visitor id ( id for relative or student number for other schoolmate)
     * @param roomNumber -  Host room number
     * @param status - sign in status ( signedIn or signedOut)
     */
    public SignInItems(Date date, Time signInTime, long hostId, long visitorId, String roomNumber, String status) {
        this.date = date;
        this.signInTime = signInTime;
        this.hostId = hostId;
        this.visitorId = visitorId;
        this.roomNumber = roomNumber;
        this.status = status;
    }

    /***
     * Copy Construct
     * @param registerItem - register copy from
     */
    public SignInItems(SignInItems registerItem) {
        this.date = registerItem.date;
        this.signInTime = registerItem.signInTime;
        this.signOutTime = registerItem.signOutTime;
        this.hostId = registerItem.hostId;
        this.visitorId = registerItem.visitorId;
        this.roomNumber = registerItem.roomNumber;
        this.status = registerItem.status;
    }
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getSignInTime() {
        return signInTime;
    }

    public void setSignInTime(Time signInTime) {
        this.signInTime = signInTime;
    }

    public Time getSignOutTime() {
        return signOutTime;
    }

    public void setSignOutTime(Time signOutTime) {
        this.signOutTime = signOutTime;
    }

    public long getHostId() {
        return hostId;
    }

    public void setHostId(long hostId) {
        this.hostId = hostId;
    }

    public long getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(long visitorId) {
        this.visitorId = visitorId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SignItems{" +
                "date=" + date +
                ", signInTime=" + signInTime +
                ", signOutTime=" + signOutTime +
                ", hostId=" + hostId +
                ", visitorId=" + visitorId +
                ", roomNumber='" + roomNumber + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    /**
     * @param details
     */
    @Override
    public void getSignDetail(String details) {
    }
}
