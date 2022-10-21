package com.application.server.model;
import com.application.server.data.Address;
import com.application.server.data.Residence;
import com.application.server.data.ResidenceRules;
import com.application.server.repository.ResidenceRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import java.time.LocalTime;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Service
public class ResidenceService{

    @Autowired
    private  AddressService addressService;
    @Autowired
    private ResidenceRepository repository;
    @Autowired
    private  ResidenceRulesService rulesService;

    @Autowired
    private OnChangesResidence onChangesResidence;

    @Autowired
    private  OnChangesRules onChangesRules;

    private  List<Residence> residenceList;

    /**
     * Fetch residences from db
     */
    @PostConstruct @PostPersist @PostUpdate @PostRemove
    public  void fetchAllResidences(){
         residenceList  = repository.getAllResidences();
    }


    /**
     *
     * @return list of residence
     */
    public List<Residence> getAllResidences() {
        return  residenceList;
    }

    /**
     * @return list of residence with student
     */
    public List<Residence> getResidenceWitStudents() {
        return  residenceList.stream().filter(residence -> residence.getDepartment()!=null).toList();
    }
    /**
     * @return list of residence with no student
     */
    public List<Residence> getResidenceWithNoStudents() {
        return  residenceList.stream().filter(residence -> residence.getDepartment()==null).toList();
    }


    /**
     * Save residence into database
     * @param residence - residence to save into database
     * @return true if successfully saved residence else false
     */
    public boolean saveResidenceInfo(Residence residence){
          boolean saved= false;
        if ( !isExists(residence.getResidenceName(), residence.getBlocks())){
                   Address address= addressService.saveAddress(residence.getAddress());
                   if(address!=null){residence.setAddress(address);}
                    repository.save(residence);
                    saved=true;
                    fetchAllResidences();
        }
        return saved;
    }

    /**
     * Update residence information
     * @param residence - to be updated
     */
    public void updateResidence(Residence residence){
        repository.save(residence);
    }

    /**
     * Update address of the residence.
     * @param residence - updated residence (residence with new properties)
     * @return - true if successfully updated otherwise false
     */
    private   boolean updateResidenceAddress(Residence residence, Address address){
        boolean updated=false;
        if(residence != null && address !=null){
            Address address1= addressService.updateAddress(address);
            updated = address1 != null;
            if(updated) {
                residence.setAddress(address1);
                updateResidence(residence);
                fetchAllResidences();
            }
        }
        return updated;
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
    public boolean updateResidenceAddress(String residenceName,String block,
                                          String streetNumber,String streetName,
                                          String suburbs,int postcode,String city) {
        Residence residence = getResidence(residenceName, block);
        Address address = Address.builder().streetNumber(streetNumber)
                .streetName(streetName).suburbs(suburbs).
                city(city).postcode(postcode).build();

        return  updateResidenceAddress(residence,address);
    }

    /**
     * Update rules of the residence.
     * @param residence - updated residence (residence with new properties)
     * @return - true if successfully updated rules otherwise false
     */
     @Transactional
     @Modifying
    private    boolean updateResidenceRules(Residence residence,ResidenceRules rules){
        boolean updated=false;
        if(residence != null && rules !=null){
            residence.getResidenceRules().setStartSigningTime(rules.getStartSigningTime());
            residence.getResidenceRules().setEndSigningTime(rules.getEndSigningTime());
            residence.getResidenceRules().setNumberVisitor(rules.getNumberVisitor());
            onChangesRules.addedRules(residence.getResidenceRules());
            fetchAllResidences();
            updated=true;
        }
        return updated;
    }

    /**
     * Update rules of residence
     * @param residenceName  to update its rules
     * @param block to update its rules
     * @param signOutTime updated end signing time
     * @param sigInTime   updated start signing time
     * @param numberVisitors updated number of visitor that can be signed by host
     * @return true if updated else false
     */
    public  boolean updateResidenceRules(String residenceName,String block,LocalTime sigInTime, LocalTime signOutTime, int numberVisitors) {

        Residence residence = getResidence(residenceName, block);
        ResidenceRules rules = ResidenceRules.builder().numberVisitor(numberVisitors)
                .startSigningTime(sigInTime).endSigningTime(signOutTime).build();
        return  updateResidenceRules(residence,rules);
    }
    /**
     * Delete  residence including its properties address and rules  associated with residence
     * @param residence - residence to delete
     * @return - true if successfully delete residence else false .
     */
    private   boolean removeResidence(Residence residence) {
        boolean removed = false;

        try {
            boolean deleted =onChangesResidence.deletedResidence(residence);
            if( residence.getDepartment() !=null && deleted) {
                repository.delete(residence);
                removed = true;
            }
             else if (residence.getDepartment()==null){
                repository.delete(residence);
                removed = true;
            }
        }catch (Exception exception){
            throw  new RuntimeException("Not deleted "+residence.getResidenceName()+residence.getBlocks()+"" +
                    "\nThis residence is foreign key to other entities");
        }
        if(removed)  fetchAllResidences();
        return  removed;
    }
    /***
     * Get address by streetNumber, street name and suburbs
     * @param streetNumber  of the address
     * @param streetName   of the address
     * @param suburbs  of the address
     * @return address if found else null
     */
    public Address getAddress(String streetNumber, String streetName, String suburbs) {
        return addressService.getAddress( streetNumber,  streetName,  suburbs);
    }

    /***
     * Remove residence records  by name and block
     * @param residenceName  of the residence
     * @param block of the residence
     * @return true if residence is removed else false
     */
    public boolean  deleteResidence(String residenceName, String block) {
        Residence residence = getResidence(residenceName,block);
        if(residence ==null)return false;
        return removeResidence(residence);
    }

    /**
     *  check if residence exists  given its name and block
     * @param name - block of residence
     * @param block - block of the residence
     * @return true if exist else false
     */
    public  boolean isExists(String name, String block){
        return residenceList.stream().anyMatch(
                residence -> residence.getResidenceName().equalsIgnoreCase(name)&&
                        residence.getBlocks().equalsIgnoreCase(block)
        );
    }

    /**
     * Get residence data  features
     * @return list of residence
     */
    public List<Residence>  getAllResidence(String name){
        return residenceList.stream().filter(
                residence -> residence.getResidenceName().equalsIgnoreCase(name)
        ).toList();
    }

    /**
     * Get residence  given residence name and block
     * @param name - residence name use to get residence
     * @param block - block of the residence
     * @return residence  of given block and name
     */
    public Residence getResidence(String name, String block) {
        for(Residence residence : residenceList){
            if(residence.getResidenceName().equalsIgnoreCase(name)
                    && residence.getBlocks().equalsIgnoreCase(block)){
                return  residence;
            }
        }
        return null;
    }

    /**
     * Delete rules for the residence
     * @param residence of rules to be deleted
     */
    public   void  deleteResidenceRules(Residence residence){
        residence = getResidence(residence.getResidenceName(),residence.getBlocks());
        if(residence !=null && residence.getResidenceRules() !=null) {
            ResidenceRules rules = residence.getResidenceRules();
            residence.setResidenceRules(null);
            updateResidence(residence);
            onChangesRules.deletedRules(rules);
        }
    }

    /**
     * Fetch rules of the residence
     * @param name of the residence
     * @param block of the residence
     * @return rules of the res if the residence exists else null
     */
    public ResidenceRules getRulesResidence(String name , String block) {
        Residence residence = getResidence(name, block);
        if(residence !=null)return  residence.getResidenceRules();
        else throw new RuntimeException("Can not find residence "+name+block);
    }


}
