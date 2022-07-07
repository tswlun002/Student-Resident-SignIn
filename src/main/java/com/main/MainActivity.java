package com.main;

import com.host.Host;
import com.register.Register;
import com.register.SignInItems;
import com.visitor.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.sql.Date;
import java.sql.Time;
import java.util.Scanner;
//@SpringBootApplication
public class MainActivity {

	/**
	 *  Sign in visitor.
	 *  Visitor can be Relative or Schoolmate
	 * @param register - object of register that stores sign items
	 * @param host  - object of host that sign in visitor
	 * @param visitor  - object of Visitor that is being signed in
	 */
	 public  static  void sinIn(Register register,Host host,Visitor visitor) throws Throwable {
		 if( visitor instanceof  SchoolMate)
		   register.setSignInItem(
				   new SignInItems(new Date(System.currentTimeMillis()), new Time(System.currentTimeMillis()), host.getHostNumber(),
				   ((SchoolMate)visitor).getStudentNumber(), host.getRoomNumber(), "Sign In")
		   );

		 else if (visitor instanceof  Relative)
			 register.setSignInItem(
					 new SignInItems(new Date(System.currentTimeMillis()), new Time(System.currentTimeMillis()), host.getHostNumber(),
					 ((Relative)visitor).getIdNumber(),host.getRoomNumber(),"Sign In")
			 );
		 else throw  new Exception("When sign in, visitor must be either relative(friend, anyone) or schoolmate").fillInStackTrace();
	 }

	/***
	 *  Sign out visitor
	 * @param register  -  object of register we sign out on
	 * @param host  - object of the host
	 * @param visitor - object of Visitor
	 * @param date - Date sign out
	 */
	 public  static  void signOut(Register register, Host host,Visitor visitor, Date date){
		 for(SignInItems item: register.getSignInItems()){
			 if( visitor instanceof  SchoolMate)
			 	if(item.getDate() == date && item.getHostId() ==host.getHostNumber() &&
						item.getVisitorId()==((SchoolMate)visitor).getStudentNumber()){
					 item.setStatus("Sign Out");
				}
			 if(visitor instanceof Relative)
				if(item.getDate() == date && item.getHostId() ==host.getHostNumber() &&
						item.getVisitorId()==((Relative)visitor).getIdNumber()){
					item.setStatus("Sign Out");
				}

		 }

	 }
	public static void main(String[] args) throws Throwable {
		//SpringApplication.run(MainActivity.class, args);
		ApplicationContext context =  new AnnotationConfigApplicationContext(MainConfig.class);
		Register register  = context.getBean(Register.class);
		Host host = context.getBean(Host.class);
		Visitor schoolMate = context.getBean(SchoolMate.class);
		Visitor relative = context.getBean(Relative.class);
		while (true) {
			System.out.println("""
					///////////////////////////////////////////////////////////////////////////
								                       SIGN IN                           \s
					//////////////////////////////////////////////////////////////////////////""");

			Scanner keyboard = new Scanner(System.in);
			//prompt sign type
			System.out.println("Enter I to sign in or O  to sign out");
			String signingType = keyboard.nextLine();
			String[] hostDetails = new String[4];
			String[] visitorDetails = new String[4];
			// Host details
			System.out.println("Enter Enter your student number (integer)");
			hostDetails[0] = keyboard.nextLine();
			System.out.println("Enter Enter your fullName");
			hostDetails[1] = keyboard.nextLine();
			System.out.println("Enter Enter your contact");
			hostDetails[2] = keyboard.nextLine();
			System.out.println("Enter Enter your roomNumber (block flat room)");
			hostDetails[3] = keyboard.nextLine();
			Address address = null;
			System.out.println("Enter UCT if visitor is a student else relative");
			String visitor = keyboard.nextLine();
			if (visitor.equalsIgnoreCase("UCT")) {
				// UCT Visitor details
				System.out.println("Enter Enter visitor student number (integer)");
				visitorDetails[0] = keyboard.nextLine();
				System.out.println("Enter Enter visitor fullName");
				visitorDetails[1] = keyboard.nextLine();
				System.out.println("Enter Enter visitor contact");
				visitorDetails[2] = keyboard.nextLine();
				System.out.println("Enter Enter visitor resident");
				visitorDetails[3] = keyboard.nextLine();
			} else {
				// UCT Visitor details
				System.out.println("Enter Enter visitor ID number (integer)");
				visitorDetails[0] = keyboard.nextLine();
				System.out.println("Enter Enter visitor fullName");
				visitorDetails[1] = keyboard.nextLine();
				System.out.println("Enter Enter visitor contact");
				visitorDetails[2] = keyboard.nextLine();
				System.out.println("Enter Enter address( street, suburb,postcode, city. separated by comma");
				String[] addressDetails = keyboard.nextLine().split(",");
				address = new Address(addressDetails[0], addressDetails[1], Integer.parseInt(addressDetails[2].trim()),
						addressDetails[3]);
			}
			host = new Host(Integer.parseInt(hostDetails[0].trim()),hostDetails[1],hostDetails[2],hostDetails[3]);
			System.out.println(host.showStudentCard());
			if (signingType.equalsIgnoreCase("i")) {

				if (visitor.equalsIgnoreCase("UCT")) {
					 schoolMate=new SchoolMate(visitorDetails[1],hostDetails[2],Integer.parseInt(hostDetails[0].trim()),
							 visitorDetails[3]);
					System.out.println(((OnShowStudentCard) schoolMate).showStudentCard());
					sinIn(register, host, schoolMate);
				} else {
					System.out.println(((OnShowIdentityCard) schoolMate).showIdentityCard());
					sinIn(register, host, schoolMate);
				}
			}
			if (signingType.equalsIgnoreCase("o")) {

				if (visitor.equalsIgnoreCase("UCT")) {
					schoolMate=new SchoolMate(visitorDetails[1],hostDetails[2],Integer.parseInt(hostDetails[0].trim()),
							visitorDetails[3]);
					System.out.println(((OnShowStudentCard) schoolMate).showStudentCard());
					signOut(register, host, schoolMate, new Date(System.currentTimeMillis()));
				} else {
					System.out.println(((OnShowIdentityCard) relative).showIdentityCard());
					relative = new Relative(visitorDetails[1],visitorDetails[2],Integer.parseInt(visitorDetails[0].trim()),
							new Address(address));
					signOut(register, host, schoolMate, new Date(System.currentTimeMillis()));
				}
			}
			System.out.println("Another one?");
			if(keyboard.nextLine().equalsIgnoreCase("no"))break;

		}


		final int[] count = {0};
		register.getSignInItems().forEach(signInItems -> {
			System.out.format("Item %d :%s\n", count[0],signInItems.toString());
			count[0] +=1;
		});
		System.out.println("my application");
	}

}
