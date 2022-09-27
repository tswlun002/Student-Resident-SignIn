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
    @Transactional
    @Modifying
    public  boolean updateResidenceAddress(Residence residence){
        boolean updated=false;
        Residence residence1 = repository.getResidence(residence.getResidenceName(),residence.getBlocks());
        if(residence1 != null){
            Address address =  residence.getAddress();
            updated= addressService.updateAddress(address);
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
    public   boolean updateResidenceRules(Residence residence){
        boolean updated=false;
        Residence residence1 = repository.getResidence(residence.getResidenceName(),residence.getBlocks());
        if(residence1 != null){
            ResidenceRules rules =  residence.getResidenceRules();
            residence1.getResidenceRules().setStartSigningTime(rules.getStartSigningTime());
            residence1.getResidenceRules().setEndSigningTime(rules.getEndSigningTime());
            residence1.getResidenceRules().setNumberVisitor(rules.getNumberVisitor());
            onChangesRules.addedRules(residence1.getResidenceRules());
            updated=true;
        }
        return updated;
    }


    /**
     * Delete  residence including its properties address and rules  associated with residence
     * @param residence - residence to delete
     * @return - true if successfully delete residence else false .
     */
    @Transactional
    @Modifying
    public  boolean removeResidence(Residence residence) {
        boolean removed = false;
        residence = repository.getResidence(residence.getResidenceName(),residence.getBlocks());

      if ( residence!=null){
            try {
                onChangesResidence.deletedResidence(residence);
                repository.delete(residence);
                removed=true;
            }catch (Exception exception){
                throw  new RuntimeException("Not deleted "+residence.getResidenceName()+residence.getBlocks()+"\n"+exception.getMessage() );
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
        else throw new RuntimeException("Can not find residence.");
    }
}
