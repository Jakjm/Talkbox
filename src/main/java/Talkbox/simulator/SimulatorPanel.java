package main.java.Talkbox.simulator;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.java.Talkbox.TalkboxSimulator;
import main.java.Talkbox.configurer.Configuration;
import main.java.Talkbox.log.LogController;

/**
 * Talkbox panel for the talkbox simulator panel
 * @version March 10th 2019
 * @author jordan
 */
public class SimulatorPanel extends JPanel implements ActionListener{
	public SimulatorButton[] buttons;
	private JButton downButton;
	private JButton upButton;
	private JButton mainMenuButton;
	private JLabel rowLabel;
	/**The number of rows displayed at any one time**/
	private static final int ROWS = 1;
	/**The number of cols displayed at any one time**/
	private static final int COLS = 6;
	private int currentRow = 1;
	private int numRows;
	private Configuration config;
	private TalkboxSimulator sim;
	private LogController simLogger;
	public SimulatorPanel(TalkboxSimulator sim, LogController simLogger) {
		this.simLogger = simLogger;
		this.setLayout(new BorderLayout());
		this.sim = sim;
		//Setting up buttons panel
		JPanel buttonsPanel = new JPanel(); 
		buttonsPanel.setLayout(new GridLayout(ROWS,COLS));
		buttons = new SimulatorButton[COLS];
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new SimulatorButton(this, simLogger);
			buttonsPanel.add(buttons[i]);
		}
		
		//Setting up row panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,5));
		mainMenuButton = new JButton("Back to Main Menu");
		mainMenuButton.addActionListener(this);
		buttonPanel.add(mainMenuButton);
		
		buttonPanel.add(new JLabel(" Switch Sets:"));
		
		//Creating the down button
		downButton = new JButton("View Lower Set");
		downButton.addActionListener(this);
		buttonPanel.add(downButton);
		
		
		//Creating the up button
		upButton = new JButton("View Higher Set");
		upButton.addActionListener(this);
		buttonPanel.add(upButton);
		
		
		
		//Adding the row label
		rowLabel = new JLabel();
		buttonPanel.add(rowLabel);
		
		this.add(buttonsPanel,BorderLayout.CENTER);
		this.add(buttonPanel,BorderLayout.SOUTH);
		
		this.revalidate();
		this.repaint();
	}
	/**
	 * Stops the playback of all of the buttons.
	 */
	public void stopMusic() {
		//Disabling other buttons
		for(int i = 0;i < this.buttons.length;i++) {
			if(this.buttons[i].isPlaying()) {
				this.buttons[i].stop();
			}
		}
	}
	/**
	 * Method for updating the text of the row label
	 */
	public void updateRowLabel() {
		rowLabel.setText(String.format(" Set: %d / %d",this.currentRow,this.numRows));
	}
	/**
	 * Setting up the simulator with the talkbox configuration
	 */
	public void setupConfiguration(Configuration config) {
		this.config = config;
		this.currentRow = 1;
		this.numRows = config.buttonConfigs.length / COLS;
		switchRow(currentRow);
	}
	/**
	 * Switches to the given configuration row
	 * @param row - the row to switch to.
	 * @throws IllegalArgumentException if the row is outside the range [1,this.numRows]
	 */
	public void switchRow(int row) {
		this.simLogger.logMessage(String.format("Row %d swapped for row %d", this.currentRow, row));
		if(row < 1 || row > this.numRows) {
			throw new IllegalArgumentException(String.format("Illegal row %d / %d",row,this.numRows));
		}
		this.currentRow = row;
		simLogger.logMessage("Row switched.");
		/*
		 * Loading the configurations at the given row.
		 */
		int configIndex = (row - 1) * COLS;
		for(int buttonIndex = 0; buttonIndex < COLS;buttonIndex++) {
			buttons[buttonIndex].setConfiguration(this.config.buttonConfigs[configIndex]);
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
	/**
	 * ActionListener for the buttons
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//Up button
		if(e.getSource() == this.upButton) {
			if(this.currentRow == 1)return;
			stopMusic();
			switchRow(this.currentRow - 1);
			simLogger.logMessage("Row moved upwards.");
		}
		//Down button
		else if(e.getSource() == this.downButton) {
			if(this.currentRow == this.numRows)return;
			stopMusic();
			switchRow(this.currentRow + 1);
			simLogger.logMessage("Row moved downwards.");
		}
		//Main menu button
		else if(e.getSource() == this.mainMenuButton) {
			stopMusic();
			sim.showMain();
			simLogger.logMessage("Main menu button pressed.");
		}
	}
}
