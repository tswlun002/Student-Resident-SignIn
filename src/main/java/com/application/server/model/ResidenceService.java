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

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Service
public class ResidenceService {

    @Autowired
    private  AddressService addressService;
    @Autowired
    private ResidenceRepository repository;
    @Autowired
    private  ResidenceRulesService rulesService;


    /**
     * Save residence into database
     * @param residence - residence to save into database
     * @return true if successfully saved residence else false
     */
    public boolean saveResidenceInfo(Residence residence){
          boolean saved= false;
        if ( !isExists(residence.getResidenceName(), residence.getBlocks())){
                    repository.save(residence);
                    saved=true;
        }
        return saved;
    }

    /**
     * Update address of the residence.
     * @param residence - updated residence (residence with new properties)
     * @return - true if successfully updated otherwise false
     */
    @Transactional
    @Modifying
    public  boolean updateResidenceAddress(Residence residence){
        boolean updated=false;
        Residence residence1 = repository.getResidence(residence.getResidenceName(),residence.getBlocks());
        if(residence1 != null){
            Address address =  residence.getAddress();
            residence1.getAddress().setStreetNumber(address.getStreetNumber());
            residence1.getAddress().setStreetName(address.getStreetName());
            residence1.getAddress().setSuburbs(address.getSuburbs());
            residence1.getAddress().setCity(address.getCity());
            residence1.getAddress().setPostcode(address.getPostcode());
            updated= addressService.updateAddress(residence1.getAddress());
        }
        return updated;
    }

    /**
     * Update rules of the residence.
     * @param residence - updated residence (residence with new properties)
     * @return - true if successfully updated rules otherwise false
     */
    @Transactional
    @Modifying
    public  boolean updateResidenceRules(Residence residence){
        boolean updated=false;
        Residence residence1 = repository.getResidence(residence.getResidenceName(),residence.getBlocks());
        if(residence1 != null){
            ResidenceRules rules =  residence.getResidenceRules();
            residence1.getResidenceRules().setStartSigningTime(rules.getStartSigningTime());
            residence1.getResidenceRules().setEndSigningTime(rules.getEndSigningTime());
            residence1.getResidenceRules().setNumberVisitor(rules.getNumberVisitor());
            updated= rulesService.updateRules(residence1.getResidenceRules());
        }
        return updated;
    }


    /**
     * Delete  residence including its properties address and rules  associated with residence
     * @param residence - residence to delete
     * @return - true if successfully delete residence else false .
     */
    public  boolean removeResidence(Residence residence) {
        boolean removed = false;
        residence = repository.getResidence(residence.getResidenceName(),residence.getBlocks());

      if ( residence!=null){
            try {
                repository.deleteById(residence.getId());
                addressService.removeAddress(residence.getAddress().getId());
                rulesService.removeRules(residence.getResidenceRules().getId());
                removed=true;
            }catch (Exception exception){
                return false;
            }

        }
        return  removed;
    }

    /**
     *  check if residence exists  given its name and block
     * @param name - block of residence
     * @param block - block of the residence
     * @return true if exist else false
     */
    public  boolean isExists(String name, String block){
        return repository.getResidence(name,block)!=null;
    }

    /**
     * Get residence data  features
     * @return list of residence
     */
    public List<Residence>  getAllResidence(String name){
        return  repository.getResidenceForestHill(name);
    }

    /**
     * Get residence  given residence name and block
     * @param name - residence name use to get residence
     * @param block - block of the residence
     * @return residence  of given block and name
     */
    public Residence getResidence(String name, String block) {
        return repository.getResidence(name,block);
    }


}
