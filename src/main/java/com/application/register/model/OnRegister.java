package com.application.register.model;

import com.application.register.data.Register;
import com.application.visitor.data.Visitor;
import org.springframework.stereotype.Service;

@Service
public interface OnRegister {
    /**
     * Save  visitor to register after visitor is successfully signed in
     * @param register to signed customer
     * @param visitor to be signed
     * @return register of the visitor
     */
      Register  savedRegister(Register register, Visitor visitor);
}
