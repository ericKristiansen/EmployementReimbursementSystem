package com.revature.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dao.reimbursement.ReimbursementDao;
import com.revature.dao.reimbursement.ReimbursementDaoDb;
import com.revature.logging.Logging;
import com.revature.models.Reimbursement;
import com.revature.services.ReimbursementService;

public class ReimbursementController {
	
	private static ReimbursementDao rDao = new ReimbursementDaoDb();
	private static ReimbursementService  rs = new ReimbursementService(rDao); 
	private final static int PENDING_REQUEST_STATUS = 1;
	private final static int APPROVED_REQUEST_STATUS = 2;
	private final static int DENIED_REQUEST_STATUS = 3;
	
	
	public static void handleReimbursements(HttpServletRequest req, HttpServletResponse res) throws JsonProcessingException, IOException {
		
		if (req.getMethod().equals("GET"))
		{
			List<Reimbursement> reimbursements = rs.getAllReimbursements();
			res.getWriter().write(new ObjectMapper().writeValueAsString(reimbursements));
		}
		else {
			
			StringBuilder buffer = new StringBuilder();
			BufferedReader reader = req.getReader();
			
			String line;
			while((line = reader.readLine()) != null) {
				buffer.append(line);
				buffer.append(System.lineSeparator());
			}
			
			String data = buffer.toString();
			ObjectMapper mapper = new ObjectMapper();
			JsonNode parsedObj = mapper.readTree(data);
			
			//int userId = Integer.parseInt(parsedObj.get()))
			
			
		}
	}


	public static void submitNewRequest(HttpServletRequest req, HttpServletResponse res) {
		
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

			//reimbursement data
			int userId = parsedObj.get("id").asInt();
			int amount = parsedObj.get("amount").asInt();
			String description = parsedObj.get("description").asText();
			int typeId = parsedObj.get("typeId").asInt();
			Timestamp ts = new Timestamp(new Date().getTime());
			
			// call user service - get a boolean back if successful
			boolean result = rs.createRequest(amount, ts, description, userId, PENDING_REQUEST_STATUS, typeId);
			
			String successMessage = "New Request Successful";
			String failureMessage = "New Request Failure";
			List<Reimbursement> reimbursements = rs.getReimbursementsByUserId(userId);
			
			if (result) {
				res.setStatus(200);
				Logging.logDebugMessage(successMessage);
				res.getWriter().write(new ObjectMapper().writeValueAsString(reimbursements));

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


	public static void getRequestsByUserId(HttpServletRequest req, HttpServletResponse res) {
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

			//reimbursement data
			int userId = parsedObj.get("id").asInt();
			
			String successMessage = "Requests Obtained Successfully";
			String failureMessage = "Request Obtainment Failure";
			List<Reimbursement> reimbursements = rs.getReimbursementsByUserId(userId);
			
			if (reimbursements != null) {
				res.setStatus(200);
				Logging.logDebugMessage(successMessage);
				res.getWriter().write(new ObjectMapper().writeValueAsString(reimbursements));

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


	public static void getRequestsByStatusIdAndUserId(HttpServletRequest req, HttpServletResponse res) {
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

			//reimbursement data
			int userId = parsedObj.get("id").asInt();
			int statusId = parsedObj.get("statusId").asInt();
			
			String successMessage = "Requests Obtained Successfully";
			String failureMessage = "Request Obtainment Failure";
			List<Reimbursement> reimbursements = rs.getReimbursementByStatusIdAndAuthorId(statusId, userId);
			
			if (reimbursements != null) {
				res.setStatus(200);
				Logging.logDebugMessage(successMessage);
				res.getWriter().write(new ObjectMapper().writeValueAsString(reimbursements));

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


	public static void approveRequestForUser(HttpServletRequest req, HttpServletResponse res) {
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

			//reimbursement data
			int userId = parsedObj.get("userId").asInt();
			int requestId = parsedObj.get("requestId").asInt();
			boolean isApproved = parsedObj.get("isApproved").asBoolean();
			
			boolean success = rs.approveDenyRequest(requestId, isApproved);
			
			String successMessage = "Requests Obtained Successfully";
			String failureMessage = "Request Obtainment Failure";
			List<Reimbursement> reimbursements = rs.getReimbursementsByUserId(userId);
			
			if (success && reimbursements != null) {
				res.setStatus(200);
				Logging.logDebugMessage(successMessage);
				res.getWriter().write(new ObjectMapper().writeValueAsString(reimbursements));

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


	public static void getAllRequests(HttpServletRequest req, HttpServletResponse res) {
		try {
			String successMessage = "Requests Obtained Successfully";
			String failureMessage = "Request Obtainment Failure";
			List<Reimbursement> reimbursements = rs.getAllRequests();
			
			if (reimbursements != null) {
				res.setStatus(200);
				Logging.logDebugMessage(successMessage);
				res.getWriter().write(new ObjectMapper().writeValueAsString(reimbursements));

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


	public static void approveRequest(HttpServletRequest req, HttpServletResponse res) {
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

			//reimbursement data
			int requestId = parsedObj.get("requestId").asInt();
			boolean isApproved = parsedObj.get("isApproved").asBoolean();
			
			boolean success = rs.approveDenyRequest(requestId, isApproved);
			
			String successMessage = "Requests Obtained Successfully";
			String failureMessage = "Request Obtainment Failure";
			List<Reimbursement> reimbursements = rs.getAllReimbursements();
			
			if (success && reimbursements != null) {
				res.setStatus(200);
				Logging.logDebugMessage(successMessage);
				res.getWriter().write(new ObjectMapper().writeValueAsString(reimbursements));

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


	public static void getRequestsByStatusId(HttpServletRequest req, HttpServletResponse res) {
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


			int statusId = parsedObj.get("statusId").asInt();
			
			String successMessage = "Requests Obtained Successfully";
			String failureMessage = "Request Obtainment Failure";
			List<Reimbursement> reimbursements = rs.getReimbursementsByStatusId(statusId);
			
			if (reimbursements != null) {
				res.setStatus(200);
				Logging.logDebugMessage(successMessage);
				res.getWriter().write(new ObjectMapper().writeValueAsString(reimbursements));

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
