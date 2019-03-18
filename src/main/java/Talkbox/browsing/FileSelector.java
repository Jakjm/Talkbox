package main.java.Talkbox.browsing;

import javax.imageio.ImageIO;
/**
 * Basic GUI class for File selection.
 * @author jakjm
 * @version January 31st 2019
 */
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FileSelector extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	/** Backmost panel of the panel. */
	private JPanel outerPanel;
	/** File of the current directory we are in. */
	private File currentFile;
	/** Panel for the file buttons. */
	private JPanel innerPanel;
	private JPanel hud; // Heads up display panel.
	private JLabel directoryLabel; // Label for the current directory we are in.
	private JButton backButton; // Button for returning to the previous (containing) directory. i.e "cd .."
	private JButton returnHome; // Button for returning to the user's home directory.
	private JScrollPane innerPaneScroll; // The scroll panel for the inser panel.

	private int mode = -1; // Selection mode for the file selector.
	/** The constant for storing that the file is of unknown type **/
	public static final int UNKNOWN = -1;
	/** The constant for storing that the file is a directory **/
	public static final int DIRECTORY = 1;
	/** The constant for storing that the file is a picture file **/
	public static final int PICTURE = 2;
	/** The number constant for storing that the file is a sound file. **/
	public static final int SOUND = 3;
	/** The constant for storing that the file is a text file. **/
	public static final int TEXT = 4;
	// The home directory of the user.
	private SelectionListener listener;
	public static final String fileSep = System.getProperty("file.separator");
	public final static File homeFile = new File(System.getProperty("user.home"));

	/**
	 * Main method for testing
	 * @param args
	 */
	public static void main(String[] args) {
		FileSelector selector = new FileSelector(null, 2);
		selector.setAutoStop();
		selector.setVisible(true);
	}
	public void setAutoStop() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	/**
	 * Allows the user to select the selection mode the file selector should be in.
	 * See the static constants for arbitrary, picture, and directory selections.
	 * @param listener - the file listener for defining
	 * what should be done once a file of the mode has been clicked. null for doing nothing.
	 * @param mode - the type of file to be opened by the FileSelector
	 * FileSelector.DIRECTORY, FileSelector.SOUND, etc
	 */
	public FileSelector(SelectionListener listener, int mode) {
		this(listener);
		if (mode < UNKNOWN || mode > TEXT)
			throw new IllegalArgumentException("Mode invalid");
		this.mode = mode;
	}

	public FileSelector(SelectionListener listener) {
		// Default configuration for the window.
		super("Open File");
		this.listener = listener;
		this.setSize(650, 400);
		this.setVisible(false);
		this.setEnabled(true);
		this.setResizable(false);

		// Configuring the outermost panel.
		outerPanel = new JPanel();
		outerPanel.setSize(this.getSize());
		outerPanel.setVisible(true);
		outerPanel.setLayout(new BorderLayout());

		hud = new JPanel();
		hud.setLayout(new GridLayout(1, 2));

		directoryLabel = new JLabel();
		directoryLabel.setText("");
		hud.add(directoryLabel);

		// Buttons for the hud
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(1, 2));

		backButton = new JButton("Parent Folder");
		backButton.addActionListener(this);
		backButton.setVisible(true);
		backButton.setEnabled(true);
		buttonsPanel.add(backButton);
		

		// Buttons for the hud
		returnHome = new JButton("Back to Home");
		returnHome.addActionListener(this);
		returnHome.setVisible(true);
		returnHome.setEnabled(true);
		buttonsPanel.add(returnHome);
		hud.add(buttonsPanel);

		outerPanel.add(hud, BorderLayout.NORTH);
		this.add(outerPanel);

		// Inner Panel for Buttons
		innerPanel = new JPanel();
		innerPanel.setVisible(true);
		innerPanel.setLayout(new GridLayout(-1,4));

		// Configuring the inner scroll pane, which traverses the inner panel.
		innerPaneScroll = new JScrollPane();
		innerPaneScroll.setViewportView(innerPanel);
		innerPaneScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		innerPaneScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		innerPaneScroll.setVisible(true);
		outerPanel.add(innerPaneScroll, BorderLayout.CENTER);

		openDirectory(homeFile);
	}
	public void setVisible(boolean b) {
		super.setVisible(b);
	}
	public void reset() {
		openDirectory(homeFile);
	}
	/**
	 * Updates the panel with the list of files contained within the given directory.
	 * @param directory - the directory to move into, and create buttons for.
	 */
	public void openDirectory(File directory) {
		clearOldFileButtons();

		/*
		 * Getting the list of files under this file and adding them to the selectable
		 * button list.
		 */
		File[] fileList = directory.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			File currentFile = fileList[i];
			if (currentFile.getName().charAt(0) != '.') {
				FileButton button = new FileButton(this, currentFile);
				innerPanel.add(button);
			}
		}
		
		currentFile = directory;
		if (currentFile.getPath().equals(homeFile.getPath())) {
			returnHome.setEnabled(false);
		} else {
			returnHome.setEnabled(true);
		}

		// Refreshes the backmost and innermost panel.
		innerPanel.repaint();
		innerPanel.revalidate();
		outerPanel.repaint();
		outerPanel.revalidate();

		// Sets the directory label to the current directory we are in.
		directoryLabel.setText("Current Path: " + currentFile.getPath());
	}

	public void setSelectionListener(SelectionListener listener) {
		this.listener = listener;
	}

	/**
	 * Removes all of the components from the inner panel - which is just where we
	 * put all of our fileButtons.
	 */
	private void clearOldFileButtons() {
		innerPanel.removeAll();
	}

	/**
	 * ActionListener methods for our buttons for the file selector.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == returnHome)
			goHome();
		if (e.getSource() == backButton)
			goBack();
	}

	/**
	 * Method for returning to the containing directory. 
	 */
	private void goBack() {
		if (currentFile.getParentFile() != null) {
			openDirectory(currentFile.getParentFile());
		}
	}

	/**
	 * Method for returning to the user's home directory when the button is pressed.
	 */
	private void goHome() {
		if (!currentFile.getPath().equals(homeFile.getPath())) {
			openDirectory(homeFile);
		}
	}

	/**
	 * Generic method for when a file has been selected.
	 * 
	 * @param thisFile - the file that has been selected by the user.
	 */
	public void select(File thisFile) {
		this.currentFile = thisFile;
		if (this.listener != null)
			this.listener.onFileSelected(thisFile);
	}

	/**
	 * File button for selecting a particular directory or file from the available
	 * options.
	 * 
	 * @author jakjm
	 * @version July 5th 2018
	 */
	private class FileButton extends JButton implements ActionListener {
		private static final long serialVersionUID = 1L;
		private File thisFile;
		private final String[] PICTURE_FILE_TYPES = { "jpg", "bmp", "jpeg", "png" };
		private final String[] SOUND_FILE_TYPES = { "wav", "mp3" };
		private final String[] TEXT_FILE_TYPES = { "txt" };

		private int type;

		/** The constant for storing that the file is of unknown type **/
		public static final int UNKNOWN = -1;
		/** The constant for storing that the file is a directory **/
		public static final int DIRECTORY = 1;
		/** The constant for storing that the file is a picture file **/
		public static final int PICTURE = 2;
		/** The number constant for storing that the file is a sound file. **/
		public static final int SOUND = 3;
		/** The constant for storing that the file is a text file. **/
		public static final int TEXT = 4;


		
		
		
		private FileSelector selector;

		/**
		 * Constructs a button for selecting the current file.
		 * 
		 * @param selector - the selector frame that the button will be a part of.
		 * @param file     - the file the button is being created for.
		 */
		public FileButton(FileSelector selector, File file) {
			super();
			this.thisFile = file;
			this.setVisible(true);
			this.setEnabled(true);
			this.addActionListener(this);
			
			// Determines the type of file this button is for.
			determineType();

			String text = file.getName();
			
			//Formatting the text
			int charsWithoutNL = 0;
			for(int i = 1;i < text.length();i++) {
				char c = text.charAt(i);
				if(charsWithoutNL >= 14 || 
						(charsWithoutNL >= 5 && (c == ' ' || c == '.' || c == '_' || c == '-' || Character.isUpperCase(c)))) {
					text = text.substring(0,i) + "<br>" + text.substring(i);
					charsWithoutNL = 0;
					i = i + 4;
				}
				else {
					++charsWithoutNL;
				}
			}
			
			// Adding a button to select this file/directory.
			String buttonText = String.format("<html>%s</html>",text);
			this.setText(buttonText);
			this.setVerticalTextPosition(SwingConstants.BOTTOM);
			this.setHorizontalTextPosition(SwingConstants.CENTER);
			this.selector = selector;
		}

		public void determineType() {
			// Checking if the file is a directory.
			if (thisFile.isDirectory()) {
				type = DIRECTORY;
				this.setIcon(FileIcons.FOLDER_ICON);
				return;
			}
			String fileName = thisFile.getName();
			String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);
			// Checking if the file is a picture.
			for (int i = 0; i < PICTURE_FILE_TYPES.length; i++) {
				if (fileExtension.equals(PICTURE_FILE_TYPES[i])) {
					type = PICTURE;
					this.setIcon(FileIcons.IMAGE_ICON);
					return;
				}
			}
			// Checking if the file is a sound file.
			for (int i = 0; i < SOUND_FILE_TYPES.length; i++) {
				if (fileExtension.equals(SOUND_FILE_TYPES[i])) {
					type = SOUND;
					this.setIcon(FileIcons.SOUND_ICON);
					return;
				}
			}
			// Checking if the file is a text file.
			for (int i = 0; i < TEXT_FILE_TYPES.length; i++) {
				if (fileExtension.equals(TEXT_FILE_TYPES[i])) {
					type = TEXT;
					this.setIcon(FileIcons.TEXT_ICON);
					return;
				}
			}

			// If we do not know the filetype.
			type = UNKNOWN;
			this.setIcon(FileIcons.UNKNOWN_ICON);
			return;
		}

		/**
		 * ActionListener method for this file button . Controls the FileSelector from
		 * the button when the appropriate buttons are pressed.
		 */
		public void actionPerformed(ActionEvent e) {
			if (this.type == selector.mode) {
				/**
				 * If the file selector is in Select Directory mode, we check if the user simply
				 * wants to move in the folder or if this is the folder they want to select.
				 */
				if (selector.mode == FileSelector.DIRECTORY) {
					int result = JOptionPane.showConfirmDialog(selector,
							"Would you like to select this folder or move into it? \n (Yes to select this folder, No to move inside it)");
					if (result == JOptionPane.YES_OPTION) {
						selector.select(thisFile);
					} else {
						selector.openDirectory(thisFile);
					}
				} else {
					selector.select(thisFile);
				}
			} else if (this.type == DIRECTORY) {
				selector.openDirectory(thisFile);
			}
		}
	}
}
