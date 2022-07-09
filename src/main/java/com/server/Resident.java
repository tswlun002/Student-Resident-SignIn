package com.server;

import java.util.ArrayList;

public class Resident {

    private String[] blocks = {"A", "B","C","D","F"};

    private  int [] NumberFloors =  {6,6,6,4,5};
    private int [] NumberFlatsFloor ={10,10,10,10,10};
    private String[] room ={"A","B","C","D"};

    public int[] getNumberFloors() {
        return NumberFloors;
    }

    public void setNumberFloors(int[] numberFloors) {
        NumberFloors = numberFloors;
    }

    public String[] getBlocks() {
        return blocks;
    }

    public void setBlocks(String[] blocks) {
        this.blocks = blocks;
    }

    public int[] getNumberFlatsFloor() {
        return NumberFlatsFloor;
    }

    public void setNumberFlatsFloor(int[] numberFlatsFloor) {
        NumberFlatsFloor = numberFlatsFloor;
    }

    public String[] getRoom() {
        return room;
    }

    public void setRoom(String[] room) {
        this.room = room;
    }

    public Resident(String[] blocks, int []flatsFloor,int [] numberFloors, String[] room) {
        this.blocks = blocks;
        this.NumberFlatsFloor = flatsFloor;
        this.NumberFloors = numberFloors;
        this.room = room;
    }


    public  Resident(Resident resident){
        this.blocks = resident.blocks;
        this.NumberFlatsFloor = resident.NumberFlatsFloor;
        this.NumberFloors = resident.NumberFloors;
        this.room = resident.room;
    }

    public  Resident(){}

}
