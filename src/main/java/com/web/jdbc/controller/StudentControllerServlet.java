package com.web.jdbc.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.web.jdbc.db.StudentDbUtil;
import com.web.jdbc.model.Student;

@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// First we set up a reference for the student db util
	private StudentDbUtil studentDbUtil;
	
	// Make use of Java EE Resource Injection
	// It passes the connection to the DataSource variable
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	
	// The init method is called by Tomcat when the server is first initialized
	@Override
	public void init() throws ServletException {
		super.init();
		
		// Create the student db util ... and pass in the conn pool / datasurce
		try {
			studentDbUtil = new StudentDbUtil(dataSource);
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			// Read the "command" parameter
			String theCommand = request.getParameter("command");
			
			// If the command is missing, then default to listing students
			if(theCommand == null) {
				theCommand = "LIST";
			}
			
			// Route it to the appropriate method
			switch(theCommand) {
			
			// In case the command is LIST then we call the listStudents method
			case "LIST":
				listStudents(request, response);
				break;
				
			// In case the command is ADD then we call the addStudent method
			case "ADD":
				addStudent(request,response);
				break;
			
			// In case the command is LOAD we call the loadStudent method
			case "LOAD":
				loadStudent(request,response);
				break;
				
			// In case the command is DELETE we call the deleteStudent method
			case "DELETE":
				deleteStudent(request,response);
				break;
				
			// In case the command is UPDATE we call the updateStudent method
			case "UPDATE":
				updateStudent(request,response);
				break;	
				
			// The default method will be listStudents (if they send over a bad command this will be our default)	
			default:
				listStudents(request, response);
			}
			
			
		    
		    
		} catch(Exception e) {
			throw new ServletException(e);
		}
		
	}

	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		// Read the student id from the form data
		String theStudentId = request.getParameter("studentId");
		
		// Delete student from database
		studentDbUtil.deleteStudent(theStudentId);
		
		// Send them back to "list-students.jsp" page
		listStudents(request, response);
	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// Read student info from the form data 
		int id = Integer.parseInt(request.getParameter("studentId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		// Create a new student object
		Student theStudent = new Student(id, firstName, lastName, email);
		
		// Perform UPDATE on Database
		studentDbUtil.updateStudent(theStudent);
		
		// Send them back to the list-students.jsp
		listStudents(request, response);
	}

	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// Read the student id from the form data
		String theStudentId = request.getParameter("studentId");
		
		// Get the student from the DB Util
		Student theStudent = studentDbUtil.getStudent(theStudentId);
				
		// Place student in the request attribute
		request.setAttribute("THE_STUDENT", theStudent);
		
		// Send this to JSP page: update-student-form.jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
	}

	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// Read student info from form data
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		// Create a new student object
		Student theStudent = new Student(firstName, lastName, email);
		
		// Add the student to the database
		studentDbUtil.addStudent(theStudent);
		
		// Send back to main page (the student list)
		listStudents(request, response);
	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// Get students from DB util
		List<Student> students = studentDbUtil.getStudents();
		
		// Add the students to request object
		request.setAttribute("STUDENT_LIST", students);
		
		// Send it to the JSP Page (View)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
	}

}
