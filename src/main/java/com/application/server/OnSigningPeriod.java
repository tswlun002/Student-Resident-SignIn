package com.application.server;
import com.application.register.model.Register;
import org.springframework.stereotype.Component;

@Component
public interface OnSigningPeriod {

    public  void endSigningPeriodAlert(Register register);
}
