package main.java.Talkbox.configurer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.UIManager;

import main.java.Talkbox.TalkboxConfigurer.BasePanel;
import main.java.Talkbox.browsing.FileSelector;
import main.java.Talkbox.browsing.SelectionListener;
import main.java.Talkbox.emojiPanel.EmojiSearchPane.EmojiSearchFrame;
import main.java.Talkbox.filehandler.FileIO;
import main.java.Talkbox.musicplayer.MusicPlayer;
import main.java.Talkbox.recording.MusicRecorder;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JFrame;

/**
 * Setup panel for the Talkbox Configurer app
 * @author jordan
 * @version March 10th 2019
 */
public class SetUpPanel extends JPanel implements ActionListener {
	/**The number of rows of buttons**/
	public static final int ROWS = 1;
	/**The number of columns of buttons**/
	public static final int COLS = 6;
	private static final Font BUTTON_FONT = new Font(Font.SANS_SERIF,Font.PLAIN,15);
	private JPanel buttonPanel;
	
	private SetUpButton[] buttons;
	/**The frame for setting up the button configurations**/
	private SetUpFrame setUpFrame;
	
	/**The base panel for returning to the main menu **/
	private BasePanel panel;
	
	/**The button for going back to main menu**/
	private JButton backButton;
	/** The button for moving to 6 higher buttons**/
	public JButton upButton;
	/** The button for moving to 6 lower buttons**/
	private JButton downButton;
	/** The button for adding additional rows of buttons**/
	private JButton addButtons;
	/** The button for removing buttons**/
	private JButton removeButtons;
	/**Label for which button row we're currently on**/
	private JLabel rowLabel;
	
	/** The TalkboxConfiguration that the app is currently setup with**/
	private Configuration config; 

	/** The current row of button configurations**/
	private int currentRow = 1;
	/** The number of rows of button configurations**/
	private int numRows;
	private static final Font OUTER_FONT = new Font(Font.SERIF,Font.PLAIN,16);
	public SetUpPanel(BasePanel panel) {
		this.panel = panel;
		this.setLayout(new BorderLayout());
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(ROWS, COLS));

