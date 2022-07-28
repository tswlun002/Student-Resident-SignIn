package com.application.server.model;

import com.application.server.data.Residence;
import com.application.server.repository.ResidenceRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.IntStream;
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Service
public class ResidenceService {
    private String[] blocks =    {"A",  "B", "C", "D", "E", "F", "G"};
    private int [] numberFloors ={  6,   6,   6,   6,   3,   3,   3};
    private int [] numberFlatsFloor ={8,  8,   8,   8,   8,   8,   8};
    private String[] rooms ={"A","B","C","D"};

    private  int capacity=240;
    @Autowired
    private ResidenceRepository repository;
    @PostConstruct
    public void saveResidenceInfo(){

        IntStream.range(0, blocks.length).forEach(
            index-> {
                Residence residence = Residence.builder().residenceName("Forest Hill")
                        .blocks(blocks[index]).numberFlats(numberFlatsFloor[index]).numberRoom(4).
                        numberFloors(numberFloors[index]).capacity(capacity).build();
                repository.save(residence);
            });
    }

    /**
     * Get residence data  features
     * @return list of residence
     */
    public List<Residence>  getAllResidence(){
        return  repository.getResidenceForestHill("Forest Hill");
    }


    public String[] getBlocks() {
        return blocks;
    }

    public void setBlocks(String[] blocks) {
        this.blocks = blocks;
    }

    public int[] getNumberFloors() {
        return numberFloors;
    }

    public void setNumberFloors(int[] numberFloors) {
        this.numberFloors = numberFloors;
    }

    public int[] getNumberFlatsFloor() {
        return numberFlatsFloor;
    }

    public void setNumberFlatsFloor(int[] numberFlatsFloor) {
        this.numberFlatsFloor = numberFlatsFloor;
    }

    public String[] getRooms() {
        return rooms;
    }

    public void setRooms(String[] rooms) {
        this.rooms = rooms;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }


}
