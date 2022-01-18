package busReservation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.Pattern;

import bankManagementSystem.Bank;

import java.time.LocalDate; // import the LocalDate class

public class Booking {
static Scanner sc = new Scanner(System.in);
static String bus_name = "",email= "", from = "", to = "" , msg = "", date="",phone="";
static int noOfSeats = 1 , ticprice ;
static  Pattern pattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		 Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/busTicReserv2","root","");
		 
		System.out.println("Booking page....");
		
		
		
		System.out.println("\n\t1.Search\n\t2.Bus lists.\n\t3.Exit");
		 System.out.print("\nEnter your option : ");
		 String option = sc.nextLine();
		
      
		 switch(option) {
		 case "1":
			
			
			 while(true) {
				 bus_name = search(con);
				 if(bus_name.equals("null") || bus_name.equals("")) {
					 continue;
				 }else {
					 break;
				 }
			 }
			 
			 ticprice = noOfSeats*ticprice(con,bus_name);
			 
			 ticBooking(con,bus_name);
			 
			 
			 break;
		 case "2":			 
			  bus_name = (busList(con));
			  
			  ticprice = noOfSeats* ticprice(con,bus_name);
			  ticBooking(con,bus_name);
			 break;
		 case "3":
			 System.out.println("\n\t\t---------------------------------------------");
			 System.exit(0);
		default:
			System.out.println("Please enter correct option.");
			Booking.main(null);
		 }
		 
		 
		 Main.sendMail(email,msg);
		 
