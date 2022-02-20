package com.web.jdbc.connection_test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

// TEST CLASS FOR CONNECTION POOL 

@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Define datasource/connection pool for Resource Injection
	
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Step 1: Set up printwriter
		PrintWriter out = response.getWriter();
		response.setContentType("text/plain");
		
		// Step 2: Get a connection to the db
		Connection myConn = null;
		Statement myStm = null;
		ResultSet myRs = null;
		
		try {
			myConn = dataSource.getConnection();
			
			// Step 3: Create a SQL Statement
		    String sql = "SELECT * FROM student";
			myStm = myConn.createStatement();
			
		    // Step 4: Execute SQL query
		    myRs = myStm.executeQuery(sql);
			
		    // Step 5: Process the result set
		    while(myRs.next()) {
		    	String email = myRs.getString("email");
		    	out.println(email);
		    }
		    
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}

}
