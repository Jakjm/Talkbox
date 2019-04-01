package main.java.Talkbox.configurer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import main.java.Talkbox.TalkboxConfigurer.BasePanel;
import main.java.Talkbox.browsing.FileSelector;
import main.java.Talkbox.browsing.SelectionListener;
import main.java.Talkbox.emojiPanel.EmojiSearchPane.EmojiSearchFrame;
import main.java.Talkbox.filehandler.FileIO;
import main.java.Talkbox.log.LogController;
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
	private static final Font BUTTON_FONT = new Font("Rockwell",Font.BOLD,20);
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
	/** The current long controller. */
	private LogController logger;
	private static final Font OUTER_FONT = new Font("Rockwell",Font.PLAIN,16);
	public SetUpPanel(BasePanel panel, LogController logger) {
		this.logger = logger;
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
		topPanel.setBackground(Color.LIGHT_GRAY);
		
		//Setup label
		JLabel setupLabel = new JLabel(" Button Setup");
		setupLabel.setForeground(Color.BLACK);
		setupLabel.setBackground(Color.LIGHT_GRAY);
		setupLabel.setFont(new Font("Rockwell",Font.BOLD,25));
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
		this.logger.logMessage("Switching rows.");
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
			this.logger.logMessage("Back button pressed");
		}
		//Otherwise if it was one of the setup buttons
		else if(event.getSource() instanceof SetUpButton){
			
			setUpFrame.openSetupFrame((SetUpButton) event.getSource(),
					((SetUpButton) event.getSource()).getConfiguration());
			this.logger.logMessage("Setup button pressed");
		}
		//Up button
		else if(event.getSource() == upButton) {
			if(this.currentRow == 1)return;
			setUpFrame.hideSetupFrame();
			switchRow(this.currentRow - 1);
			this.logger.logMessage("Up button pressed");
		}
		//Down button
		else if(event.getSource() == downButton) {
			if(this.currentRow == this.numRows)return;
			setUpFrame.hideSetupFrame();
			switchRow(this.currentRow + 1);
			this.logger.logMessage("Down button pressed");
		}
		//Add button set
		else if(event.getSource() == addButtons) {
			config.addAudioSet();
			updateRows();
			switchRow(this.currentRow);
			this.logger.logMessage("Audioset added");
		}
		//Remove button set
		else if(event.getSource() == removeButtons) {
			System.out.println(Arrays.toString(this.config.buttonConfigs));
			config.removeAudioSet(this.currentRow);
			System.out.println(Arrays.toString(this.config.buttonConfigs));
			updateRows();
			if(this.currentRow == 1) {
				switchRow(this.currentRow);
			}
			else {
				switchRow(this.currentRow - 1);
			}
			this.logger.logMessage("Audioset removed");
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
			
			if(config.getImageFile() != null) {
				BufferedImage readImage = null;
				try {
					readImage = ImageIO.read(config.getImageFile());
				}
				catch(IOException e) {
					return;
				}
				this.setIcon(new ImageIcon(readImage));
				this.setVerticalTextPosition(SwingConstants.BOTTOM);
				this.setHorizontalTextPosition(SwingConstants.CENTER);
			}
			else {
				this.setIcon(null);
			}
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
		private JLabel currentSoundPath;
		private JLabel currentImagePath;
		private JButton setColor;
		private JButton confirmSetup;
		private JPanel buttonsPanel;
		
		private JButton selectImage;
		
		
		
		
		// Panel for color of button
		private JPanel currentColorPanel;
		
		/**Music Player for the sound selected**/
		private MusicPlayer musicPlayer;
		
		/**Button color selection frame*/
		private ColorFrame colorFrame;
		
		/**File Selection Frame*/
		private FileSelector fileSelector;
		
		/**Emoji Selection frame*/
		private EmojiSearchFrame emojiFrame;
		
		/**Image selection frame**/
		private ImageFrame imageFrame;
		
		/**Recording frame**/
		private RecordingFrame recordingFrame;
		
		/** Current color that the user has selected for the current SetUpButton */
		private Color currentColor;
		
		/**Current AudioFile that has been selected for the current SetUpButton*/
		private File currentAudioFile;
		
		
		
		/** Current image file that has been selected for the current button */
		private File currentImageFile;
		
		/**Current button being edited**/
		private SetUpButton currentButton;
		
		private ConfigPanel config;
		
		/**The default color of JButtons*/
		private final Color DEFAULT_COLOR = new Color(UIManager.getColor("Button.background").getRGB());

		public SetUpFrame() {
			// Frame initial values
			super("Setup Button");
			this.addWindowListener(this);
			this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			this.setSize(450, 450);
			this.setVisible(false);

			// Intializing other frames
			emojiFrame = new EmojiSearchFrame(new EmojiListener());
			colorFrame = new ColorFrame();
			fileSelector = new FileSelector(null, FileSelector.SOUND);
			recordingFrame = new RecordingFrame();
			imageFrame = new ImageFrame();

			// Getting default color for a jbutton
			currentColor = DEFAULT_COLOR;

			// Adding the organizing configuration panel.
			config = new ConfigPanel();
			this.setContentPane(config);
		}
		/**
		 * Hides the button setup frame, disabling the music player. 
		 */
		public void hideSetupFrame() {
			config.hideAllSubFrames();

			
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
			if(this.currentButton == button && this.isVisible() == true) {
				
				//Bringing setup frame to the front. 
				java.awt.EventQueue.invokeLater(new Runnable() {
				    @Override
				    public void run() {
				        setUpFrame.toFront();
				        
				        //Moving up under panels.
				        if(recordingFrame.isVisible())recordingFrame.toFront();
				        if(emojiFrame.isVisible())emojiFrame.toFront();
				        if(imageFrame.isVisible())imageFrame.toFront();
				        if(colorFrame.isVisible())colorFrame.toFront();
				        if(fileSelector.isVisible())fileSelector.toFront();
				    }
				});
				return;
			}
			else if(this.isVisible() == true) {
				JOptionPane.showMessageDialog(null,"Don't lose your work!\nEither cancel or confirm setup before editing a new button!");
				
				//Bringing setup frame to the front. 
				java.awt.EventQueue.invokeLater(new Runnable() {
				    @Override
				    public void run() {
				        setUpFrame.toFront();
				        
				        //Moving up under panels.
				        if(recordingFrame.isVisible())recordingFrame.toFront();
				        if(emojiFrame.isVisible())emojiFrame.toFront();
				        if(imageFrame.isVisible())imageFrame.toFront();
				        if(colorFrame.isVisible())colorFrame.toFront();
				        if(fileSelector.isVisible())fileSelector.toFront();
				    }
				});
				return;
			}
			setUpFrame.hideSetupFrame();
			this.currentButton = button;
			this.currentColor = config.getButtonColor();
			this.currentColorPanel.setBackground(this.currentColor);
			this.currentAudioFile = config.getSoundFile();
			this.currentImageFile = config.getImageFile();
			nameField.setText(config.getButtonText());
			
			//Setting current audio file
			if (this.currentAudioFile != null) {
				currentSoundPath.setText("Sound Path: " + this.currentAudioFile.getPath());
				musicPlayer = new MusicPlayer(this.currentAudioFile);
			} else {
				currentSoundPath.setText("Sound Path: (none)");
				musicPlayer = null;
			}
			//Setting current image file.
			if(this.currentImageFile !=null) {
				currentImagePath.setText("Image Path: " + this.currentImageFile.getPath());
			}
			else {
				currentImagePath.setText("ImagePath: (none)");
			}
			setUpFrame.setVisible(true);
		}
		
		
		/**
		 * Listener for when an audio file has been opened. 
		 * @author jordan
		 * @version March 28th 2019.
		 */
		public class OpenSoundListener implements SelectionListener {
			public void onFileSelected(File file) {
				if (FileIO.checkWaveFormat(file)) {
					currentAudioFile = file;
					currentSoundPath.setText("Sound Path: " + currentAudioFile.getPath());
					if(musicPlayer != null)musicPlayer.stop();
					musicPlayer = new MusicPlayer(currentAudioFile);
					logger.logMessage("Sound file added.");
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
				buttonsPanel.setLayout(new GridLayout(6, 1));

				
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
				
				
				

				currentSoundPath = new JLabel("Sound Path: (none)");
				buttonsPanel.add(currentSoundPath);

				// Button for setting the color that the button should have.
				setColor = new JButton("Select Color");
				setColor.addActionListener(this);
				buttonsPanel.add(setColor);

				//Color panel
				currentColorPanel = new JPanel();
				currentColorPanel.add(new JLabel("Current Color"));
				currentColorPanel.setBackground(currentColor);
				buttonsPanel.add(currentColorPanel);
				

				
				//Select image button
				selectImage = new JButton("Select Image");
				selectImage.addActionListener(this);
				buttonsPanel.add(selectImage);
				
				
				
				currentImagePath = new JLabel("Image Path: (none)");
				buttonsPanel.add(currentImagePath);
				
				
				

				this.add(buttonsPanel, BorderLayout.CENTER);

				//Confirm setup panel. 
				confirmSetup = new JButton("Confirm Setup");
				confirmSetup.addActionListener(this);
				this.add(confirmSetup, BorderLayout.SOUTH);
				

			}
			public void hideAllSubFrames() {
				imageFrame.setVisible(false);
				emojiFrame.setVisible(false);
				fileSelector.setVisible(false);
				colorFrame.setVisible(false);
				recordingFrame.hideRecordingFrame();
			}
			public void actionPerformed(ActionEvent event) {
				if (event.getSource() == setColor) {
					hideAllSubFrames();
					
					// Bringing up color frame
					colorFrame.setLocation(new Point(getSetupLocation().x + 60, getSetupLocation().y + 200));
					colorFrame.setVisible(true);
					
					
					logger.logMessage("Color panel opened.");
				} else if (event.getSource() == emojiButton) {
					hideAllSubFrames();
					
					// Bringing up emoji pane
					emojiFrame.setLocation(new Point(getSetupLocation().x + 200, getSetupLocation().y + 60));
					emojiFrame.setVisible(true);
					logger.logMessage("Emoji panel opened.");
				
				} else if (event.getSource() == selectSound) { //Select sound button
					hideAllSubFrames();
					
					fileSelector.setMode(FileSelector.SOUND);
					fileSelector.setSelectionListener(new OpenSoundListener());
					logger.logMessage("Sound selection panel opened.");
					fileSelector.setVisible(true);
					
				}else if(event.getSource() == selectImage) {
					hideAllSubFrames();
					
					//Opening image frame. 
					BufferedImage currentImage = null;
					try {
						currentImage = ImageIO.read(currentImageFile);
					}
					catch(Exception e) {
						
					}
					imageFrame.openFor(currentImage);
					imageFrame.setVisible(true);
				}else if(event.getSource() == recordSound) {
					hideAllSubFrames();
					
					recordingFrame.setVisible(true);
				}
				else if (event.getSource() == playSound) {
					if (musicPlayer != null) {
						logger.logMessage("Play sound button pressed.");
						musicPlayer.play();
					}
				} else if (event.getSource() == confirmSetup) {
					ButtonConfiguration button = currentButton.getConfiguration();
					button.setText(nameField.getText());
					button.addColor(currentColor);
					button.addSoundFile(currentAudioFile);
					button.addImageFile(currentImageFile);
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
			private static final String RECORDING = " ● Recording ● ";
			private static final String NOT_RECORDING = " Not recording";
			private volatile boolean isRecording = false;
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
				flashingLabel = new JLabel(NOT_RECORDING);
				flashingLabel.setHorizontalAlignment(SwingConstants.CENTER);
				flashingLabel.setForeground(Color.black);
				this.add(flashingLabel);
				
				//Setting up the stop button
				stopButton = new JButton("Stop Recording");
				stopButton.addActionListener(this);
				stopButton.setEnabled(false);
				this.add(stopButton);
				this.setVisible(false);
			}
			/**
			 * Method for hiding the recording panel. 
			 */
			public void hideRecordingFrame() {
				this.setVisible(false);
				if(recorder.isRecording()) {
					isRecording = false;
					recorder.stop();
					
					//Resetting the buttons. 
					recordButton.setEnabled(true);
					stopButton.setEnabled(false);
				}
			}
			/**
			 * ActionListener for the buttons for the recording frame
			 */
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == recordButton) {
					logger.logMessage("Recording button pressed.");
					//Updating buttons
					recordButton.setEnabled(false);
					stopButton.setEnabled(true);
					recorder.record();
					isRecording = true;
					Thread blinkThread = new Thread(new BlinkTask());
					blinkThread.start();
				}
				else if(e.getSource() == stopButton) {
					logger.logMessage("Stopped recording.");
					//Updating buttons
					stopButton.setEnabled(false);
					recordButton.setEnabled(true);
					
					File soundFile = new File(currentButton.getConfiguration().tempAudioPath());
					MusicRecorder.writeToFile(recorder.stop(),recorder.getFormat(),soundFile);
					
					currentAudioFile = soundFile;
					currentSoundPath.setText("Sound Path: " + currentAudioFile.getPath());
					if(musicPlayer != null && musicPlayer.isPlaying())musicPlayer.stop();
					musicPlayer = new MusicPlayer(currentAudioFile);
					isRecording = false;
				}
			}
			/**
			 * BlinkTask for the blinking recording label. 
			 * @author jordan
			 * @version March 30th 2019
			 */
			public class BlinkTask implements Runnable {
				public void run() {
					//Flashing label blinking 
					flashingLabel.setText("");
					flashingLabel.setForeground(Color.red);
					flashingLabel.setText(RECORDING);
					long delayTime = System.currentTimeMillis() + 500;
					boolean blink = true;
					while(isRecording) {
						if(System.currentTimeMillis() < delayTime) {
							continue;
						}
						else {
							delayTime = System.currentTimeMillis() + 500;
						}
						blink =! blink;
						if(!blink) {
							flashingLabel.setText(RECORDING);
						}
						else{
							flashingLabel.setText("");
						}
					}
					flashingLabel.setText(NOT_RECORDING);
					flashingLabel.setForeground(Color.black);
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
		 * Image frame for selecting and orienting the image for the buttons. 
		 * @author jordan
		 * @version Friday March 29th 2019
		 */
		public class ImageFrame extends JFrame implements ActionListener{
			public JLabel imageLabel; 
			public int orientation; 
			
			//Orientations
			public static final int VERTICAL = 0;
			public static final int HORIZONTAL = 1;
			public static final int SQUARE = 2; 
			
			//Buttons
			private JButton openImage; 
			private JButton vertical;
			private JButton horizontal;
			private JButton square;
			private JButton confirmImage;
			
			private static final int MAX_WIDTH = 160;
			private static final int MAX_HEIGHT = 160; 
			
			
			//The current icon image and the original image. 
			private BufferedImage iconImage; 
			private BufferedImage originalImage; 
			
			public ImageFrame() {
				super("Select Image");
				super.setSize(220,400);
				super.setVisible(false);
				super.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				super.setLayout(new BorderLayout());
				
				openImage = new JButton("Open Image");
				openImage.addActionListener(this);
				this.add(openImage,BorderLayout.NORTH);
				
				imageLabel = new JLabel("(No Image)");
				imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
				this.add(imageLabel,BorderLayout.CENTER);
				
				JPanel orientations = new JPanel();
				orientations.setBackground(Color.white);
				orientations.setLayout(new GridLayout(5,1));
				
				JLabel setOr = new JLabel("Set Orientation");
				setOr.setHorizontalAlignment(SwingConstants.CENTER);
				setOr.setHorizontalTextPosition(SwingConstants.CENTER);
				setOr.setBackground(Color.white);
				orientations.add(setOr);
				
				vertical = new JButton("Vertical");
				vertical.addActionListener(this);
				orientations.add(vertical);
				vertical.setEnabled(false);
				
				square = new JButton("Square");
				square.addActionListener(this);
				orientations.add(square);
				
				horizontal = new JButton("Horizontal");
				horizontal.addActionListener(this);
				orientations.add(horizontal);
				
				confirmImage = new JButton("Confirm Image");
				confirmImage.addActionListener(this);
				orientations.add(confirmImage); 
				
				this.add(orientations, BorderLayout.SOUTH);
			}
			/**
			 * Opens the image for this button for selecting and orienting. 
			 * @param image
			 */
			public void openFor(BufferedImage image) {
				if(this.iconImage == image) {
					return;
				}
				this.iconImage = image;
				this.originalImage = image;
				if(image != null) {
					imageLabel.setIcon(new ImageIcon(iconImage));
					imageLabel.setText("");
				
					if(this.iconImage.getWidth(null) == MAX_WIDTH && this.iconImage.getHeight(null) == MAX_HEIGHT) {
						this.orientation = SQUARE;
					}
					else if(this.iconImage.getWidth(null) == MAX_WIDTH) {
						this.orientation = HORIZONTAL;
					}
					else {
						this.orientation = VERTICAL;
					}
				}
				else {
					imageLabel.setIcon(null);
					imageLabel.setText("(No Image)");
				}
			}
			public void setOrientation(int or) {
				if(this.orientation == or) {
					return;
				}
				orientation = or;
				if(or == VERTICAL) {
		
					//Resetting buttons
					vertical.setEnabled(false);
					square.setEnabled(true);
					horizontal.setEnabled(true);
				}
				else if(or == HORIZONTAL){
					
					//Resetting buttons
					horizontal.setEnabled(false);
					vertical.setEnabled(true);
					square.setEnabled(true);
				}
				else if(or == SQUARE) {
					
					//Resetting buttons
					square.setEnabled(false);
					horizontal.setEnabled(true);
					vertical.setEnabled(true);
				}
				if(originalImage != null) {
					iconImage = scaleImage(originalImage);
					imageLabel.setIcon(new ImageIcon(iconImage));
				}
				
				
			}
			/**
			 * Action Listener for the ImageFrame's buttons 
			 */
			public void actionPerformed(ActionEvent event) {
				if(event.getSource() == openImage){
					fileSelector.setMode(FileSelector.PICTURE);
					fileSelector.setSelectionListener(new OpenImageListener());
					fileSelector.setVisible(true);
				}
				else if(event.getSource() == vertical) {
					setOrientation(VERTICAL);
				}
				else if(event.getSource() == horizontal) {
					setOrientation(HORIZONTAL);
				}
				else if(event.getSource() == square) {
					setOrientation(SQUARE);
				}
				else if(event.getSource() == confirmImage) {
					if(iconImage != null) {
						try {
							File tempFile = new File(currentButton.getConfiguration().tempImagePath());
							ImageIO.write(iconImage,"png",tempFile);
							currentImageFile = tempFile;
							currentImagePath.setText("Image Path: " + currentImageFile.getPath());
							this.setVisible(false);
						}
						catch(IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		/**
		 * Scales the image to the correct size. 
		 * @param image - the image to be scaled.
		 * @return the image scaled to the correct size. 
		 */
		public BufferedImage scaleImage(Image image) {
			if(imageFrame.orientation == ImageFrame.SQUARE) {
				if(image.getWidth(null) != ImageFrame.MAX_WIDTH || image.getHeight(null) != ImageFrame.MAX_HEIGHT) {
					image = image.getScaledInstance(ImageFrame.MAX_WIDTH,ImageFrame.MAX_HEIGHT,Image.SCALE_SMOOTH);
					
				}
			}
			else if(imageFrame.orientation == ImageFrame.HORIZONTAL) {
				if(image.getWidth(null) != ImageFrame.MAX_WIDTH || image.getHeight(null) != (ImageFrame.MAX_HEIGHT / 2)) {
					image = image.getScaledInstance(ImageFrame.MAX_WIDTH,(ImageFrame.MAX_HEIGHT / 2),Image.SCALE_SMOOTH);
				}
			}
			else if(imageFrame.orientation == ImageFrame.VERTICAL) {
				if(image.getWidth(null) != (ImageFrame.MAX_WIDTH / 2) || image.getHeight(null) != ImageFrame.MAX_HEIGHT) {
					image = image.getScaledInstance((ImageFrame.MAX_WIDTH / 2),ImageFrame.MAX_HEIGHT,Image.SCALE_SMOOTH);
				}
			}
			BufferedImage newImage = new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_ARGB);
			Graphics2D imageGraphics = (Graphics2D) newImage.getGraphics();
			imageGraphics.drawImage(image,0,0,null);
			return newImage;
		}
		public class OpenImageListener implements SelectionListener {
			public void onFileSelected(File file) {
				if(!FileIO.checkImageFile(file)) {
					JOptionPane.showMessageDialog(null,"Please choose a valid image file");
				}
				else {
					currentImageFile = file;
					imageFrame.originalImage = null;
					try {
						imageFrame.originalImage = ImageIO.read(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
					imageFrame.iconImage = scaleImage(imageFrame.originalImage);
					
					ImageIcon icon = new ImageIcon(imageFrame.iconImage);
					imageFrame.imageLabel.setText("");
					imageFrame.imageLabel.setIcon(icon);
					fileSelector.setVisible(false); 
				}
			}
			
			
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
				this.add(new ColorButton(Color.LIGHT_GRAY, this));
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
				logger.logMessage("New color added");
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
