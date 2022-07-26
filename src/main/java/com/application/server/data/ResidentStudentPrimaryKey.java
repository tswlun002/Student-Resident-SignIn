package com.application.server.data;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ResidentStudentPrimaryKey implements Serializable {


    private  String blocks;
    private  String flat;
    private  String room;

    public String getBlocks() {
        return blocks;
    }

    public void setBlocks(String block) {
        this.blocks = block;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }


    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }


}
