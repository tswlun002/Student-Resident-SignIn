package com.main;

import com.student.Student;
import com.visitor.Visitor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

//@SpringBootApplication
public class MainActivity {

	public static void main(String[] args) {
		//SpringApplication.run(MainActivity.class, args);
		ApplicationContext context =  new AnnotationConfigApplicationContext(MainConfig.class);
		MemberProof memberVisited = context.getBean(Student.class);
		memberVisited.provideID();
		MemberProof memberVisitor = context.getBean(Visitor.class);
		memberVisitor.provideID();
		System.out.println("my application");
	}

}
