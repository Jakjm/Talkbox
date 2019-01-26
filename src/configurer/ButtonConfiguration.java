package configurer;

import java.awt.Color;
import java.io.File;

import javax.swing.UIManager;

public class ButtonConfiguration {
	public String buttonText;
	public Color buttonColor;
	public File soundFile;
	public static final Color DEFAULT_COLOR =  UIManager.getColor("Button.background");
	public ButtonConfiguration(String buttonText,Color color,File soundFile) {
		if(color == null) {
			buttonColor = DEFAULT_COLOR;
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
