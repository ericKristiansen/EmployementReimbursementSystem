package com.revature.dao.reimbursement;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import com.revature.models.Reimbursement;


public interface ReimbursementDao {
	
	
	//Read
	HashMap<Integer, String> loadReimbursementTypes();
	HashMap<Integer, String> loadReimbursementStatus();

	boolean createNewRequest(int amount, Timestamp ts, String description, int userId, int statusId, int typeId);

	List<Reimbursement> getReimbursementsByUserId(int userId);

	List<Reimbursement> getAllReimbursements();

	List<Reimbursement> getReimbursementByStatusIdAndAuthorId(int statusId, int userId);

	boolean approveDenyRequest(int requestId, boolean isApproved);
	List<Reimbursement> getReimbursementsByStatus(int statusId);
	

}
