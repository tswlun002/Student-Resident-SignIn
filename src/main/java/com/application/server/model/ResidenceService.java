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
    @Autowired
    private ResidenceRepository repository;
    @PostConstruct
    public void saveResidenceInfo(){
        String[] blocks =    {"A",  "B", "C", "D", "E", "F", "G"};
        int [] numberFloors ={  6,   6,   6,   6,   3,   3,   3};
        int [] numberFlatsFloor ={8,  8,   8,   8,   8,   8,   8};
        String[] rooms ={"A","B","C","D"};
        IntStream.range(0, blocks.length).forEach(
            index-> {

                Residence residence = Residence.builder().residenceName("Forest Hill")
                        .blocks(blocks[index]).numberFlats(numberFlatsFloor[index]).numberRoom(4).
                        numberFloors(numberFloors[index]).capacity(240).build();
                repository.save(residence);
            });
    }

    public List<Residence>  getAllResidence(){
        return  repository.getResidenceForestHill("Forest Hill");
    }

}
