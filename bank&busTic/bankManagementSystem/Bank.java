package bankManagementSystem;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.sql.*;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import busReservation.*;

public class Bank {
//    private static final Connection Connection = null;
	BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    public static void main(String[] args) throws  Exception{
    	
    	
    	
        Class.forName("com.mysql.cj.jdbc.Driver");
        int ch=0;
        Bank main=new Bank();
        Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","");
        
        
       
        
        while (true) {
            System.out.println("----------Bank Management System----------");
            System.out.println("1.Create\n2.Signin\n3.Exit");
            System.out.print("Enter your option:");
            try {
                ch = Integer.parseInt(main.bufferedReader.readLine());
                System.out.println(ch);
            }catch (Exception e){
                System.out.println("Please Enter the right choice.");
                continue;
            }
            switch (ch) {
                case 1:
                    main.createUser(con);
                    
                    
                    break;
                case 2:
                    main.login(con);
//                    main.loginUser(con);
                    break;
                case 3:
                    System.exit(0);
                default:
                    break;
            }
        }
    }
    
    
    





	private void login(Connection con) throws Exception {
		
    	
    	Scanner sc = new Scanner(System.in);
    	
    	System.out.print("Enter your Account Number : ");
    	int acc_no = sc.nextInt();
    	
    	System.out.print("Enter yout Password : ");
    	String password = sc.next();
    	
    	
    	 PreparedStatement stmt = con.prepareStatement("select * from users where acc_no=?");
    	
    	 stmt.setInt(1, acc_no);
    	 ResultSet rs=stmt.executeQuery();
    	 
    	 while(rs.next()) {
    		 String name = rs.getString(2);
    		 String aadhar = rs.getString(3);
    		 if(rs.getString(9).equals(password)) {
    			 System.out.println("password correct");
    			 Dashboard dashboard = new Dashboard( aadhar , con, bufferedReader);
                 dashboard.main();
    			 
//				loginUser(con,name,acc_no);
    			 break;
    		 }
    		 else {
    			 System.out.println("Incorrect Password");
    			 login(con);
    			 
    		 }
    	 }
   		
	}




	public void createUser(Connection con) throws Exception{
        Pattern pattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
        Pattern regexForPhone = Pattern.compile("[0-9]{10}");
        Pattern regexForAadhar = Pattern.compile("[0-9]{12}");
        PreparedStatement stmt=con.prepareStatement("insert into users(name,aadhar,phone,amount,gender,dob,email,password)values(?,?,?,?,?,?,?,?)");
        String name ="",dob="",email="",password="",phone="",aadhar="";
        int amount=0,gender=0;
        while (true){
           
                if (name.length() == 0) {
                    System.out.print("Enter the name :");
                    name = bufferedReader.readLine().trim();
                    continue;
                }
                if (!pattern.matcher(dob).matches()) {
                    System.out.print("Enter the Date (YYYY-MM-DD):");
                    dob = bufferedReader.readLine();
                    continue;
                }
                if (!email.contains("@")||!email.contains(".com")) {
                    System.out.print("Enter the Email :");
                    email = bufferedReader.readLine();
                    continue;
                }
                if (gender != 1 && gender != 2 && gender != 3) {
                	try {
                        System.out.print("Select your Gender \n1.Male\n2.Female\n3.Others\nEnter :");
                        gender = Integer.parseInt(bufferedReader.readLine());
                    }catch (Exception e) {
                        System.out.print("Enter the Given options in number only.");
                        continue;
                    }
                } 
                
                if (!regexForPhone.matcher(phone).matches()) {
                    System.out.print("Enter your Phone no :");
                    phone = bufferedReader.readLine();
                    continue;
                }
                if (!regexForAadhar.matcher(aadhar).matches()) {
                    System.out.print("Enter your Adhaar no :");
                    aadhar = bufferedReader.readLine();
                    continue;
                }
                
                if (password.length() < 4) {
                    System.out.print("Enter the Password :");
                    password = bufferedReader.readLine();
                    continue;
                }
               
               
                
                if (String.valueOf(amount).contains("-") || String.valueOf(amount).length() == 0 || (String.valueOf(amount).length() == 1 && String.valueOf(amount).equals("0"))) {
                	try {
                    System.out.print("Enter your Amount :");
                    amount = Integer.parseInt(bufferedReader.readLine());
                    
                    }catch(Exception e) {
                	  System.out.println("Please enter correct value...");
                	  continue;
                    }
                }
               
                else {
                    stmt.setString(1, name);
                    stmt.setString(2, aadhar);
                    stmt.setString(3, phone);
                    stmt.setInt(4, amount);
                    stmt.setInt(5, gender);
                    stmt.setString(6, dob);
                    stmt.setString(7, email);
                    stmt.setString(8, password);
                    
                    
                    
                    
                    int gnotp= 0;
                    
                    Random random = new Random();
       			 OTP =  random.nextInt(899999)+100000; 
                    msg = "Your OTP is : " + OTP + "\nEnter this OTP to create account in our bank. \n\n\nNOTE : Please do not share your OTP with anyone.";
                    
                    if (stmt.executeUpdate() == 1) {
                    	 sendMail(email);
                       while(true) {	 
                    	 try {
                         System.out.print("Enter OTP : ");
                         Scanner sc = new Scanner(System.in);
                         gnotp = sc.nextInt();
                         if(gnotp != OTP) {
                        	 System.out.println("please enter correct OTP ...");
                        	 continue;
                         }
                         break;
                         
                         
                    	 }catch(Exception e) {
                    		 continue;
                    	 }
                       } 
                         if(gnotp==OTP) {
                         
                         
                         
                        System.out.println("\nData Inserted Successfully.\n");
                        Dashboard dashboard = new Dashboard( aadhar , con, bufferedReader);
                        dashboard.main();
                         }
                         else {
                        	 System.out.println("incorrect OTP.");
                        	 createUser(con);
                         }
                    } else {
                        System.out.println("Failed.");
                    }
                    break;
                }
          
        }
    }
	
	
	
	
	
