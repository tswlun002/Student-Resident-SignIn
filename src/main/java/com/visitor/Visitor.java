package com.visitor;

import com.main.MemberProof;
import com.student.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Visitor implements MemberProof {
    /**
     * Show proof using ID or passport
     */
    @Override
    public void provideID() {
        System.out.println("980209....");
    }
}
