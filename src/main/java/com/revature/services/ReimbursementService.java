package com.revature.services;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import com.revature.dao.reimbursement.ReimbursementDao;
import com.revature.models.Reimbursement;
import com.revature.models.display.ReimbursementDisplay;

public class ReimbursementService {

		private ReimbursementDao rDao;
		private static HashMap<Integer, String> reimbursementTypes = new HashMap<>();
		private static HashMap<Integer, String> reimbursementStatus = new HashMap<>();
		
		public ReimbursementService(ReimbursementDao rDao)
		{
			this.rDao = rDao;
			loadReimbursementTypes();
			loadReimbursementStatus();
		}
		
		public void loadReimbursementStatus()
		{
			reimbursementStatus = rDao.loadReimbursementStatus();
		}
		
		public void loadReimbursementTypes()
		{
			reimbursementTypes = rDao.loadReimbursementTypes();
		}
		
		public static HashMap<Integer, String> getReimbursementTypes() {return reimbursementTypes;}
	
		public static HashMap<Integer, String> getReimbursementStatus() {return reimbursementStatus;}
		
		public BufferedImage getBufferedImageReciept(Blob blob) {
			InputStream in = null;
			BufferedImage tmp = null;
			try {
				in = blob.getBinaryStream();
				tmp = ImageIO.read(in);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}
			return tmp;
		}

		public List<Reimbursement> getAllReimbursements() {
			return rDao.getAllReimbursements();
		}
		
		public boolean createRequest(int amount, Timestamp ts, String description, int userId, int statusId,
				int typeId) {
			return rDao.createNewRequest(amount, ts, description, userId, statusId, typeId);
		}

		public List<Reimbursement> getReimbursementsByUserId(int userId) {
			return rDao.getReimbursementsByUserId(userId);
		}


		public List<Reimbursement> getReimbursementByStatusIdAndAuthorId(int statusId, int userId) {
			return rDao.getReimbursementByStatusIdAndAuthorId(statusId, userId);
		}

		public boolean approveDenyRequest(int requestId, boolean isApproved) {
			return rDao.approveDenyRequest(requestId, isApproved);
		}

		public List<Reimbursement> getAllRequests() {
			return rDao.getAllReimbursements();
		}

		public List<Reimbursement> getReimbursementsByStatusId(int statusId) {
			return rDao.getReimbursementsByStatus(statusId);
		}
}
