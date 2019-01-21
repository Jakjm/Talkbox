package talkbox;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import simulator.SimulatorButton;

import javax.swing.JButton;
public class TalkboxSimulator {
	TalkboxFrame frame;
	TalkboxPanel panel;
	public TalkboxSimulator() {
		frame = new TalkboxFrame();
		panel = new TalkboxPanel();
		frame.setContentPane(panel);
		frame.setVisible(true);
	}
	public static void main(String [] args) {
		new TalkboxSimulator();
	}
	public class TalkboxPanel extends JPanel{
		public static final int ROWS = 1;
		public static final int COLS = 5;
		public final String [] tracks = new String [] {"exp.wav","arceus.wav","deoxys.wav","rayquaza.wav","crescelia.wav"};
		public TalkboxPanel() {
			char buttonLetter = 'A';
			GridLayout layout = new GridLayout(ROWS,COLS);
			for(int row = 0;row < ROWS;++row) {
				for(int col = 0;col < COLS;++col) {
					String buttonText = String.format("Button %c",buttonLetter);
					JButton button = new SimulatorButton("/testSounds/" + tracks[col]);
					button.setText(buttonText);
					button.setEnabled(true);
					this.add(button);
					buttonLetter++;
				}
			}
			this.setLayout(layout);
			this.revalidate();
			this.repaint();
		}
	}
	public class TalkboxFrame extends JFrame{
		public static final int FRAME_X = 800;
		public static final int FRAME_Y = 600;
		public TalkboxFrame() {
			super.setSize(FRAME_X,FRAME_Y);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setResizable(false);
			this.setLocation(20, 20);
			this.setTitle("Talkbox Simulator");
			this.setVisible(false);
		}
	}
}
