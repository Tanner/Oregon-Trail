package component;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.*;
import java.util.*;
import java.util.regex.*;

/**
 * {@code Label} inherits from {@code Component} to extend features that provide
 * functionality that draws text.
 */
public class Label extends Component {
	public enum Alignment {
		Center,
		Left
	}
	ArrayList<String> lines;
	private String text;
	private Font font;
	private Color c;
	private Color backgroundColor;
	private Alignment alignment;
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
		super (context, width, height);
		this.font = font;
		this.c = c;
		this.text = text;
		alignment = Alignment.Left;
		clip = true;
		lines = new ArrayList<String>();
		parseLines();
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
		super(context, width, font.getLineHeight());
		this.font = font;
		this.c = c;
		this.text = text;
		lines = new ArrayList<String>();
		lines.add(0, text);
		alignment = Alignment.Left;
		clip = true;
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
		
		if (clip == true)
			g.setClip(getX(), getY(), getWidth(), getHeight());
		
		if (backgroundColor != null) {
			g.setColor(backgroundColor);
			g.fillRect(getX(), getY(), getWidth(), getHeight());
		}
		
		for (int i = 0; i < lines.size(); i++) {
			if (alignment == Alignment.Center) {
				font.drawString(getX() + (getWidth() - font.getWidth(lines.get(i))) / 2,
						getY() + (font.getLineHeight())*i,
						lines.get(i), c);
			} else if (alignment == Alignment.Left){
				font.drawString(getX(),
						getY() + (font.getLineHeight())*i,
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
	public void parseLines() {
		Scanner lineScan = new Scanner(text);
		Scanner wordScan;
		
		String currentText = "";
		int currentLine = 0;
		
		while ( lineScan.hasNext() ) {
			wordScan = new Scanner(lineScan.nextLine());
			while (wordScan.hasNext()) {
				String nextWord = wordScan.next();
				if ( font.getWidth(currentText + nextWord) > getWidth() ) {
					lines.add(currentLine,currentText.trim());
					currentText = "";
					currentLine++;
				}
				currentText += nextWord + " ";
			}
			lines.add(currentLine,currentText.trim());
			currentText = "";
			currentLine++;
		}
	}
	
	/**
	 * Set background color.
	 * @param color New background color
	 */
	public void setBackgroundColor(Color color) {
		backgroundColor = color;
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
	 * Set the text.
	 * @param text The text to be drawn
	 */
	public void setText(String text) {
		this.text = text;
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
	 * Set the text alignment.
	 * @param alignment The new text alignment
	 */
	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}
}
