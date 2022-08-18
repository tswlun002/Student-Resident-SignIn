package com.application.server.model;
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
public class ResidenceRulesService {
    @Autowired
    private ResidentRulesRepository repository;
    @Autowired
    private ResidenceService residenceService;
    /**
     * Remove Rules  by id
     * @param id - identity of the rules
     */
    public void removeRules(Long id) {
        repository.deleteById(id);
    }

}
