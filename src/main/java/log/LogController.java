package main.java.log;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import main.java.Talkbox.filehandler.FileIO;

public class LogController {
	private final File LOG_DEST;
	private final Logger TALKBOX_LOG = Logger.getLogger("Talkbox Log");
	/**
	 * Initialize the LogController.
	 * @param folder The destination folder for the logger file.
	 */
	public LogController(File folder) {
		this.LOG_DEST = new File(folder.getPath() + FileIO.SEP + "logs.log");
		try {
			// Handler for writing logs to log file
			Handler logHandler = new FileHandler(this.LOG_DEST.getPath());
			logHandler.setFormatter(new SimpleFormatter());
			this.TALKBOX_LOG.addHandler(logHandler);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Log a general message.
	 * @param message The string message.
	 */
	public void logMessage(String message) {
		TALKBOX_LOG.log(Level.INFO, message);
	}
	/**
	 * Resets the log file.
	 */
	public void wipe() {
		FileIO.textToFile(this.LOG_DEST, "");
	}
}
