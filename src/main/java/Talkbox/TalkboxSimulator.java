package main.java.Talkbox;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
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

import main.java.Talkbox.browsing.FileSelector;
import main.java.Talkbox.browsing.SelectionListener;
import main.java.Talkbox.configurer.Configuration;
import main.java.Talkbox.filehandler.FileIO;
import main.java.Talkbox.log.LogController;
import main.java.Talkbox.simulator.SimulatorPanel;

/**
 * Talkbox simulator app for the talkbox.
 * @author jordan
 * @version February 20th 2019
 */
public class TalkboxSimulator {
	TalkboxFrame frame;
	
	SimulatorPanel simulatorPanel;
	FileSelector selector;
	public Configuration configuration;
	public JPanel backPanel;
	public MenuPanel menuPanel;
	private static final String SIMULATOR = "SIM";
	private static final String MAIN_MENU = "MAIN";
	/**The size of the frame while in the main menu**/
	private static final Dimension MAIN_SIZE = new Dimension(300,240);
	/**The size of the frame while using the actual simulator**/
	private static final Dimension SIM_SIZE = new Dimension(900,600);
	private CardLayout layout; 
	private LogController simLogger;
	/** Main method that simply creates a new talkbox simulator instance **/
	public static void main(String[] args) {
		new TalkboxSimulator();
	}

	public TalkboxSimulator() {
		// Initializing frame,panel...
		frame = new TalkboxFrame();
		backPanel = new JPanel();
		layout = new CardLayout();
		backPanel.setLayout(layout);
		
		//Adding the simulator panel to the back panel
		this.simLogger = new LogController(LogController.LogType.SIM_LOG, null);
		simulatorPanel = new SimulatorPanel(this, this.simLogger);
		backPanel.add(simulatorPanel,SIMULATOR);
		
		menuPanel = new MenuPanel();
		backPanel.add(menuPanel,MAIN_MENU);
		frame.setContentPane(backPanel);
		showMain();
		

		// Trying to open a configuration file
		selector = new FileSelector(null, FileSelector.DIRECTORY);
		frame.setVisible(true);
	}
	public void showMain() {
		frame.setSize(MAIN_SIZE);
		layout.show(backPanel,MAIN_MENU);
	}
	public void showSim() {
		frame.setSize(SIM_SIZE);
		layout.show(backPanel,SIMULATOR);
	}
	public class MenuPanel extends JPanel implements ActionListener{
		private JButton openConfig;
		private JButton openSimulator;
		public MenuPanel() {
			this.setLayout(new GridLayout(3,1));
			this.setBackground(Color.LIGHT_GRAY);
			
			JLabel titleLabel = new JLabel("Talkbox Simulator");
			titleLabel.setFont(new Font("Rockwell",Font.PLAIN,32));
			titleLabel.setHorizontalAlignment(JLabel.CENTER);
			titleLabel.setForeground(Color.BLACK);
			this.add(titleLabel);
			
			openConfig = new JButton("Open Talkbox Configuration");
			openConfig.setFont(new Font("Rockwell",Font.PLAIN,12));
			openConfig.addActionListener(this);
			this.add(openConfig);
			
			openSimulator = new JButton("Simulate Configuration");
			openSimulator.setFont(new Font("Rockwell",Font.PLAIN,12));
			openSimulator.addActionListener(this);
			this.add(openSimulator);
			openSimulator.setEnabled(false);
		}
		public void actionPerformed(ActionEvent event) {
			if(event.getSource() == openConfig) {
				simLogger.logMessage("Select configuration button pressed.");
				JOptionPane.showMessageDialog(null, "Please select a TalkboxData configuration.");
				// Making the selector visible.
				selector.setVisible(true);
				
				// Setting up what should happen once a file is found
				selector.setSelectionListener(new SelectionListener() {
					public void onFileSelected(File file) {
						Configuration loadConfig = Configuration.readConfiguration(file);
						// If the configuration is valid, open it
						if (loadConfig != null) {
							selector.setVisible(false);
							configuration = loadConfig;
							simulatorPanel.setupConfiguration(configuration);
							openSimulator.setEnabled(true);
							simLogger.addLogFolder(new File(configuration.getConfigDir() + FileIO.SEP + "logs"));
							simLogger.logMessage("Config added at " + configuration.getConfigDir());
						}
						// Otherwise, keep going until one is found
						else {
							JOptionPane.showMessageDialog(null,
									"Configuration not valid, please try again.");
							simLogger.logMessage("Failed to add configuration");
						}
					}
				});
				
			}
			else if(event.getSource() == openSimulator) {
				showSim();
				simLogger.logMessage("Simulator opened.");
			}
		}
	}

	/**
	 * Frame for the talkbox simulator app
	 * 
	 * @author jordan
	 * @version January 31st 2019
	 */
	public class TalkboxFrame extends JFrame {
		public TalkboxFrame() {
			/** Setting the frame to shutdown the program when closed **/
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setResizable(false);
			this.setLocation(20, 20);
			this.setTitle("Talkbox Simulator");
			this.setVisible(false);
		}
	}
}