		 System.out.println("Booked Successfully....\nPlease check your Email.");
		 
		
	}		
	
	
	
	
	private static int ticprice(Connection con, String bus_name2) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(ticprice);
		 PreparedStatement stmt=con.prepareStatement("select price,start,end from bus_list where bus_name=\""+bus_name2+"\"");
			
			ResultSet rs= stmt.executeQuery();
			
			while(rs.next()) {
				from = rs.getString(2);
				to = rs.getString(3);
				return rs.getInt(1);
			}
		
	    return 0;
		
	}




	/**
	 * 
	 */
	static int i = 0;	
		 private static void ticBooking(Connection con, String bus_name2) throws Exception {
		// TODO Auto-generated method stub
			 
			 
			 LocalDate currentDate = LocalDate.now(); // Create a date object
			 String datearr[] = String.valueOf(currentDate).split("-");  // datearr[0] == year , [1]==month, [2]==day
			 int dd = Integer.parseInt(datearr[2]);
			 int mm = Integer.parseInt(datearr[1]);
			 int yyyy = Integer.parseInt(datearr[0]);
			 
			 
			while(true) {
			 if (!pattern.matcher(date).matches()) {
                 System.out.print("Enter the Date (YYYY-MM-DD):");
                 date = sc.next();
                 
    			 
                 
                 continue;
             }else {
            	 break;
             }
			} if(checkingDate(dd,mm,yyyy,date,con,bus_name2)) {
			 
			 PreparedStatement stmt=con.prepareStatement("select count(*) from "+bus_name2+" where date=\""+date+"\"");
				
				ResultSet rs= stmt.executeQuery();
				int seat_count = -1;
				
				while(rs.next()) {
					seat_count = rs.getInt(1);
					
				}
				System.out.println("Booked seats : "+seat_count);
			 System.out.println("Available seats : "+(20-seat_count));
			 
			 
	
		System.out.print("Enter the number of seats : ");
		 noOfSeats = sc.nextInt();
		 ticprice *= noOfSeats;
		 
		 
		if(20-seat_count>= noOfSeats) {
			 
			for( i=1; i<=noOfSeats; i++) {
			 
			 while(true) {
			 int seat_no = seatselection(con,bus_name2,date);
			 if((seat_no)!=-1) {
				 
				 System.out.println("seat selected...");
				 ticbooking(con,bus_name2,date,seat_no);
			    break;
		    	 }
	     	 }
			 
			 
			 
			 
			}	 
			 
			 
		} 
			 
			 
			 
			 
		 
			}else {
				ticBooking(con,bus_name2);
			}
			
			
			
			
			
			
	}

		 
	/**
	 * 	 
	 * @param con
	 * @param bus_name2
	 * @param date2
	 * @param seat_no
	 * @throws Exception
	 * 
	 * 
	 * booking ticket ,   passenger's detail
	 */
		 
		 
		 private static void ticbooking(Connection con, String bus_name2, String date2, int seat_no) throws Exception {
			// TODO Auto-generated method stub
			 Pattern regexForPhone = Pattern.compile("[0-9]{10}");
			 String name="";
			 int age=0,gender=0;
			 
			 
			 
			 PreparedStatement stmt=con.prepareStatement("insert into "+bus_name2+"(seat_no,name,age,gender,phone,email,date)values(?,?,?,?,?,?,?)");
				
			 
			 while(true) {
				 if (name.length() == 0) {
	                    System.out.print("Enter Your Name :");
	                    name = sc.nextLine();
	                    name = sc.nextLine();
	                    continue;
	                }
				  if(age==0) {
					  System.out.print("age : ");
					  age = sc.nextInt();
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
				  
				  
				  if (!regexForPhone.matcher(phone).matches() && i==1) {
	                    System.out.print("Enter your Phone no :");
	                    phone = sc.nextLine();phone = sc.nextLine();
	                    
	                    continue;
	                }
				  if (!email.contains("@")||!email.contains(".com") && i==1) {
	                    System.out.print("Enter the Email :");
	                    email = sc.nextLine();
	                    continue;
	                }
				  
				 
				 
				 else {
					 
					 msg += "\nSeat Number : "+seat_no+"\nFrom : "+from+"\nTo : "+to+"\nNAME : "+name+"\nAGE : "+age+"\nPhone Number : "+phone+"\nTravelling Date : "+date+"\nemail : "+email+"\n\n\t\tHappy Journey...! ";    	 
			        	
					 
					 if(i==1) {
					 amountTransaction(ticprice);//price  function
					 }
					 
					 stmt.setInt(1, seat_no);       
			         stmt.setString(2, name);       
			         stmt.setInt(3, age);
			         stmt.setInt(4, gender); 
			         stmt.setString(5, phone);
			         stmt.setString(6, email);
			         stmt.setString(7, date);
			         
			         if (stmt.executeUpdate() == 1) {
			        	 System.out.println("");
			         }
					 
					 break;
				 }
			 }
			 
//			 System.out.print("Pasenger Name : ");name = sc.nextLine();name = sc.nextLine();
//			 System.out.print("Age : "); age = sc.nextInt();
//			 System.out.println("gender : ");gender= sc.nextInt();
//			 
//			 if(i==1) {
//			 System.out.println("phone : "); phone = sc.nextLine();phone = sc.nextLine();
//			 System.out.println("email : ");email = sc.nextLine();
//			 }
			 
//			 amountTransaction(price);//price  function
//			 
			
			
		}


public static void amountTransaction(int ticprice) throws Exception {
		// TODO Auto-generated method stub
	System.out.print("Enter your Account Number : ");
	int acc_no = sc.nextInt();
	
	System.out.print("Enter your Password : ");
	String password = sc.nextLine();
	password = sc.nextLine();
	
	Bank.onlineTrans(acc_no,password,ticprice);
	
		
	}




/**
 * 
 * @param con
 * @param bus_name2
 * @param date
 * @return  seat number
 * @throws Exception
 * 
 * 
 * shows bus seats,  selecting seats
 */


		private static int seatselection(Connection con, String bus_name2, String date) throws Exception {
			// TODO Auto-generated method stub
			 PreparedStatement stmt=con.prepareStatement("select * from "+bus_name2+" where date = \""+date+"\"");
				
				String ticsList[] = {"1-E","2-E","3-E","4-E","5-E","6-E","7-E","8-E","9-E","10-E","11-E","12-E","13-E","14-E","15-E","16-E","17-E","18-E","19-E","20-E"}; 
			   	    ResultSet rs=stmt.executeQuery();
			   	    while(rs.next()) {
			   	    	
			   	    	ticsList[rs.getInt(1)-1] = (rs.getInt(1)+"-"+rs.getString(4));
			   	    	
			   	    }
			   	    
			   	   for(int i=1; i<=20; i++) {
			   		   if(i==11) {
			   			   System.out.println("");
			   		   }
			   		   System.out.print("\t"+ticsList[i-1]);
			   		   if(i%5==0) {
			   			   System.out.println("");
			   		   }
			   		   
			   	   }
			   	   
			   	   
			   	   
			   	  
			   	boolean valid = true;    
			   	
		     	
		     	
		     	 int seatno = -1;
			   	 System.out.print("select your seat number : ");
			   	    seatno = sc.nextInt();
			   	
			   	  stmt=con.prepareStatement("select seat_no from "+bus_name2+" where date = \""+date+"\"");
			     	rs=stmt.executeQuery();
			     	 
			   	   
		     	while(rs.next()) {
		     		
		     		
		     		if(rs.getInt(1)==seatno || seatno<1 || seatno >20) {
		     			System.out.println("This seat already booked...(or) please select valid seat.");
		     			valid = false;
		     			break;
		     			
		     		}else {
		     			valid = true;
		     		}
		     	}
		     	
		     	if(valid==true) {
		     		return seatno;
		     	}else {
		     		
		     		return -1;
		     		
		     	}
		     	
		     	
		}
		
		
		
		/**
		 * 
		 * @param dd
		 * @param mm
		 * @param yyyy
		 * @param date2
		 * @param con
		 * @param bus_name2
		 * @return
		 * @throws Exception
		 * 
		 * 
		 * 
		 * checking given date is a valid.   it does nat take past dates...
		 */

		private static boolean checkingDate(int dd, int mm, int yyyy, String date2, Connection con, String bus_name2) throws Exception {
			// TODO Auto-generated method stub
			String gdatearr[] = String.valueOf(date).split("-");  // datearr[0] == year , [1]==month, [2]==day
            int d = Integer.parseInt(gdatearr[2]);
			 int m = Integer.parseInt(gdatearr[1]);
			 int y = Integer.parseInt(gdatearr[0]);
			 if((yyyy==y && mm ==m && d> dd) || (yyyy==y && m>mm) || (y>yyyy)) {
				 date = date;
				 return true;
			 }else {
				 System.out.println("Sorry no tickets for this day...");
				 System.out.print("\nEnter the Date (YYYY-MM-DD):");
                 
				 date = sc.next();
				return false;
				 
			 }
			
		}
		
		
		/**    
		 * 
		 * @param con
		 * @return bus name
		 * @throws Exception
		 */

		private static String search(Connection con) throws Exception {
				// TODO Auto-generated method stub
				System.out.print("from : ");
				String from = sc.nextLine();
//				from = sc.nextLine().toLowerCase();
				
				System.out.print("To : ");
				String to = sc.nextLine().toLowerCase();
				
				PreparedStatement stmt =  con.prepareStatement("select * from bus_list");
				
				ResultSet rs = stmt.executeQuery();
				int count = 0;
				while(rs.next()) {
					if(rs.getString(2).equals(from) && rs.getString(3).equals(to)) {
						System.out.println("AVAILABLE BUSES..");
					System.out.println(rs.getString(1)+" : " + rs.getString(2)+" - " + rs.getString(3)+", time : "+rs.getString(4)+", Price : "+rs.getInt(5));
					count++;
					return rs.getString(1);
				}
				}
				
				if(count==0) {
					System.out.println("Sorry no buses available...");					
					return "null";
				}else {
					count = 0;
					return "null";
				}
				
				
			}		 
	
		 
	
	private static String busList(Connection con) throws Exception {
		// TODO Auto-generated method stub
		PreparedStatement stmt=con.prepareStatement("select * from bus_list");
		ResultSet rs = stmt.executeQuery();
		int i = 1;
		
		while(rs.next()) {
			System.out.println("\n"+i + ". "+rs.getString(1)+" : " + rs.getString(2)+" - " + rs.getString(3)+", time : "+rs.getString(4)+", Price : "+rs.getInt(5));
		   i++;
		}
        
		
		System.out.print("\nEnter option : ");
		String option = sc.nextLine();
		if(option.equals("1")){
			return "tnstc";
			}
		if(option.equals("2")) {
			return "cheran_travels";
		}
		if(option.equals("3")) {
			return "kpn_travels";
		}
		else {
			   System.out.println("\nPlease give correct option ...");
			   busList(con);
				return null;
			}
		
		
	}
	

}




