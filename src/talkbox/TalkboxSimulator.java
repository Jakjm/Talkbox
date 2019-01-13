package talkbox;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
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
		public static final int ROWS = 4;
		public static final int COLS = 4;
		public TalkboxPanel() {
			GridLayout layout = new GridLayout(4,4);
			for(int row = 0;row < ROWS;++row) {
				for(int col = 0;col < COLS;++col) {
					String buttonText = String.format("Button %d",1 + row*4 +col);
					JButton button = new JButton(buttonText);
					button.setEnabled(true);
					this.add(button);
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
