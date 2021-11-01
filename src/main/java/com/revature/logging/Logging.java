package com.revature.logging;

import org.apache.log4j.Logger;


/// This is a static class used for logging purposes.
/// Import in external class, and use the appropriate
/// method to log your message.
public class Logging {

	final static Logger log = Logger.getLogger(Logging.class);
	

	public final static void logTraceMessage(String message) {
		log.trace(message);
	}

	public final static void logDebugMessage(String message) {
		log.debug(message);
	}

	public final static void logInfoMessage(String message) {
		log.info(message);
	}

	public final static void logWarnMessage(String message) {
		log.warn(message);
	}

	public final static void logErrorMessage(String message) {
		log.error(message);
	}

	public final static void logFatalMessage(String message) {
		log.fatal(message);
	}

}
