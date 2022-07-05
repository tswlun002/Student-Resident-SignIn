package com.main;

import com.student.Student;
import com.visitor.Visitor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.student","com.visitor"})
class MainConfig {
  /**
    @Bean
    public Student student(){
        return new Student();
    }
    @Bean
    public Visitor visitor(){
        return new Visitor();
    }
    */
}
