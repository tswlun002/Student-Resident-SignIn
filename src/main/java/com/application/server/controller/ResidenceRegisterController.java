package com.application.server.controller;

import com.application.server.data.ResidenceRegister;
import com.application.server.model.ResidenceRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ResidenceRegisterController {
   @Autowired private ResidenceRegisterService residenceRegisterService;
   private List<ResidenceRegister> registerList;

    /**
     * Get all register of rooms
     * @return List of  ResidenceRegister
     */
    public List<ResidenceRegister> getRegisterList() {
        return registerList;
    }

    /**
     * Saves all room/flat of the residence
     * @param residenceName name of residence
     * @param blocks   list of blocks in the residence
     */
    public  void saveRooms(String residenceName, String...blocks){
        for(String block:blocks) residenceRegisterService.residenceRoom(residenceName,block);
    }

    /**
     * Places student to the room
     * @param studentId of student to be placed to room
     * @param residenceName of the residence
     * @param block  of the residence
     * @param floor floor of room
     * @param flat flat of the room
     * @param room to be assigned to student
     * @return true if student is placed else false
     */
    public  boolean placeStudentToRoom(long studentId  ,String residenceName, String block,int floor,
                                       String flat,String room){
       return residenceRegisterService.placeStudentToRoom(residenceName, block,floor,flat,room,studentId);
    }

    /**
     * Change room for student from one room to another on same residence
     * @param studentId of the student that change room
     * @param newBlock isa block  where student is moving to
     * @param newFloor for the new room
     * @param newFlat for the new room
     * @param newRoom where student is moving to
     * @return true if student change the room else false
     */
    public  boolean changeRoomForStudent(long studentId, String newBlock, int newFloor,String newFlat, String newRoom){
         return residenceRegisterService.changeStudentRoom(studentId,newBlock,newFloor,newFlat,newRoom);
    }
    /**
     * Remove student from residence
     * @param studentNumber  of the student to be removed
     * @return true if student successfully removed else false
     */
    public  boolean  removeCorrectStudent(long studentNumber){
       return residenceRegisterService.removeStudent(studentNumber);
    }

    /**
     * Removes all students from room
     */
    public  void removeAllStudents(){
        residenceRegisterService.removeAllStudents();
    }

}
