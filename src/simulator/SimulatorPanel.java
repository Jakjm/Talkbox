package simulator;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import configurer.Configuration;
import talkbox.TalkboxSimulator;

/**
 * Talkbox panel for the talkbox simulator panel
 * 
 * @author jordan
 *
 */
public class SimulatorPanel extends JPanel implements ActionListener{
	SimulatorButton[] buttons;
	private JButton downButton;
	private JButton upButton;
	private JButton mainMenuButton;
	private JLabel rowLabel;
	private static final int ROWS = 1;
	private static final int COLS = 6;
	private int currentRow = 1;
	private int numRows;
	private Configuration config;
	private TalkboxSimulator sim;
	public SimulatorPanel(TalkboxSimulator sim) {
		this.setLayout(new BorderLayout());
		this.sim = sim;
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
		rowPanel.setLayout(new GridLayout(1,5));
		mainMenuButton = new JButton("Back to Main Menu");
		mainMenuButton.addActionListener(this);
		rowPanel.add(mainMenuButton);
		
		rowPanel.add(new JLabel(" Switch Rows:"));
		
		//Creating the down button
		downButton = new JButton("View Lower Row");
		downButton.addActionListener(this);
		rowPanel.add(downButton);
		
		
		//Creating the up button
		upButton = new JButton("View Higher Row");
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
		if(row < 1 || row > this.numRows) {
			throw new IllegalArgumentException(String.format("Illegal row %d / %d",row,this.numRows));
		}
		this.currentRow = row;
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
			switchRow(this.currentRow - 1);
		}
		//Down button
		else if(e.getSource() == this.downButton) {
			if(this.currentRow == this.numRows)return;
			switchRow(this.currentRow + 1);
		}
		//Main menu button
		else if(e.getSource() == this.mainMenuButton) {
			sim.showMain();
		}
		
	}
}
