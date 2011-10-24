package component;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.*;
import java.util.*;

/**
 * {@code Label} inherits from {@code Component} to extend features that provide
 * functionality that draws text.
 */
public class Label extends Component {
	private static final int Y_OFFSET = 1;
	
	public enum Alignment {
		CENTER,
		LEFT
	}
	public enum VerticalAlignment {
		CENTER,
		TOP
	}
	private ArrayList<String> lines;
	private String text;
	private Font font;
	private Color c;
	private Alignment alignment;
	private VerticalAlignment verticalAlignment;
	
	private boolean clip;

	/**
	 * Creates a multi-line label that fits within a given width and height.
	 * 
	 * @param context The game container
	 * @param width Width of the label
	 * @param height Height of the label
	 * @param font Font the text will be drawn in
	 * @param c Color of the text
	 * @param text The text to draw
	 */
	public Label(GUIContext context, int width, int height, Font font, Color c, String text) {
		super(context, width, height);
		this.font = font;
		this.c = c;
		this.text = text;
		alignment = Alignment.CENTER;
		verticalAlignment = VerticalAlignment.CENTER;
		clip = true;
		lines = new ArrayList<String>();
		lines = parseLines(font, getWidth(), text);
	}
	
	/**
	 * Constructs a {@code Label} with a width, font, color, and text.
	 * 
	 * @param context The GUI context
	 * @param width Width of the label
	 * @param font Font the text will be drawn in
	 * @param c Color of the text
	 * @param text The text to draw
	 */
	public Label(GUIContext context, int width, Font font, Color c, String text) {
		this(context, width, font.getLineHeight(), font, c, text);
	}
	
	/**
	 * Creates a {@code Label} with a font, color, and text.
	 * 
	 * @param context The GUI context
	 * @param font Font the text will be drawn in
	 * @param c Color of the text
	 * @param text The text to draw
	 */
	public Label(GUIContext context, Font font, Color c, String text) {
		this(context, font.getWidth(text), font, c, text);
		clip = false;
	}
	
	/**
	 * Constructs a {@code Label} with a width, font, and color.
	 * 
	 * @param context The GUI context
	 * @param width Width of the label
	 * @param font Font the text will be drawn in
	 * @param c	Color of the text
	 */
	public Label(GUIContext context, int width, Font font, Color c) {
		this(context, width, font, c, "");
	}
	
	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		if (!isVisible()) {
			return;
		}
		
		super.render(container, g);
		
		if (clip) {
			g.setClip(getX(), getY(), getWidth(), getHeight());
		}
		
		int startY;
		
		if (verticalAlignment == VerticalAlignment.CENTER) {
			startY = getY() + (getHeight() - (lines.size() * font.getLineHeight())) / 2;
		} else {
			startY = getY();
		}
		
		startY += Y_OFFSET;
		
		for (int i = 0; i < lines.size(); i++) {
			if (alignment == Alignment.CENTER) {
				font.drawString(getX() + 1 + (getWidth() - font.getWidth(lines.get(i))) / 2,
						startY + (font.getLineHeight()) * i,
						lines.get(i), c);
			} else if (alignment == Alignment.LEFT){
				font.drawString(getX(),
						startY + (font.getLineHeight()) * i,
						lines.get(i), c);
			}
		}
		g.clearClip();
	}
	
	/**
	 * Parse lines of text for a multi-line label.
	 * Separates lines by new line characters, and also
	 * makes sure lines do not extend over the given label
	 * width.
	 */
	public static ArrayList<String> parseLines(Font font, int width, String text) {
		ArrayList<String> lines = new ArrayList<String>();
		
		Scanner lineScan = new Scanner(text);
		Scanner wordScan;
		
		String currentText = "";
		int currentLine = 0;
		int maxLines = (int) (getHeight() / getFontHeight()) - 1;
		while ( lineScan.hasNext() ) {
			wordScan = new Scanner(lineScan.nextLine());
			while (wordScan.hasNext()) {
				String nextWord = wordScan.next();

				if (currentLine != maxLines && font.getWidth(currentText + nextWord) > getWidth() ) {.
					lines.add(currentLine, currentText.trim());
					currentText = "";
					currentLine++;
				}
				currentText += nextWord + " ";
			}
			
			lines.add(currentLine, currentText.trim());
			currentText = "";
			currentLine++;
		}
		
		return lines;
	}
	
	/**
	 * Set font.
	 * @param font New font
	 */
	public void setFont(Font font) {
		this.font = font;
	}

	/**
	 * Get the width for the current font.
	 * @return Width of the label using the current font
	 */
	public float getFontWidth() {
		return font.getWidth(text);
	}
	
	/**
	 * Get the height for the current font.
	 * @return Height for the current font
	 */
	public float getFontHeight() {
		return font.getLineHeight();
	}
	
	/**
	 * Set the text.
	 * @param text The text to be drawn
	 */
	public void setText(String text) {
		this.text = text;
		this.lines.clear();
		lines = parseLines(font, getWidth(), text);
	}
	
	/**
	 * Get the text.
	 * @return The current text
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * Set the text's color.
	 * @param color New color
	 */
	public void setColor(Color color) {
		c = color;
	}
	
	/**
	 * Set the horizontal text alignment.
	 * @param alignment The new text alignment
	 */
	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}
	
	/**
	 * Set the vertical text alignment.
	 * @param alignment The new text alignment
	 */
	public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
		this.verticalAlignment = verticalAlignment;
	}
}
