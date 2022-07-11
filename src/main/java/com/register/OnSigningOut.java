package com.register;
import java.sql.Date;
public interface OnSigningOut {
    public  boolean signingOutVisitor(long hostId, long visitorId, Date date) throws Exception;
}
