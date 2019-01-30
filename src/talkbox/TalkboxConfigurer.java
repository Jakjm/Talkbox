package talkbox;

import java.awt.CardLayout;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import browsing.FileSelector;
import browsing.SelectionListener;
import configurer.ConfigSerialization;
import configurer.Configuration;
import configurer.RecordingPanel;
import configurer.SetUpPanel;
import configurer.RecordingPanel.SaveFileSelectionListener;
import filehandler.FileIO;

import javax.swing.JButton;
import javax.swing.JFileChooser;

public class TalkboxConfigurer {
	ConfigurerFrame frame;
	BasePanel panel;
	JFileChooser dirChoose;
	// temporarily create new configuration 
	Configuration config;

	public TalkboxConfigurer() {
		frame = new ConfigurerFrame();
		panel = new BasePanel();
		frame.setContentPane(panel);
		frame.setVisible(true);
	}

	public class MenuPanel extends JPanel implements ActionListener {
		JButton setUpButtons;
		JButton recordAudio;
		JButton createNew;
		JButton editOld;
		JButton selectExisting;
		FileSelector selector;
		public MenuPanel() {
			this.setLayout(new GridLayout(2, 2));
			// Recording Button
			recordAudio = new JButton("Record Audio");
			recordAudio.addActionListener(this);
			this.add(recordAudio);
			// Set up button
			setUpButtons = new JButton("Set Up Buttons");
			setUpButtons.addActionListener(this);
			this.add(setUpButtons);
			// Create new config directory
			createNew = new JButton("Create new configuration directory");
			createNew.addActionListener(this);
			this.add(createNew);
			// Select existing config directory
			selectExisting = new JButton("Select existing configuration directory");
			selectExisting.addActionListener(this);
			this.add(selectExisting);
			
			selector = new FileSelector(null,FileSelector.DIRECTORY);
		}
		private void initButtons() {
			setUpButtons.setEnabled(false);
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == setUpButtons) {
				panel.showSetup();
			} else if (event.getSource() == recordAudio) {
				panel.showRecording();
			}
			// set pre-existing configration
			else if (event.getSource() == selectExisting) {
				selector.setVisible(true);
				selector.setSelectionListener(new SelectionListener() {
					public void onFileSelected(File file) {
						// TODO Auto-generated method stub
					}
				});
			}
			// create new config directory
			else if (event.getSource() == createNew) {
				selector.setVisible(true);
				selector.setSelectionListener(new SelectionListener() {
					public void onFileSelected(File file) {
						config = new Configuration(file.getPath());
					}
				});
			}
		}
	}

	public class BasePanel extends JPanel {
		CardLayout layout;
		public static final String MENU = "MENU";
		public static final String SETUP = "SETUP";
		public static final String RECORD = "RECORD";
		public MenuPanel menu;
		public SetUpPanel setup;
		public RecordingPanel record;

		public BasePanel() {
			layout = new CardLayout();
			this.setLayout(layout);
			// Adding menu to the base
			menu = new MenuPanel();
			this.add(MENU, menu);

			// Adding recording panel to the base
			record = new RecordingPanel(this);
			this.add(RECORD, record);

			// Adding setup panel to the base; send it
			// reference of current configurance
			setup = new SetUpPanel(this);
			this.add(SETUP, setup);
			layout.show(this, MENU);

			this.revalidate();
			this.repaint();
		}

		public void showSetup() {
			layout.show(this, SETUP);
		}

		public void showMainMenu() {
			layout.show(this, MENU);
		}

		public void showRecording() {
			layout.show(this, RECORD);
		}
	}

	public static void main(String[] args) {
		new TalkboxConfigurer();
	}

	public class ConfigurerFrame extends JFrame {
		public static final int FRAME_X = 1000;
		public static final int FRAME_Y = 700;

		public ConfigurerFrame() {
			super("TalkBox Configurer");
			this.setSize(FRAME_X, FRAME_Y);
			this.setLocation(20, 20);
			this.setResizable(false);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setVisible(false);
		}
	}
}
