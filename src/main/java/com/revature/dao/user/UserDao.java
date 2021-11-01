package com.revature.dao.user;

import java.util.HashMap;
import java.util.List;

import com.revature.models.User;

public interface UserDao {
	
	HashMap<Integer, String> loadUserRoles();
	
	public User login(String username, String password);
	
	public User getUserByUsername(String username);

	boolean register(String firstName, String lastName, String username, String password, String email);

	boolean updateUserInfo(int id, String firstName, String lastName, String username, String password, String email);

	List<User> getAllUsers();
}
