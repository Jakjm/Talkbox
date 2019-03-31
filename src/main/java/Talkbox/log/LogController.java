package main.java.Talkbox.log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import main.java.Talkbox.filehandler.FileIO;

/**
 * This class controls a Logger instance. Every log is automatically written to the log file
 * in the destination folder.
 */
public class LogController {
	private File logDest;
	private Logger customLogger;
	public enum LogType {
		CONFIG_LOG, SIM_LOG;
	}
	public Handler logHandler;
	/**
	 * Initialize the LogController.
	 * @param logType The log type (CONFIG_LOG, SIM_LOG)
	 * @param logs The folder to put the logs.
	 */
	public LogController(LogType logType, File logs) {
		if (logs == null) {
			logs = new File("logs");
			logs.mkdir();
		}
		this.customLogger = Logger.getLogger(logType.toString());
		this.logDest = new File(logs.getPath() + FileIO.SEP + logType.toString());
		try {
			// Handler for writing and appending logs to log file
			logHandler = new FileHandler(this.logDest.getPath(), true);
			logHandler.setFormatter(new SimpleFormatter());
			this.customLogger.addHandler(logHandler);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Add logs to the specified folder.
	 * @param logFolder The folder to store the logs.
	 */
	public void addLogFolder(File logFolder) {
		if (!logFolder.exists()) {
			logFolder.mkdir();
		}
		File newLog = new File(logFolder.getPath() + FileIO.SEP + this.logDest.getName());
		FileIO.copyFile(this.logDest, newLog);
		this.logDest.delete();
		this.logDest = newLog;
		try {
			this.logHandler = new FileHandler(this.logDest.getPath(), true);
			this.logHandler.setFormatter(new SimpleFormatter());
			this.customLogger.addHandler(this.logHandler);
		} catch (IOException | SecurityException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Log a general message.
	 * @param message The string message.
	 */
	public void logMessage(String message) {
		this.customLogger.log(Level.INFO, message);
		
	}
}
