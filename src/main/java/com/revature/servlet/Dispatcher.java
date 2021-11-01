package com.revature.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.revature.controllers.EmployeeController;
import com.revature.controllers.LoginController;
import com.revature.controllers.ReimbursementController;
import com.revature.logging.Logging;

public class Dispatcher {

	public static void process(HttpServletRequest req, HttpServletResponse res) throws JsonProcessingException, IOException, ServletException {
		
		String requestURI = req.getRequestURI();
		Logging.logDebugMessage("Servlet Dispatcher - requestURI: " + requestURI);
		
		switch (requestURI)
		{
		case "/ERS/api/login":
			System.out.println("ers login match case...");
			LoginController.login(req, res);
			break;
		case "/ERS/api/register":
			LoginController.register(req, res);
			break;
		case "/ERS/api/updateUserInfo":
			EmployeeController.updateEmployeeInfo(req, res);
			break;
		case "/ERS/api/submitNewRequest":
			ReimbursementController.submitNewRequest(req, res);
			break;
		case "/ERS/api/getRequestsByUserId":
			ReimbursementController.getRequestsByUserId(req, res);
			break;
		case "/ERS/api/getRequestsByStatusIdAndUserId":
			ReimbursementController.getRequestsByStatusIdAndUserId(req, res);
			break;
		case "/ERS/api/getAllUsers":
			LoginController.getAllUsers(req, res);
			break;
		case "/ERS/api/approveRequestForUser":
			ReimbursementController.approveRequestForUser(req, res);
			break;
		case "/ERS/api/getAllRequests":
			ReimbursementController.getAllRequests(req, res);
			break;
		case "/ERS/api/approveRequest":
			ReimbursementController.approveRequest(req, res);
			break;
		case "/ERS/api/getRequestsByStatusId":
			ReimbursementController.getRequestsByStatusId(req, res);
			break;
		default: 
			System.out.println("no case match...");
			res.sendRedirect("./login");
			break;
		}
		
		System.out.println("line 40");
		
	}
}
