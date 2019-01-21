package configurer;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import talkbox.TalkboxConfigurer.BasePanel;

import javax.swing.JLabel;
import javax.swing.JFrame;
public class SetUpPanel extends JPanel implements ActionListener{
	public static final int ROWS = 1;
	public static final int COLS = 5;
	public JPanel buttonPanel;
	public JButton backButton;
	private BasePanel panel;
	public SetUpPanel(BasePanel panel) {
		this.panel = panel;
		this.setLayout(new BorderLayout());
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(ROWS,COLS));
		for(int row = 0;row < ROWS;row++) {
			for(int col = 0;col < COLS;col++) {
				String buttonString = String.format("Button %d",1+4*row+col);
				JButton button = new JButton(buttonString);
				buttonPanel.add(button);
			}
		}
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1,2));
		JLabel setupLabel = new JLabel("Button Setup");
		topPanel.add(setupLabel);
		backButton = new JButton("Back to Main Menu");
		backButton.addActionListener(this);
		topPanel.add(backButton);
		this.add(topPanel,BorderLayout.NORTH);
		this.add(buttonPanel,BorderLayout.CENTER);
	}
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == backButton) {
			panel.showMainMenu();
		}
	}
	public class SetUpButton extends JButton{
		public SetUpButton() {
			
		}
	}
	public class SetUpFrame extends JFrame{
		
	}
	
}
