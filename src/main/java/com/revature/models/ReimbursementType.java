package com.revature.models;

import java.util.HashMap;

public class ReimbursementType {

	private static HashMap<Integer, String> types = new HashMap<>();
	
	public static HashMap<Integer, String> getTypes()
	{
		return types;
	}
	
	public static void setTypes(HashMap<Integer, String> pTypes)
	{
		types = pTypes; 
	}
}