//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------


/*System.out.println("\n\t\tBooking page");
       
       Class.forName("com.mysql.cj.jdbc.Driver");
		 Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/busTicReserv","root","");
		 
		 System.out.println("\n\t1.Search\n\t2.Bus lists.");
		 System.out.print("\nEnter your option : ");
		 int option = sc.nextInt();
       
		 switch(option) {
		 case 1:
			 search(con);
			 break;
		 case 2:
			 busList(con);
			 break;
		default:
			System.out.println("Please enter correct option.");
			Booking.main(null);
		 }
		 
		 ticBooking(con);*/




/*
 * /**	ticket booking  class
 * bus seats lists,
 * amountTransaction()
 * DB values insert
 * sendingMail()
 * 
 * 
	static String email;
	static String msg="----";
	static String busName="",from="",to="";
	private static void ticBooking(Connection con) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Enter bus name : ");
		 busName = sc.nextLine();
		 String date="";
		 int count = 0;
		 System.out.print("Date : "); date = sc.next();
		PreparedStatement stmt=con.prepareStatement("select count(*) from "+busName+" where date=\""+date+"\"");
		
		ResultSet rs= stmt.executeQuery();

		
		while(rs.next()) {
			count = rs.getInt(1);
			System.out.println(rs.getString(1));
		}
		
		stmt=con.prepareStatement("select price,start,end from bus where name=\""+busName+"\"");
		rs = stmt.executeQuery();
		int price = 0;
		while(rs.next()) {
			price = rs.getInt(1);
			from = rs.getString(2);
			to = rs.getString(3);
		}
		
		
		System.out.println(" Total number of Seats : 20 \n Available : "+(20-count)+"\n Booked : "+count);
		if(count<20) {
			
		  stmt=con.prepareStatement("insert into "+busName+"(name,age,gender,phone,email,date)values(?,?,?,?,?,?)");
		
		 String name="",phone="",seat_no="";
		 int age=0,gender=0;
		 
		 System.out.print("Pasenger Name : ");name = sc.nextLine();name = sc.nextLine();
		 System.out.print("Age : "); age = sc.nextInt();
		 System.out.println("gender : ");gender= sc.nextInt();
		 System.out.println("phone : "); phone = sc.nextLine();phone = sc.nextLine();
		 System.out.println("email : ");email = sc.nextLine();
		 
		 amountTransaction(price);//price  function
		 
		 stmt.setString(1, name);       
         stmt.setInt(2, age);       
         stmt.setInt(3, gender);
         stmt.setString(4, phone);     
         stmt.setString(5, email);
         stmt.setString(6, date);
         
         if (stmt.executeUpdate() == 1) {//if values inserted       	 
        	 stmt=con.prepareStatement("select seat_no from "+busName+" where name=\""+name+"\" and age="+age+" and email=\""+email+"\"");// fetching seat number
        	 rs = stmt.executeQuery();
        	 while(rs.next()) {       		 
        		seat_no = rs.getString(1); 
        	 }      	
        	// creating message for ticket - email 
        	 msg = "Seat Number : "+seat_no+"\nFrom : "+from+"\nTo : "+to+"\nNAME : "+name+"\nAGE : "+age+"\nPhone Number : "+phone+"\nTravelling Date : "+date+"\nemail : "+email+"\n\n\t\tHappy Journey...! ";    	 
        	 Main.sendMail(email,msg);       	 
        	 System.out.println("\nBooked Successfully...\nPlease check your E-mail.");
         }
		}else {
			System.out.println("No seats available...");
		}
		
		
	}
	
	
	

	
	
	
	
	/**
	 * amountTransaction()
	 * it get acc no  from user and password
	 * cheching acc number and passsword.
	 * verifyAmount()
	 * takingamount()
	 * 
	 * 
	
	public static void amountTransaction(int price) throws Exception {
		// TODO Auto-generated method stub
		
		 Class.forName("com.mysql.cj.jdbc.Driver");
		 Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","");
	        
		
		 
		 Scanner sc = new Scanner(System.in);
	    	
	    	System.out.print("Enter your Account Number : ");
	    	int acc_no = sc.nextInt();
	    	
	    	System.out.print("Enter your Password : ");
	    	String password = sc.nextLine();
	    	password = sc.nextLine();
	    	
	    	Bank.onlineTrans(acc_no,password,price);
	    	
		
		
	}
	
	
	
	
	
	
	
	
	
	
	

	private static void busList(Connection con) throws Exception {
		// TODO Auto-generated method stub
		PreparedStatement stmt =  con.prepareStatement("select * from bus");
		ResultSet rs = stmt.executeQuery();
		
		while(rs.next()) {
			System.out.println(rs.getString(1)+" : " + rs.getString(2)+" - " + rs.getString(3)+", time : "+rs.getString(4)+", Price : "+rs.getInt(5));
		}
		
		busName = sc.nextLine();
	}
	private static void search(Connection con) throws Exception {
		// TODO Auto-generated method stub
		System.out.print("from : ");
		String from = sc.nextLine();
		from = sc.nextLine().toLowerCase();
		
		System.out.print("To : ");
		String to = sc.nextLine().toLowerCase();
		
		PreparedStatement stmt =  con.prepareStatement("select * from bus");
		
		ResultSet rs = stmt.executeQuery();
		
		while(rs.next()) {
			if(rs.getString(2).equals(from) && rs.getString(3).equals(to)) {
				System.out.println("AVAILABLE BUSES..");
			System.out.println(rs.getString(1)+" : " + rs.getString(2)+" - " + rs.getString(3)+", time : "+rs.getString(4)+", Price : "+rs.getInt(5));
		}
		}
		
		
	}
*/