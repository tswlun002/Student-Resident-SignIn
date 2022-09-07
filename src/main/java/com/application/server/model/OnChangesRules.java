package com.application.server.model;
import com.application.server.data.ResidenceRules;
public interface OnChangesRules {
    /**
     * Notify when residence is added
     * @param residenceRules added  or to be added
     */
    public void addedRules(ResidenceRules residenceRules);
    /**
     * Notify when residence is a deleted
     * @param residenceRules deleted  or to be deleted
     */
    public void deletedRules(ResidenceRules residenceRules);
}
