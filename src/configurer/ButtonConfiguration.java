package configurer;

import java.awt.Color;
import java.io.File;

public class ButtonConfiguration {
	private String buttonText;
	private Color buttonColor;
	private File soundFile;
	public ButtonConfiguration(String buttonText,Color color,File soundFile) {
		if(color == null) {
			buttonColor = Color.blue;
		}
		else {
			buttonColor = color;
		}
		this.soundFile = soundFile;
		this.buttonText = buttonText;
	}
	public ButtonConfiguration(String buttonText) {
		this(buttonText,null,null);
	}
	public void writeTo(File file) {
		
	}
	public void readFrom(File file) {
		
	}
}
