package com.application.visitor.model;

import com.application.server.data.Address;
import com.application.server.model.AddressService;
import com.application.visitor.repository.GuestRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import  com.application.visitor.data.Visitor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Service
public class RelativeGuestService {
    @Autowired private GuestRepository guestRepository;
    @Autowired private AddressService addressService;
    public boolean saveGuest(Visitor visitor){
        if(visitor==null)return false;
        if(checkVisitorType(visitor)) {
            boolean isSaved = false;
            Visitor visitor1 = getVisitor(visitor);
            if (visitor1 == null && visitor.getAddress() != null) {
                Address address = addressService.saveAddress(visitor.getAddress());
                visitor.setAddress(address);
                guestRepository.save(visitor);
                isSaved = true;
            }
            return isSaved;
        }else return false;
    }

    /**
     * Check if the visitor is relative
     * @param visitor to be checked
     * @return true if visitor relative type
     * @exception  RuntimeException if the visitor type is not relative
     */
    private boolean  checkVisitorType(Visitor visitor){
        if (visitor.getVisitorType().equalsIgnoreCase(GuestType.RELATIVE.name()))return true;
        else throw new RuntimeException("Only "+ GuestType.RELATIVE+" allowed");
    }
    /**
     * Fetch visitor with given id number
     * @param visitor to be fetched
     * @return Visitor if visitor id is found else null
     */
    public  Visitor getVisitor(Visitor visitor){
        if(visitor==null)return null;
        return  guestRepository.findByIdNumber(visitor.getIdNumber());
    }

    /**
     * Fetch visitors based on their type
     * @param guestType of the visitors to be retrieved
     * @return list of visitors if the visitor type if correct else null
     */
    public List<Visitor> getAllVisitors(GuestType guestType){
        if(guestType ==null) return null;
        return guestRepository.getsStudentVisitors(guestType.name());
    }
    /**
     * Fetch all visitors
     * @return list of visitors
     */
    public List<Visitor> getAllVisitors(){
        return guestRepository.getAllVisitors();
    }

    /**
     * delete the student guest
     * @param visitor relative guest to be deleted
     * @return  true if  deleted relative guest  else false
     * @exception  RuntimeException throw when use none relative guest
     */
    public boolean deleteVisitor(Visitor visitor) {
        if (visitor == null) return false;
        if (checkVisitorType(visitor)) {
            Visitor visitor1 = guestRepository.findByIdNumber(visitor.getIdNumber());
            if (visitor1 != null) {
                boolean deleted = false;
                try {
                    guestRepository.delete(visitor1);
                    deleted = true;
                } catch (Exception ignored) {
                }
                return deleted;


            } else return false;
        }else return false;
    }

    /**
     * delete guest by an id
     * @param name of the guest to be deleted
     * @param id of the guest to be deleted
     * @return true if guest deleted  else false
     */
   public boolean deleteVisitor(String name,long id){
        Visitor visitor = guestRepository.findByIdNumber(id);
        if(visitor==null) return false;
        return deleteVisitor(visitor);

    }
    /**
     * Delete all  guests
     */
    public void deleteAll(){
        guestRepository.deleteAll();
    }
    /**
     * Delete all  guests
     */
    public void deleteAll(GuestType type){
        List<Visitor> visitor = guestRepository.getsStudentVisitors(type.name());
        visitor.forEach(this::deleteVisitor);
    }



}
