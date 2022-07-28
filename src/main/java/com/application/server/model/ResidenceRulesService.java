package com.application.server.model;
import com.application.server.data.Residence;
import com.application.server.data.ResidenceRules;
import com.application.server.repository.ResidentRulesRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Service
public class ResidenceRulesService {
    @Autowired
    private ResidentRulesRepository repository;
    /**
     * Save rules
     * @param residenceRules - rules object to be saved
     */
    void saveRules(ResidenceRules residenceRules){
           if(! repository.findAll().contains(residenceRules))
            repository.save(residenceRules);
    }
    /**
     * Save rules
     * @param residenceRules - list residenceRules object to be saved
     */
    void saveRules(List<ResidenceRules> residenceRules){
        residenceRules.forEach(
                rule-> repository.save(rule)
        );
    }

    /**
     * Fetch all rules
     * @return - list of  ResidenceRules
     */
    protected List<ResidenceRules>  getRules(){
        return repository.findAll();
    }

    /**
     * Fetch all rules  by residence object
     * @param  residence  - residence object  to get  rules
     * @return - list of  ResidenceRules
     */
    protected List<ResidenceRules>  getRules(Residence residence){
        return repository.getRuleForResidence(residence.getResidenceName());
    }

    /**
     * Fetch all rules by residence name and block
     * @param residence - residence name to get  rules
     * @param blocks - block  to get  rules
     * @return - list of  ResidenceRules
     */
    protected List<ResidenceRules>  getRules(String residence, String blocks){
        return repository.getRuleForResidence(residence, blocks);
    }

}
