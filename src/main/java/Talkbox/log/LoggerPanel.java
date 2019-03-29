package main.java.Talkbox.log;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.io.File;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JTextArea;

import main.java.Talkbox.filehandler.FileIO;
import main.java.Talkbox.log.LogController.LogType;

import java.awt.SystemColor;

/**
 * Re-usable panel for user and caregiver log viewers.
 *
 */
public class LoggerPanel extends JPanel {
	private JTextArea textArea;
	private File logsFolder;
	public final LogType LOG_TYPE;
	/**
	 * Construct a new Logger Panel with the LOG type.
	 * @param The LogType (CONFIG_LOG or SIM_LOG).
	 */
	public LoggerPanel(LogType lg) {
		this.LOG_TYPE = lg;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		textArea = new JTextArea();
		textArea.setBackground(SystemColor.info);
		// Add scrolling 
		JScrollPane scrollPane = new JScrollPane();
		this.add(scrollPane);
		scrollPane.setViewportView(textArea);

	}
	/**
	 * Adds the folder of log files.
	 * @param logs The folder containing the log files.
	 */
	public void addLogsFolder(File logs){ 
		this.logsFolder = logs;
		writeToView();
	}
	/**
	 * Writes the contents of the log file to the view.
	 */
	private void writeToView() { 
		StringBuilder output = new StringBuilder("");
		ArrayList<File> files = FileIO.getAllFiles(this.LOG_TYPE.toString(), this.logsFolder);
		ArrayList<String> lines;
		for (File f : files) {
			lines = FileIO.readTextFile(f);
			for (int i = 0; i < lines.size(); i++) {
				output.append(lines.get(i) + '\n');
			}
		}
		textArea.setText(output.toString());
	}
	/**
	 * Erase the contents of the logger.
	 */
	public void reset() {
		LogController.wipe(this.LOG_TYPE.toString(), this.logsFolder);
		textArea.setText("");
	}
	
}
