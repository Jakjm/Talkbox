package configurer;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import recording.MusicRecorder;
import talkbox.TalkboxConfigurer.BasePanel;

import javax.sound.sampled.AudioFormat;
import javax.swing.JButton;
import javax.swing.JTextField;

import browsing.FileSelector;
import browsing.SelectionListener;
public class RecordingPanel extends JPanel implements ActionListener{
	MusicRecorder recorder = new MusicRecorder();
	JButton recordingButton;
	JButton mainMenu;
	JButton stopRecording;
	BasicField nameField;
	BasePanel parent;
	FileSelector fileSelector;
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
		
		nameField = new BasicField("Filename of recording:");
		nameField.setText("rec.wav");
		this.add(nameField);
		
		recordingButton = new JButton("Record");
		recordingButton.addActionListener(this);
		this.add(recordingButton);
		
		stopRecording = new JButton("Stop Recording");
		stopRecording.addActionListener(this);
		this.add(stopRecording);
		stopRecording.setEnabled(false);
		
		fileSelector = new FileSelector(null,FileSelector.DIRECTORY);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == recordingButton) {
			recordingButton.setEnabled(false);
			stopRecording.setEnabled(true);
			mainMenu.setEnabled(false);
			recorder.record();
		}
		else if(event.getSource() == mainMenu) {
			parent.showMainMenu();
		}
		else if(event.getSource() == stopRecording) {
			mainMenu.setEnabled(true);
			recordingButton.setEnabled(true);
			stopRecording.setEnabled(false);
			fileSelector.setSelectionListener(new SaveFileSelectionListener(recorder.stop(),recorder.getFormat()));
			JOptionPane.showMessageDialog(null,"Please select the directory to save your recording in.");
			fileSelector.setVisible(true);
			
		}

	}
	public class SaveFileSelectionListener implements SelectionListener{
		ByteArrayOutputStream stream;
		AudioFormat format;
		public SaveFileSelectionListener(ByteArrayOutputStream stream,AudioFormat format) {
			this.stream = stream;
			this.format = format;
		}
		@Override
		public void onFileSelected(File file) {
			File newFile = new File(file.getPath() + System.getProperty("file.separator") + nameField.getText());
			MusicRecorder.writeToFile(stream,format,newFile);
			fileSelector.setVisible(false);
		}
	}

	
}