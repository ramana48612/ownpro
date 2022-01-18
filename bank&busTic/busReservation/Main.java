package busReservation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Main {

	
   static Scanner sc = new Scanner(System.in);
   static HashMap<String,String>hashMap;
   public static int option = 0;
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Class.forName("com.mysql.cj.jdbc.Driver");
		 Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/busTicReserv2","root","");
		 
		
		while(true) {
			System.out.println("\n1.Log in\n2.sign up\n3.skip log in\n4.exit");
			
		   int option = sc.nextInt();
		   switch(option) {
		   case 1:
			   login(con);
			   break;
		   case 2:
			   signup(con);
			   break;
		   case 3:
			   Booking.main(null);
			   break;
		   case 4:
			   System.exit(0);
			   break;
		   default:
			   System.out.println("Please enter correct option ...");
			   main(null);
			   break;
				   
		   }
			
		}
		
	

		 
	}
	static int check = 0;
	private static void login(Connection con)throws Exception {
		Pattern pattern = Pattern.compile("[0-9]{5}");
		// TODO Auto-generated method stub
		int id_no = 0;String Inputpassword = "" , password = "";
		while(true) {			
			if(!pattern.matcher(String.valueOf(id_no)).matches()) {
		      System.out.print("Enter your id number : ");		     
		      id_no = (sc.nextInt());	
		      
			}else {break;}		
		}
		PreparedStatement stmt=con.prepareStatement("select id_no from users where id_no = "+id_no);
		
   	    ResultSet rs=stmt.executeQuery();
   	    
   	    while(rs.next()){
   	    	if(rs.getInt(1)==id_no) {
   	    		check++;
   	    	}	    	
   	    }
   	    if(check==0) {  	    	
   	    	System.out.println("Please give correct id number ...");
   	    	login(con);
   	    }else {
   	    	check = 0;
   	    }
   	    
   	    System.out.print("Password : ");
   	    Inputpassword = sc.nextLine();Inputpassword = sc.nextLine();
   	    
   	 stmt=con.prepareStatement("select password from users where id_no = "+id_no);
		
	     rs=stmt.executeQuery();
   	    
   	    while(rs.next()) {
   	    	if(Inputpassword.equals(rs.getString(1))) {
   	    		check++;
   	    	}
   	    } 	    
   	    if(check==0) {
   	    	System.out.println("Please enter correct password...");
   	    	login(con);
   	    }else {
   	    	check = 0;
   	    	System.out.println("Login successful....");
   	    	Booking.main(null);
   	    }
   	    
   	    
		
		
	}
	private static void signup(Connection con) throws Exception  {
		// TODO Auto-generated method stub
		 Pattern pattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
	        Pattern regexForPhone = Pattern.compile("[0-9]{10}");
	        Pattern regexForAadhar = Pattern.compile("[0-9]{12}");
	        
	        String name = "" , phone="", email="", password = "" , dob= "" ; int gender = 0 ;
	        
	        PreparedStatement stmt=con.prepareStatement("insert into users(name,phone,gender,dob,email,password)values(?,?,?,?,?,?)");
	        
	        while(true) {
	        	if (name.length() == 0) {
                    System.out.print("Enter Your Name :");
                    name = sc.nextLine();
                    name = sc.nextLine();
                    continue;
                }
	        	if (!pattern.matcher(dob).matches()) {
                    System.out.print("Enter the Date (YYYY-MM-DD):");
                    dob = sc.nextLine();
                    continue;
                }
	        	 if (!email.contains("@")||!email.contains(".com")) {
	                    System.out.print("Enter the Email :");
	                    email = sc.nextLine();
	                    continue;
	                }
	        	 if (gender != 1 && gender != 2 && gender != 3) {
	                	try {
	                        System.out.print("Select your Gender \n1.Male\n2.Female\n3.Others\nEnter :");
	                        gender = sc.nextInt();
	               
	                    }catch (Exception e) {
	                        System.out.print("Enter the Given options in number only.");
	                        continue;
	                    }
	                } 
	        	 
	        	 if (!regexForPhone.matcher(phone).matches()) {
	                    System.out.print("Enter your Phone no :");
	                    phone = sc.nextLine();phone = sc.nextLine();
	                    
	                    continue;
	                }
	        	 if (password.length() < 4) {
	                    System.out.print("Enter the Password :");
	                    password = sc.nextLine();
	                    continue;
	                }
	               
	        	 else {
	        		 
	        		   stmt.setString(1, name);	                   
	                    stmt.setString(2, phone);                
	                    stmt.setInt(3, gender);
	                    stmt.setString(4, dob);
	                    stmt.setString(5, email);
	                    stmt.setString(6, password);
	        		 
	        		 if(stmt.executeUpdate()==1) {
	        			 System.out.println("signup successfull...!");
	        		 }
	        		 
	        		 Booking.main(null);
	        		 
	        		 break;
	        	 }
	        	
	        	
	        }
	        
	       
		
	}
	
	
	
	
	
	//---------------------------------------Sending mail Bus ticket--------------------


		public static void sendMail(String recepient,String msg) throws Exception {
			System.out.println("preparing ....");
			
			Properties properties = new Properties();
			
			 properties.put("mail.smtp.auth","true");
		        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
		        properties.put("mail.smtp.starttls.enable","true");
		        properties.put("mail.smtp.host","smtp.gmail.com");
		        properties.put("mail.smtp.port","587");
			
			String username = "bus.tic.book@gmail.com";
			String password = "thamirabarani";
			
			Session session = Session.getInstance(properties,new Authenticator(){
				
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
				
			});
			
			
			Message message = prepareMessage(session,username,recepient,msg);
			
				Transport.send(message);
				
			
			
		}
