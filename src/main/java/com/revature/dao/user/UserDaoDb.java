package com.revature.dao.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.revature.logging.Logging;
import com.revature.models.User;
import com.revature.utils.ConnectionUtil;

public class UserDaoDb implements UserDao{
	
	private static final String SELECT_ERS_USER_ROLES_SQL = "SELECT ERS_USER_ROLE_ID, USER_ROLE FROM ERS_USER_ROLE;";
	private static final String SELECT_USER_BY_USERNAME = "SELECT ERS_USERS_ID, USER_FIRST_NAME, USER_LAST_NAME, ERS_USERNAME, "
			+ "ERS_PASSWORD, USER_EMAIL, USER_ROLE_ID FROM ERS_USERS WHERE ERS_USERNAME = ?;";
	private static final String REGISTER_USER_SQL = "INSERT INTO ERS_USERS (USER_FIRST_NAME, USER_LAST_NAME, ERS_USERNAME, "
			+ "ERS_PASSWORD, USER_EMAIL, USER_ROLE_ID) VALUES (?, ?, ?, ?, ?, ?);";
	private static final String UPDATE_USER_SQL = "UPDATE ERS_USERS SET USER_FIRST_NAME = ?, USER_LAST_NAME = ?, ERS_USERNAME = ?, "
			+ "ERS_PASSWORD = ?, USER_EMAIL = ? WHERE ERS_USERS_ID = ?";
	private static final String SELECT_ALL_USERS_SQL = "SELECT ERS_USERS_ID, USER_FIRST_NAME, USER_LAST_NAME, ERS_USERNAME, "
			+ " ERS_PASSWORD, USER_EMAIL, USER_ROLE_ID FROM ERS_USERS";
	private static final int EMPLOYEE_ROLE = 1;

	ConnectionUtil conUtil = ConnectionUtil.getConnectionUtil();


	public HashMap<Integer, String> loadUserRoles()
	{
		Connection con = conUtil.getConnection();
		HashMap<Integer, String> userRoles = new HashMap<>();

		try {
			PreparedStatement ps = con.prepareStatement(SELECT_ERS_USER_ROLES_SQL);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				userRoles.put(rs.getInt(1), rs.getString(2));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return userRoles;
	}


	/**
	 * Return the user id if the credential set is valid, or return a -1 if not.
	 */
	@Override
	public User login(String username, String password) {
		User u = getUserByUsername(username);
		boolean result = (u != null);
		if (result) { result = u.getPassword().equals(password);}
		return result ? u: null;
	}
	
	/**
	 * Internal function to populate a user from a result set. Returns the populated user.
	 * @param rs
	 * @return
	 */
	private User populateUser(ResultSet rs) {
		User tempUser = null;
		try {
			tempUser = new User(rs.getInt(USER.ID.value()), rs.getString(USER.FIRST_NAME.value()),
					rs.getString(USER.LAST_NAME.value()), rs.getString(USER.USER_NAME.value()),
					rs.getString(USER.PASSWORD.value()), rs.getString(USER.EMAIL.value()),
					rs.getInt(USER.USER_ROLE_ID.value()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tempUser;
	}


	@Override
	public User getUserByUsername(String username) {
		User tempUser = null;
		try {
			Connection con = conUtil.getConnection();

			PreparedStatement ps = con.prepareStatement(SELECT_USER_BY_USERNAME);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				tempUser = populateUser(rs);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tempUser;
	}


	/* 
	 * INSERT INTO ERS_USERS (USER_FIRST_NAME, USER_LAST_NAME, ERS_USERNAME, ERS_PASSWORD, 
	 * USER_EMAIL, USER_ROLE_ID) VALUES (?, ?, ?, ?, ?, ?);
	 * */
	@Override
	public boolean register(String firstName, String lastName, String username, String password, String email) {
		boolean success = true;

		try {
			Connection con = conUtil.getConnection();

			PreparedStatement ps = con.prepareStatement(REGISTER_USER_SQL);
			ps.setString(1, firstName);
			ps.setString(2, lastName);
			ps.setString(3, username);
			ps.setString(4, password);
			ps.setString(5, email);
			ps.setInt(6, EMPLOYEE_ROLE);
			ps.execute();

		} catch (SQLException e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}


	@Override
	public boolean updateUserInfo(int id, String firstName, String lastName, String username, String password,
			String email) 
	{
			boolean success = true;

			try {
				Connection con = conUtil.getConnection();

				//UPDATE ERS_USERS SET USER_FIRST_NAME = ?, USER_LAST_NAME = ?, ERS_USERNAME = ?, 
				//ERS_PASSWORD = ?, USER_EMAIL = ? WHERE ERS_USERS_ID = ?
				PreparedStatement ps = con.prepareStatement(UPDATE_USER_SQL);
				ps.setString(1, firstName);
				ps.setString(2, lastName);
				ps.setString(3, username);
				ps.setString(4, password);
				ps.setString(5, email);
				ps.setInt(6, id);
				ps.execute();

			} catch (SQLException e) {
				e.printStackTrace();
				success = false;
			}
			return success;
	}


	@Override 
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<User>();
		try {
			Connection con = conUtil.getConnection();

			PreparedStatement ps = con.prepareStatement(SELECT_ALL_USERS_SQL);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				users.add(populateUser(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}


}
