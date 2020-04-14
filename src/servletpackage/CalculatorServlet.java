package servletpackage;

import java.io.IOException;

import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HttpServlet implementation class CalculatorServlet
 */
@SuppressWarnings("deprecation")
//@ServletSecurity(@HttpConstraint( rolesAllowed = {"admin", "guest"}))
@WebServlet(description = "This is a sample servlet", urlPatterns = { "/CalculatorServlet" })
public class CalculatorServlet extends HttpServlet implements SingleThreadModel {

	private static final long serialVersionUID = 1L;
	private int count =0;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		increment();

		// Define the variables
		
		float result = 0;
		
		float in1=0;
		float in2=0;
		PrintWriter write  = response.getWriter();
		String operation = request.getParameter("operation");

		if (operation == null || (operation = htmlFilter(operation.trim())).length() == 0)
			operation = null;

		try{
			in1 = Float.parseFloat(request.getParameter("in1"));
			in2 = Float.parseFloat(request.getParameter("in2"));
		} catch(NumberFormatException ex){
			write.println(	"<b>Error! Please enter numeric values!"); 
		}

		try{

			switch(operation){

			case "summation": 
				result = sum(in1,in2);
				break;
			case "subtraction": 
				result = subtract(in1,in2);
				break;
			case "multiplication": 
				result = multiply(in1,in2);
				break;
			case "division": 

				try{
					result = divide(in1,in2);
				}catch(Exception e){
					write.println(	"<b>Error! Please enter nonzero values!"); 
				}
				break;
			}
		} catch(NullPointerException npe){
			write.println(	"<b>Error! Please go back and select an operation!"); 
		}

		write.println(	"<b>The "+operation+" of "  + in1 + " and " + in2+ " is <i>" + result+"</i></b>");
		/*
		 * write.println("<br><br><a href=\"https://ca.linkedin.com/in/berksoysal\">"+
		 * "<img src = \"image.png\" width=\"99\" height=\"76\" alt=\"LinkedIn\"/></a>"
		 * );
		 */

		decrement();
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}
	
	
	// Filter the string for special HTML characters to prevent command injection attack
	private static String htmlFilter(String message) {
		if (message == null) return null;
		int len = message.length();
		StringBuffer result = new StringBuffer(len + 20);
		char aChar;

		for (int i = 0; i < len; ++i) {
			aChar = message.charAt(i);
			switch (aChar) {
			case '<': result.append("&lt;"); break;
			case '>': result.append("&gt;"); break;
			case '&': result.append("&amp;"); break;
			case '"': result.append("&quot;"); break;
			default: result.append(aChar);
			}
		}
		return (result.toString());
	}

	protected synchronized float sum(float a, float b){
		return a+b;
	}

	protected synchronized float subtract(float a, float b){
		return a-b;
	}

	protected synchronized float multiply(float a, float b){
		return a*b;
	}

	protected synchronized float divide(float a, float b){
		return a/b;
	}
	
	protected synchronized void increment(){
		count++;
	}
	
	protected synchronized void decrement(){
		count--;
	}

	protected synchronized int getCount(){
		return count;
	}
}
