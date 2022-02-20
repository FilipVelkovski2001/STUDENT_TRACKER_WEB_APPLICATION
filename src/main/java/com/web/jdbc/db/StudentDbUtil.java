package com.web.jdbc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.web.jdbc.model.Student;

public class StudentDbUtil {
	
	// Give a reference to the db data source defined in the xml file
	private DataSource dataSource;
	
	// Create a constructor that uses the dataSource
	public StudentDbUtil(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	// Create a getStudent method that returns a list of all the students
	public List<Student> getStudents() throws Exception {
	    	
		// Create a new ArrayList of students inside the method
		List<Student> students = new ArrayList<>();
		
		// Initialize the jdbc variables we need
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			
		   	// Get a connection to the db
		    myConn = dataSource.getConnection();
		    
		    // Create a sql statement
		    String sql = "SELECT * FROM student ORDER BY last_name;";
		    
		    myStmt = myConn.createStatement();
		    
		    // Execute the query
            myRs = myStmt.executeQuery(sql);
            
		    // Process the ResultSet
            while(myRs.next()) {
            	
            	// Retrieve data from result set row
            	int id = myRs.getInt("id");
            	String firstName = myRs.getString("first_name");
            	String lastName = myRs.getString("last_name");
            	String email = myRs.getString("email");
            	
            	// Create new Student Object using the constructor from Student
            	// And pass the values from the ResultSet
            	Student tempStudent = new Student(id, firstName, lastName, email);
            	
            	// Add it to the list of stdents
            	students.add(tempStudent);
            
            }
            
		    		
			// Return the student list
		    return students;
		    
		} finally {
			
			// Close JDBC Statements
			close(myConn, myStmt, myRs);
		}	
		
	}

	// Method for closing the JDBC Statements
	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
		
		try{
			// We check if they are not null, and if so then we close them
			if(myRs != null) {
				myRs.close();
			}
			
			if(myStmt != null) {
				myStmt.close();
			}
			if(myConn != null) {
				myConn.close(); // Doesn't really close it.. just puts it back in connection pool
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	public void addStudent(Student theStudent) throws Exception {
		
		// Setup the JDBC Objects
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			// Get db connection
			myConn = dataSource.getConnection();
			
		    // Create sql for inserting a student
		    String sql = "INSERT INTO student "
		    		     + "(first_name, last_name, email) "
		    		     + "VALUES (?, ?, ?);";
		    
		    myStmt = myConn.prepareStatement(sql);
		    
		    // Set the param values for placeholders
		    myStmt.setString(1, theStudent.getFirstName());
		    myStmt.setString(2, theStudent.getLastName());
		    myStmt.setString(3, theStudent.getEmail());
		    
		    // Execute sql insert
		    myStmt.execute();
		    
		} finally {
			// Clean up JDBC objects
			close(myConn, myStmt, null);
		}
		 
		
	}

	public Student getStudent(String theStudentId) throws Exception {
		
		Student theStudent = null;
		
		// Create JDBC Objects
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		int studentId;
		
		try {
			
			
			// Convert theStudentId String to int
		    studentId = Integer.parseInt(theStudentId);
	
			// Get connection to DB
			myConn = dataSource.getConnection();
			
			// Create SQL to get selected student
			String sql = "SELECT * FROM student WHERE id=?;";
			
			// Create prepared statement
			myStmt = myConn.prepareStatement(sql);
			
			// Set parameters for prepared statement
			myStmt.setInt(1, studentId);
			
			// Execute query
			myRs = myStmt.executeQuery();
				
			// Retrieve data from result set
			if(myRs.next()) {
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				// use the studentId during construction 
				theStudent = new Student(studentId, firstName, lastName, email);
				
			} else {
				throw new Exception("Could not find student id: "+ studentId);
				
			}
				
			return theStudent;
			
		} finally {
			close(myConn, myStmt, myRs);
		}
			
	}

	public void updateStudent(Student theStudent) throws Exception {
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		    try {
		         		    
		        // Get DB Connection
		        myConn = dataSource.getConnection();

		        // Create SQL Update Statement
		        String sql = "UPDATE student "
				            +"SET first_name=?, last_name=?, email=? " 
		                    +"WHERE id=?";
		
		        // Prepare statement
		        myStmt = myConn.prepareStatement(sql);
		
		        // Set parameters
		        myStmt.setString(1, theStudent.getFirstName());
		        myStmt.setString(2, theStudent.getLastName());
		        myStmt.setString(3, theStudent.getEmail());
		        myStmt.setInt(4, theStudent.getId());
		
		        // Execute SQL Statement
                myStmt.execute();
            } finally {	    	
		    	
		      	// Close JDBC Objects
			    close(myConn, myStmt, null);
        }	   	
	}

	public void deleteStudent(String theStudentId) throws Exception {
		
		// Define JDBC Variables
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			// Convert student id to integer
			int studentId = Integer.parseInt(theStudentId);
			 
			// Get connection to DB
			myConn = dataSource.getConnection();
			
			// Get a sql to delete student
			String sql = "DELETE FROM student WHERE id=?";
			
			// Prepare statement
			myStmt = myConn.prepareStatement(sql);
			
			// Set up parameters
			myStmt.setInt(1, studentId);
			
			// Execute the sql statement
			myStmt.execute();
			
		} finally {
			// Clean up JDBC objects
			close(myConn, myStmt, null);
		}
		
		
	}
}   
