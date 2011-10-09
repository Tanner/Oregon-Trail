package component;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.*;

/**
 * {@code Label} inherits from {@code Component} to extend features that provide
 * functionality that draws text.
 */
public class Label extends Component {
	public enum Alignment {
		Center,
		Left
	}
	
	private String text;
	private Font font;
	private Color c;
	private Color backgroundColor;
	private Alignment alignment;
	private boolean clip;

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
		
		g.setClip(getX(), getY(), getWidth(), getHeight());
		
		if (backgroundColor != null) {
			g.setColor(backgroundColor);
			g.fillRect(getX(), getY(), getWidth(), getHeight());
		}
		
		String renderText = text;
		while (font.getWidth(renderText) > getWidth()) {
			renderText = text.substring(0, renderText.length()-1);
		}
		
		if (alignment == Alignment.Center) {
			font.drawString(getX() + (getWidth() - font.getWidth(renderText)) / 2,
					getY() + (getHeight() - font.getLineHeight()) / 2,
					renderText,
					c);
		} else if (alignment == Alignment.Left){
			font.drawString(getX(),
					getY() + (getHeight() - font.getLineHeight()) / 2,
					renderText,
					c);
		}
		
		g.clearClip();
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
