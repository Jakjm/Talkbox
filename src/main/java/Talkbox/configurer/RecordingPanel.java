package main.java.Talkbox.configurer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.sound.sampled.AudioFormat;
import javax.swing.JButton;
import javax.swing.JTextField;

import main.java.Talkbox.TalkboxConfigurer.BasePanel;
import main.java.Talkbox.browsing.FileSelector;
import main.java.Talkbox.browsing.SelectionListener;
import main.java.Talkbox.recording.MusicRecorder;

/**
 * Recording panel for the Talkbox Configurer Application
 * @author jordan
 * @version February 19th 2019
 */
public class RecordingPanel extends JPanel implements ActionListener {
	/** Music recorder instance **/
	MusicRecorder recorder = new MusicRecorder();
	/** Button for starting recording**/
	JButton recordingButton;
	/**Button for finishing recording**/
	JButton stopRecording;
	/** Button for returning to main menu**/
	JButton mainMenu;
	/**Field for the name of the saved audio wave file**/
	BasicField nameField;
	
	/**BasePanel reference for returning to main menu**/
	BasePanel parent;
	/**File selector for selecting where to save file**/
	FileSelector fileSelector;
	/**Label that blinks while the user is recording**/
	JLabel blinkingLabel;
	/** Recording string constant for the blinking label **/
	private static final String RECORDING = " ● Recording ● ";
	/**Stopped string constant for the blinking label**/
	private static final String STOPPED = " Not Currently Recording ";
	/**Interval between blinks**/
	private static final int BLINK_TIME = 500;
	
	/**Font for most of the panel buttons**/
	private static final Font PANEL_FONT = new Font(Font.SERIF,Font.PLAIN,28);
	private volatile boolean isRecording = false;
	
	/**
	 * Creates the recording panel 
	 * @param parent - the parent panel, used for returning to the main menu
	 */
	public RecordingPanel(BasePanel parent) {
		this.parent = parent;
		this.setLayout(new GridLayout(5, 1));

		//Top panel
		JPanel topPanel = new JPanel();
		topPanel.setBackground(Color.blue);
		topPanel.setLayout(new GridLayout(1, 2));
		
		//Title label
		JLabel TitleLabel = new JLabel(" Audio Recording ");
		TitleLabel.setForeground(Color.orange);
		TitleLabel.setFont(new Font(Font.SERIF,Font.BOLD,36));
		topPanel.add(TitleLabel);

		// Adding main menu button to top panel.
		mainMenu = new JButton("Back to main menu");
		mainMenu.addActionListener(this);
		mainMenu.setFont(PANEL_FONT);
		topPanel.add(mainMenu);
		
		//Adding top panel to recording panel
		this.add(topPanel);
		
		//Blinking label while recording is taking place
		blinkingLabel = new JLabel(String.format(STOPPED));
		blinkingLabel.setFont(PANEL_FONT);
		blinkingLabel.setForeground(Color.black);
		this.add(blinkingLabel);
		blinkingLabel.setHorizontalAlignment(JLabel.CENTER);

		//Adding recording button
		recordingButton = new JButton("Record");
		recordingButton.setFont(PANEL_FONT);
		recordingButton.addActionListener(this);
		this.add(recordingButton);
		
		
		//Adding name field
		nameField = new BasicField(" Filename of recording:");
		nameField.setFieldFont(PANEL_FONT);
		nameField.setText("rec.wav");
		this.add(nameField);

		//Stop recording button
		stopRecording = new JButton("Stop Recording");
		stopRecording.setFont(PANEL_FONT);
		stopRecording.addActionListener(this);
		this.add(stopRecording);
		stopRecording.setEnabled(false);

		//Initializing button
		fileSelector = new FileSelector(null, FileSelector.DIRECTORY);
	}
	/**
	 * Start making the label blink.
	 */
	public void startBlinking() {
		Thread blinkingThread = new Thread(new Runnable() {
			public void run() {
				boolean on = true;
				blinkingLabel.setText("");
				blinkingLabel.setForeground(Color.red);
				while(isRecording) {
					on =! on;
					if(on) {
						blinkingLabel.setText(RECORDING);
					}
					else {
						blinkingLabel.setText("");
					}
					long waitTime = System.currentTimeMillis() + 700;
					//Wait 700ms
					while(System.currentTimeMillis() < waitTime) {
					}
				}
				
			}
		});
		blinkingThread.start();
	}
	/**
	 * Stops the blinking label
	 */
	public void stopBlinking() {
		//Resetting recorder boolean for blinking thread
		isRecording = false;
		//Waiting 50 millis
		long timeDelay = System.currentTimeMillis() + 50;
		while(System.currentTimeMillis() < timeDelay) {
			
		}
		blinkingLabel.setForeground(Color.black);
		blinkingLabel.setText(STOPPED);
	}
	/**
	 * ActionListener for the buttons
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		//Recording button
		if (event.getSource() == recordingButton) {
			if(isRecording) {
				return;
			}
			recordingButton.setEnabled(false);
			stopRecording.setEnabled(true);
			mainMenu.setEnabled(false);
			recorder.record();
			isRecording = true;
			startBlinking();
		//Main menu button
		} else if (event.getSource() == mainMenu) {
			recorder.stop();
			parent.showMainMenu();
			stopBlinking();
			
		//Stop recording button
		} else if (event.getSource() == stopRecording) {
			if(!isRecording) {
				return;
			}
			stopBlinking();
			mainMenu.setEnabled(true);
			recordingButton.setEnabled(true);
			stopRecording.setEnabled(false);
			fileSelector.setSelectionListener(new SaveFileSelectionListener(recorder.stop(), recorder.getFormat()));
			JOptionPane.showMessageDialog(null, "Please select the directory to save your recording in.");
			fileSelector.setVisible(true);
		}

	}
	
	/**
	 * SaveFileSelectionListener 
	 * @author jordan
	 *
	 */
	public class SaveFileSelectionListener implements SelectionListener {
		ByteArrayOutputStream stream;
		AudioFormat format;
		final char [] BAD_CHARS = {'/','\\',' ','\t','\0'};
		public SaveFileSelectionListener(ByteArrayOutputStream stream, AudioFormat format) {
			this.stream = stream;
			this.format = format;
		}
		/**
		 * Checks whether the file name is of the proper format.
		 * @param name - the name of the file to check.
		 */
		public boolean checkNameFormat(String name) {
			for(int i = 0;i < BAD_CHARS.length;i++) {
				if(name.contains(BAD_CHARS[i] + ""))return false;
			}
			//If no file extension exists, return false.
			if(name.indexOf(".") == -1) {
				return false;
			}
			String fileExtension = name.substring(name.lastIndexOf("."));
			if(fileExtension.equals(".wav") || fileExtension.equals(".wave")) {
				return true;
			}
			else {
				return false;
			}
		}
		/**
		 * What to do once a file has been selected 
		 * @Param directory - the directory that has been selected by the user in which to save the file.
		 */
		@Override
		public void onFileSelected(File directory) {
			String fileName = nameField.getText();
			while(!checkNameFormat(fileName)) {
				fileName = JOptionPane.showInputDialog("Filename invalid! Please enter a correct name for the file, ending in .wav");
			}
			File newFile = new File(directory.getPath() + System.getProperty("file.separator") + fileName);
			MusicRecorder.writeToFile(stream, format, newFile);
			fileSelector.setVisible(false);
		}
	}

}