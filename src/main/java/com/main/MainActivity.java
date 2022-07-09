package com.main;

import com.register.SignVisitor;
import com.server.OnDayEnd;
import com.server.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Time;

@SpringBootApplication
public class MainActivity {

	public static void main(String[] args) throws Throwable {
		 //ApplicationContext context = SpringApplication.run(MainActivity.class, args);
		ApplicationContext context =  new AnnotationConfigApplicationContext(MainConfig.class);

		Server server = context.getBean(Server.class);
		SignVisitor signVisitor = context.getBean(SignVisitor.class);

	}

}
