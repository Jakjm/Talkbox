package talkbox;

import java.awt.GridLayout;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import browsing.FileSelector;
import browsing.SelectionListener;
import configurer.Configuration;
import simulator.SimulatorButton;

/**
 * Talkbox simulator app for the talkbox.
 * 
 * @author jordan
 *
 */
public class TalkboxSimulator {
	TalkboxFrame frame;
	TalkboxPanel panel;
	FileSelector selector;
	public Configuration configuration;

	/** Main method that simply creates a new talkbox simulator instance **/
	public static void main(String[] args) {
		new TalkboxSimulator();
	}

	public TalkboxSimulator() {
		// Initializing frame,panel...
		frame = new TalkboxFrame();
		panel = new TalkboxPanel();
		frame.setContentPane(panel);

		// Trying to open a configuration file
		selector = new FileSelector(null, FileSelector.DIRECTORY);
		JOptionPane.showMessageDialog(null, "Please select a talkboxData configuration.");

		// Setting up what should happen once a file is found
		selector.setSelectionListener(new SelectionListener() {
			public void onFileSelected(File file) {
				configuration = Configuration.readConfiguration(file);
				// If the configuration is valid, open it
				if (configuration != null) {
					selector.setVisible(false);
					panel.setupConfiguration(configuration);
					frame.setVisible(true);
				}
				// Otherwise, keep going until one is found
				else {
					int response = JOptionPane.showConfirmDialog(null,
							"Configuration not valid, please try again.\n" + "Would you like to quit?");
					if (response == JOptionPane.YES_OPTION) {
						System.exit(0);
					}
				}
			}
		});
		// Making the selector visible.
		selector.setVisible(true);
	}

	/**
	 * Talkbox panel for the talkbox simulator panel
	 * 
	 * @author jordan
	 *
	 */
	public class TalkboxPanel extends JPanel {
		SimulatorButton[] buttons;

		public TalkboxPanel() {
			GridLayout layout = new GridLayout(1, 6);
			this.setLayout(layout);
			buttons = new SimulatorButton[6];
			for (int i = 0; i < buttons.length; i++) {
				buttons[i] = new SimulatorButton();
				this.add(buttons[i]);
			}
			this.revalidate();
			this.repaint();
		}

		public void setupConfiguration(Configuration config) {
			for (int i = 0; i < buttons.length; i++) {
				buttons[i].setupWithConfiguration(config.buttonConfigs[i]);
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
		/** The width of the Talkbox frame **/
		public static final int FRAME_X = 800;
		/** The height of the Talkbox frame **/
		public static final int FRAME_Y = 600;

		public TalkboxFrame() {
			super.setSize(FRAME_X, FRAME_Y);
			/** Setting the frame to shutdown the program when closed **/
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setResizable(false);
			this.setLocation(20, 20);
			this.setTitle("Talkbox Simulator");
			this.setVisible(false);
		}
	}
}
