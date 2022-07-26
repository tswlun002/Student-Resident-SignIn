package com.application.server;
import com.application.server.repository.ResidentStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
@Controller
public class ServerController {
    @Autowired
    ResidentStudentRepository repository;


}