		// Initializing button array
		buttons = new SetUpButton[COLS];
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				SetUpButton button = new SetUpButton();
				buttons[col] = button;
				button.addActionListener(this);
				buttonPanel.add(button);
			}
		}
		
		//Creating the top panel
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1, 2));
		topPanel.setBackground(Color.blue);
		
		//Setup label
		JLabel setupLabel = new JLabel(" Button Setup");
		setupLabel.setForeground(Color.orange);
		setupLabel.setBackground(Color.blue);
		setupLabel.setFont(new Font(Font.SANS_SERIF,Font.BOLD,25));
		topPanel.add(setupLabel);
		
		//Creating the back to main menu button
		backButton = new JButton("Back to Main Menu");
		backButton.setFont(OUTER_FONT);
		backButton.addActionListener(this);
		topPanel.add(backButton);
		
		//Creating the bottom panel
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(1,5));
		
		
		//Add rows button
		addButtons = new JButton("Add Set of Buttons");
		addButtons.setFont(OUTER_FONT);
		addButtons.addActionListener(this);
		bottomPanel.add(addButtons);
				
		//Delete rows button
		removeButtons = new JButton("Remove This Set");
		removeButtons.setFont(OUTER_FONT);
		removeButtons.addActionListener(this);
		bottomPanel.add(removeButtons);
		
		//Down button
		downButton = new JButton("View Lower Set");
		downButton.setFont(OUTER_FONT);
		downButton.addActionListener(this);
		bottomPanel.add(downButton);
		
		
		//Creating the up button
		upButton = new JButton("View Higher Set");
		upButton.setFont(OUTER_FONT);
		upButton.addActionListener(this);
		bottomPanel.add(upButton);
		
		//Row label
		rowLabel = new JLabel();
		rowLabel.setFont(OUTER_FONT);
		bottomPanel.add(rowLabel);
		
		//Adding items to the setup panel
		this.add(topPanel, BorderLayout.NORTH);
		this.add(buttonPanel, BorderLayout.CENTER);
		this.add(bottomPanel, BorderLayout.SOUTH);

		setUpFrame = new SetUpFrame();
	}
	/**
	 * Updating the text of the current row label
	 */
	public void updateRowLabel() {
		rowLabel.setText(String.format(" Set: %d / %d",this.currentRow,this.numRows));
	}
	
	/**
	 * Switches to the given configuration row
	 * @param row - the row to switch to.
	 * @throws IllegalArgumentException if the row is outside the range [1,this.numRows]
	 */
	public void switchRow(int row) {
		if(row < 1 || row > this.numRows) {
			throw new IllegalArgumentException(String.format("Illegal row %d / %d",row,this.numRows));
		}
		this.currentRow = row;
		/*
		 * Loading the configurations at the given row.
		 */
		int configIndex = (row - 1) * COLS;
		for(int buttonIndex = 0; buttonIndex < COLS;buttonIndex++) {
			buttons[buttonIndex].setConfiguration(config.buttonConfigs[configIndex]);
			++configIndex;
		}
		updateRowLabel();
		
		/*
		 * Updating which buttons are enabled.
		 */
		if(this.numRows == 1) {
			this.removeButtons.setEnabled(false);
		}
		else {
			this.removeButtons.setEnabled(true);
		}
		if(this.currentRow == 1) {
			this.upButton.setEnabled(false);
		}
		else {
			this.upButton.setEnabled(true);
		}
		if(this.currentRow == this.numRows) {
			this.downButton.setEnabled(false);
		}
		else {
			this.downButton.setEnabled(true);
		}
	}
	//Updates the number of rows
	public void updateRows() {
		this.numRows = config.buttonConfigs.length / COLS;
	}
	/**
	 * Setting the SetUpPanel to be editing the given TalkboxConfiguration
	 * @param config - the configuration to set up with.
	 */
	public void setConfiguration(Configuration config) {
		this.config = config;
		//Setting the number of rows
		this.numRows = config.buttonConfigs.length / COLS;
		//Switching to row 1.
		switchRow(1);
	}
	/**
	 * ActionListener method for the buttons
	 * Handles the events of the SetUpPanel buttons being pressed
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		//If the button pressed was the back button...
		if (event.getSource() == backButton) {
			setUpFrame.hideSetupFrame();
			panel.showMainMenu();
		}
		//Otherwise if it was one of the setup buttons
		else if(event.getSource() instanceof SetUpButton){
			setUpFrame.hideSetupFrame();
			setUpFrame.setVisible(true);
			setUpFrame.openSetupFrame((SetUpButton) event.getSource(),
					((SetUpButton) event.getSource()).getConfiguration());
			setUpFrame.colorFrame.setVisible(false);
		}
		//Up button
		else if(event.getSource() == upButton) {
			if(this.currentRow == 1)return;
			setUpFrame.hideSetupFrame();
			switchRow(this.currentRow - 1);
		}
		//Down button
		else if(event.getSource() == downButton) {
			if(this.currentRow == this.numRows)return;
			setUpFrame.hideSetupFrame();
			switchRow(this.currentRow + 1);
		}
		//Add button set
		else if(event.getSource() == addButtons) {
			config.addAudioSet();
			updateRows();
			switchRow(this.currentRow);
		}
		//Remove button set
		else if(event.getSource() == removeButtons) {
			config.removeAudioSet(this.currentRow);
			updateRows();
			if(this.currentRow == 1) {
				switchRow(this.currentRow);
			}
			else {
				switchRow(this.currentRow - 1);
			}
		}
	}

	/**
	 * Setup button to display the visuals of the button once created
	 * @author jordan
	 * @version jakjm
	 */
	public class SetUpButton extends JButton {	
		private ButtonConfiguration config;
		public SetUpButton() {
			this.setFont(BUTTON_FONT);
			this.setFocusable(false);
		}
		public ButtonConfiguration getConfiguration() {
			return config;
		}
		public void setConfiguration(ButtonConfiguration config) {
			this.config = config;
			adaptToConfig();
		}

		public void adaptToConfig() {
			this.setBackground(config.getButtonColor());
			// Adjusting the text to use html that way the body linewraps
			String adjustedText = String.format("<html><body>%s</body></html>", config.getButtonText());
			this.setText(adjustedText);
		}
	}

	/**
	 * Inner class for the pop-up frame that handles the configuration of buttons.
	 * 
	 * @author jordan
	 * @version Friday January 25th 2019
	 */
	public class SetUpFrame extends JFrame implements WindowListener {

		private BasicField nameField;
		private JButton emojiButton;
		
		/**Button for selecting sound from a pre-existing audio file.**/
		private JButton selectSound;
		
		/**Button for recording sound directly to the button**/
		private JButton recordSound;
		
		/**Button for playing the currently selected sound**/
		private JButton playSound;
		private JLabel currentPath;
		private JButton setColor;
		private JButton confirmSetup;
		private JPanel buttonsPanel;
		
		
		// Panel for color of button
		private JPanel currentColorPanel;
		/**Music Player for the sound selected**/
		private MusicPlayer musicPlayer;
		/**Button color selection frame*/
		private ColorFrame colorFrame;
		/**Audio File Selection Frame*/
		private FileSelector fileSelector;
		/**Emoji Selection frame*/
		private EmojiSearchFrame emojiFrame;
		
		private RecordingFrame recordingFrame;
		
		/** Current color that the user has selected for the current SetUpButton */
		private Color currentColor;
		
		/**Current AudioFile that has been selected for the current SetUpButton*/
		private File currentAudioFile;
		
		
		
		/** Current image file that has been selected for the current button */
		private File currentImageFile;
		
		/**Current button being edited**/
		private SetUpButton currentButton;
		
		/**The default color of JButtons*/
		private final Color DEFAULT_COLOR = new Color(UIManager.getColor("Button.background").getRGB());

		public SetUpFrame() {
			// Frame initial values
			super("Setup Button");
			this.addWindowListener(this);
			this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			this.setSize(400, 300);
			this.setVisible(false);

			// Intializing other frames
			emojiFrame = new EmojiSearchFrame(new EmojiListener());
			colorFrame = new ColorFrame();
			fileSelector = new FileSelector(new OpenListener(), FileSelector.SOUND);
			recordingFrame = new RecordingFrame();

			// Getting default color for a jbutton
			currentColor = DEFAULT_COLOR;

			// Adding the organizing configuration panel.
			ConfigPanel config = new ConfigPanel();
			this.setContentPane(config);
		}
		/**
		 * Hides the button setup frame, disabling the music player. 
		 */
		public void hideSetupFrame() {
			colorFrame.setVisible(false);
			emojiFrame.setVisible(false);
			fileSelector.setVisible(false);
			recordingFrame.hideRecordingFrame();
			
			//Reset music if it is still playing
			if(musicPlayer != null && musicPlayer.isPlaying()) {
				musicPlayer.stop();
				musicPlayer.reset();
			}
			this.setVisible(false);
		}

		/**
		 * Updates the setup frame based on the button that has just been opened.
		 * 
		 * @param button
		 * @param config
		 */
		public void openSetupFrame(SetUpButton button, ButtonConfiguration config) {
			this.currentButton = button;
			this.currentColor = config.getButtonColor();
			this.currentColorPanel.setBackground(this.currentColor);
			this.currentAudioFile = config.getSoundFile();
			if (this.currentAudioFile != null) {
				currentPath.setText("Sound Path: " + this.currentAudioFile.getPath());
				musicPlayer = new MusicPlayer(this.currentAudioFile);
			} else {
				currentPath.setText("Sound Path:(none)");
				musicPlayer = null;
			}
			nameField.setText(config.getButtonText());
		}

		public class OpenListener implements SelectionListener {
			public void onFileSelected(File file) {
				if (FileIO.checkWaveFormat(file)) {
					currentAudioFile = file;
					currentPath.setText("Sound Path: " + currentAudioFile.getPath());
					musicPlayer = new MusicPlayer(currentAudioFile);
				} else {
					JOptionPane.showMessageDialog(null, "Audio File must be of .wav format");
				}
				fileSelector.setVisible(false);
			}
		}

		public class EmojiListener implements ActionListener {
			public void actionPerformed(ActionEvent event) {
				JButton emojiButton = (JButton) event.getSource();
				nameField.insertTextAtCursor(emojiButton.getText());
			}
		}

		public Point getSetupLocation() {
			return this.getLocation();
		}

		public void setButton(SetUpButton currentButton) {
			this.currentButton = currentButton;
		}

		
		/**
		 * Method for handling when the window has been closed.
		 * Used by the WindowListener interface that the SetupFrame implements.
		 */
		public void windowClosing(WindowEvent arg0) {
			hideSetupFrame();
		}

		/*
		 * Window state changed methods
		 */
		public void windowActivated(WindowEvent arg0) {}
		public void windowClosed(WindowEvent event) {}
		public void windowDeactivated(WindowEvent event) {}
		public void windowDeiconified(WindowEvent event) {}
		public void windowIconified(WindowEvent event) {}
		public void windowOpened(WindowEvent event) {}

		public class ConfigPanel extends JPanel implements ActionListener {
			public ConfigPanel() {
				this.setLayout(new BorderLayout());

				// Adding textField and emoji button to the name panel
				JPanel namePanel = new JPanel();
				namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
				nameField = new BasicField("Button Text:");
				namePanel.add(nameField);

				emojiButton = new JButton("Add Emoji");
				emojiButton.addActionListener(this);
				namePanel.add(emojiButton);
				this.add(namePanel, BorderLayout.NORTH);

				// Buttons panel for selecting the sound and the color
				buttonsPanel = new JPanel();
				buttonsPanel.setLayout(new GridLayout(4, 1));

				
				//Panel concerning button sound.
				JPanel soundButtons = new JPanel();
				soundButtons.setLayout(new GridLayout(1, 2));
				
				// Adding a play sound button
				playSound = new JButton("Play Sound");
				playSound.addActionListener(this);
				soundButtons.add(playSound);
				
				
				
				//Panel for selecting the sound for the button
				JPanel soundSelection = new JPanel();
				soundSelection.setLayout(new GridLayout(2,1));
				
				//Adding a sound selection button and a label displaying the current selection
				selectSound = new JButton("Select Sound");
				selectSound.setForeground(Color.red);
				selectSound.addActionListener(this);
				soundSelection.add(selectSound);
				
				//Adding a record sound button
				recordSound = new JButton("Record Sound");
				recordSound.addActionListener(this);
				recordSound.setForeground(Color.red);
				soundSelection.add(recordSound);
				
				
				soundButtons.add(soundSelection);
				buttonsPanel.add(soundButtons);
				
				
				

				currentPath = new JLabel("Sound Path:(none)");
				buttonsPanel.add(currentPath);

				// Button for setting the color that the button should have.
				setColor = new JButton("Select Color");
				setColor.addActionListener(this);
				buttonsPanel.add(setColor);

				currentColorPanel = new JPanel();
				currentColorPanel.add(new JLabel("Current Color"));
				currentColorPanel.setBackground(currentColor);
				buttonsPanel.add(currentColorPanel);

				this.add(buttonsPanel, BorderLayout.CENTER);

				confirmSetup = new JButton("Confirm Setup");
				confirmSetup.addActionListener(this);
				this.add(confirmSetup, BorderLayout.SOUTH);

			}

			public void actionPerformed(ActionEvent event) {
				if (event.getSource() == setColor) {
					// Bringing up color frame
					colorFrame.setLocation(new Point(getSetupLocation().x + 60, getSetupLocation().y + 200));
					colorFrame.setVisible(true);
					
					// Hiding other frames
					emojiFrame.setVisible(false);
					fileSelector.setVisible(false);
					recordingFrame.hideRecordingFrame();
				} else if (event.getSource() == emojiButton) {
					// Bringing up emoji pane
					emojiFrame.setLocation(new Point(getSetupLocation().x + 200, getSetupLocation().y + 60));
					emojiFrame.setVisible(true);
					
					// Hiding other frames
					fileSelector.setVisible(false);
					colorFrame.setVisible(false);
					recordingFrame.hideRecordingFrame();
				} else if (event.getSource() == selectSound) {
					fileSelector.setVisible(true);
					
					// Hiding other frames
					colorFrame.setVisible(false);
					emojiFrame.setVisible(false);
					recordingFrame.hideRecordingFrame();
				}else if(event.getSource() == recordSound) {
			
					recordingFrame.setVisible(true);
					
					// Hiding other frames
					colorFrame.setVisible(false);
					emojiFrame.setVisible(false);
					fileSelector.setVisible(false);
				}
				else if (event.getSource() == playSound) {
					if (musicPlayer != null) {
						musicPlayer.play();
					}
				} else if (event.getSource() == confirmSetup) {
					ButtonConfiguration button = currentButton.getConfiguration();
					button.setText(nameField.getText());
					button.addColor(currentColor);
					button.addSoundFile(currentAudioFile);
					// TODO: set local variable for selected image --> button.addImageFile(selectedImage)
					//button.addImageFile(currentImageFile);
					currentButton.setConfiguration(currentButton.getConfiguration());
					hideSetupFrame();
				}
			}
		}

		/**
		 * Recording frame for recording sound directly from the SetUpButtons.
		 * @author jordan
		 * @version Saturday March 16th 2019
		 */
		public class RecordingFrame extends JFrame implements ActionListener, WindowListener{
			public JButton recordButton;
			public JLabel flashingLabel;
			public JButton stopButton;
			public MusicRecorder recorder;
			public RecordingFrame() {
				super("Record Sound");
				this.setSize(250,200);
				this.setLayout(new GridLayout(3,1));
				this.addWindowListener(this);
				
				recorder = new MusicRecorder();
				
				//Setting up recording button
				recordButton = new JButton("Record");
				recordButton.addActionListener(this);
				this.add(recordButton);
				
				//Setting up recording indicator
				flashingLabel = new JLabel("");
				flashingLabel.setForeground(Color.red);
				this.add(flashingLabel);
				
				//Setting up the stop button
				stopButton = new JButton("Stop Recording");
				stopButton.addActionListener(this);
				stopButton.setEnabled(false);
				this.add(stopButton);
				this.setVisible(false);
			}
			public void hideRecordingFrame() {
				this.setVisible(false);
				if(recorder.isRecording()) {
					recorder.stop();
				}
			}
			/**
			 * ActionListener for the buttons for the recording frame
			 */
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == recordButton) {
					
					//Updating buttons
					recordButton.setEnabled(false);
					stopButton.setEnabled(true);
					recorder.record();
				}
				else if(e.getSource() == stopButton) {
					
					//Updating buttons
					stopButton.setEnabled(false);
					recordButton.setEnabled(true);
					
					File soundFile = new File(currentButton.getConfiguration().audioFilePath());
					MusicRecorder.writeToFile(recorder.stop(),recorder.getFormat(),soundFile);
					
					currentAudioFile = soundFile;
					currentPath.setText("Sound Path: " + currentAudioFile.getPath());
					musicPlayer = new MusicPlayer(currentAudioFile);
				}
			}
			/**
			 * Used for handling when the window has been closed.
			 */
			public void windowClosing(WindowEvent arg0) {
				hideRecordingFrame();
			}
			/*
			 * Window state changed methods
			 */
			public void windowActivated(WindowEvent arg0) {}
			public void windowClosed(WindowEvent event) {}
			public void windowDeactivated(WindowEvent arg0) {}
			public void windowDeiconified(WindowEvent arg0) {}
			public void windowIconified(WindowEvent arg0) {}
			public void windowOpened(WindowEvent arg0) {}
		}
		/**
		 * Color frame for changing the color of the button being configured.
		 * @author jordan
		 */
		public class ColorFrame extends JFrame implements ActionListener {
			public ColorFrame() {
				super("Select Color");
				this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				this.setSize(200, 220);
				this.setResizable(false);
				this.setVisible(false);
				this.setLayout(new GridLayout(3, 3));

				//Adding the different colors.
				this.add(new ColorButton(Color.red, this));
				this.add(new ColorButton(Color.blue, this));
				this.add(new ColorButton(Color.orange, this));
				this.add(new ColorButton(Color.yellow, this));
				this.add(new ColorButton(Color.pink, this));
				this.add(new ColorButton(Color.green, this));
				this.add(new ColorButton(Color.cyan, this));
				this.add(new ColorButton(Color.white, this));
				this.add(new ColorButton(ButtonConfiguration.DEFAULT_COLOR, this));
			}

			/**
			 * Color button for selecting a particular color from the color frame
			 * @author jordan
			 */
			public class ColorButton extends JButton {
				private Color thisColor;
				public ColorButton(Color color, ActionListener listener) {
					thisColor = color;
					this.setIcon(createColorButtonIcon(color, 60));
					this.addActionListener(listener);
				}
				public Color getColor() {
					return thisColor;
				}
			}

			public void actionPerformed(ActionEvent event) {
				ColorButton button = (ColorButton) event.getSource();
				currentColor = button.getColor();
				this.setVisible(false);
				currentColorPanel.setBackground(currentColor);
			}
		}
	}

	public static ImageIcon createColorButtonIcon(Color color, int size) {
		BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		g.setColor(color);
		g.fillRoundRect(0, 0, size, size, size / 5, size / 5);
		return new ImageIcon(image);
	}

}
