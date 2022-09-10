package com.application.visitor.model;

import com.application.visitor.repository.GuestRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import  com.application.visitor.data.Visitor;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Service
public class RelativeGuestService {
    @Autowired
    private GuestRepository guestRepository;

    public boolean saveGuest(Visitor ...visitors){
        if(visitors.length>3)throw  new RuntimeException("You cannot sign more 3 guests");
        boolean isSaved = false;
        for(Visitor visitor : visitors){
            guestRepository.save(visitor);
            isSaved=true;
        }
        return  isSaved;
    }
}
