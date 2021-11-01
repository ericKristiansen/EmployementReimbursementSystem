package com.revature.driver;

import com.revature.logging.Logging;
import com.revature.services.ServiceWrangler;

public class ERSDriver {
	
	private final static String WELCOME_MESSAGE = "Welcome to the program";
	
	public static void main(String[] args)
	{
		Logging.logTraceMessage("System Startup...");
		System.out.println(WELCOME_MESSAGE);
		
		ServiceWrangler sw = new ServiceWrangler();

	}

}
