package configurer;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.JPanel;
import recording.MusicRecorder;
import talkbox.TalkboxConfigurer.BasePanel;

import javax.swing.JButton;
import javax.swing.JTextField;
public class RecordingPanel extends JPanel implements ActionListener{
	MusicRecorder recorder = new MusicRecorder();
	JButton recordingButton;
	JButton mainMenu;
	JButton stopRecording;
	JTextField pathField;
	BasePanel parent;
	public RecordingPanel(BasePanel parent) {
		this.parent = parent;
		this.setLayout(new GridLayout(5,1));
		
		//Top panel
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1,2));
		
		topPanel.add(new JLabel("Recording"));
		
		//Adding main menu button to top panel.
		mainMenu = new JButton("Back to main menu");
		mainMenu.addActionListener(this);
		topPanel.add(mainMenu);
		this.add(topPanel);
		
		JPanel pathPanel = new JPanel();
		pathPanel.setBackground(Color.red);
		pathPanel.setLayout(new GridLayout(1,2));
		pathPanel.add(new JLabel("Recording Save Path:"));
		
		pathField = new JTextField();
		pathField.setText(System.getProperty("user.home")+"/recording22.wav");
		pathPanel.add(pathField);
		this.add(pathPanel);
		
		
		
		recordingButton = new JButton("Record");
		recordingButton.addActionListener(this);
		this.add(recordingButton);
		
		stopRecording = new JButton("Stop Recording");
		stopRecording.addActionListener(this);
		this.add(stopRecording);
		stopRecording.setEnabled(false);
	}
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == recordingButton) {
			recordingButton.setEnabled(false);
			stopRecording.setEnabled(true);
			mainMenu.setEnabled(false);
			recorder.record(new File(pathField.getText()));
		}
		else if(event.getSource() == mainMenu) {
			parent.showMainMenu();
		}
		else if(event.getSource() == stopRecording) {
			mainMenu.setEnabled(true);
			recordingButton.setEnabled(true);
			stopRecording.setEnabled(false);
			recorder.stop();
		}
	}
}
