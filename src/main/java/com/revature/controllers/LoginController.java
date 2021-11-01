package com.revature.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import com.revature.logging.Logging;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dao.user.UserDao;
import com.revature.dao.user.UserDaoDb;
import com.revature.models.User;
import com.revature.services.UserService;

public class LoginController {

	private static UserDao uDao = new UserDaoDb();

	private static UserService uServ = new UserService(uDao);

	public static void login(HttpServletRequest req, HttpServletResponse res)
			throws JsonProcessingException, IOException, ServletException {
		try {
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

			String username = parsedObj.get("username").asText();
			String password = parsedObj.get("password").asText();

			User tmpUser = uServ.login(username, password);

			if (tmpUser != null) {
				// save session information
				req.getSession().setAttribute("id", tmpUser.getId());
				res.setStatus(200);
				Logging.logDebugMessage("User Log In Successful" + username);
				res.getWriter().write(new ObjectMapper().writeValueAsString(tmpUser));

			} else {
				// Log the bad credentials, and display an error message to the user.
				Logging.logWarnMessage("Invalid Credentials: " + username + "; " + password);
				res.setStatus(res.SC_GONE);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static void register(HttpServletRequest req, HttpServletResponse res) {
		
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

			String firstName = parsedObj.get("firstName").asText();
			String lastName = parsedObj.get("lastName").asText();
			String username = parsedObj.get("username").asText();
			String password = parsedObj.get("password").asText();
			String email = parsedObj.get("email").asText();
			
			// call user service - get a boolean back if successful
			boolean result = uServ.register(firstName, lastName, username, password, email);
			
			String successMessage = "Registration Successful";
			String failureMessage = "Registration Failure";
			
			if (result) {
				res.setStatus(200);
				Logging.logDebugMessage(successMessage + " : " + username);
				res.getWriter().write(new ObjectMapper().writeValueAsString(successMessage));

			} else {
				// Log the bad credentials, and display an error message to the user.
				Logging.logWarnMessage(failureMessage + " : " + username);
				res.getWriter().write(new ObjectMapper().writeValueAsString(failureMessage));
				res.setStatus(res.SC_GONE);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static void getAllUsers(HttpServletRequest req, HttpServletResponse res) {
		
		try {
			
			// call user service - get a boolean back if successful
			List<User> users = uServ.getAllUsers();
			
			String successMessage = "Get all Users Successful";
			String failureMessage = "Get all Users Failure";
			
			if (users != null) {
				res.setStatus(200);
				Logging.logDebugMessage(successMessage);
				res.getWriter().write(new ObjectMapper().writeValueAsString(users));

			} else {
				// Log the bad credentials, and display an error message to the user.
				Logging.logWarnMessage(failureMessage);
				res.getWriter().write(new ObjectMapper().writeValueAsString(failureMessage));
				res.setStatus(res.SC_GONE);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
