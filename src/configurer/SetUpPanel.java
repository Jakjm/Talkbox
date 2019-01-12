package configurer;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JFrame;
public class SetUpPanel extends JPanel{
	public static final int ROWS = 4;
	public static final int COLS = 4;
	public JPanel buttonPanel;
	public SetUpPanel() {
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
		JButton backButton = new JButton("Back to Main Menu");
		topPanel.add(backButton);
		this.add(topPanel,BorderLayout.NORTH);
		this.add(buttonPanel,BorderLayout.CENTER);
	}
	public class SetUpButton extends JButton{
		public SetUpButton() {
			
		}
	}
	public class SetUpFrame extends JFrame{
		
	}
}
