/*
 * LibraryModel.java
 * Author:
 * Created on:
 */



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.swing.*;

public class LibraryModel {

    // For use in creating dialogs and making them modal
    private JFrame dialogParent;

    //The connection to the database
    Connection con;

    public LibraryModel(JFrame parent, String userid, String password) {
    	dialogParent = parent;
        try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/moshiewan_jdbc", userid, password);
			
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
    }

    public String bookLookup(int isbn) {
    	String output = "";
    	try {
			/* Create SQL query as string */
    		String select = "SELECT * FROM book WHERE book.isbn = "+isbn;
			Statement st1 = con.createStatement(); //create statement
			ResultSet rs1 = st1.executeQuery(select);
			/*Iterate over table Book and extract book name*/
			if(rs1.next()) {
				output += "Name of Book: "+rs1.getString("title"); //concatenate the name of book to output
			}
			else {
				return "No book found with that isbn"; //leave this whole method since no isbn found
			}

			/* Create SQL query as string */
    		String select2 = "SELECT a.name,a.surname, ba.authorseqno FROM book_author ba, author a "
    						+ "WHERE ba.isbn = "+isbn+" AND a.authorid = ba.authorid "
    						+ "ORDER BY authorseqno;";
			Statement st2 = con.createStatement(); //create statement
			ResultSet rs2 = st2.executeQuery(select2);

			/*Iterate over table BookAuthor and extract names of authors that have authored the selected book*/
			output += "\nAuthor/s: \n";
			while(rs2.next()) {
				output += rs2.getString("name") + rs2.getString("surname")+"\n";
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

    	return output;
    }

    public String showCatalogue() {
    	String output = "";
    	try {
			/* Create SQL query as string */
    		String select = "SELECT * FROM book";
			Statement st1 = con.createStatement(); //create statement
			ResultSet rs1 = st1.executeQuery(select);
			/*Iterate over table Book and extract book name*/
			while(rs1.next()) {
					output += "\nName of Book: "+rs1.getString("title")+ //concatenate the attributes of book to output
								"\nEdition Number: "+rs1.getString("edition_no")+
								"\nNumber of Copies Available: "+rs1.getString("numofcop")+
								"\nNumber of copies Left: "+rs1.getString("numleft")+
								"\n____________________________________________________________________\n";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	if(output.equals("")) { //If the result set is empty, print that no books were on loan
    		return "No books found";
    	}
		return output ;
    }

    public String showLoanedBooks() {
    	String output = "";
    	try {
			/* Create SQL query as string */
    		String select = "SELECT * FROM book b, cust_book cb "+
    						"WHERE cb.isbn = b.isbn";
			Statement st1 = con.createStatement(); //create statement
			ResultSet rs1 = st1.executeQuery(select);
			/*Iterate over result set and extract details about loaned books*/
			while(rs1.next()) {
				output += "\nCustomerId : "+rs1.getString("CustomerId")+ //concatenate the attributes to output
							"\nName of Book: "+rs1.getString("title")+
							"\nEdition Number: "+rs1.getString("edition_no")+
							"\nDue Date: "+rs1.getString("DueDate")+
							"\nNumber of copies Left: "+rs1.getString("numleft")+
							"\n____________________________________________________________________\n";
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
    	if(output.equals("")) { //If the result set is empty, print that no books were on loan
    		return "No books on loan";
    	}
    	return output;
    }

    public String showAuthor(int authorID) {
    	String output = "";
    	try {
			/* Create SQL query as string */
    		String select = "SELECT * FROM author a "+
    						"WHERE a.AuthorId = "+authorID;
			Statement st1 = con.createStatement(); //create statement
			ResultSet rs1 = st1.executeQuery(select);
			/*Iterate over Author table and extract attributes*/
			while(rs1.next()) {
				output += "\nAuthor Name : "+rs1.getString("Name")+ //concatenate the attributes to output
						"\nAuthor Surname: "+rs1.getString("Surname");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	if(output.equals("")) { //If the result set is empty, print that no authors exist under that ID
    		return "No authors found with that ID";
    	}
		return output;
    }

    public String showAllAuthors() {
    	String output = "";
    	try {
			/* Create SQL query as string */
    		String select = "SELECT * FROM author a "+
			"ORDER BY a.authorid";
			Statement st1 = con.createStatement(); //create statement
			ResultSet rs1 = st1.executeQuery(select);
			/*Iterate over Author table and extract attributes*/
			while(rs1.next()) {
				output += "\nAuthor Name : "+rs1.getString("Name")+ rs1.getString("Surname")+ //concatenate the attributes to output
						"\n_________________________________________________\n";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	if(output.equals("")) { //If the result set is empty, print that no authors exist
    		return "No authors found";
    	}
		return output;
    }

    public String showCustomer(int customerID) {
    	String output = "";
    	try {
			/* Create SQL query as string */
    		String select = "SELECT * FROM Customer c "+
    						"WHERE c.CustomerID = "+customerID;
			Statement st1 = con.createStatement(); //create statement
			ResultSet rs1 = st1.executeQuery(select);
			/*Iterate over Customer table and extract attributes*/
			while(rs1.next()) {
				output += "\nCustomer Name : "+rs1.getString("F_Name") +rs1.getString("L_Name")+ //concatenate the attributes to output
						"\nLives in: "+rs1.getString("City");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	if(output.equals("")) { //If the result set is empty, print that no Customer exist under that ID
    		return "No Customer found with that CustomerID";
    	}
		return output;
    }

    public String showAllCustomers() {
    	String output = "";
    	try {
			/* Create SQL query as string */
    		String select = "SELECT * FROM Customer c "+
			"ORDER BY c.CustomerID";
			Statement st1 = con.createStatement(); //create statement
			ResultSet rs1 = st1.executeQuery(select);
			/*Iterate over Customer table and extract attributes*/
			while(rs1.next()) {
				output += "\nCustomer Name : "+rs1.getString("F_Name")+ rs1.getString("L_Name")+ //concatenate the attributes to output
						"\nLives in: "+rs1.getString("City")+
						"\n_________________________________________________\n";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	if(output.equals("")) { //If the result set is empty, print that no customers exist
    		return "No Customer Found";
    	}
		return output;
    }

    
	/**
	 * Step 1.Check whether customer exists (lock customer if he/she exists)
	 * Step 2.Lock the book (if book exists and a copy is available)
	 * Step 3.Insert tuple in Cust_Book with the corresponding values
	 * Step 4.Update Book table
	 * Step5.Commit transaction if successful, else rollback
	 * */
    public String borrowBook(int isbn, int customerID, int day, int month, int year) {
    	String output = "";
    	try {
			con.setAutoCommit(false);
			con.setReadOnly(false);
			
			/****************** Step 1*****************/
    		if(showCustomer(customerID).equals("No Customer found with that CustomerID")) {
    			return "No Customer found with that CustomerID";
    		}
    		/******************* Step 2 ******************/
    		if(bookLookup(isbn).equals("No book found with that isbn")) {
    			return "No book found with that isbn";
    		}

    		
			/* Create SQL query as string */
    		String select = "SELECT b.NumLeft FROM Book b WHERE b.isbn = "+isbn + "FOR UPDATE";
			Statement st1 = con.createStatement(); //create statement
			ResultSet rs1 = st1.executeQuery(select);
			
			/*Iterate over Customer table and extract attributes*/
			if(rs1.next()) {
	    		//Book exists, but now we must check if a copy is available
				int numbOfCopiesLeft = rs1.getInt("NumLeft");
		    	//Check if book is available
				if(numbOfCopiesLeft > 0) {
			    	/******************* Step 3 ******************/
					/*Insert tuple into cust_book*/
					String insert1 = "INSERT INTO cust_book VALUES ("+isbn+", '"+year+ "-" +month+ "-" +day+" '," +customerID+ ")";
					st1.executeUpdate(insert1);
					
			    	/******************* Step 4 ******************/
					/*Reduce 1 from the value of numleft in relation Book*/
					String update1 = "UPDATE Book SET NumLeft = "+Integer.toString(numbOfCopiesLeft - 1)+ "WHERE isbn = "+isbn;
					st1.executeUpdate(update1);
					output += "Successfully updated";
				}
				else {
					output += "No more copies of this book left";
				}
				 JOptionPane.showMessageDialog(null, "Locked, Click 'OK' to proceed"); //between step 3 and 4 to lock transaction
			}

		    	/******************* Step 5 ******************/
				con.commit();
				con.setAutoCommit(true);;
				con.setReadOnly(true);

			} catch (SQLException e) {
				try {
					con.rollback(); //STEP 5: If unsuccessful, rollback
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
    	
    	return output;
    }

    public String returnBook(int isbn, int customerid) {
    	String output = "";
    	try {
			con.setReadOnly(false);
    		con.setAutoCommit(false);
    		
			/* Create SQL query as string */
    		String select = "SELECT * FROM Cust_Book cb WHERE cb.isbn = "+isbn+ "AND CustomerID = "+customerid+" FOR UPDATE";
    		Statement st1 = con.createStatement();
    		ResultSet rs1 = st1.executeQuery(select);
    	
    		if(rs1.next()) {
    			String select2 = "SELECT b.isbn, b.NumLeft FROM Book b WHERE b.isbn = " +isbn+ "FOR UPDATE";
    			ResultSet rs2 = st1.executeQuery(select2);
    			if(rs2.next()) {
    				int numbOfCopiesLeft = rs2.getInt("NumLeft");
    				
    				/*Delete the tuple from Cust_Book now that the customer has returned book*/
					String delete1 = "DELETE FROM Cust_Book WHERE isbn = "+isbn+"AND CustomerID = "+customerid;
					st1.executeUpdate(delete1);
					
					/*Increment the NumLeft attribute now that the book has been returned*/
					String update1 = "UPDATE Book SET NumLeft = "+Integer.toString(numbOfCopiesLeft + 1) + "WHERE isbn = "+isbn;
					st1.executeUpdate(update1);
					output += "Successfully updated";
    			}
    			else {
    				output += "ISBN does not exist";
    			}
				 JOptionPane.showMessageDialog(null, "Locked, Click 'OK' to proceed"); //lock transaction
    		}
    		else {
    			output += "Book loan not found";
    		}
    		
    		con.commit();
    		con.setAutoCommit(true);
    		con.setReadOnly(true);
    		
		} catch (SQLException e) {
			try {
				con.rollback(); //STEP 5: If unsuccessful, rollback
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
    	
    	return output;
    }

    public void closeDBConnection() {
    	try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    public String deleteCus(int customerID) {
    	return "Delete Customer";
    }

    public String deleteAuthor(int authorID) {
    	return "Delete Author";
    }

    public String deleteBook(int isbn) {
    	return "Delete Book";
    }
}