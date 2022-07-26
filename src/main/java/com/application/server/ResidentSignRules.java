package com.application.server;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class ResidentSignRules {
    private LocalTime signOutTime = LocalTime.MAX;
    private LocalTime sigInTime = LocalTime.of(6,0,0,0);
    private  int numberVisitors = 3;

    public ResidentSignRules(LocalTime signOutTime,LocalTime sigInTime, int numberVisitors) {
        this.signOutTime = signOutTime;
        this.numberVisitors = numberVisitors;
    }
    public ResidentSignRules(ResidentSignRules signRules) {
        this.signOutTime = signRules.signOutTime;
        this.sigInTime = signRules.sigInTime;
        this.numberVisitors = signRules.numberVisitors;
    }
    public ResidentSignRules() {}

    @Override
    public String toString() {
        return "ResidentSignRules{" +
                "sigInTime=" + sigInTime +
                ", signOutTime=" + signOutTime +
                ", numberVisitors=" + numberVisitors +
                '}';
    }

    public LocalTime getSigInTime() {
        return sigInTime;
    }

    public void setSigInTime(LocalTime sigInTime) {
        this.sigInTime = sigInTime;
    }

    public LocalTime getSignOutTime() {
        return signOutTime;
    }

    public void setSignOutTime(LocalTime signOutTime) {
        this.signOutTime = signOutTime;
    }

    public int getNumberVisitors() {
        return numberVisitors;
    }

    public void setNumberVisitors(int numberVisitors) {
        this.numberVisitors = numberVisitors;
    }
}
