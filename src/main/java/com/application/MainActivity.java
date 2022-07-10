package com.application;
import com.register.SignVisitor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
@SpringBootApplication
public class MainActivity {
	public static void main(String[] args) throws Throwable {
		 ApplicationContext context = SpringApplication.run(MainActivity.class, args);
		SignVisitor signVisitor = context.getBean(SignVisitor.class);

	}

}
