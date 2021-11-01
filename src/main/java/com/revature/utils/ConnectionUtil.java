package com.revature.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectionUtil {

	private static ConnectionUtil cu;
	private static Properties prop = new Properties();
	private static Connection con = null;
	
	private ConnectionUtil() {
		
	}
	
	public static synchronized ConnectionUtil getConnectionUtil() {
		if (cu == null)
		{
			return new ConnectionUtil();
		}
		return cu;
	}
	
	public Connection getConnection() {
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream is = classLoader.getResourceAsStream("jdbc.properties");
		String url = "";
		String password = "";
		String username = "";
		
		try {
			prop.load(is);
			
			url = (String)prop.getProperty("url");
			username = (String)prop.getProperty("username");
			password = (String)prop.getProperty("password");

		}
		catch(Exception e)
		{e.printStackTrace();}
		
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(url, username, password);
		} catch(Exception ex) {ex.printStackTrace();}
		
		return con;
	}
	
}