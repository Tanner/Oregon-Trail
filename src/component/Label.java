package component;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.*;

/**
 * A label class to draw text on screen.  Uses
 * Font's drawString method to accomplish this.
 * 
 * @author Jeremy
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
	 * Creates a label to be drawn on the screen.
	 * 
	 * @param context The game container
	 * @param font Font the text will be drawn in
	 * @param c Color of the text
	 * @param text The text to draw
	 * @param width Width of the label
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
	 * Creates a label to be drawn on the screen.
	 * 
	 * @param context The game container
	 * @param font Font the text will be drawn in
	 * @param c Color of the text
	 * @param text The text to draw
	 */
	public Label(GUIContext context, Font font, Color c, String text) {
		this(context, font.getWidth(text), font, c, text);
		clip = false;
	}
	
	/**
	 * Creates a label that is initially empty.
	 * 
	 * @param context The game container
	 * @param font Font the text will be drawn in
	 * @param c	Color of the text
	 * @param width Width of the label
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
	
	public void setBackgroundColor(Color color) {
		backgroundColor = color;
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
	
	/**
	 * Updates the label with new text, and calculates new width
	 * and height based on that text.
	 * 
	 * @param text The text you want drawn on the label
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * Get the text of this label.
	 * 
	 * @return Label's current text
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * Changes the color of the label.
	 * @param color New color
	 */
	public void setColor(Color color) {
		c = color;
	}
	
	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}
}