//		static String msg = "";
		
	static int OTP = 0;
		private static Message prepareMessage(Session session,String username, String recepient, String msg)
		 {
			Message message=new MimeMessage(session);
			
			try {
				message.setFrom(new InternetAddress(username));
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
				message.setSubject("Ticket Booked...!");
				
				message.setText(msg);
				
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			return message;
			
		}



		//-----------------------------------------------

	
}





/*
 * 
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		 Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/busTicReserv","root","");
		 
		 
		
		System.out.println("\t\tBUS TICKET RESERVATION . . .");
		
		
		while(true) {
		System.out.println("\n\t\t1.Login\n\t\t2.signup.\n\t\t3.Exit.");
		
		 
		 System.out.print("Enter Your Option : ");
		
		 option = sc.nextInt();
		
		 switch (option) {
         case 1:   
             login(con);
             break;
         case 2:
             signin(con);
             break;
         case 3:
             System.exit(0);
         default:
        	 System.out.println("Please give correct option ...");
             break;
     }
		 
		}
		 

		 
 */



/*
 * 
 * private static void login(Connection con) throws Exception {
		// TODO Auto-generated method stub
		System.out.print("Enter your Id no : ");
    	int id_no = sc.nextInt();
    	
    	System.out.print("Enter yout Password : ");
    	String password = sc.next();
    	
    	PreparedStatement stmt = con.prepareStatement("select * from users where id_no=?");
		
    	stmt.setInt(1, id_no);
    	ResultSet rs=stmt.executeQuery();
    	String loginAns = "";
    	while(rs.next()) {
    		
    		if(password.equals(rs.getString(7)) && id_no==(rs.getInt(1))) {
    			loginAns = ("\nlog in successfully !");
    			break;
    		}else {
    			loginAns = "\nWrong id no  or password";
    			
    		}
    	}
    	System.out.println(loginAns);
    	if(loginAns.equals("Wrong id no  or password")) {
    		login(con);
    	}else {
    		Booking.main(null);
    	}
	}
	
	private static void signin(Connection con) throws Exception {
		// TODO Auto-generated method stub
		
		
		
		PreparedStatement stmt = con.prepareStatement("insert into users(name,phone,gender,dob,email,password)values(?,?,?,?,?,?)");
		System.out.print("Name : ");
		 name = sc.nextLine();
		name = sc.nextLine();
		
		while(true) {

		System.out.print("Phone number : ");
		 phone = sc.nextLine();
		 boolean checkingPhoneNumber = false;
		 PreparedStatement stmt1 = con.prepareStatement("select phone from users");
		  ResultSet rs=stmt1.executeQuery(); 
		 while(rs.next()) {
			 String s = rs.getString(1);
			 if(phone.equals(s)) {
				 checkingPhoneNumber=true;
				 break;
			 }
			 
		 }
		 
		 if(checkingPhoneNumber==true) {
			 System.out.println("This number already Registered.\n");
			 
		 }else {	
     		 break;
		 }
		}
		
		System.out.println("Gender \n1.Male\n2.Female\n3.others");
		int gender = sc.nextInt();
		
		System.out.print("Email : ");
		String email = sc.next();
		
		System.out.println("Dob : ");
		String dob = sc.next();
		
		System.out.println("Password : ");
		password = sc.nextLine();
		password = sc.nextLine();
		
		stmt.setString(1, name);
        stmt.setString(2, phone);
        stmt.setInt(3, gender);
        stmt.setString(4, dob);
        stmt.setString(5, email);
        stmt.setString(6, password);
		
		stmt.executeUpdate();
		userdetail(con);
		System.out.println("Successfully registered!");
		
		id_number = hashMap.get("id_no");
		System.out.println("ID Number : "+id_number);
		
		
		Booking.main(null);
	}
  
	
	static String name;
	static String  password;
	static String id_number;
	static String phone;
	static void userdetail(Connection con) throws Exception{
		 hashMap=new HashMap<>();//map, 
		    PreparedStatement stmt=con.prepareStatement("select * from users where name=? and password=? and phone=?");
		    stmt.setString(1,name);
		    stmt.setString(2,password);
		    stmt.setString(3,phone);
		    
		    ResultSet rs=stmt.executeQuery();
		    while (rs.next()){
		        hashMap.put("id_no",rs.getString(1));
		        hashMap.put("name",rs.getString(2));
		        hashMap.put("phone",rs.getString(3));
		        hashMap.put("gender",rs.getString(4));
		        hashMap.put("dob",rs.getString(5));
		        hashMap.put("email",rs.getString(6));
		        hashMap.put("password",rs.getString(7));
		    }
		    
		    
		
	}
	
	
	
	



//---------------------------------------Sending mail Bus ticket--------------------


	public static void sendMail(String recepient,String msg) throws Exception {
		System.out.println("preparing ....");
		
		Properties properties = new Properties();
		
		 properties.put("mail.smtp.auth","true");
	        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
	        properties.put("mail.smtp.starttls.enable","true");
	        properties.put("mail.smtp.host","smtp.gmail.com");
	        properties.put("mail.smtp.port","587");
		
		String username = "bus.tic.book@gmail.com";
		String password = "thamirabarani";
		
		Session session = Session.getInstance(properties,new Authenticator(){
			
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
			
		});
		
		
		Message message = prepareMessage(session,username,recepient,msg);
		
			Transport.send(message);
			
		
		
	}
//	static String msg = "";
	
static int OTP = 0;
	private static Message prepareMessage(Session session,String username, String recepient, String msg)
	 {
		Message message=new MimeMessage(session);
		
		try {
			message.setFrom(new InternetAddress(username));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
			message.setSubject("Ticket Booked...!");
			
			message.setText(msg);
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return message;
		
	}



	//-----------------------------------------------


 */
















