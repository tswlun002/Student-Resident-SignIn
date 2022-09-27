package com.application.register.model;

import com.application.register.data.Register;
import com.application.register.data.VisitorsRegister;
import com.application.register.repository.VisitorsRegisterRepository;
import com.application.visitor.data.Visitor;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import java.time.LocalTime;
import java.util.List;

@Builder
@Service
public class VisitorsRegisterService {

    @Autowired private VisitorsRegisterRepository repository;
    @Autowired private RegisterService registerService;
    private  List<VisitorsRegister> visitorsRegisterList;
    @Autowired private OnSaveRegister onSaveRegister;

    /**
     * Fetch all visitors register after each update
     */
    @PostConstruct @PostUpdate @PostPersist @PostRemove
    public void fetchVisitorsRegister(){
        visitorsRegisterList= repository.getVisitorsRegister();
    }

    /**
     * @return List of visitorsRegister present else null
     */
    public  List<VisitorsRegister> getVisitorsRegisterList(){
        return  visitorsRegisterList;
    }
    /**
     * Fetch all visitors register
     * With the given status
     * @param  status if the status(signedIn or SignedOut)  of the visitor
     * @return list of visitorsRegister if present else null
     */
    public List<VisitorsRegister> getVisitorsRegister(SigningStatus status){
        return repository.getVisitorsRegister(status.name());
    }


    /**
     * Add visitor
     * if not already added
     * @param visitorsRegister  to be added
     * @return true if successfully added else false
     * @throws  RuntimeException if visitor or register is null
     */
    public  boolean addVisitor(VisitorsRegister visitorsRegister){
         if( visitorsRegister!=null && visitorsRegister.getRegister() !=null && visitorsRegister.getVisitor()!=null){
            if(! checkVisitorIsSigned(visitorsRegister.getVisitor())){
                  Register register = onSaveRegister.savedRegister(visitorsRegister.getRegister(),visitorsRegister.getVisitor());
                  visitorsRegister.setRegister(register);
                  repository.save(visitorsRegister);
                  return  true;
            }else  return  false;

         }else throw new RuntimeException("Register or visitor can not be null");
    }

    /**
     * Check if the visitor is not already signed return
     * @param visitor  to be checked
     * @return true if visitor is signed already else false
     */
    public    boolean checkVisitorIsSigned(Visitor visitor){
        if (visitor==null) return  false;
        List<VisitorsRegister> registerList  = getVisitorsRegister(SigningStatus.SIGNEDIN);
        if(registerList==null || registerList.size()==0) return false;
        return  registerList.stream().anyMatch(register -> register.getVisitor().equals(visitor));
    }

    /**
     * Fetch visitors who are still signed in
     * @param visitorId  of the signed visitor
     * @return signed visitor with given id else null if the visitor is not found
     */
    public Visitor getSignedInVisitorRegister(long visitorId) {
       for(VisitorsRegister register: getVisitorsRegisterList()) {
           if (register.getVisitor().getIdNumber() == visitorId &&
                   SigningStatus.valueOf(register.getSigningStatus())==SigningStatus.SIGNEDIN )
               return register.getVisitor();
       }
       return  null;
    }

    /**
     * Sign out visitor
     * @param register  of the visitor
     * @return  true if signed out else false
     */
    public boolean signOut(Register register,Visitor visitor) {
        for(VisitorsRegister register1: getVisitorsRegisterList()){
             if (register.equals(register1.getRegister())
                     && register1.getVisitor().equals(visitor)
                     && SigningStatus.valueOf(register1.getSigningStatus())==SigningStatus.SIGNEDIN)
             {
                 register1.setSignOutTime(LocalTime.now());
                 register1.setSigningStatus(SigningStatus.SIGNEDOUT.name());
                 repository.save(register1);
                 return true;
             }
        }
        return  false;

    }

    /**
     * Delete all the visitors
     * @return true if all visitors are deleted else false
     */
    public boolean deleteAll() {
        repository.deleteAll();
        fetchVisitorsRegister();
        return getVisitorsRegisterList().isEmpty();
    }

    /**
     * Delete all visitors of the give register
     * @param register of the visitors to be deleted
     * @return true if visitors of the register are deleted else false
     */
    public boolean deleteVisitorRegister(Register register) {
        boolean deleted  =false;
        for (VisitorsRegister record: visitorsRegisterList) {
            if(record.getRegister().equals(register)){
                 repository.delete(record);
                deleted= true;
            }
        }
        return  deleted;
    }
}
