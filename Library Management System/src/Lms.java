import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
public class Lms 
{
	static Scanner sc=new Scanner(System.in);
	static Connection con;
	public static void main(String[] args) 
	{
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/library","root","kumar123");
			System.out.println("Connection to sql database succesful");
			while (true) 
			{
                System.out.println("---Welcome to Library Management System Of Our College---");
                System.out.println("1.Add New Book");
                System.out.println("2.View Books");
                System.out.println("3.Issue Book");
                System.out.println("4.Return Book");
                System.out.println("5.Exit");
                System.out.print("Enter your choice: ");
                int choice=sc.nextInt();
                sc.nextLine();
                switch(choice)
                {
                	case 1:
                		addBook();
                		break;
                	case 2:
                		viewBooks();
                		break;
                	case 3:
                		issueBook();
                		break;
                	case 4:
                		returnBook();
                		break;
                	case 5:
                		sc.close();
                		return;
                	default:
                		System.out.print("Invalid choice,Try again..");
                }
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
		static void addBook()
		{
			System.out.println("Enter Book Name");
			String title=sc.nextLine();
			System.out.println("Enter author Name");
			String author=sc.nextLine();
			try 
			{
				PreparedStatement st=con.prepareStatement("insert into books(title,author) values(?,?)");
				st.setString(1,title);
				st.setString(2,author);
				st.executeUpdate();
				System.out.println("Book added successfully");
			}
			catch(Exception e)
			{
				System.out.println("Error while adding Book");
			}
		}
		static void viewBooks()
		{
			try 
			{				
			PreparedStatement st=con.prepareStatement("select * from books");
			ResultSet rs=st.executeQuery();
				while(rs.next())
				{
					System.out.println("Id:"+rs.getInt("id")+"\n"+"Title:"+rs.getString("title")+"\n"+"Author:"+rs.getString("author")+"\n"+"Availability:"+rs.getBoolean("available"));
				}
			}
			catch (Exception e) 
			{
	            System.out.println("Error retrieving Books");
	        }
		}
		static void issueBook()
		{
				System.out.print("Enter Book Id to issue: ");
		        int bookId = sc.nextInt();
		        System.out.print("Enter user ID: ");
		        int userId = sc.nextInt();
		        try 
		        {
		        	PreparedStatement st=con.prepareStatement("select available from books where id=?");
		        	st.setInt(1, bookId);
		        	ResultSet rs=st.executeQuery();
		        	if(rs.next() && rs.getBoolean("available"))
		        	{
		        		PreparedStatement ist=con.prepareStatement("insert into books_issued(book_id,user_id) values(?,?)");
		        		ist.setInt(1, bookId);
		        		ist.setInt(2, userId);
		        		ist.executeUpdate();
		        		System.out.println("Book issued to candidate");
		        		PreparedStatement ust=con.prepareStatement("update books set available=false where id=?");
		        		ust.setInt(1,bookId);
		        		ust.executeUpdate();
		        	}
		        	else
		        	{
		        		System.out.println("Book not available");
		        	}
		        }
		        catch(Exception e)
		        {
		        	System.out.println("BookId is invalid");
		        }        		
		}
		static void returnBook()
		{
			System.out.println("Enter Book Id to return: ");
			int bookId=sc.nextInt();
			try 
			{
				PreparedStatement st=con.prepareStatement("select * from books_issued where book_id=? and return_date is null");
				st.setInt(1, bookId);
				ResultSet rs=st.executeQuery();
				if(rs.next())
				{
					int issueId = rs.getInt("id");
					PreparedStatement ist=con.prepareStatement("update books_issued set return_date=now() where id=?");
					ist.setInt(1, bookId);
					ist.executeUpdate();
					System.out.println("Book return success");
					PreparedStatement ust=con.prepareStatement("update books set available=true where id=?");
					ust.setInt(1, issueId);
					ust.executeUpdate();					
				}
			}
			catch(Exception e)
			{
				System.out.println("Error Occured");
			}			
		}
	}