	static int balAmount = -1;
	public static void onlineTrans(int acc_no, String password, int price) throws Exception {
		
		
		
		if(Dashboard.verifyaccount(acc_no)) {
			
		  	
		 if(authentication(acc_no,password)) {	
			
			if(Dashboard.verifyAmount(price)) {
				
				Class.forName("com.mysql.cj.jdbc.Driver");	       
		        Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","");	        
		        PreparedStatement stmt = con.prepareStatement("select amount from users where acc_no="+acc_no);
				ResultSet rs = stmt.executeQuery();
				
				while(rs.next()) {
					balAmount = rs.getInt(1);
				}				
				balAmount = balAmount - price;
				
				 stmt = con.prepareStatement("update users set amount= "+balAmount+" where acc_no = "+acc_no);
				stmt.executeUpdate();				
				System.out.println("amount geted from the Bank....");
				
				
				
				
			
			}else {
				System.out.println("You didn't have enough amount...\nTry another Account");
				Booking.amountTransaction(price);
			}
		  }else {
			 System.out.println("Wrong Password");
			 Booking.amountTransaction(price);
		 }
			
		}else {
			System.out.println("Wrong Account Number");
			Booking.amountTransaction(price);
	
		}
	}
	
	
	
	
	
public static boolean authentication(int acc_no,String password) throws Exception {
	Class.forName("com.mysql.cj.jdbc.Driver");
   
    Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","");
    
    PreparedStatement stmt = con.prepareStatement("select password from users where acc_no="+acc_no);
    
    
    ResultSet rs=stmt.executeQuery();
    String pass = "";
    while(rs.next()) {
    	
    	if(rs.getString(1).equals(password)) {
    		return true;
    	}
    }
    
  
    return false;
    
    
	
	
	
}
	
	
	
	
	









//	---------------------------------------Sending mail otp verification.--------------------
	
	public static void sendMail(String recepient) throws Exception {
		System.out.println("preparing ....");
		
		Properties properties = new Properties();
		
		 properties.put("mail.smtp.auth","true");
	        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
	        properties.put("mail.smtp.starttls.enable","true");
	        properties.put("mail.smtp.host","smtp.gmail.com");
	        properties.put("mail.smtp.port","587");
		
		String username = "otp.verify.bank@gmail.com";
		String password = "thamirabarani";
		
		Session session = Session.getInstance(properties,new Authenticator(){
			
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
			
		});
		
		
		Message message = prepareMessage(session,username,recepient);
		
			Transport.send(message);
			
		
		
	}
	static String msg = "";
	
static int OTP = 0;
	private static Message prepareMessage(Session session,String username, String recepient)
	 {
		Message message=new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(username));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
			message.setSubject("Royal mint of Spain");
			
//			int rnd = (int) (Math.random()*50000 + Math.random()*50000 + 100000);
			
			
			
			message.setText(msg);
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return message;
		
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//---------------------------------------Sending mail Bus ticket from bank--------------------


			public static void sendMailFromBank(String recepient,String msg) throws Exception {
				System.out.println("preparing ....");
				
				Properties properties = new Properties();
				
				 properties.put("mail.smtp.auth","true");
			        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
			        properties.put("mail.smtp.starttls.enable","true");
			        properties.put("mail.smtp.host","smtp.gmail.com");
			        properties.put("mail.smtp.port","587");
				
			        String username = "otp.verify.bank@gmail.com";
					String password = "thamirabarani";
				
				Session session = Session.getInstance(properties,new Authenticator(){
					
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
					
				});
				
				
				Message message = prepareMessage(session,username,recepient,msg);
				
					Transport.send(message);
					
				
				
			}
//			static String msg = "";
			
		
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



















//-----------------------------------------------















