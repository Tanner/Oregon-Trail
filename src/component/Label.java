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
	private Vector2f position;
	private Color c;
	private Color backgroundColor;
	private int width, height;
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
	public Label(GUIContext context, Font font, Color c, String text, int width) {
		super(context);
		
		this.position = new Vector2f(0, 0);
		this.font = font;
		this.c = c;
		this.text = text;
		this.width = width;
		this.height = font.getLineHeight();
		
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
		this(context, font, c, text, font.getWidth(text));
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
	public Label(GUIContext context, Font font, Color c, int width) {
		this(context, font, c, "", width);
	}
	
	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		if (!visible) {
			return;
		}
		
		super.render(container, g);
		
		g.setClip(getX(), getY(), getWidth(), getHeight());
		
		if (backgroundColor != null) {
			g.setColor(backgroundColor);
			g.fillRect(getX(), getY(), width, height);
		}
		
		String renderText = text;
		while (font.getWidth(renderText) > width) {
			renderText = text.substring(0, renderText.length()-1);
		}
		
		if (alignment == Alignment.Center) {
			font.drawString(position.getX() + (width - font.getWidth(renderText)) / 2, position.getY() + (height - font.getLineHeight()) / 2, renderText, c);
		} else if (alignment == Alignment.Left){
			font.drawString(position.getX(), position.getY() + (height - font.getLineHeight()) / 2, renderText, c);
		}
		
		g.clearClip();
	}
	
	public void setBackgroundColor(Color color) {
		backgroundColor = color;
	}
	
	/**
	 * Updates the label with new text, and calculates new width
	 * and height based on that text.
	 * 
	 * @param text The text you want drawn on the label
	 */
	public void setText(String text) {
		this.text = text;
		
		if (!clip) {
			width = font.getWidth(text);
		}
	}
	
	/**
	 * Get the text of this label.
	 * 
	 * @return Label's current text
	 */
	public String getText() {
		return text;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getX() {
		return (int)position.getX();
	}

	@Override
	public int getY() {
		return (int)position.getY();
	}

	@Override
	public void setLocation(int x, int y) {
		if (font != null) {
			position = new Vector2f(x, y);
		}
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public void setHeight(int height) {
		this.height = height;
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
