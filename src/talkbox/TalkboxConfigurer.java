package talkbox;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import configurer.SetUpPanel;

import javax.swing.JButton;
public class TalkboxConfigurer {
	ConfigurerFrame frame;
	BasePanel panel;
	public TalkboxConfigurer() {
		frame = new ConfigurerFrame();
		panel = new BasePanel();
		frame.setContentPane(panel);
		frame.setVisible(true);
	}
	public class MenuPanel extends JPanel implements ActionListener{
		JButton setUpButtons;
		JButton recordAudio;
		public MenuPanel() {
			this.setLayout(new GridLayout(2,2));
			recordAudio = new JButton("Record Audio");
			this.add(recordAudio);
			setUpButtons = new JButton("Set Up Buttons");
			setUpButtons.addActionListener(this);
			this.add(setUpButtons);
		}
		@Override
		public void actionPerformed(ActionEvent event) {
			if(event.getSource() == setUpButtons) {
				panel.showSetup();
			}
		}
	}
	public class BasePanel extends JPanel{
		CardLayout layout;
		public static final String MENU = "MENU";
		public static final String SETUP = "SETUP";
		public MenuPanel menu;
		public SetUpPanel setup;
		public BasePanel() {
			layout = new CardLayout();
			this.setLayout(layout);
			menu = new MenuPanel();
			this.add(MENU,menu);
			setup = new SetUpPanel(this);
			this.add(SETUP,setup);
			layout.show(this,MENU);
			this.revalidate();
			this.repaint();
		}
		public void showSetup() {
			layout.show(this,SETUP);
		}
		public void showMainMenu() {
			layout.show(this,MENU);
		}
	}
	public static void main(String [] args) {
		new TalkboxConfigurer();
	}
	public class ConfigurerFrame extends JFrame{
		public static final int FRAME_X = 1000;
		public static final int FRAME_Y = 700;
		public ConfigurerFrame() {
			super("TalkBox Configurer");
			this.setSize(FRAME_X,FRAME_Y);
			this.setLocation(20, 20);
			this.setResizable(false);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setVisible(false);
		}
	}
}
