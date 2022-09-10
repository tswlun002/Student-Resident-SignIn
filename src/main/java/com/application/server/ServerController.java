package com.application.server;
import com.application.server.repository.ResidenceRegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
@Controller
public class ServerController {
    @Autowired
    ResidenceRegisterRepository repository;


}
