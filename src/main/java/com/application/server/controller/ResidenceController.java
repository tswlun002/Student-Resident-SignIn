package com.application.server.controller;
import com.application.server.data.Address;
import com.application.server.data.Residence;
import com.application.server.data.ResidenceRules;
import com.application.server.model.ResidenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;
@RestController
@CrossOrigin("*")
@RequestMapping("/admin")
public class ResidenceController {

    @Autowired
    ResidenceService residenceService;

    /**
     * Saves residence information
     * block res have, number floors in each block, flats in each floor and room in each flat.
     * Save capacity , address and name of residence
     * @param block  of the residence
     * @param numberFloors  of the block
     * @param numberFlats  in each floor
     * @param numberRooms in each  flat
     * @param capacity of whole residence( number of rooms)
     * @param address  of the residence
     * @param residenceName of the residence
     */
    @PostMapping(value = "/residence")
    public boolean saveResidenceInfo(
                           @RequestParam String residenceName,
                           @RequestParam String block, @RequestParam String numberFloors,
                           @RequestParam String numberFlats, @RequestParam int numberRooms,
                           @RequestParam int capacity, @RequestParam String address,
                           @RequestParam int signInHour, @RequestParam int signInMinute ,
                           @RequestParam int signOutHour, @RequestParam int signOutMinute ,
                           @RequestParam int numberVisitors
                           ) {
        ResidenceRules residenceRules = ResidenceRules.builder().startSigningTime(LocalTime.of(signInHour,signInMinute))
                .endSigningTime(LocalTime.of(signOutHour,signOutMinute)).numberVisitor(numberVisitors).build();
        String [] blocks = block.split(",");
        String [] floors= numberFloors.split(",");
        String [] flats= numberFlats.split(",");
        String[] addressInfo= address.split(",");

        AtomicBoolean saved = new AtomicBoolean(false);
        IntStream.range(0, blocks.length).forEach(
                index->{
                    Address address1 =  Address.builder().streetNumber(addressInfo[0])
                            .streetName(addressInfo[1]).suburbs(addressInfo[2]).
                            city(addressInfo[3]).postcode(Integer.parseInt(addressInfo[4].trim())).build();




                    Residence residence =Residence.builder().residenceName(residenceName).blocks(blocks[index].trim()).
                            numberFloors(Integer.parseInt(floors[index].trim())).
                            numberFlats(Integer.parseInt(flats[index].trim())).
                            numberRoom(numberRooms).address(address1).capacity(capacity).
                            residenceRules(residenceRules).build();

                    saved.set(residenceService.saveResidenceInfo(residence));
                }
        );
        return saved.get();

    }

    /**
     *  Get all  residence available
     * @return  List of residence
     */
    @GetMapping(value = "/residences")
    public List<Residence> getAllResidences(){
        return  residenceService.getAllResidences();
    }

    /**
     * Get all residence has student allocated to it
     * @return list of residence if there are students allocated to residence else null
     */
    @GetMapping(value = "/residences/residences-student")
    public List<Residence> getResidenceWitStudents() {
        return  residenceService.getResidenceWitStudents();
    }
    /**
     * Get all residence has student allocated to it
     * @return list of residence  if there are no students allocated to residence else null
     */
    @GetMapping(value = "/residences/residences-no-student")
    public List<Residence> getResidenceWithNoStudents() {
        return  residenceService.getResidenceWithNoStudents();
    }

    /**
     *Delete residence by name and block
     * @param residenceName  of the residence to deleted
     * @param block of the residence to delete
     * @return true of block of residence deleted
     */
    @DeleteMapping(value = "/residences/delete")
   public boolean  deleteResidence(@RequestParam  String residenceName,@RequestParam  String block) {
       return  residenceService.deleteResidence(residenceName, block);
    }

    /**
     * Update address of the residence.
     * @param residenceName of the residence to updated
     * @param block of the residence to updated
     * @param streetNumber  updated street number of residence
     * @param streetName  updated street name
     * @param suburbs  updated suburbs  of residence
     * @param postcode  updated postcode of residence
     * @param city   updated city of residence
     * @return true if updated address else false
     */
    @PostMapping(value = "/residences/update-address")
    public boolean updateResidenceAddress(@RequestParam String residenceName,@RequestParam String block,
                                          @RequestParam String streetNumber,@RequestParam String streetName,
                                          @RequestParam String suburbs,@RequestParam int postcode,
                                          @RequestParam String city){

        return  residenceService.updateResidenceAddress(
                 residenceName, block,
                 streetNumber, streetName,  suburbs, postcode, city);

    }

    /**
     * Update the  signing rules of the residence
     * @param residenceName  of residence to change its rules
     * @param block of the residence to change its rules
     * @param numberVisitors  maximum number of visitors allowed to be signed in by each host
     * @return true if  updated rules else false
     */
    @PostMapping(value = "/residences/update-rules")
    public  boolean updateResidenceRules(@RequestParam String residenceName,@RequestParam String block,
                                         @RequestParam int signInHour, @RequestParam int signInMinute ,
                                         @RequestParam int signOutHour, @RequestParam int signOutMinute ,
                                         @RequestParam int numberVisitors) {

        return  residenceService.updateResidenceRules(
                residenceName,block,LocalTime.of(signInHour,signInMinute),
                LocalTime.of(signOutHour,signOutMinute),numberVisitors
        );
    }
}
