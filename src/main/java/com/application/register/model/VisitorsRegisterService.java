package com.application.register.model;

import com.application.register.data.VisitorsRegister;
import com.application.register.repository.VisitorsRegisterRepository;
import com.application.visitor.data.Visitor;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import java.util.List;

@Builder
@Service
public class VisitorsRegisterService {

    @Autowired private VisitorsRegisterRepository repository;
    @Autowired private RegisterService registerService;
    private  List<VisitorsRegister> visitorsRegisterList;

    /**
     * Fetch all visitors register
     */
    @PostConstruct @PostUpdate @PostPersist
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
}
