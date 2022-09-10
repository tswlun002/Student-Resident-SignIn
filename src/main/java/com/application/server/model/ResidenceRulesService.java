package com.application.server.model;

import com.application.server.data.ResidenceRules;
import com.application.server.repository.ResidentRulesRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Service
public class ResidenceRulesService implements OnChangesRules {
    @Autowired
    private ResidentRulesRepository repository;
    @Autowired
    private ResidenceService residenceService;


    private void addRule(ResidenceRules rules){
        if(rules != null){
            repository.save(rules);
        }
    }
    /**
     * Remove Rules  by id
     * @param id - identity of the rules
     */
    private void removeRules(Long id) {
        repository.deleteById(id);
    }

    /**
     * Update rules  existing rule
     * Throw exception  if rules does not exist
     * @param residenceRules with updated information
     * @return true when rule is updated else false
     */
    private boolean updateRules(ResidenceRules residenceRules) {
        try {
            ResidenceRules rules =repository.getReferenceById(residenceRules.getId());
            repository.save(rules);
        }catch (Exception e){
            throw  new RuntimeException("Can not update rule\n" +e.getMessage());
        }

        return true;
    }

    /**
     * Notify when residence is added
     * @param residenceRules added  or to be added
     */
    @Override
    public void addedRules(ResidenceRules residenceRules) {
        addRule(residenceRules);
    }
    /**
     * Notify when residence is a deleted
     *
     * @param residenceRules deleted  or to be deleted
     */
    @Override
    public void deletedRules(ResidenceRules residenceRules) {
        if(residenceRules !=null) removeRules(residenceRules.getId());
    }
}
