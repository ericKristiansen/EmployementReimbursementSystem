package com.revature.dao.reimbursement;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.revature.dao.user.USER;
import com.revature.models.Reimbursement;
import com.revature.models.User;
import com.revature.utils.ConnectionUtil;

public class ReimbursementDaoDb implements ReimbursementDao{
	
	private final static String SELECT_REIMB_TYPE_SQL = "SELECT REIMB_TYPE_ID, REIMB_TYPE FROM ERS_REIMBURSEMENT_TYPE;";
	private final static String SELECT_REIMB_STATUS_SQL = "SELECT REIMB_STATUS_ID, REIMB_STATUS FROM ERS_REIMBURSEMENT_STATUS;";
	private final static String NEW_REQUEST_SQL = "INSERT INTO ERS_REIMBURSEMENT (REIMB_AMOUNT, REIMB_SUBMITTED, REIMB_DESCRIPTION, "
			+ "REIMB_AUTHOR, REIMB_STATUS_ID, REIMB_TYPE_ID) VALUES (?, ?, ?, ?, ?, ?);";
	private final static String SELECT_REQUESTS_BY_USER_ID = "SELECT REIMB_ID, REIMB_AMOUNT, REIMB_SUBMITTED, REIMB_RESOLVED, "
			+ "REIMB_DESCRIPTION, REIMB_RECEIPT, REIMB_AUTHOR, REIMB_RESOLVER, REIMB_STATUS_ID, REIMB_TYPE_ID  "
			+ "FROM ERS_REIMBURSEMENT WHERE REIMB_AUTHOR = ?;";
	private final static String SELECT_REQUESTS_BY_STATUS_ID_AND_USER_ID = "SELECT REIMB_ID, REIMB_AMOUNT, REIMB_SUBMITTED, REIMB_RESOLVED, "
			+ "REIMB_DESCRIPTION, REIMB_RECEIPT, REIMB_AUTHOR, REIMB_RESOLVER, REIMB_STATUS_ID, REIMB_TYPE_ID  "
			+ "FROM ERS_REIMBURSEMENT WHERE REIMB_STATUS_ID = ? AND REIMB_AUTHOR = ?;";
	private final static String SET_REQUEST_STATUS_SQL = "UPDATE ERS_REIMBURSEMENT SET REIMB_STATUS_ID = ? WHERE REIMB_ID = ?;"; 
	private final static String GET_ALL_REQUESTS_SQL = "SELECT REIMB_ID, REIMB_AMOUNT, REIMB_SUBMITTED, REIMB_RESOLVED, "
			+ "REIMB_DESCRIPTION, REIMB_RECEIPT, REIMB_AUTHOR, REIMB_RESOLVER, REIMB_STATUS_ID, REIMB_TYPE_ID "
			+ "FROM ERS_REIMBURSEMENT;";
	private final static String GET_REQUESTS_BY_STATUS_ID = "SELECT REIMB_ID, REIMB_AMOUNT, REIMB_SUBMITTED, REIMB_RESOLVED, "
			+ "REIMB_DESCRIPTION, REIMB_RECEIPT, REIMB_AUTHOR, REIMB_RESOLVER, REIMB_STATUS_ID, REIMB_TYPE_ID "
			+ "FROM ERS_REIMBURSEMENT WHERE REIMB_STATUS_ID = ?;";
	
	private final static int APPROVED = 2;
	private final static int DENIED = 3;

	
	ConnectionUtil conUtil = ConnectionUtil.getConnectionUtil();
	
