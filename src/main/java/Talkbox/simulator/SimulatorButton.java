package main.java.Talkbox.simulator;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

import main.java.Talkbox.configurer.ButtonConfiguration;
import main.java.Talkbox.musicplayer.MusicPlayer;

/**
 * Simulator button for the talkbox simulator app
 * 
 * @author jordan
 * @version January 31st 2019
 */
public class SimulatorButton extends JButton implements ActionListener {
	private MusicPlayer player;
	private ButtonConfiguration config;
	private static final Font BUTTON_FONT = new Font(Font.SANS_SERIF,Font.PLAIN,15);
	public SimulatorButton() {
		super("");
		this.setFont(BUTTON_FONT);
	}
	public void setConfiguration(ButtonConfiguration config) {
		this.config = config;
		// Adjusting the text to use html that way the body linewraps
		String adjustedText = String.format("<html><body>%s</body></html>", config.buttonText);
		this.setText(adjustedText);
		this.setBackground(config.buttonColor);
		if (config.soundFile != null) {
			player = new MusicPlayer(config.soundFile);
		} else {
			player = null;
		}
		this.addActionListener(this);
	}
	public boolean isPlaying() {
		return player.isPlaying();
	}
	public void stop() {
		player.stop();
		player.reset();
	}
	@Override
	public void actionPerformed(ActionEvent event) {
		// If there is no soundplayer, do nothing
		if (player == null)
			return;
		if (player.isPlaying()) {
			return;
		} else {
			player.play();
		}
	}

}
