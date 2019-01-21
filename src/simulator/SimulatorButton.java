package simulator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import musicplayer.MusicPlayer;
public class SimulatorButton extends JButton implements ActionListener{
	private MusicPlayer player;
	public SimulatorButton(String path) {
		this.addActionListener(this);
		player = new MusicPlayer(path);
	}
	@Override
	public void actionPerformed(ActionEvent event) {
		if(player.isPlaying()) {
			return;
		}
		else {
			player.play();
		}
	}
	
}
