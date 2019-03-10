package main.java.Talkbox.browsing;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class FileIcons {
	private static final String iconPath = "/main/java/Talkbox/browsing/FileIcons/";
	public static final ImageIcon IMAGE_ICON = loadIcon(iconPath + "Image.png");
	public static final ImageIcon SOUND_ICON = loadIcon(iconPath + "Music.png");
	public static final ImageIcon FOLDER_ICON = loadIcon(iconPath + "Folder.png");
	public static final ImageIcon TEXT_ICON = loadIcon(iconPath + "Text.png");
	public static final ImageIcon UNKNOWN_ICON = loadIcon(iconPath + "Unknown.png");
	
	/**Loads the image icons from the Jar resources.**/
	private static ImageIcon loadIcon(String path) {
		try {
			return new ImageIcon(ImageIO.read(FileIcons.class.getResourceAsStream(path)));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
