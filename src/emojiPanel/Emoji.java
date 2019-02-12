package emojiPanel;

/**
 * Class for grouping an emoji with its description
 * 
 * @author jordan
 */
public class Emoji {
	private String emoji;
	private String description;

	public Emoji(String emoji, String description) {
		this.emoji = emoji;
		this.description = description;
	}

	public String emoji() {
		return emoji;
	}

	public String description() {
		return description;
	}
}
