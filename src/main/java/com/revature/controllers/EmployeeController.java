package com.revature.controllers;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dao.user.UserDao;
import com.revature.dao.user.UserDaoDb;
import com.revature.logging.Logging;
import com.revature.services.UserService;

public class EmployeeController {

	
	private static UserDao uDao = new UserDaoDb();

	private static UserService us = new UserService(uDao);
	
	public static void updateEmployeeInfo(HttpServletRequest req, HttpServletResponse res) {
		try {
			//read data posted
			StringBuilder buffer = new StringBuilder();
			BufferedReader reader = req.getReader();
			String line;

			while ((line = reader.readLine()) != null) {
				buffer.append(line);
				buffer.append(System.lineSeparator());
			}

			String data = buffer.toString();
			Logging.logDebugMessage("data: " + data);

			ObjectMapper mapper = new ObjectMapper();
			JsonNode parsedObj = mapper.readTree(data);

			int id = parsedObj.get("id").asInt();
			String firstName = parsedObj.get("firstName").asText();
			String lastName = parsedObj.get("lastName").asText();
			String username = parsedObj.get("username").asText();
			String password = parsedObj.get("password").asText();
			String email = parsedObj.get("email").asText();
			
			// call user service - get a boolean back if successful
			boolean result = us.updateUserInfo(id, firstName, lastName, username, password, email);
			
			System.out.println("result: " + result);
			
			if (result) {
				res.setStatus(200);
				Logging.logDebugMessage("User Update Successful: " + username);
				res.getWriter().write(new ObjectMapper().writeValueAsString("Update Successful"));

			} else {
				// Log the bad credentials, and display an error message to the user.
				Logging.logWarnMessage("Update Failed: " + username);
				res.getWriter().write(new ObjectMapper().writeValueAsString("Update Failure"));
				res.setStatus(res.SC_GONE);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
