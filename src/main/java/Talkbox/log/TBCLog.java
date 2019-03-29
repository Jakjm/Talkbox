package main.java.Talkbox.log;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import main.java.Talkbox.browsing.FileSelector;
import main.java.Talkbox.browsing.SelectionListener;
import main.java.Talkbox.filehandler.FileIO;
import java.awt.Font;

public class TBCLog extends JFrame {
	private JMenuItem resetLog;
	private JMenuItem addLogsFolder;
	private JMenuItem exportLog;
	private LoggerPanel loggerPanel;
	private FileSelector fileSelector;
	private FileSelector exportSelector;
	private File logFolder;

	public static void main(String[] args) {
		TBCLog logFrame = new TBCLog();
		logFrame.setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public TBCLog() {
		this.setTitle("TBCLog");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		this.loggerPanel = new LoggerPanel(LogController.LogType.CONFIG_LOG);
		getContentPane().add(this.loggerPanel);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu options = new JMenu("Menu");
		options.setFont(new Font("Yu Gothic UI Semilight", Font.PLAIN, 12));
		TBCMenuListener menuListener = new TBCMenuListener();
		addLogsFolder = new JMenuItem("Add Log Folder");
		addLogsFolder.setFont(new Font("Yu Gothic UI Semilight", Font.PLAIN, 12));
		addLogsFolder.addActionListener(menuListener);
		options.add(addLogsFolder);
		
		resetLog = new JMenuItem("Reset Log");
		resetLog.setFont(new Font("Yu Gothic UI Semilight", Font.PLAIN, 12));
		resetLog.setEnabled(false);
		resetLog.addActionListener(menuListener);
		options.add(resetLog);
		
		exportLog = new JMenuItem("Export Log");
		exportLog.setFont(new Font("Yu Gothic UI Semilight", Font.PLAIN, 12));
		exportLog.setEnabled(false);
		exportLog.addActionListener(menuListener);
		options.add(exportLog);
		
		menuBar.add(options);
		this.setJMenuBar(menuBar);
		
	}
	/**
	 * Action listener for menu.
	 *
	 */
	public class TBCMenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == addLogsFolder) {
				fileSelector = new FileSelector(new ConfigListener(), 1);
				fileSelector.setVisible(true);
			}
			else if (e.getSource() == resetLog) {
				loggerPanel.reset();
				JOptionPane.showMessageDialog(null, "Logs erased");
				resetLog.setEnabled(false);
				exportLog.setEnabled(false);
			}
			else if (e.getSource() == exportLog) {
				exportSelector = new FileSelector(new ExportListener(), 1);
				exportSelector.setVisible(true);
			}
			
		}

	}
	/**
	 * SelectionListener for file selector.
	 *
	 */
	public class ConfigListener implements SelectionListener {
		public void onFileSelected(File folder) {
			// seeing if Logs are present
			if (((FileIO.getAllFiles(loggerPanel.LOG_TYPE.toString(), folder).size()) != 0)) {
				logFolder = folder;
				loggerPanel.addLogsFolder(folder);
				resetLog.setEnabled(true);
				exportLog.setEnabled(true);
			} else {
				JOptionPane.showMessageDialog(null, "Could not find log files. Try to correct directory.");
			}
			fileSelector.setVisible(false);
		}
	}
	public class ExportListener implements SelectionListener {
		public void onFileSelected(File folder) {
			FileIO.compressText(loggerPanel.LOG_TYPE.toString(), logFolder, folder);
			exportSelector.setVisible(false);
			JOptionPane.showMessageDialog(null, "Log exported.");
		}
	}
}
