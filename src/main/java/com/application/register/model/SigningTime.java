package com.application.register.model;

import com.application.server.data.ResidenceRules;
import com.application.server.model.ResidenceService;
import lombok.Builder;

import java.time.LocalTime;
@Builder
public class SigningTime {
    /***
     * Check if the current time is within the signing period
     * @param residenceService object
     * @param name name of the residence
     * @param block of the residence
     * @param currentTime to be checked is within signing period at the residence
     * @return true if the within the signing period else false
     * @throws  RuntimeException residence service if is null
     */
    public   boolean checkSigningTime(ResidenceService residenceService,String name, String block, LocalTime currentTime){
               if(residenceService !=null){
                   ResidenceRules  residenceRules = getRulesOfResidence(residenceService,name, block);
                   return  currentTime.compareTo(residenceRules.getStartSigningTime())>=0 &&
                           currentTime.compareTo(residenceRules.getEndSigningTime())<=0;

               }else  throw  new RuntimeException("Residence service is not available(null)");
    }

    /**
     * Fetch rules of the residence
     * @param residenceService object
     * @param name of the residence
     * @param block of the residence
     * @return rules of the residence
     */
    private ResidenceRules getRulesOfResidence(ResidenceService residenceService,String name, String block){
        return residenceService.getRulesResidence(name,block);
    }
}
