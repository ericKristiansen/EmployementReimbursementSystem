package com.revature.services;

import java.util.HashMap;

import com.revature.dao.reimbursement.ReimbursementDao;
import com.revature.dao.reimbursement.ReimbursementDaoDb;
import com.revature.dao.user.UserDao;
import com.revature.dao.user.UserDaoDb;
import com.revature.logging.Logging;

public class ServiceWrangler {
	
	private ReimbursementDao rDao = new ReimbursementDaoDb();
	private UserDao uDao = new UserDaoDb();
	
	private ReimbursementService rs;
	private UserService us;
	
	public ServiceWrangler()
	{
		this.rs = new ReimbursementService(rDao);
		this.us = new UserService(uDao);
	}
	
	public int getInt()
	{
		return 5;
	}
	
	public HashMap<Integer, String> getReimbursementTypes()
	{
		return ReimbursementService.getReimbursementTypes();
	}
	
	public HashMap<Integer, String> getReimbursementStatus()
	{
		return ReimbursementService.getReimbursementStatus();
	}
	
	public HashMap<Integer, String> getUserRoles()
	{
		return UserService.getUserRoles();
	}
}
