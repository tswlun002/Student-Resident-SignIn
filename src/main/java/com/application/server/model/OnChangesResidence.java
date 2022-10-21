package com.application.server.model;
import com.application.server.data.Residence;
public interface OnChangesResidence {
    /**
     * Notify when residence is added
     * @param residence added  or to be added
     */
    public default void addedResidence(Residence residence){}
    /**
     * Notify when residence is a deleted
     * @param residence deleted  or to be deleted
     */
    public boolean deletedResidence(Residence residence);
}
