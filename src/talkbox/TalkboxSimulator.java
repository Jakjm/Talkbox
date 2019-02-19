package talkbox;

import java.awt.BorderLayout;
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
					panel.setupConfiguration();
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
	public class TalkboxPanel extends JPanel implements ActionListener{
		SimulatorButton[] buttons;
		private JButton downButton;
		private JButton upButton;
		private JLabel rowLabel;
		private static final int ROWS = 1;
		private static final int COLS = 6;
		private int currentRow = 1;
		private int numRows;
		public TalkboxPanel() {
			this.setLayout(new BorderLayout());
			
			//Setting up buttons panel
			JPanel buttonsPanel = new JPanel(); 
			buttonsPanel.setLayout(new GridLayout(ROWS,COLS));
			buttons = new SimulatorButton[COLS];
			for (int i = 0; i < buttons.length; i++) {
				buttons[i] = new SimulatorButton();
				buttonsPanel.add(buttons[i]);
			}
			
			//Setting up row panel
			JPanel rowPanel = new JPanel();
			rowPanel.setLayout(new GridLayout(1,4));
			rowPanel.add(new JLabel(" Switch Rows:"));
			
			//Creating the down button
			downButton = new JButton("▼");
			downButton.addActionListener(this);
			rowPanel.add(downButton);
			
			
			//Creating the up button
			upButton = new JButton("▲");
			upButton.addActionListener(this);
			rowPanel.add(upButton);
			
			//Adding the row label
			rowLabel = new JLabel();
			rowPanel.add(rowLabel);
			
			this.add(buttonsPanel,BorderLayout.CENTER);
			this.add(rowPanel,BorderLayout.SOUTH);
			
			this.revalidate();
			this.repaint();
		}
		/**
		 * Method for updating the text of the row label
		 */
		public void updateRowLabel() {
			rowLabel.setText(String.format(" Row: %d / %d",this.currentRow,this.numRows));
		}
		/**
		 * Setting up the simulator with the talkbox configuration
		 */
		public void setupConfiguration() {
			this.currentRow = 1;
			numRows = configuration.buttonConfigs.length / COLS;
			switchRow(currentRow);
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
				buttons[buttonIndex].setConfiguration(configuration.buttonConfigs[configIndex]);
				++configIndex;
			}
			updateRowLabel();
			
			/*
			 * Updating which buttons are enabled.
			 */
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

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
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
