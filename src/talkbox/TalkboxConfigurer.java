package talkbox;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import browsing.FileSelector;
import browsing.SelectionListener;
import configurer.Configuration;
import configurer.RecordingPanel;
import configurer.SetUpPanel;

/**
 * Main class for the Talkbox Configuration App
 * 
 * @author jordan
 * @version January 31st 2019
 */
public class TalkboxConfigurer {
	/** The main frame for the app **/
	ConfigurerFrame frame;
	/** Base panel that selects between the other supporting panels **/
	BasePanel panel;
	/** The configuration that the configurer is currently setup with. **/
	Configuration config;

	public TalkboxConfigurer() {
		frame = new ConfigurerFrame();
		panel = new BasePanel();
		frame.setContentPane(panel);
		frame.setVisible(true);
	}

	public class MenuPanel extends JPanel implements ActionListener {
		JButton setUpButtons;
		JButton recordAudio;
		JButton createNew;
		JButton editOld;
		JButton selectExisting;
		FileSelector selector;
		private static final String TITLE = "Talkbox Configurator";
		private final Font TITLE_FONT = new Font(Font.SANS_SERIF,Font.BOLD,42);
		private final Font BUTTON_FONT = new Font(Font.SANS_SERIF,Font.PLAIN,26);
		public MenuPanel() {
			this.setLayout(new BorderLayout());
			//Adding title label
			JLabel titleLabel = new JLabel(TITLE);
			titleLabel.setBackground(Color.blue);
			titleLabel.setForeground(Color.orange);
			titleLabel.setFont(TITLE_FONT);
			titleLabel.setHorizontalAlignment(JLabel.CENTER);
			this.add(titleLabel,BorderLayout.NORTH);
			this.setBackground(Color.blue);
			
			
			//Adding buttons to the button panel
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridLayout(2, 2));
			
			// Recording Button
			recordAudio = new JButton("Record Audio");
			recordAudio.addActionListener(this);
			recordAudio.setFont(BUTTON_FONT);
			buttonPanel.add(recordAudio);
			
			// Set up button
			setUpButtons = new JButton("Set Up Buttons");
			setUpButtons.addActionListener(this);
			setUpButtons.setFont(BUTTON_FONT);
			buttonPanel.add(setUpButtons);
			// Set it disabled by default
			setUpButtons.setEnabled(false);
			
			// Create new config directory
			createNew = new JButton("Create New Configuration");
			createNew.setFont(BUTTON_FONT);
			createNew.addActionListener(this);
			buttonPanel.add(createNew);
			
			// Select existing config directory
			selectExisting = new JButton("Open Existing Configuration");
			selectExisting.addActionListener(this);
			selectExisting.setFont(BUTTON_FONT);
			buttonPanel.add(selectExisting);
			this.add(buttonPanel,BorderLayout.CENTER);
			selector = new FileSelector(null, FileSelector.DIRECTORY);
		}
		@Override
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == setUpButtons) {
				panel.showSetup();
			} else if (event.getSource() == recordAudio) {
				panel.showRecording();
			}
			// set pre-existing configration
			else if (event.getSource() == selectExisting) {
				JOptionPane.showMessageDialog(null,"Please select a talkboxData Configuration Directory");
				selector.setVisible(true);
				selector.setSelectionListener(new SelectionListener() {
					public void onFileSelected(File file) {
						config = Configuration.readConfiguration(file);
						// If the configuration was successfully opened
						if (config != null) {
							panel.configureSetup();
							setUpButtons.setEnabled(true);
						}
						// Otherwise, show an error message.
						else {
							JOptionPane.showMessageDialog(null,
									"Failed to read a Talkbox Configuration from the selected directory.\n"
											+ " Please ensure it is a valid talkboxData Directory");
						}
						//Set selector to invisible. 
						selector.setVisible(false);
					}
				});
			}
			//Create new configuration directory, and use it. 
			else if (event.getSource() == createNew) {
				JOptionPane.showMessageDialog(null, 
						"Please select a directory for the talkboxData Directory to be saved in.");
				selector.setVisible(true);
				selector.setSelectionListener(new SelectionListener() {
					public void onFileSelected(File file) {
						//Create the directory within the dir selected by user.
						config = new Configuration(file.getPath());
						panel.configureSetup();
						
						//Adjust enabling of buttons.
						setUpButtons.setEnabled(true);
						//Set selector to invisible. 
						selector.setVisible(false);
					}
				});
			}
		}
	}

	public class BasePanel extends JPanel {
		CardLayout layout;
		public static final String MENU = "MENU";
		public static final String SETUP = "SETUP";
		public static final String RECORD = "RECORD";
		public MenuPanel menu;
		public SetUpPanel setup;
		public RecordingPanel record;

		public BasePanel() {
			layout = new CardLayout();
			this.setLayout(layout);
			// Adding menu to the base
			menu = new MenuPanel();
			this.add(MENU, menu);

			// Adding recording panel to the base
			record = new RecordingPanel(this);
			this.add(RECORD, record);

			// Adding setup panel to the base; send it
			// reference of current configurance
			setup = new SetUpPanel(this);
			this.add(SETUP, setup);
			layout.show(this, MENU);

			this.revalidate();
			this.repaint();
		}

		public void configureSetup() {
			setup.setConfiguration(config);
		}

		public void showSetup() {
			layout.show(this, SETUP);
		}

		public void showMainMenu() {
			layout.show(this, MENU);
		}

		public void showRecording() {
			layout.show(this, RECORD);
		}
	}

	public static void main(String[] args) {
		new TalkboxConfigurer();
	}

	public class ConfigurerFrame extends JFrame {
		public static final int FRAME_X = 1000;
		public static final int FRAME_Y = 700;

		public ConfigurerFrame() {
			super("TalkBox Configurer");
			this.setSize(FRAME_X, FRAME_Y);
			this.setLocation(20, 20);
			this.setResizable(false);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setVisible(false);
		}
	}
}
