package main.java.Talkbox.emojiPanel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import main.java.Talkbox.fonts.FontUtils;

/**
 * Search pane for emojis within the app.
 * 
 * @author jordan
 * @version February 20th 2018
 */
public class EmojiSearchPane extends JPanel implements KeyListener {
	private static final long serialVersionUID = 1L;
	private JPanel emojiPanel;
	private static final int EMOJIS_PER_LINE = 5;
	private static final int MINIMUM_SEARCH_QUERY = 2;
	private EmojiTextField searchField;
	private Emoji[] emojiList;
	private ActionListener buttonListener;
	private final static Font emojiFont = FontUtils.getNotoFont(20);

	public static void main(String[] args) {
		EmojiSearchFrame frame = new EmojiSearchFrame(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public EmojiSearchPane(ActionListener buttonListener) {
		super();
		setLayout(new BorderLayout());

		this.buttonListener = buttonListener;
		searchField = new EmojiTextField("Search Emoji:");
		searchField.addKeyListener(this);
		add(searchField, BorderLayout.NORTH);

		emojiPanel = new JPanel();
		emojiPanel.setLayout(new GridLayout(0, EMOJIS_PER_LINE));
		JScrollPane emojiScroll = new JScrollPane(emojiPanel);
		emojiScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		emojiScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		add(emojiScroll, BorderLayout.CENTER);
		emojiList = EmojiUtils.getEmojiList();

		JLabel instructionLabel = new JLabel("Search for an emoji and select it!");
		add(instructionLabel, BorderLayout.SOUTH);

	}

	public void clearPanel() {
		clearButtons();
		searchField.setValue("");
	}

	private void clearButtons() {
		emojiPanel.removeAll();
		emojiPanel.repaint();
		emojiPanel.revalidate();
	}

	/**
	 * Adds the emojis that match the given search query. TODO: Improve this method.
	 * 
	 * @param searchQuery
	 */
	private void addContent(String searchQuery) {
		// System.out.println("***" + searchQuery + "***");
		ArrayList<Emoji> searchList = searchList(searchQuery);
		for (Emoji currentEmoji : searchList) {
			JButton emojiButton = new JButton();
			emojiButton.addActionListener(buttonListener);
			emojiButton.setFont(emojiFont);
			emojiButton.setText(currentEmoji.emoji());
			emojiPanel.add(emojiButton);
		}
		emojiPanel.repaint();
		emojiPanel.revalidate();
	}

	/**
	 * Adds the emojis that match the given search query.
	 * @param searchQuery
	 * @return the list of emojis that match the criteria.
	 */
	private ArrayList<Emoji> searchList(String searchQuery) {
		ArrayList<Emoji> list = new ArrayList<Emoji>();
		String upperQuery = searchQuery.toUpperCase();

		// First check is for the emojis that match quickly.
		for (int i = 0; i < emojiList.length; i++) {
			Emoji currentEmoji = emojiList[i];
			String emojiDescription = currentEmoji.description().toUpperCase();
			int indexOf = emojiDescription.indexOf(upperQuery);
			if (indexOf <= 2 && indexOf > -1) {
				list.add(currentEmoji);
			}
		}

		for (int i = 0; i < emojiList.length; i++) {
			Emoji currentEmoji = emojiList[i];
			String emojiDescription = currentEmoji.description().toUpperCase();
			if (emojiDescription.contains(upperQuery)) {
				list.add(currentEmoji);
			}
		}
		return list;
	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	/**
	 * Search for emojis based on the search query after the user has typed.
	 * 
	 * @param e
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		// Getting rid of old search results
		if (emojiPanel.getComponentCount() > 0) {
			clearButtons();
		}
		// And getting new ones if necessary
		if (searchField.getValue().length() >= MINIMUM_SEARCH_QUERY) {
			addContent(searchField.getValue());
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public static class EmojiSearchFrame extends JFrame {
		public EmojiSearchFrame(ActionListener listener) {
			super("Search Emojis");
			this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			this.setSize(300, 200);
			this.setContentPane(new EmojiSearchPane(listener));
			this.setVisible(false);
		}
	}
}
