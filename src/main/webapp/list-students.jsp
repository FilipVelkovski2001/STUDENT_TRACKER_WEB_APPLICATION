<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix = "c" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Student Tracker App</title> 
<link type="text/css" rel="stylesheet" href="css/style.css">
</head>

<body>

     <div id="wrapper">
         <div id="header">
             <h2 align="center">Apex University</h2> 
         </div>    
     </div>
 
     <div id="container">
         <div id="content">
         
         <!-- put new button for add student -->
         
         <input type="button" value="Add Student"
         onclick="window.location.href='add-student-form.jsp'; return false;"
         class="add-student-nutton" />
         
             <table>
                 <tr>
                     <th>First Name</th>
                     <th>Last Name</th>
                     <th>E-Mail</th>
                     <th>Action</th>
                 </tr>
                 
                <c:forEach var="tempStudent" items="${STUDENT_LIST}">  
                 
                 <!-- Set up a link for updating each student -->
                 <c:url var="tempLink" value="StudentControllerServlet">
                     <c:param name="command" value="LOAD" />
                     <c:param name="studentId" value="${tempStudent.id}" />                          
                 </c:url>                 
              	  
              	  <!-- Set up a link to delete each student -->
              	   <c:url var="deleteLink" value="StudentControllerServlet">
                     <c:param name="command" value="DELETE" />
                     <c:param name="studentId" value="${tempStudent.id}" />                          
                 </c:url>
              	            	 
                 <tr>                                          
                      <!-- Insert the students in the table cells -->
                	  <td> ${tempStudent.firstName} </td>
                	  <td> ${tempStudent.lastName} </td>
                	  <td> ${tempStudent.email} </td>
                	  <!-- Links for updating and deleting the student we connect the variables
                	  to an href -->
                	  <td> <a href="${tempLink}">Update</a>             
                	  <!-- We are also going to use JavaScript to prompt the user if he is sure to delete the student. -->   	 
                	   <a href= "${deleteLink}" onclick="if (!(confirm('Are you sure you want to delete this student?'))) return false">
                	   | Delete</a>
                	   
                	   </td>
                 </tr>
                	 
                </c:forEach>               
             </table>
         </div>
     </div> 

</body>
</html>