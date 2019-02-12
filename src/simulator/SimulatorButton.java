package simulator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import configurer.ButtonConfiguration;
import musicplayer.MusicPlayer;

/**
 * Simulator button for the talkbox simulator app
 * 
 * @author jordan
 * @version January 31st 2019
 */
public class SimulatorButton extends JButton implements ActionListener {
	private MusicPlayer player;
	private ButtonConfiguration config;

	public SimulatorButton() {
	}

	public void setupWithConfiguration(ButtonConfiguration config) {
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
