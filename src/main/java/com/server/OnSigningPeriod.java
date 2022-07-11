package com.server;
import com.register.Register;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public interface OnSigningPeriod {

    public  void endSigningPeriodAlert(Register register);
}
