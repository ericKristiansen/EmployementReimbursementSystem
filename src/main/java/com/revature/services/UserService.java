package com.revature.services;

import java.util.HashMap;
import java.util.List;

import com.revature.dao.user.UserDao;
import com.revature.models.User;

public class UserService {

	private static HashMap<Integer, String> userRoles = new HashMap<>();
	
	private UserDao uDao;
	
	public UserService(UserDao uDao)
	{
		this.uDao = uDao;
		this.loadUserRoles();
	}
	
	private void loadUserRoles()
	{
		userRoles = uDao.loadUserRoles(); 
	}
	
	public static HashMap<Integer, String> getUserRoles()
	{ return userRoles;}

	public User login(String username, String password) {
		return uDao.login(username, password);
	}

	public boolean register(String firstName, String lastName, String username, String password, String email) {
		return uDao.register(firstName, lastName, username, password, email);
	}

	public boolean updateUserInfo(int id, String firstName, String lastName, String username, String password,
			String email) {
		return uDao.updateUserInfo(id, firstName, lastName, username, password, email);
	}

	public List<User> getAllUsers() {
		return uDao.getAllUsers();
	}
	
}