	@Override
	public HashMap<Integer, String> loadReimbursementTypes()
	{
		Connection con = conUtil.getConnection();
		HashMap<Integer, String> types = new HashMap<>();

		try {
			PreparedStatement ps = con.prepareStatement(SELECT_REIMB_TYPE_SQL);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				types.put(rs.getInt(1), rs.getString(2));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return types;
	}

	@Override
	public HashMap<Integer, String> loadReimbursementStatus() {
		Connection con = conUtil.getConnection();
		HashMap<Integer, String> status = new HashMap<>();

		try {
			PreparedStatement ps = con.prepareStatement(SELECT_REIMB_STATUS_SQL);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				status.put(rs.getInt(1), rs.getString(2));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return status;
	}

	@Override
	public boolean createNewRequest(int amount, Timestamp ts, String description, int userId, int statusId,
			int typeId) {
		boolean success = true;

		try {
			Connection con = conUtil.getConnection();
			//"INSERT INTO ERS_REIMBURSEMENT (REIMB_AMOUNT, REIMB_SUBMITTED, REIMB_DESCRIPTION, "
			//+ "REIMB_AUTHOR, REIMB_STATUS_ID, REIMB_TYPE_ID) VALUES (?, ?, ?, ?, ?, ?);";
			PreparedStatement ps = con.prepareStatement(NEW_REQUEST_SQL);
			ps.setInt(1, amount);
			ps.setTimestamp(2, ts);
			ps.setString(3, description);
			ps.setInt(4, userId);
			ps.setInt(5, statusId);
			ps.setInt(6, typeId);
			ps.execute();

		} catch (SQLException e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}

	@Override
	public List<Reimbursement> getReimbursementsByUserId(int userId) {
		Connection con = conUtil.getConnection();
		List<Reimbursement> requests = new ArrayList<Reimbursement>();

		try {
			PreparedStatement ps = con.prepareStatement(SELECT_REQUESTS_BY_USER_ID);
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				requests.add(populateReimbursement(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return requests;
	}
	
	
	private Reimbursement populateReimbursement(ResultSet rs) {
		Reimbursement tempReimb = null;
		try {
			tempReimb = new Reimbursement(rs.getInt(REIMBURSEMENT.ID.value()), rs.getInt(REIMBURSEMENT.AMOUNT.value()),
					 rs.getTimestamp(REIMBURSEMENT.SUBMITTED_TIMESTAMP.value()), rs.getTimestamp(REIMBURSEMENT.RESOLVED_TIMESTAMP.value()),
					rs.getString(REIMBURSEMENT.DESCRIPTION.value()), rs.getBlob(REIMBURSEMENT.RECIEPT_BLOB.value()),
					rs.getInt(REIMBURSEMENT.AUTHOR_ID.value()), rs.getInt(REIMBURSEMENT.RESOLVER_ID.value()),
					rs.getInt(REIMBURSEMENT.STATUS_ID.value()), rs.getInt(REIMBURSEMENT.TYPE_ID.value()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tempReimb;
	}


	@Override
	public List<Reimbursement> getReimbursementByStatusIdAndAuthorId(int statusId, int userId) {
		Connection con = conUtil.getConnection();
		List<Reimbursement> requests = new ArrayList<Reimbursement>();

		try {
			PreparedStatement ps = con.prepareStatement(SELECT_REQUESTS_BY_STATUS_ID_AND_USER_ID);
			ps.setInt(1, statusId);
			ps.setInt(2, userId);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				requests.add(populateReimbursement(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return requests;
	}

	@Override
	public boolean approveDenyRequest(int requestId, boolean isApproved) {
		
		boolean success = true;
		int statusId = isApproved ? APPROVED : DENIED;
		
		Connection con = conUtil.getConnection();

		try {
			PreparedStatement ps = con.prepareStatement(SET_REQUEST_STATUS_SQL);
			
			ps.setInt(1, statusId);
			ps.setInt(2, requestId);
			ps.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
			success = false;
		}
		
		return success;
	}
	
	
	@Override
	public List<Reimbursement> getAllReimbursements() {
		Connection con = conUtil.getConnection();
		List<Reimbursement> requests = new ArrayList<Reimbursement>();

		try {
			PreparedStatement ps = con.prepareStatement(GET_ALL_REQUESTS_SQL);			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				requests.add(populateReimbursement(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return requests;
	}

	@Override
	public List<Reimbursement> getReimbursementsByStatus(int statusId) {
		Connection con = conUtil.getConnection();
		List<Reimbursement> requests = new ArrayList<Reimbursement>();

		try {
			PreparedStatement ps = con.prepareStatement(GET_REQUESTS_BY_STATUS_ID);
			ps.setInt(1, statusId);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				requests.add(populateReimbursement(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return requests;
	}
	
}
