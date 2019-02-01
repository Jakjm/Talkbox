package fonts;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;
public final class FontUtils {
	/**
	 * Loads the font into the local graphics environment??
	 * @param f
	 */
	public static boolean loadFont(Font f) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		return ge.registerFont(f);
	}
	public static Font getNotoFont(int size) {
		Font font;
		try {
			InputStream fontStream = FontUtils.class.getResourceAsStream("NotoColorEmoji.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT,fontStream);
			loadFont(font);
			font = new Font("NotoColorEmoji",Font.PLAIN,size);
			return font;
		}
		catch(IOException | FontFormatException e) {
			e.printStackTrace();
			return null;
		} 
	}
}
