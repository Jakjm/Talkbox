package emojiPanel;

/**
 * Class for organizing a group of emojis.
 * @author jordan
 *
 */
public class Emoji {
	private String emoji;
	private String description;
	public Emoji(String emojis,String description) {
		this.emoji = emojis;
		this.description = description;
	}
	public String emoji() {
		return emoji;
	}
	public String description() {
		return description;
	}
}
