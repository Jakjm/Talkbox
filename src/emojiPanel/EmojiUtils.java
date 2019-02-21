package emojiPanel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Hardcoded class to get emojis and descriptions.
 * @author jordan
 */
public final class EmojiUtils {
	/**The list of emojis within the file**/
	private static Emoji[] emojiList = readEmojis("emoji-data.txt");
	private static final int HEX_RADIX = 0x10;
	public static Emoji[] getEmojiList() {
		return emojiList;
	}
	private static InputStream getStream(String path) {
		return EmojiUtils.class.getResourceAsStream(path);
	}
	/**
	 * Reads the emojis from the text file.
	 * @param path - the path of the text file within the package.
	 * @return an array of the emojis.
	 */
	private static Emoji [] readEmojis(String path) {
		//Setting up input stream to read emojis from the file
		InputStream emojiFileStream = getStream(path);
		BufferedReader fileReader = new BufferedReader(new InputStreamReader(emojiFileStream));
		
		//Parsing emojis and their descriptions.
		ArrayList<Emoji> emojiList = new ArrayList<Emoji>();
		try {
			String line = fileReader.readLine();
			while(line != null) {
				if(line.length() > 0 && line.charAt(0) != '#') {
					String unicode = line.substring(0,line.indexOf(" ;"));
					//For now, skip if the line is too long
					String emoji = null;
					if(unicode.length() > 8) {
						int space = unicode.indexOf(" ");
						//Getting the two sets of chars
						String first = unicode.substring(0,space);
						char [] firstChars = Character.toChars(Integer.parseInt(first,HEX_RADIX));
						String last = unicode.substring(space + 1);
						char [] lastChars = Character.toChars(Integer.parseInt(last,HEX_RADIX));
						
						//Combining the arrays into one
						char [] combined = new char[firstChars.length + lastChars.length];
						for(int i = 0;i < firstChars.length;i++) {
							combined[i] = firstChars[i];
						}
						for(int i = 0;i < lastChars.length;i++) {
							combined[i + firstChars.length] = lastChars[i];
						}
						emoji = new String(combined,0,combined.length);
					}
					else {
						char [] emojiChars = Character.toChars(Integer.parseInt(unicode,HEX_RADIX));
						emoji = new String(emojiChars,0,emojiChars.length);
					}
					
					String description = line.substring(line.lastIndexOf(")") + 1);
					emojiList.add(new Emoji(emoji,description));
				}
				line = fileReader.readLine();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		//Putting the array list into an array
		Emoji [] actualList = new Emoji[emojiList.size()];
		int lineCount = 0;
		for(Emoji current : emojiList) {
			actualList[lineCount] = current;
			lineCount++;
		}
		return actualList;
	}
}
