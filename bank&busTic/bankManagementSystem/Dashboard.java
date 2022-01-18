package bankManagementSystem;

import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



public class Dashboard {
	
	 private static final String PreparedStatement = null;
	static Connection con;
	    static String adhar;
	    static HashMap<String,String>hashMap;
	    static BufferedReader bufferedReader;
	    
	    
	    
	    public Dashboard(String adhaar, Connection connection, BufferedReader bufferedReader) {
	        this.adhar=adhaar;
	        this.con=connection;
	        this.bufferedReader=bufferedReader;
	}
	    


public static void main() throws Exception {
    int ch=0;
    while (true) {
        fetchUserData();
        System.out.println("---------------Account----------------");
        System.out.println("\nName : " + hashMap.get("name"));
        System.out.println("Account no : " + hashMap.get("acc_no"));
        System.out.println("Balance : " + hashMap.get("amount"));
        System.out.println("\n\t\t\t1.Withdraw\n\t\t\t2.deposit\n\t\t\t3.Transaction\n\t\t\t4.Edit Profile\n\t\t\t5.Exit");
        
        
        try {
        	System.out.print("Enter your options :");
            ch=Integer.parseInt(bufferedReader.readLine());
        
        }catch(Exception e) {
        	continue;
        }
        switch(ch) {
        case 1:
        	System.out.println("1");
        	if(verifyAmount()) {
        		if(Integer.parseInt(hashMap.get("amount")) > amount) {
        			withdraw();
        			
        		}
        	}
        	break;
        case 2:
        	System.out.println("2");
        	if(verifyAmount()) {
        		deposit();
        			
        		}
        	break;
        case 3:
        	System.out.println("3");
        	transaction();
        	
        	break;
        case 4:
        	System.out.println("4");
        	editprofile();
        	break;
        case 5:
        	
        	System.exit(0);
        	break;
      	
        }
        
    }
}  


private static void editprofile() throws Exception {

	Scanner sc = new Scanner(System.in);
	
		try {
		
		System.out.println("\n\t\t\t1.Name.\n\t\t\t2.Phone Number.\n\t\t\t3.Aadhar Number.\n\t\t\t4.Date of birth.\n\t\t\t5.Gender\n\t\t\t6.Email\n\t\t\t7.password.\n\t\t\t8.Exit");
		System.out.print("Enter your option : ");
		int val = sc.nextInt();
		if(val==8) {
			System.exit(0);
		}
		
		String word = (val==1)?"name":(val==2)?"phn_no":(val==3)?"aadhar":(val==4)?"dob":(val==5)?"gender":(val==6)?"email":(val==7)?"password":"";
		if(word.equals("")) {
			System.out.println("Please give correct option.");
			editprofile();
		}
		
		PreparedStatement stmt =  con.prepareStatement("update users set "+word+"=? where acc_no=?");
		
		
		if(val==5) {
			System.out.println("\t\t\t1.male\n\t\t\t2.female\n\t\t\t3.others");
			System.out.print("Enter your option : ");
			int newval = sc.nextInt();
			
			stmt.setInt(1, newval);
			
		}
		else {
			
			System.out.println("Enter new value : ");
			String newval = sc.next();
			
			stmt.setString(1, newval);
			
		}
		
		stmt.setString(2, hashMap.get("acc_no"));
		
		if(stmt.executeUpdate()==1) {
		System.out.println("changed Successfully...!");
		}
		editprofile();		
		}catch(Exception e) {
			System.out.println("Please give correct value...");
			editprofile();
			
		}
		
}





public static int recAcc = 0;
public static int amount1 = 0;
public static String recEmail = "";
private static void transaction() throws Exception {
	
	Scanner sc = new Scanner(System.in);
	
	
	try {
		
	System.out.print("Enter receiver Account number : ");
	
	recAcc = sc.nextInt();
//	verifyaccount(recAcc);
    
	
	}catch(Exception e) {
		transaction();
		
	}
	
	
	
	
	
	if(verifyaccount(recAcc)) {
	
	
	if(verifyAmount()) {
		
		 amount1 =  amount;
		
		amount = Integer.parseInt(hashMap.get("amount")) - amount;
		
		
		
		PreparedStatement stmt =  con.prepareStatement("update users set amount=? where acc_no=?");
		
		int acc = Integer.parseInt(hashMap.get("acc_no"));
		stmt.setInt(1, amount);
		stmt.setInt(2, acc);
		
		stmt.executeUpdate();
		
		
		stmt =  con.prepareStatement("select * from users");
	       
	       ResultSet rs = stmt.executeQuery();
	       
	       while(rs.next()) {
	    	   if(rs.getInt(1) == recAcc) {
	    		   amount = rs.getInt(5);
	    		   recEmail = rs.getString(8);
	    		   break;
	    	   }
	       }
		amount = amount + amount1;
		
     stmt =  con.prepareStatement("update users set amount=? where acc_no=?");
		

		stmt.setInt(1, amount);
		stmt.setInt(2, recAcc);
		
		stmt.executeUpdate();
		
		
		Bank.msg = "your A/C "+recAcc + " is debited by Rs."+amount1+" balance is Rs."+amount+".";
      Bank.sendMail(recEmail);
		
		
		
		System.out.println("Transcation Successfully.....");
		
	}
	}
	
	
	
}






private static void deposit() throws Exception {
	// TODO Auto-generated method stub
	System.out.println("Deposited Successfully...");
	amount = Integer.parseInt(hashMap.get("amount")) + amount;
	System.out.println("Balance : "+  amount );
	
	 
     
	
	PreparedStatement stmt = con.prepareStatement("update users set amount=? where acc_no=?");
	
	int acc = Integer.parseInt(hashMap.get("acc_no"));
	stmt.setInt(1, amount);
	stmt.setInt(2, acc);
	
	stmt.executeUpdate();
	
}






private static void withdraw() throws Exception {
	// TODO Auto-generated method stub
	
	System.out.println("Withdraw Successfully...");
	amount = Integer.parseInt(hashMap.get("amount")) - amount;
	System.out.println("Balance : "+  amount );
	
	 
     
	
	PreparedStatement stmt = con.prepareStatement("update users set amount=? where acc_no=?");
	
	int acc = Integer.parseInt(hashMap.get("acc_no"));
	stmt.setInt(1, amount);
	stmt.setInt(2, acc);
	
	stmt.executeUpdate();
	
	
	
	
}






public static void fetchUserData() throws Exception{
    hashMap=new HashMap<>();//map, dict
    PreparedStatement stmt=con.prepareStatement("select * from users where aadhar=?");
    stmt.setString(1,adhar);
    ResultSet rs=stmt.executeQuery();
    while (rs.next()){
        
        hashMap.put("name",rs.getString(2));
        hashMap.put("aadhar",rs.getString(3));
        hashMap.put("phone",rs.getString(4));
        hashMap.put("amount",String.valueOf(rs.getInt(5)));
        hashMap.put("gender",rs.getString(6));
        hashMap.put("dob",rs.getString(7));
        hashMap.put("email",rs.getString(8));
        hashMap.put("password",rs.getString(9));
    }
}

static int amount = -1;
public static boolean verifyAmount() {
	Scanner sc = new Scanner(System.in);
	
	while(true) {
    try{		
	System.out.print("Enter your amount : ");
	 amount = sc.nextInt();
	 if(amount>=0) {
	 return true;
	 }else {
		 Exception e;
	 }
    }
    catch(Exception e) {
    	System.out.print("Re-enter");
    	continue;
    	
    	   	
    }
	
    
    
	}
}
 static boolean verifyaccount(int recAcc) throws Exception{
	 Class.forName("com.mysql.cj.jdbc.Driver");
     
     Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","");
     
	 
	 
    PreparedStatement stmt = con.prepareStatement("select acc_no from users where acc_no=?");
    stmt.setInt(1, recAcc);
    ResultSet rs = stmt.executeQuery();
    return !rs.next()?false:true;
}
 
 
 
 public static boolean verifyAmount(int price) {
		
		
		while(true) {
	    try{		
		amount = price;
		 if(amount>=0) {
		 return true;
		 }else {
			 Exception e;
		 }
	    }
	    catch(Exception e) {
	    	System.out.print("Re-enter");
	    	continue;
	    	
	    	   	
	    }
		
	    
	    
		}
	}


}