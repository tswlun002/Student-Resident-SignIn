package com.student;

import com.main.MemberProof;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Student implements MemberProof {

    /**
     * show student card as proof
     */
    @Override
    public void provideID() {
        System.out.println("TSWLUN002");
    }
}
